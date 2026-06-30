package study.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
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
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
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
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article foundArticle = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found " + id));

        foundArticle.update(request.title(), request.content(), request.imageUrl());

        return foundArticle;
    }
}
