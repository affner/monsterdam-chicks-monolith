package com.monsterdam.app.service.bff.admin;

import com.monsterdam.app.domain.User;
import com.monsterdam.app.service.UserService;
import com.monsterdam.app.service.dto.AdminUserDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminUserBffService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminUserBffService.class);

    private final UserService userService;

    public AdminUserBffService(UserService userService) {
        this.userService = userService;
    }

    public User createUser(AdminUserDTO adminUserDTO) {
        LOG.debug("BFF admin request to create user {}", adminUserDTO);
        return userService.createUser(adminUserDTO);
    }

    public Optional<AdminUserDTO> updateUser(AdminUserDTO adminUserDTO) {
        LOG.debug("BFF admin request to update user {}", adminUserDTO);
        return userService.updateUser(adminUserDTO);
    }

    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        LOG.debug("BFF admin request to list users");
        return userService.getAllManagedUsers(pageable);
    }

    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        LOG.debug("BFF admin request to fetch user {}", login);
        return userService.getUserWithAuthoritiesByLogin(login);
    }

    public void deleteUser(String login) {
        LOG.debug("BFF admin request to delete user {}", login);
        userService.deleteUser(login);
    }

    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }
}
