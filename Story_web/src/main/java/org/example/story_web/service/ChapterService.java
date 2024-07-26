package org.example.story_web.service;

import lombok.RequiredArgsConstructor;
import org.example.story_web.entity.Chapter;
import org.example.story_web.repository.ChapterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;

    public List<Chapter> getChapterListOfStory(Integer storyId) {
        return chapterRepository.findByStory_IdOrderByIdAsc(storyId);
    }
    public Chapter getChapterByNumber(Integer storyId, Integer chapterNumber) {
        return chapterRepository.findByStory_IdAndChapterNumber(storyId, chapterNumber)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
    }
}
