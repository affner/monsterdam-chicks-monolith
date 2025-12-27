package com.monsterdam.app.web.rest.bff.creator;

import com.monsterdam.app.repository.MoneyEarningRepository;
import com.monsterdam.app.repository.MoneyGiftRepository;
import com.monsterdam.app.repository.ViewerWalletRepository;
import com.monsterdam.app.service.dto.MoneyEarningDTO;
import com.monsterdam.app.service.dto.MoneyGiftDTO;
import com.monsterdam.app.service.dto.ViewerWalletDTO;
import com.monsterdam.app.service.mapper.MoneyEarningMapper;
import com.monsterdam.app.service.mapper.MoneyGiftMapper;
import com.monsterdam.app.service.mapper.ViewerWalletMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/creator/earnings")
@Tag(name = "BFF Creator Earnings")
public class EarningsController {

    private static final Logger LOG = LoggerFactory.getLogger(EarningsController.class);

    private final MoneyEarningRepository moneyEarningRepository;
    private final MoneyGiftRepository moneyGiftRepository;
    private final ViewerWalletRepository viewerWalletRepository;
    private final MoneyEarningMapper moneyEarningMapper;
    private final MoneyGiftMapper moneyGiftMapper;
    private final ViewerWalletMapper viewerWalletMapper;

    public EarningsController(
        MoneyEarningRepository moneyEarningRepository,
        MoneyGiftRepository moneyGiftRepository,
        ViewerWalletRepository viewerWalletRepository,
        MoneyEarningMapper moneyEarningMapper,
        MoneyGiftMapper moneyGiftMapper,
        ViewerWalletMapper viewerWalletMapper
    ) {
        this.moneyEarningRepository = moneyEarningRepository;
        this.moneyGiftRepository = moneyGiftRepository;
        this.viewerWalletRepository = viewerWalletRepository;
        this.moneyEarningMapper = moneyEarningMapper;
        this.moneyGiftMapper = moneyGiftMapper;
        this.viewerWalletMapper = viewerWalletMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<MoneyEarningDTO>> getEarnings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator earnings from BFF");
        Page<MoneyEarningDTO> page = moneyEarningRepository.findAllByDeletedDateIsNull(pageable).map(moneyEarningMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/gifts")
    public ResponseEntity<List<MoneyGiftDTO>> getGifts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator gifts from BFF");
        Page<MoneyGiftDTO> page = moneyGiftRepository.findAllByDeletedDateIsNull(pageable).map(moneyGiftMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/wallets")
    public ResponseEntity<List<ViewerWalletDTO>> getWallets(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get viewer wallets from BFF");
        Page<ViewerWalletDTO> page = viewerWalletRepository.findAllByDeletedDateIsNull(pageable).map(viewerWalletMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
