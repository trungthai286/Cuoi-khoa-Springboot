package org.example.story_web.repository;

import org.example.story_web.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findBySlug(String slug);

}