package com.alaaet.youtubeclone.repository;

import com.alaaet.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository  extends MongoRepository<Video, String> {
}
