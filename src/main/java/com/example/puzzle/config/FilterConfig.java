package com.example.puzzle.config;

import com.example.puzzle.filter.CommentFilter;
import com.example.puzzle.filter.PieceFilter;
import com.example.puzzle.filter.PuzzleFilter;
import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class FilterConfig {

  private final PieceFilter pieceFilter;
  private final CommentFilter commentFilter;
  private final PuzzleFilter puzzleFilter;

  @Bean
  public FilterRegistrationBean<Filter> registerPieceFilter() {
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter(pieceFilter);
    bean.addUrlPatterns("/piece/edit/*");
    bean.addUrlPatterns("/piece/delete/*");
    bean.setOrder(1);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<Filter> registerCommentFilter() {
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter(commentFilter);
    bean.addUrlPatterns("/comment/edit/*");
    bean.addUrlPatterns("/comment/delete/*");
    bean.setOrder(2);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<Filter> registerPuzzleFilter() {
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter(puzzleFilter);
    bean.addUrlPatterns("/puzzle/edit/*");
    bean.addUrlPatterns("/puzzle/delete/*");
    bean.setOrder(3);
    return bean;
  }
}
