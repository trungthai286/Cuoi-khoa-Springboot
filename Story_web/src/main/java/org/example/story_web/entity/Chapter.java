package org.example.story_web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chapters")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    Integer chapterNumber;

    @Column(nullable = false)
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(nullable = false)
    Boolean status;

    @Column(nullable = false)
    Double price;

    @Column(nullable = false)
    LocalDateTime createdAt;

    LocalDateTime updatedAt;
    LocalDateTime publishedAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    Stories story;
}
