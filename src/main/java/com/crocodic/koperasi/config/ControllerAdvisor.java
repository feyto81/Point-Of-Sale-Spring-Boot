package com.crocodic.koperasi.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handle(Exception ex, ModelAndView model) {
        model.addObject("page_title","Page not found");
        model.setViewName("error/404");
        return model;
    }
}
