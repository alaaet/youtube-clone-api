package com.alaaet.youtubeclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Document(value="User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private Set<String> subscribedToUsers= ConcurrentHashMap.newKeySet();
    private Set<String> subscribers= ConcurrentHashMap.newKeySet();
    private Set<String> videoHistory= ConcurrentHashMap.newKeySet();
    private Set<String> likedVideos = ConcurrentHashMap.newKeySet();
    private Set<String> dislikedVideos = ConcurrentHashMap.newKeySet();
    private String sub;


    public void addToLikedVideos(String videoId) {
        this.likedVideos.add(videoId);
    }

    public void removeFromLikedVideos(String videoId) {
        this.likedVideos.remove(videoId);
    }
    public void addToDislikedVideos(String videoId) {
        this.dislikedVideos.add(videoId);
    }

    public void removeFromDislikedVideos(String videoId) {
        this.dislikedVideos.remove(videoId);
    }

    public void addToViewedVideos(String videoId) {
        this.videoHistory.add(videoId);
    }

    public void addToSubscribedToUsers(String userId) {
        this.subscribedToUsers.add(userId);
    }

    public void addToSubscribers(String userId) {
        this.subscribers.add(userId);
    }

    public void removeFromSubscribedToUsers(String userId) {
        this.subscribedToUsers.remove(userId);
    }

    public void removeFromSubscribers(String id) {
        this.subscribers.remove(id);
    }
}
