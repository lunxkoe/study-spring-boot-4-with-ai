package study.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springbootdeveloper.domain.Article;
import study.springbootdeveloper.dto.AddArticleRequest;
import study.springbootdeveloper.dto.UpdateArticleRequest;
import study.springbootdeveloper.repository.BlogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;

    @Transactional
    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    @Transactional
    public void delete(Long id) {
        Article foundArticle = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        authorizeArticleAuthor(foundArticle);
        blogRepository.delete(foundArticle);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article foundArticle = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found " + id));

        authorizeArticleAuthor(foundArticle);

        foundArticle.update(request.title(), request.content(), request.imageUrl());

        return foundArticle;
    }

    private static void authorizeArticleAuthor(Article article) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!article.getAuthor().equals(username)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
