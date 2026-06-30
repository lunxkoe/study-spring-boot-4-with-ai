package study.springbootdeveloper.dto;

import study.springbootdeveloper.domain.Article;

public record AddArticleRequest(
        String title,
        String content,
        String imageUrl
) {

    public Article toEntity() {
        return Article.builder()
                .title(this.title)
                .content(this.content)
                .imageUrl(this.imageUrl)
                .build();
    }
}
