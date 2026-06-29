package study.springbootdeveloper.dto;

import study.springbootdeveloper.domain.Article;

public record AddArticleRequest(
        String title,
        String content
) {

    public Article toEntity() {
        return Article.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
