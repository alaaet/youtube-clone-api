package com.alaaet.youtubeclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Document(value="Video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    private String id;
    private String title;
    private String description;
    private String userId;
    private AtomicInteger likes = new AtomicInteger(0);
    private AtomicInteger dislikes = new AtomicInteger(0);
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private AtomicInteger viewCount = new AtomicInteger(0);
    private String thumbnailUrl;
    private List<Comment> comments;

    public void incrementViewCount() {
        this.viewCount.incrementAndGet();
    }

    public void incrementLikes() {
        this.likes.incrementAndGet();
    }

    public void decrementLikes() {
        this.likes.decrementAndGet();
    }

    public void incrementDislikes() {
        this.dislikes.incrementAndGet();
    }
    public void decrementDislikes() {
        this.dislikes.decrementAndGet();
    }
}
