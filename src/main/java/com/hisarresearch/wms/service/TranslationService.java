package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.User;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Locale;


@Service
@Transactional
public class TranslationService {

    private final MessageSource messageSource;

    private final UserService userService;

    public TranslationService(MessageSource messageSource, UserService userService) {
        this.messageSource = messageSource;
        this.userService = userService;
    }

    public String getErrorMessage(String errorCode,Object... args) {
        User user = userService.getLoggedUser();
        Locale locale = new Locale(user.getLangKey());
        return messageSource.getMessage(errorCode, args, locale);
    }
}

