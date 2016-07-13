package com.security.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by thangnguyen-imac on 7/12/16.
 */
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response,
                          Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to "
                    + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        String url = "";
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Set<String> roles = new HashSet<>();

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }

        if(isDba(roles)){
            url = "/db";
        }else if(isAdmin(roles)){
            url = "/admin";
        }else if(isUser(roles)){
            url = "/user";
        }else {
            url = "accessDenied";
        }

        return url;
    }

    private boolean isUser(Set<String> roles) {
        return roles.contains("ROLE_USER");
    }

    private boolean isAdmin(Set<String> roles) {
        return roles.contains("ROLE_ADMIN");
    }

    private boolean isDba(Set<String> roles) {
        return roles.contains("ROLE_DBA");
    }

    @Override
    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
