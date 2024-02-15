package com.example.puzzle.filter;

import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import com.example.puzzle.service.FilterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommentFilter extends OncePerRequestFilter {
   private final FilterService filterService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("name : {}",name);
        Long commentId = Long.valueOf(request.getParameter("commentId"));
        String method = request.getMethod();

        if(!filterService.isCommentWriterOrPieceWriter(method,name,commentId)){
            throw new CustomException(ErrorCode.WRONG_ACCESS);
        }

        filterChain.doFilter(request,response);
    }

}
