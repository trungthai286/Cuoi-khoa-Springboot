package org.example.story_web.service;


import lombok.RequiredArgsConstructor;
import org.example.story_web.entity.Review;
import org.example.story_web.entity.Stories;
import org.example.story_web.entity.User;
import org.example.story_web.exception.ResourceNotFoundException;
import org.example.story_web.model.request.CreateReviewRequest;
import org.example.story_web.model.request.UpdateReviewRequest;
import org.example.story_web.repository.ReviewRepository;
import org.example.story_web.repository.StoriesRepository;
import org.example.story_web.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoriesRepository storiesRepository;

    public List<Review> getReviewsByStory(Integer id) {
        return reviewRepository.findByStories_IdOrderByCreatedAtDesc(id);
    }

    // TODO: Validation hướng dẫn sau (SpringBoot Validation)
    public Review createReview(CreateReviewRequest request) {
        // TODO: Fix user. Về sau user chính là user đang đăng nhập
        Integer userId = 1;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Stories stories = storiesRepository.findById(request.getStoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .stories(stories)
                .build();

        reviewRepository.save(review);
        return review;
    }

    public Review updateReview(Integer id, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // TODO: Fix user. Về sau user chính là user đang đăng nhập
        Integer userId = 1;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can't update this review");
        }

        review.setContent(request.getContent());
        review.setRating(request.getRating());
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        return review;
    }

    public void deleteReview(Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // TODO: Fix user. Về sau user chính là user đang đăng nhập
        Integer userId = 1;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can't delete this review");
        }

        reviewRepository.delete(review);
    }
}
