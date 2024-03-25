package com.nickesqueda.socialmediademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class SocialMediaDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SocialMediaDemoApplication.class, args);
  }

}
