package com.example.puzzle.config;

import com.example.puzzle.filter.PieceFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;


@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final PieceFilter pieceFilter;
    @Bean
    public FilterRegistrationBean<Filter> registerPieceFilter(){
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(pieceFilter);
        bean.addUrlPatterns("/piece/edit");
        bean.addUrlPatterns("/piece/delete");
        return bean;
    }
}
