package study.springbootdeveloper.dto;

public record UpdateArticleRequest(
        String title,
        String content,
        String imageUrl
) {

    public UpdateArticleRequest(String title, String content) {
        this(title, content, null);
    }
}
