package com.security.config;

import com.security.config.security.CustomSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by thangnguyen-imac on 7/12/16.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    CustomSuccessHandler customSuccessHandler;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("thang").password("thang").roles("USER");
        auth.inMemoryAuthentication().withUser("thang2").password("thang2").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("thang2").password("thang3").roles("ADMIN", "USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/home").access("hasRole('USER')")
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .and()
                .formLogin().loginPage("/login").successHandler(customSuccessHandler)
                .usernameParameter("ssoId").passwordParameter("password")
                .and().csrf()
                .and().exceptionHandling().accessDeniedPage("/Access_Denied");
    }

    public CustomSuccessHandler getCustomSuccessHandler() {
        return customSuccessHandler;
    }


    public void setCustomSuccessHandler(CustomSuccessHandler customSuccessHandler) {
        this.customSuccessHandler = customSuccessHandler;
    }
}
