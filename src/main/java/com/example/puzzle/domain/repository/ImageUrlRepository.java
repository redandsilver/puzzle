package com.example.puzzle.domain.repository;

import com.example.puzzle.domain.model.entity.ImageUrl;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {

  @Transactional
  void deleteByFileName(String fileName);

}
