package org.example.story_web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.story_web.model.enums.StoriesType;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String title;

    String slug;

    @Column(columnDefinition = "TEXT")
    String description;

    String author;
    String poster;
    String imageUrl;

    @Enumerated(EnumType.STRING)
    StoriesType type;

    Boolean status;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime publishedAt;

    @ManyToMany
    @JoinTable(
            name = "stories_category",
            joinColumns = @JoinColumn(name = "stories_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    List<Category> categories;
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Chapter> chapters;


    @JsonIgnore
    @OneToMany(mappedBy = "stories", cascade = CascadeType.ALL)
    List<Review> reviews;

}
