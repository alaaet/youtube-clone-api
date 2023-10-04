package com.alaaet.youtubeclone.service;

import com.alaaet.youtubeclone.repository.UserRepository;
import com.alaaet.youtubeclone.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrenyUser() {
        String sub = ((Jwt)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaim("sub");
        return userRepository.findBySub(sub).orElseThrow(()->new IllegalArgumentException("User not found with sub - "+sub));
    }

    public void addToLikedVideos(String videoId) {
        User usr = getCurrenyUser();
        usr.addToLikedVideos(videoId);
        userRepository.save(usr);
    }

    public void addToDislikedVideos(String videoId) {
        User usr = getCurrenyUser();
        usr.addToDislikedVideos(videoId);
        userRepository.save(usr);
    }


    public boolean ifLikedVideo(String videoId) {
        User usr = getCurrenyUser();
        return usr.getLikedVideos().stream().anyMatch(v->v.equals(videoId));
    }
    public boolean ifDislikedVideo(String videoId) {
        User usr = getCurrenyUser();
        return usr.getDislikedVideos().stream().anyMatch(v->v.equals(videoId));
    }

    public void removeFromLikedVideos(String videoId) {
        User usr = getCurrenyUser();
        usr.removeFromLikedVideos(videoId);
        userRepository.save(usr);
    }

    public void removeFromDislikedVideos(String videoId) {
        User usr = getCurrenyUser();
        usr.removeFromDislikedVideos(videoId);
        userRepository.save(usr);
    }

    public void addToViewedVideos(String videoId) {
        User usr = getCurrenyUser();
        usr.addToViewedVideos(videoId);
        userRepository.save(usr);
    }
}
