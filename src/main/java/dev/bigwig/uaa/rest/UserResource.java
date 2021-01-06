package dev.bigwig.uaa.rest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserResource {

  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }

  @GetMapping("/principal")
  public String getPrincipal() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
