package study.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import study.springbootdeveloper.domain.Article;
import study.springbootdeveloper.dto.ArticleListViewResponse;
import study.springbootdeveloper.dto.ArticleViewResponse;
import study.springbootdeveloper.service.BlogService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> response = blogService.findAll().stream()
                .map(ArticleListViewResponse::from)
                .toList();

        model.addAttribute("articles", response);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article response = blogService.findById(id);

        model.addAttribute("article", response);

        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            // 생성
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article response = blogService.findById(id);
            model.addAttribute("article", ArticleViewResponse.from(response));
        }

        return "newArticle";
    }
}
