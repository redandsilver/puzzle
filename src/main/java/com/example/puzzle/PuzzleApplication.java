package com.example.puzzle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ServletComponentScan
@SpringBootApplication
@EnableJpaAuditing
@EnableAutoConfiguration
public class PuzzleApplication {

  public static void main(String[] args) {

    SpringApplication.run(PuzzleApplication.class, args);
  }

}
