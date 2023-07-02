package com.vti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/messages")
public class MessagesController {
    @Autowired
    private MessageSource messageSource;

    private String getMessages(Locale locale,String code, String...args){
        return messageSource.getMessage(code, args , "Hello from MessageController",locale);
    }
    @GetMapping
    public  String getMessage(@RequestParam String code){
        return getMessages(LocaleContextHolder.getLocale(),code);
    }
    @GetMapping("/vi")
    public String getMessageVi(@RequestParam String code){
        return getMessages(new Locale("vi"),code);
    }
    @GetMapping("/en")
    public String getMessageEn(@RequestParam String code){
        return getMessages(Locale.ENGLISH,code);
    }
}
