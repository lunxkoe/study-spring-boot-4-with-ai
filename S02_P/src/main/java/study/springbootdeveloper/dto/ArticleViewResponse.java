package study.springbootdeveloper.dto;

import study.springbootdeveloper.domain.Article;

import java.time.LocalDateTime;

public record ArticleViewResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public ArticleViewResponse() {
        this(null, "", "", LocalDateTime.now());
    }

    public static ArticleViewResponse from(Article article) {
        return new ArticleViewResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt()
        );
    }
}
