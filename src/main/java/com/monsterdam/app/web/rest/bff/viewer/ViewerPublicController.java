package com.monsterdam.app.web.rest.bff.viewer;

import com.monsterdam.app.service.bff.ViewerBffService;
import com.monsterdam.app.service.dto.bff.ContentSetDTO;
import com.monsterdam.app.service.dto.bff.MenuDTO;
import com.monsterdam.app.service.dto.bff.SingleSetDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/viewer")
@Tag(name = "Public Viewer BFF")
public class ViewerPublicController {

    private static final Logger LOG = LoggerFactory.getLogger(ViewerPublicController.class);

    private final ViewerBffService viewerBffService;

    public ViewerPublicController(ViewerBffService viewerBffService) {
        this.viewerBffService = viewerBffService;
    }

    @GetMapping("/set/list")
    public ResponseEntity<List<SingleSetDTO>> listSets(
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "type", required = false) String type,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to list viewer sets sort={} type={}", sort, type);
        Page<SingleSetDTO> page = viewerBffService.listSets(sort, type, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/set/content/{id}")
    public ResponseEntity<ContentSetDTO> getContentSet(@PathVariable Long id) {
        LOG.debug("REST request to get content set {}", id);
        return ResponseEntity.ok(viewerBffService.getContentSet(id));
    }

    @GetMapping("/model/list")
    public ResponseEntity<ContentSetDTO> getModels() {
        // aqui se traeran la lista de modelos, pero sera aleatoria y solo regresara maximo 10
        //  LOG.debug("REST request to get content set {}", id);
        return null;
    }
    //  @GetMapping("/model/profile")

}
