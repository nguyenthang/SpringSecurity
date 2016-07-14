package com.security.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public String HomePage(ModelMap model){
        model.addAttribute("user", getPrincipal());
        return "user/user";
    }

    @RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
    public String AdminPage(ModelMap model){
        model.addAttribute("user", getPrincipal());
        return "admin/admin";
    }

    @RequestMapping(value = {"/dba"}, method = RequestMethod.GET)
    public String DbaPage(ModelMap model){
        model.addAttribute("user", getPrincipal());
        return "dba/index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap map){
        map.addAttribute("user", getPrincipal());
        return "accessDenied";
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
