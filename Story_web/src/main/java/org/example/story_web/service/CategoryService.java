package org.example.story_web.service;

import lombok.RequiredArgsConstructor;
import org.example.story_web.entity.Category;
import org.example.story_web.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}
