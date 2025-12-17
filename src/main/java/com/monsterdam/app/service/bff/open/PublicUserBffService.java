package com.monsterdam.app.service.bff.open;

import com.monsterdam.app.service.UserService;
import com.monsterdam.app.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PublicUserBffService {

    private static final Logger LOG = LoggerFactory.getLogger(PublicUserBffService.class);

    private final UserService userService;

    public PublicUserBffService(UserService userService) {
        this.userService = userService;
    }

    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        LOG.debug("BFF request to fetch public users");
        return userService.getAllPublicUsers(pageable);
    }
}
