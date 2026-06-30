package study.springbootdeveloper.dto;

import study.springbootdeveloper.domain.Article;

public record ArticleListViewResponse(
        Long id,
        String title,
        String content,
        String imageUrl
) {
    public static ArticleListViewResponse from(Article article) {
        return new ArticleListViewResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getImageUrl()
        );
    }
}
