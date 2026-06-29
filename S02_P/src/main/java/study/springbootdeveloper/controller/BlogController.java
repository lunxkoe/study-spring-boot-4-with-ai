package study.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springbootdeveloper.domain.Article;
import study.springbootdeveloper.dto.AddArticleRequest;
import study.springbootdeveloper.service.BlogService;

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
}
