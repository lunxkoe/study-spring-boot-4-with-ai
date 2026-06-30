package study.springbootdeveloper.dto;

public record UpdateArticleRequest(
        String title,
        String content,
        String imageUrl
) {
}
