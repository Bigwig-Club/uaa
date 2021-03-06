package dev.bigwig.uaa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bigwig.uaa.filter.RestAuthenticationFilter;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Import(SecurityProblemSupport.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final ObjectMapper objectMapper;
  private final SecurityProblemSupport securityProblemSupport;
  private final UserDetailsService userDetailsService;
  private final UserDetailsPasswordService userDetailsPasswordService;

  private static void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) throws IOException {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().println();
  }

  private static void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException exception) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().println();
  }

  private RestAuthenticationFilter restAuthenticationFilter() throws Exception {
    RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);
    filter.setAuthenticationSuccessHandler(SecurityConfig::onAuthenticationSuccess);
    filter.setAuthenticationFailureHandler(SecurityConfig::onAuthenticationFailure);
    filter.setAuthenticationManager(authenticationManager());
    filter.setFilterProcessesUrl("/authorize/login");
    return filter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .exceptionHandling(exp -> exp
        .accessDeniedHandler(securityProblemSupport)
        .authenticationEntryPoint(securityProblemSupport)
      )
      .authorizeRequests(req -> req
        .antMatchers("/authorize/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/api/**").hasRole("USER")
        .anyRequest()
        .authenticated()
      )
      .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
      .formLogin(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().mvcMatchers("/public/**", "/h2-console/**")
      .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Override
  protected void configure(
    AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
      .userDetailsPasswordManager(userDetailsPasswordService)
      .passwordEncoder(passwordEncoder());
  }
}
