package dev.bigwig.uaa.config;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(
    ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/webjars/**")
      .addResourceLocations("/webjars/")
      .resourceChain(false);
    registry.setOrder(1);
  }

  @Override
  public void addViewControllers(
    ViewControllerRegistry registry) {
    registry.addViewController("/login").setViewName("login");
    registry.setOrder(HIGHEST_PRECEDENCE);
  }
}
