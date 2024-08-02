package org.example.story_web.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.example.story_web.entity.*;
import org.example.story_web.exception.ResourceNotFoundException;
import org.example.story_web.model.enums.StoriesType;
import org.example.story_web.repository.UserRepository;
import org.example.story_web.response.VerifyResponse;
import org.example.story_web.security.IsUser;
import org.example.story_web.service.*;
import org.springframework.data.domain.Page;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class WebController {
    private final WebService webService;
    private final CategoryService categoryService;
    private final ChapterService chapterService;
    private final AuthService authService;
    private final ReviewService reviewService;
private final UserRepository userRepository;

    @IsUser
    @GetMapping("/user")
    public String getUser() {
        return "web/user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String getAdmin() {
        return "web/admin";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "web/dang-nhap";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "web/dang-ky";
    }

    // http://localhost:8080/xac-thuc-tai-khoan?token=e35bcf61-033f-4162-8321-7110cc3ba231
    @GetMapping("/xac-thuc-tai-khoan")
    public String getVerifyAccount(@RequestParam String token, Model model) {
        VerifyResponse response = authService.verifyAccount(token);
        model.addAttribute("response", response);
        return "verify-account";
    }

    @GetMapping
    public String getHomepage(Model model) {
        List<Stories> dsTruyenHoanThanh = webService.findbyType(StoriesType.COMPLETED, true, 1, 6).getContent();
        model.addAttribute("dsTruyenHoanThanh", dsTruyenHoanThanh);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Stories randomStory = webService.getRandomStory();
        model.addAttribute("randomStory", randomStory);
        return "web/story-index";

    }

    @GetMapping("/truyen-hoan-thanh")
    public String getStoryCompleted(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "12") Integer limit) {
        Page<Stories> pageData = webService.findbyType(StoriesType.COMPLETED, true, page, limit);
        model.addAttribute("pageData", pageData);
        model.addAttribute("currentPage", page);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        Stories randomStory = webService.getRandomStory();
        model.addAttribute("randomStory", randomStory);
        return "web/truyen-hoan-thanh";
    }

    @GetMapping("/truyen/{id}")
    public String getStoryDetail(Model model, @PathVariable Integer id) {


        // Trả về thông tin truyen
        Stories stories = webService.getStoryDetail(id);
        model.addAttribute("stories", stories);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Chapter> chapter = chapterService.getChapterListOfStory(id);
        model.addAttribute("chapter", chapter);
        Stories randomStory = webService.getRandomStory();
        model.addAttribute("randomStory", randomStory);
        List<Review> reviews = reviewService.getReviewsByStory(id);
        model.addAttribute("reviews", reviews);

        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        model.addAttribute("currentUser", currentUser);
        return "web/chi-tiet-truyen";

    }

    @GetMapping("/theloai/{slug}")
    public String getCategory(Model model, @PathVariable String slug, @RequestParam(defaultValue = "1") Integer page) {
        Category category = categoryService.findBySlug(slug);
        Page<Stories> pageData = webService.getStoriesByCategory(category, page);
        model.addAttribute("pageData", pageData);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("categorySlug", category.getSlug());
        Stories randomStory = webService.getRandomStory();
        model.addAttribute("randomStory", randomStory);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "web/the-loai";
    }

    @GetMapping("/doctruyen/{storyId}/chuong-so/{chapterNumber}")
    public String getChapterDetailByNumber(Model model, @PathVariable Integer storyId, @PathVariable Integer chapterNumber) {
        Stories stories = webService.getStoryDetail(storyId);
        model.addAttribute("stories", stories);
        Chapter chapter = chapterService.getChapterByNumber(storyId, chapterNumber);
        model.addAttribute("chapter", chapter);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        Stories randomStory = webService.getRandomStory();
        model.addAttribute("randomStory", randomStory);
        // Thêm danh sách các chương của truyện vào model
        List<Chapter> chapters = chapterService.getChapterListOfStory(storyId);
        model.addAttribute("chapters", chapters);

        // Debug: In ra số lượng chương
        System.out.println("Số lượng chương: " + chapters.size());
        for (Chapter chap : chapters) {
            System.out.println("Chương: " + chap.getTitle());
        }
        return "web/doc-truyen";
    }

    @GetMapping("/thong-tin-ca-nhan")
    public String getProfilePage(Model model) {
        return "web/thong-tin-ca-nhan";
    }
}
