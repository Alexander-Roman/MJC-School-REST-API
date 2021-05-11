package com.epam.esm.security.config;

import com.epam.esm.persistence.entity.Permission;
import com.epam.esm.persistence.entity.Role;
import com.epam.esm.security.jwt.JwtConfig;
import com.epam.esm.security.jwt.JwtTokenVerifyFilter;
import com.epam.esm.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.epam.esm.security.keycloak.CustomKeycloakAuthenticationProvider;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.epam.esm.security")
@PropertySource("classpath:security.properties")
@EnableConfigurationProperties(JwtConfig.class)
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    private final JwtConfig jwtConfig;
    private final UserDetailsService userDetailsService;

    @Autowired
    public KeycloakSecurityConfig(JwtConfig jwtConfig,
                                  @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        PasswordEncoder passwordEncoder = passwordEncoder();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilterAfter(new JwtTokenVerifyFilter(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)

                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), LogoutFilter.class)
                .addFilterAfter(keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class)
                .addFilterAfter(keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class)


                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler())
                .logoutUrl("/sso/logout").permitAll()
                .logoutSuccessUrl("/");

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/certificates", "/api/v1/certificates/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/certificates").hasAuthority(Permission.CERTIFICATE_CREATE.getName())
                .antMatchers(HttpMethod.PATCH, "/api/v1/certificates").hasAuthority(Permission.CERTIFICATE_UPDATE.getName())
                .antMatchers(HttpMethod.DELETE, "/api/v1/certificates").hasAuthority(Permission.CERTIFICATE_DELETE.getName())
                .antMatchers(HttpMethod.GET, "/api/v1/tags", "/api/v1/tags/*").hasAuthority(Permission.TAG_READ.getName())
                .antMatchers(HttpMethod.POST, "/api/v1/tags").hasAuthority(Permission.TAG_CREATE.getName())
                .antMatchers(HttpMethod.DELETE, "/api/v1/tags").hasRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/api/v1/accounts").hasAuthority(Permission.ACCOUNT_READ.getName())
                .antMatchers(HttpMethod.POST, "/api/v1/accounts").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/purchases").hasAuthority(Permission.PURCHASE_READ.getName())
                .antMatchers(HttpMethod.POST, "/api/v1/purchases").hasAuthority(Permission.PURCHASE_CREATE.getName())
                .antMatchers("/api/**").hasRole(Role.ADMIN.name())
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
        DaoAuthenticationProvider daoAuthenticationProvider = daoAuthenticationProvider();
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider);

        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        authenticationManagerBuilder.authenticationProvider(keycloakAuthenticationProvider);
    }


    @Bean
    @Override
    protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        return new CustomKeycloakAuthenticationProvider();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
//        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
//        authenticationManagerBuilder.authenticationProvider(keycloakAuthenticationProvider);
//    }


    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

}
