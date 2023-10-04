package com.alaaet.youtubeclone.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService{

    public static final String BUCKET_NAME = "youtube-clone-videos-alaaet";
    private final AmazonS3Client awsS3Client;

    @Override
    public String uploadFile(MultipartFile file) {
        // Upload file to AWS S3
        // Prepare a unique key for the file
        var fileNameExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
        var key = UUID.randomUUID().toString()+"."+ fileNameExt;
        var metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Upload file to AWS S3
        try {
            awsS3Client.putObject(BUCKET_NAME, key, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading file to S3", e);
        }

        // Access control list
        awsS3Client.setObjectAcl(BUCKET_NAME, key, com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead);
        return awsS3Client.getResourceUrl(BUCKET_NAME, key);
    }
}
