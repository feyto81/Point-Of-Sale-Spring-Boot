package com.crocodic.koperasi.security;

import com.crocodic.koperasi.string.AdminUrlString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserPrincipalDetailsService userPrincipalDetailsService;

    public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }


    @Autowired
    private LoggingFilter loggingFilter;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(loggingFilter, AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(AdminUrlString.LOGIN).permitAll()
                .antMatchers("/assets*").permitAll()
                .antMatchers("/storage*").permitAll()
                .antMatchers("/admin/**").authenticated()
                .antMatchers("/management/**").hasRole("SUPER_ADMIN")
                .antMatchers("/management/**").authenticated()
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler())
                .failureHandler(new CustomAuthenticationFailureHandler())
                .loginPage(AdminUrlString.LOGIN)
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl(AdminUrlString.LOGIN)
//                .defaultSuccessUrl("/admin/home", true)
//                .failureUrl("/admin/login?error=true")
                .and()
                .logout()
                .logoutUrl(AdminUrlString.LOGOUT)
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedPage(AdminUrlString.DENIED);

    }

//    @Bean
//    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
//        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
//        filterSecurityInterceptor.setSecurityMetadataSource(securityMetadataSource());
//        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
//        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
//        filterSecurityInterceptor.setPublishAuthorizationSuccess(true);
//        return filterSecurityInterceptor;
//    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        RoleVoter roleVoter = new RoleVoter();
        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
        voters.add(authenticatedVoter);
        voters.add(roleVoter);
        return new AffirmativeBased(voters);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

//    @Bean
//    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
//        return new DbFilterInvocationSecurityMetadataSource();
//    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/storage/**", "/assets/**");
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
