package dev.bigwig.uaa.service;

import dev.bigwig.uaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

  private final UserRepository userRepository;

  @Override
  public UserDetails updatePassword(
    UserDetails userDetails, String newPassword) {
    return userRepository.findByUsername(userDetails.getUsername())
      .map(user -> (UserDetails) userRepository.save(user.withPassword(newPassword)))
      .orElse(userDetails);
  }
}
