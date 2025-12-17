package com.monsterdam.app.service.bff.common;

import com.monsterdam.app.domain.User;
import com.monsterdam.app.repository.UserRepository;
import com.monsterdam.app.security.SecurityUtils;
import com.monsterdam.app.service.MailService;
import com.monsterdam.app.service.UserService;
import com.monsterdam.app.service.dto.AdminUserDTO;
import com.monsterdam.app.service.dto.PasswordChangeDTO;
import com.monsterdam.app.web.rest.errors.EmailAlreadyUsedException;
import com.monsterdam.app.web.rest.errors.InvalidPasswordException;
import com.monsterdam.app.web.rest.vm.KeyAndPasswordVM;
import com.monsterdam.app.web.rest.vm.ManagedUserVM;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountBffService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountBffService.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountBffService(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    public void registerAccount(ManagedUserVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    public void activateAccount(String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountBffServiceException("No user was found for this activation key");
        }
    }

    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountBffServiceException("User could not be found"));
    }

    public void saveAccount(AdminUserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new AccountBffServiceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountBffServiceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    public void changePassword(PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    public void requestPasswordReset(String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.orElseThrow());
        } else {
            LOG.warn("Password reset requested for non existing mail");
        }
    }

    public void finishPasswordReset(KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountBffServiceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    public static class AccountBffServiceException extends RuntimeException {

        public AccountBffServiceException(String message) {
            super(message);
        }
    }
}
