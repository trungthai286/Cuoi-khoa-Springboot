package org.example.story_web.repository;

import org.example.story_web.entity.Category;
import org.example.story_web.entity.Stories;
import org.example.story_web.model.enums.StoriesType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoriesRepository extends JpaRepository<Stories, Integer> {

    Page<Stories> findByTypeAndStatus(StoriesType type, Boolean status, Pageable pageable);

    Optional<Stories> findByIdAndStatus(Integer id, Boolean status);

    List<Stories> findAllByStatus(Boolean status);

    Page<Stories> findByCategoriesAndStatus(Category category, Pageable pageable, Boolean status);


}
