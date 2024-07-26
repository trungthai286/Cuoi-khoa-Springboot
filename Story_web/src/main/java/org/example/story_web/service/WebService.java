package org.example.story_web.service;

import lombok.RequiredArgsConstructor;
import org.example.story_web.entity.Category;
import org.example.story_web.entity.Stories;
import org.example.story_web.model.enums.StoriesType;
import org.example.story_web.repository.StoriesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WebService {
    private final StoriesRepository storiesRepository;

    public Page<Stories> findbyType(StoriesType type, Boolean status, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("publishedAt").descending());
        return storiesRepository.findByTypeAndStatus(type, status, pageable);
    }

    public List<Stories> getAllStory(Boolean status) {
        return storiesRepository.findAllByStatus(true);
    }

    public Stories getStoryDetail(Integer id) {
        return storiesRepository.findByIdAndStatus(id, true).orElse(null);
    }

    public Stories getRandomStory() {
        List<Stories> storiesList = storiesRepository.findAllByStatus(true);
        long count = storiesList.size();
        if (count == 0) {
            return null; // Hoặc throw exception tùy yêu cầu
        }
        Random random = new Random();
        int randomIndex = random.nextInt((int) count);
        return storiesList.get(randomIndex);
    }

    public Page<Stories> getStoriesByCategory(Category category, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return storiesRepository.findByCategoriesAndStatus(category, pageRequest, true);
    }
}
