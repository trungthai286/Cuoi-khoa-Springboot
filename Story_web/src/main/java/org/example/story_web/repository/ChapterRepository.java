package org.example.story_web.repository;


import org.example.story_web.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    List<Chapter> findByStory_IdOrderByIdAsc(Integer storyId);
    Optional<Chapter> findByStory_IdAndChapterNumber(Integer storyId, Integer chapterNumber);
}

