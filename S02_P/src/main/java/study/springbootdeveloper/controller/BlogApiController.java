package study.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.springbootdeveloper.domain.Article;
import study.springbootdeveloper.dto.*;
import study.springbootdeveloper.service.BlogService;
import study.springbootdeveloper.service.ThumbnailGeneratorService;
import study.springbootdeveloper.service.WritingAssistantService;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/articles")
public class BlogApiController {

    private final BlogService blogService;
    private final WritingAssistantService writingAssistantService;
    private final ThumbnailGeneratorService thumbnailGeneratorService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article response = blogService.save(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> response = blogService.findAll().stream()
                .map(ArticleResponse::from)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {
        Article article = blogService.findById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ArticleResponse.from(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        blogService.delete(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        Article response = blogService.update(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // LLM Function
    @PostMapping("/api/ai-suggestions")
    public ResponseEntity<WritingSuggestionResponse> writingAssist(@RequestBody WritingSuggestionRequest request) {
        WritingSuggestionResponse response = writingAssistantService.getWritingAssist(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/api/ai-thumbnails")
    public ResponseEntity<GeneratorThumbnailResponse> thumbnailGenerator(@RequestBody GeneratorThumbnailRequest request) {
        GeneratorThumbnailResponse response = thumbnailGeneratorService.generatorThumbnail(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
