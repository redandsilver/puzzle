package com.example.puzzle.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping
    public ResponseEntity<String> gohome(){
        log.info("member home {}:",SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass());
        return ResponseEntity.ok("home!");
    }
}
