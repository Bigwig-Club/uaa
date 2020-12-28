package dev.bigwig.uaa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bigwig.uaa.filter.RestAuthenticationFilter;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Import(SecurityProblemSupport.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final ObjectMapper objectMapper;

  private final SecurityProblemSupport securityProblemSupport;

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
    val idForDefault = "bcrypt";
    val encoders = Map.of(
      idForDefault, new BCryptPasswordEncoder(),
      "SHA-1", new MessageDigestPasswordEncoder("SHA-1")
    );
    return new DelegatingPasswordEncoder(idForDefault, encoders);
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
    web.ignoring().mvcMatchers("/public/**")
      .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }
}
