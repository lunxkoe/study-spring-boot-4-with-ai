package study.springbootdeveloper.dto;

import study.springbootdeveloper.domain.Article;

public record ArticleResponse(
        String title,
        String content,
        String imageUrl
) {
    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getTitle(),
                article.getContent(),
                article.getImageUrl()
        );
    }
}
