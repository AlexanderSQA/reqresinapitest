package com.api.managers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiModule {

  @Bean
  public UserManager getUserApi() {
    return new UserManager();
  }

  @Bean
  public ResourceManager getResourceApi() {
    return new ResourceManager();
  }

  @Bean
  public AuthManager getRegisterApi() {
    return new AuthManager();
  }

}
