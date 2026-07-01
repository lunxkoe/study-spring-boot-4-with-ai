package study.springbootdeveloper.dto;

import study.springbootdeveloper.domain.Article;

import java.time.LocalDateTime;

public record AddArticleRequest(
        String title,
        String content,
        String imageUrl
) {

    public AddArticleRequest(String title, String content) {
        this(title, content, null);
    }

    public Article toEntity(String author) {
        return Article.builder()
                .author(author)
                .title(this.title)
                .content(this.content)
                .imageUrl(this.imageUrl)
                .build();
    }
}
