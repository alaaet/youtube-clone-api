package com.alaaet.youtubeclone.service;

import com.alaaet.youtubeclone.repository.UserRepository;
import com.alaaet.youtubeclone.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String sub = ((Jwt)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaim("sub");
        return userRepository.findBySub(sub).orElseThrow(()->new IllegalArgumentException("User not found with sub - "+sub));
    }

    public void addToLikedVideos(String videoId) {
        User usr = getCurrentUser();
        usr.addToLikedVideos(videoId);
        userRepository.save(usr);
    }

    public void addToDislikedVideos(String videoId) {
        User usr = getCurrentUser();
        usr.addToDislikedVideos(videoId);
        userRepository.save(usr);
    }


    public boolean ifLikedVideo(String videoId) {
        User usr = getCurrentUser();
        return usr.getLikedVideos().stream().anyMatch(v->v.equals(videoId));
    }
    public boolean ifDislikedVideo(String videoId) {
        User usr = getCurrentUser();
        return usr.getDislikedVideos().stream().anyMatch(v->v.equals(videoId));
    }

    public void removeFromLikedVideos(String videoId) {
        User usr = getCurrentUser();
        usr.removeFromLikedVideos(videoId);
        userRepository.save(usr);
    }

    public void removeFromDislikedVideos(String videoId) {
        User usr = getCurrentUser();
        usr.removeFromDislikedVideos(videoId);
        userRepository.save(usr);
    }

    public void addToViewedVideos(String videoId) {
        User usr = getCurrentUser();
        usr.addToViewedVideos(videoId);
        userRepository.save(usr);
    }

    public void subscribeUser(String userId) {
        User currentUser = getCurrentUser();
        currentUser.addToSubscribedToUsers(userId);
        User destinyUser = getUserById(userId);
        destinyUser.addToSubscribers(currentUser.getId());
        userRepository.save(currentUser);
        userRepository.save(destinyUser);
    }

    public void unsubscribeUser(String userId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromSubscribedToUsers(userId);
        User destinyUser = getUserById(userId);
        destinyUser.removeFromSubscribers(currentUser.getId());
        userRepository.save(currentUser);
        userRepository.save(destinyUser);
    }

    public Set<String> getUserHistory(String userId) {
        User user = getUserById(userId);
        return user.getVideoHistory();
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id - " + userId));
    }
}
