package org.example.story_web.repository;

import org.example.story_web.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByStories_IdOrderByCreatedAtDesc(Integer storyId);
}