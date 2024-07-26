package org.example.story_web;

import com.github.javafaker.Faker;

import com.github.slugify.Slugify;
import org.example.story_web.entity.Category;
import org.example.story_web.entity.Chapter;
import org.example.story_web.entity.Role;
import org.example.story_web.entity.Stories;
import org.example.story_web.entity.User;
import org.example.story_web.model.enums.StoriesType;
import org.example.story_web.repository.CategoryRepository;
import org.example.story_web.repository.ChapterRepository;
import org.example.story_web.repository.RoleRepository;
import org.example.story_web.repository.StoriesRepository;
import org.example.story_web.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class StoryWebApplicationTests {
    @Autowired
    private StoriesRepository storiesRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository; // Thêm repository cho Role
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void save_categories() {
        Slugify slugify = Slugify.builder().build();
        List<String> categories = Arrays.asList("Huyền huyễn", "Đô thị", "Tiên Hiệp", "Kiếm Hiệp", "Võng Du", "Lịch Sử", "Khoa Huyễn");
        for (String name : categories) {
            Category category = Category.builder()
                    .name(name)
                    .slug(slugify.slugify(name))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            categoryRepository.save(category);
        }
    }

    @Test
    void save_stories() {
        Faker faker = new Faker(new Locale("vi"));

        List<Category> allCategories = categoryRepository.findAll();

        for (int i = 0; i < 50; i++) {
            String title = faker.book().title();
            Boolean status = faker.bool().bool();
            String randomColor = String.format("%02x%02x%02x", faker.number().numberBetween(0, 256), faker.number().numberBetween(0, 256), faker.number().numberBetween(0, 256));

            // Shuffle the list of categories to ensure randomness
            Collections.shuffle(allCategories);
            // Get a sublist of up to 3 categories
            List<Category> randomCategories = allCategories.stream()
                    .limit(faker.number().numberBetween(1, 4))
                    .collect(Collectors.toList());

            Stories story = Stories.builder()
                    .title(title)
                    .slug(title.toLowerCase().replaceAll(" ", "-").replaceAll("[^a-z0-9-]", ""))
                    .description(faker.lorem().paragraph())
                    .author(faker.book().author())
                    .imageUrl("https://placehold.co/400x600?text=" + title.substring(0, 1).toUpperCase() + "&bg=" + randomColor)
                    .poster("https://placehold.co/1100x300?text=" + title.substring(0, 1).toUpperCase() + "&bg=" + randomColor)
                    .type(i % 3 == 0 ? StoriesType.TRANSLATING : i % 3 == 1 ? StoriesType.COMPLETED : StoriesType.PAUSE)
                    .status(status)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .publishedAt(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365)))
                    .categories(randomCategories)
                    .build();
            storiesRepository.save(story);
        }
    }

    @Test
    void save_chapters() {
        Faker faker = new Faker(new Locale("vi"));

        List<Stories> allStories = storiesRepository.findAll();

        allStories.forEach(story -> {
            int numChapters = faker.number().numberBetween(5, 31);
            LocalDateTime initialTime = LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365));

            List<String> chapterTitles = IntStream.range(0, numChapters)
                    .mapToObj(i -> faker.book().title())
                    .collect(Collectors.toList());

            List<Chapter> chapters = IntStream.range(0, numChapters)
                    .mapToObj(i -> {
                        String title = chapterTitles.get(i);
                        String content = faker.lorem().paragraphs(faker.number().numberBetween(7, 14)).stream()
                                .collect(Collectors.joining(" ")); // Combine paragraphs to create content of 500-1000 words
                        Boolean status = i < numChapters - 2;
                        Double price = status ? 0.0 : 10.0;
                        LocalDateTime publishedAt = (i >= numChapters - 2) ? LocalDateTime.now().minusDays(2) : initialTime.plusHours(i);

                        return Chapter.builder()
                                .chapterNumber(i + 1)
                                .title(title)
                                .content(content)
                                .status(status)
                                .price(price)
                                .createdAt(initialTime.plusHours(i))
                                .updatedAt(LocalDateTime.now())
                                .publishedAt(publishedAt)
                                .story(story)
                                .build();
                    })
                    .collect(Collectors.toList());
            chapterRepository.saveAll(chapters);
        });
    }
    @Test
    void save_roles() {
        Role userRole = Role.builder().name("USER").build();
        Role adminRole = Role.builder().name("ADMIN").build();
        roleRepository.save(userRole);
        roleRepository.save(adminRole);
    }
    @Test
    void save_users() {
        // Tìm các vai trò "USER" và "ADMIN" từ cơ sở dữ liệu
        Role userRole = roleRepository.findByName("USER").orElse(null);
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);

        // Tạo người dùng với vai trò "USER" và "ADMIN"
        User user1 = User.builder()
                .name("thai")
                .email("thai@gmail.com")
                .password(passwordEncoder.encode("123"))
                .roles(List.of(userRole, adminRole)) // Gán cả hai vai trò
                .avatar("https://placehold.co/600x400?text=Hien") // Thêm avatar
                .balance(1000.0) // Thêm số dư
                .enabled(true) // Đảm bảo rằng tài khoản được kích hoạt
                .build();
        userRepository.save(user1);

        // Tạo người dùng với chỉ vai trò "USER"
        User user2 = User.builder()
                .name("dung")
                .email("dung@gmail.com")
                .password(passwordEncoder.encode("123"))
                .roles(List.of(userRole)) // Gán chỉ vai trò "USER"
                .avatar("https://placehold.co/600x400?text=Duy") // Thêm avatar
                .balance(500.0) // Thêm số dư
                .enabled(true) // Đảm bảo rằng tài khoản được kích hoạt
                .build();
        userRepository.save(user2);
    }

}
