package com.alaaet.youtubeclone.service;

import com.alaaet.youtubeclone.dto.CommentDto;
import com.alaaet.youtubeclone.dto.UploadVideoResponse;
import com.alaaet.youtubeclone.dto.VideoDto;
import com.alaaet.youtubeclone.model.Comment;
import com.alaaet.youtubeclone.model.Video;
import com.alaaet.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final UserService userService;
    private final VideoRepository videoRepository;
    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {
        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);
        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto) {
        // Find the video by video id from the database
        Video savedVideo = getVideoById(videoDto.getId());
        // Map the videoDto to the video object
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());

        // save the video object to the database
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        // Find the video by video id from the database
        Video savedVideo = getVideoById(videoId);
        // Upload the thumbnail to S3
        String thumbnailUrl = s3Service.uploadFile(file);
        // Set the thumbnail url to the video object
        savedVideo.setThumbnailUrl(thumbnailUrl);
        // Save the video object to the database
        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    Video getVideoById(String id) {
        return videoRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Video not found by id: "+id));
    }

    public VideoDto getVideoDetails(String videoId) {
        // Find the video by video id from the database
        Video savedVideo = getVideoById(videoId);
        // Increment the view count
        increaseViewCount(savedVideo);
        // Add videoId to the user's viewed videos history
        userService.addToViewedVideos(videoId);
        // Map the video object to the videoDto and return the videoDto
        return mapToVideoDto(savedVideo);
    }

    private void increaseViewCount(Video video) {
        video.incrementViewCount();
        videoRepository.save(video);
    }
    public VideoDto likeVideo(String videoId) {
        // Get video by video id
        Video video = getVideoById(videoId);
        if(userService.ifLikedVideo(videoId)) {
            // If the user has already liked the video, decrement the likes
            video.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        } else if(userService.ifDislikedVideo(videoId)) {
            // If the user has already disliked the video, increment the likes and decrement the dislikes
            video.incrementLikes();
            video.decrementDislikes();
            userService.removeFromDislikedVideos(videoId);
            userService.addToLikedVideos(videoId);
        }else{
            // If the user has not liked or disliked the video, increment the likes
            video.incrementLikes();
            userService.addToLikedVideos(videoId);
        }
        videoRepository.save(video);

        return mapToVideoDto(video);
    }

    private VideoDto mapToVideoDto(Video video){
        var videoDto = new VideoDto();
        videoDto.setId(video.getId());
        videoDto.setTitle(video.getTitle());
        videoDto.setDescription(video.getDescription());
        videoDto.setTags(video.getTags());
        videoDto.setVideoUrl(video.getVideoUrl());
        videoDto.setVideoStatus(video.getVideoStatus());
        videoDto.setThumbnailUrl(video.getThumbnailUrl());
        videoDto.setLikeCount(video.getLikes().get());
        videoDto.setDislikeCount(video.getDislikes().get());
        videoDto.setViewCount(video.getViewCount().get());
        return videoDto;
    }

    public VideoDto dislikeVideo(String videoId) {
        // Get video by video id
        Video video = getVideoById(videoId);
        if(userService.ifDislikedVideo(videoId)) {
            // If the user has already disliked the video, decrement the dislikes
            video.decrementDislikes();
            userService.removeFromDislikedVideos(videoId);
        } else if(userService.ifLikedVideo(videoId)) {
            // If the user has already liked the video, increment the dislikes and decrement the likes
            video.decrementLikes();
            video.incrementDislikes();
            userService.removeFromLikedVideos(videoId);
            userService.addToDislikedVideos(videoId);
        }else{
            // If the user has not liked or disliked the video, increment the dislikes
            video.incrementDislikes();
            userService.addToDislikedVideos(videoId);
        }
        videoRepository.save(video);

        return mapToVideoDto(video);
    }

    public void addComment(String videoId, CommentDto commentDto) {

        Video video = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setAuthorId(commentDto.getAuthorId());
        comment.setText(commentDto.getCommentText());
        video.addComment(comment);
        videoRepository.save(video);

    }

    public List<CommentDto> getAllComments(String videoId) {
        Video video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();
     return commentList.stream().map(this::mapToCommentDto).toList();
    }

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText(comment.getText());
        commentDto.setAuthorId(comment.getAuthorId());
        return commentDto;
    }

    public List<VideoDto> getAllVideos() {
        List<Video> videoList = videoRepository.findAll();
        return videoList.stream().map(this::mapToVideoDto).toList();
    }
}
