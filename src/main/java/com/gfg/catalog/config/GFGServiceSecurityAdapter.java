package com.gfg.catalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class GFGServiceSecurityAdapter extends WebSecurityConfigurerAdapter {

  @Autowired
  private AuthenticationEntryPoint authEntryPoint;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic()
        .authenticationEntryPoint(authEntryPoint);
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // gfgcatalogpass
    auth.inMemoryAuthentication().withUser("gfgcatalog")
        .password("$2a$04$PjMAl.pgbUmkjQOIYWyF9eW/b0d1vHmqa9wkq5fv7zQsTZDW8hdPi").roles("USER");
  }

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

}
