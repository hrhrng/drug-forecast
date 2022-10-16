package com.hrhrng.drugforecast.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class RootController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getRoot() {
        return new ModelAndView( "index.html");
    }
}
