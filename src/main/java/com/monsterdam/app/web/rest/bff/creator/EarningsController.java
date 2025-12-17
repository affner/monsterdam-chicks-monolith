package com.monsterdam.app.web.rest.bff.creator;

import com.monsterdam.app.service.MoneyEarningService;
import com.monsterdam.app.service.MoneyGiftService;
import com.monsterdam.app.service.ViewerWalletService;
import com.monsterdam.app.service.dto.MoneyEarningDTO;
import com.monsterdam.app.service.dto.MoneyGiftDTO;
import com.monsterdam.app.service.dto.ViewerWalletDTO;
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
@RequestMapping("/api/bff/creator/earnings")
@Tag(name = "BFF Creator Earnings")
public class EarningsController {

    private static final Logger LOG = LoggerFactory.getLogger(EarningsController.class);

    private final MoneyEarningService moneyEarningService;
    private final MoneyGiftService moneyGiftService;
    private final ViewerWalletService viewerWalletService;

    public EarningsController(
        MoneyEarningService moneyEarningService,
        MoneyGiftService moneyGiftService,
        ViewerWalletService viewerWalletService
    ) {
        this.moneyEarningService = moneyEarningService;
        this.moneyGiftService = moneyGiftService;
        this.viewerWalletService = viewerWalletService;
    }

    @GetMapping("")
    public ResponseEntity<List<MoneyEarningDTO>> getEarnings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator earnings from BFF");
        Page<MoneyEarningDTO> page = moneyEarningService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/gifts")
    public ResponseEntity<List<MoneyGiftDTO>> getGifts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator gifts from BFF");
        Page<MoneyGiftDTO> page = moneyGiftService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/wallets")
    public ResponseEntity<List<ViewerWalletDTO>> getWallets(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get viewer wallets from BFF");
        Page<ViewerWalletDTO> page = viewerWalletService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
