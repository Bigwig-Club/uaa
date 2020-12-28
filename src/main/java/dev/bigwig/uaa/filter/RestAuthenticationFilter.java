package dev.bigwig.uaa.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class RestAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    UsernamePasswordAuthenticationToken authRequest;
    try {
      InputStream is = request.getInputStream();
      val jsonNode = objectMapper.readTree(is);
      String username = jsonNode.get("username").textValue();
      String password = jsonNode.get("password").textValue();
      authRequest = new UsernamePasswordAuthenticationToken(username, password);
    } catch (IOException e) {
      e.printStackTrace();
      throw new BadCredentialsException("用户名或密码错误");
    }

    setDetails(request, authRequest);
    return this.getAuthenticationManager().authenticate(authRequest);
  }
}
