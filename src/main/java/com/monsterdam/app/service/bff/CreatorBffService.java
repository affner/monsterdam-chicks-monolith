package com.monsterdam.app.service.bff;

import com.monsterdam.app.service.dto.bff.CreatorProfileDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CreatorBffService {

    private final ViewerBffService viewerBffService;

    public CreatorBffService(ViewerBffService viewerBffService) {
        this.viewerBffService = viewerBffService;
    }

    public CreatorProfileDTO getCreatorProfile(Long creatorId, String type, Pageable pageable) {
        return viewerBffService.getCreatorProfile(creatorId, type, pageable);
    }
}
