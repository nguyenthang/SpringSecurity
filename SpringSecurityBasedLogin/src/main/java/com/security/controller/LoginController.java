package com.security.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ThangN on 7/13/2016.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }

    @RequestMapping(value = {"/", "/user"}, method = RequestMethod.GET)
    public String homePage(ModelMap model){
        model.addAttribute("user", getPrincipal());
        return "user/user";
    }

    private Object getPrincipal() {

        String username = null;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            username = ((UserDetails)principal).getUsername();
        }else {
            username = principal.toString();
        }

        return username;
    }
}
