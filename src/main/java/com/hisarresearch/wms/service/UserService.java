package com.hisarresearch.wms.service;

import com.hisarresearch.wms.config.Constants;
import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.exception.api.EmailAlreadyUsedException;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.exception.validation.InvalidPasswordException;
import com.hisarresearch.wms.exception.validation.UsernameAlreadyUsedException;
import com.hisarresearch.wms.repository.*;
import com.hisarresearch.wms.security.AuthoritiesConstants;
import com.hisarresearch.wms.security.SecurityUtils;
import com.hisarresearch.wms.service.dto.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final AurUserRepository aurUserRepository;

    private final AurCompanyRepository aurCompanyRepository;

    private final AurUserRoleRelRepository aurUserRoleRelRepository;

    private final JhiUserRepository jhiUserRepository;

    private final AurRoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager, AurUserRepository aurUserRepository, AurCompanyRepository aurCompanyRepository, AurUserRoleRelRepository aurUserRoleRelRepository, JhiUserRepository jhiUserRepository, AurRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.aurUserRepository = aurUserRepository;
        this.aurCompanyRepository = aurCompanyRepository;
        this.aurUserRoleRelRepository = aurUserRoleRelRepository;
        this.jhiUserRepository = jhiUserRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                }
            );
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(
                user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String genericPassword = "Depoyonetim_20xx!*";
        String encryptedPassword = passwordEncoder.encode(genericPassword);
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setCompanyCode(getUserCompanyCode());
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        if (userDTO.getRoles() != null) {
            Set<AurRole> roles = userDTO
                .getRoles()
                .stream()
                .map(roleRepository::findByRoleName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    this.clearUserCaches(user);
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    if (userDTO.getPasswordVersion()== 0) {
                        String genericPassword = "Depoyonetim_20xx!*";
                        String encryptedPassword = passwordEncoder.encode(genericPassword);
                        user.setPassword(encryptedPassword);
                    }
                    user.setPasswordVersion(userDTO.getPasswordVersion());
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    user.setLangKey(userDTO.getLangKey());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO
                        .getAuthorities()
                        .stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                    Set<AurRole> roles = user.getRoles();
                    roles.clear();

                    userDTO
                        .getRoles()
                        .stream()
                        .map(roleRepository::findByRoleName)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(roles::add);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                    log.debug("Deleted User: {}", user);
                }
            );
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setLangKey(langKey);
                    user.setImageUrl(imageUrl);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                }
            );
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    user.setPasswordVersion(user.getPasswordVersion() + 1);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);
                }
            );
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);


    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                }
            );
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    /**
     * Gets a list of users the authorities.
     *
     * @return a list of users the authorities.
     */
    @Transactional(readOnly = true)
    public List<UserAuthorityDto> getUserAuthorities() {
        String userName = getUserName();
        User user = userRepository.findOneByLogin(userName).get();
        return userRepository.getUserAuthorities(user.getId());
    }

    @Transactional(readOnly = true)
    public User getLoggedUser() {
        String userName = getUserName();
        Optional<User> user = userRepository.findOneByLogin(userName);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("Test");
        }
    }

    public void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public AurCompanyDTO getUserCompanyInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AurUser aurUser = aurUserRepository.findByLogin(userName);
        AurCompany aurCompany = aurCompanyRepository.findByCompanyCode(aurUser.getCompanyCode());
        // Sirketi olmayan ya da sirketi silinmis kullanici: ModelMapper null kaynakta
        // IllegalArgumentException firlatip 500 donduruyordu.
        if (aurUser.getCompanyCode() == null || aurCompany == null) {
            throw new BusinessException("Kullanıcının şirketi tanımlı değil", "AurCompany", "userCompanyNotFound");
        }
        ModelMapper mm = new ModelMapper();
        return mm.map(aurCompany, AurCompanyDTO.class);
    }

    public Integer getUserCompanyCode() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AurUser aurUser = aurUserRepository.findByLogin(userName);
        return aurUser.getCompanyCode();
    }

    public long getUserId() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AurUser aurUser = aurUserRepository.findByLogin(userName);
        return aurUser.getId();
    }

    public String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<AurUser> getUsersByRoleId(Long roleId) {
        return aurUserRoleRelRepository.findByRole_IdAndUser_Activated(roleId,true).stream().map(AurUserRoleRel::getUser).filter(Objects::nonNull).collect(Collectors.toList());

    }

    public ErpConnectionType getUserErpType() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AurUser aurUser = aurUserRepository.findByLogin(userName);
        AurCompany aurCompany = aurCompanyRepository.findByCompanyCode(aurUser.getCompanyCode());
        return aurCompany.getErpType();
    }

    public AurUser getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return aurUserRepository.findByLogin(userName);
    }

    public void transferUser() {
        jhiUserRepository.findAll().forEach(jhiUser -> userRepository
            .findOneByLogin(jhiUser.getLogin())
            .ifPresent(user -> {
                user.setPassword(jhiUser.getPassword());
                user.setLastName(jhiUser.getLastName());
                user.setEmail(jhiUser.getEmail());
                user.setImageUrl(jhiUser.getImageUrl());
                user.setActivated(jhiUser.isActivated());
                user.setActivationKey(jhiUser.getActivationKey());
                user.setFirstName(jhiUser.getFirstName());
                user.setResetKey(jhiUser.getResetKey());
                user.setLangKey(jhiUser.getLangKey());
                user.setCreatedBy(jhiUser.getCreatedBy());
                user.setResetDate(jhiUser.getResetDate());
                user.setLastModifiedBy(jhiUser.getLastModifiedBy());
                user.setLastModifiedDate(jhiUser.getLastModifiedDate());
            }));
    }

    @Transactional
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new BadRequestAlertException("User not found", "user", "user");
    }

    public AurCompanyDTO checkErpType(ErpConnectionType erpConnectionType) throws UnsupportedOperationException{
        AurCompanyDTO aurCompanyDto = getUserCompanyInfo();
        if (erpConnectionType != aurCompanyDto.getErpType()) {
            throw new UnsupportedOperationException("Erp code mismatch");
        }
        return aurCompanyDto;
    }
}
