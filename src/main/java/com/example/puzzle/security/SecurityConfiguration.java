package com.example.puzzle.security;

import com.example.puzzle.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter authenticationFilter;

    @Override
    protected void configure (HttpSecurity http) throws Exception{
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**/signup","/**/signin","/home").permitAll()
                .and()
                .addFilterBefore(this.authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
    @Override
    public void configure(final WebSecurity web) throws Exception{
        // 인증 정보가 없어도 해당 경로 이하는 자유롭게 접근가능
        web.ignoring()
                .antMatchers("/h2-console/**","/swagger-ui/**");
    }
    // spring boot 2.x
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManagerBean();
    }
}
