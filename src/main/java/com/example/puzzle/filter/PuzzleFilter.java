package com.example.puzzle.filter;

import com.example.puzzle.service.FilterService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class PuzzleFilter extends OncePerRequestFilter {

  private final FilterService filterService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    log.info("name : {}", name);
    Long puzzleId = Long.valueOf(request.getParameter("puzzleId"));
    filterService.validatePuzzlePermission(name, puzzleId);
    filterChain.doFilter(request, response);
  }

}
