package com.example.puzzle.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final AmazonS3Client amazonS3Client;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String uploadImage(MultipartFile multipartFile, String key) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    try {
      objectMetadata.setContentLength(multipartFile.getInputStream().available());
      amazonS3Client.putObject(bucket, key, multipartFile.getInputStream(), objectMetadata);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.TOO_LARGE);
    }
    return amazonS3Client.getUrl(bucket, key).toString();
  }

  public String makefileName(String originalFilename) {
    return UUID.randomUUID().toString()
        + originalFilename;
  }

  public void deleteImageObject(String key) {
    amazonS3Client.deleteObject(bucket, key);
  }
}
