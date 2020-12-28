package dev.bigwig.uaa.rest;

import dev.bigwig.uaa.domain.dto.UserDTO;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/authorize")
@RestController
public class AuthorizeResource {

  @PostMapping("/register")
  public UserDTO register(@Valid @RequestBody UserDTO userDTO) {
    return userDTO;
  }
}
