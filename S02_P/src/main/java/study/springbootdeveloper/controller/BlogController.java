package study.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.springbootdeveloper.domain.Article;
import study.springbootdeveloper.dto.AddArticleRequest;
import study.springbootdeveloper.dto.ArticleResponse;
import study.springbootdeveloper.dto.UpdateArticleRequest;
import study.springbootdeveloper.service.BlogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class BlogController {

    private final BlogService blogService;

    @PostMapping()
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article response = blogService.save(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping()
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> response = blogService.findAll().stream()
                .map(ArticleResponse::from)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {
        Article article = blogService.findById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ArticleResponse.from(article));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        blogService.delete(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        Article response = blogService.update(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
