package com.example.tp.service;

import com.example.tp.model.Article;
import com.example.tp.model.Comment;
import com.example.tp.repository.ArticleRepository;
import com.example.tp.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public Article updateArticle(Long id, Article updated) {
        Article existing = articleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Article introuvable avec l'id : " + id));
        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        return articleRepository.save(existing);
    }

    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new EntityNotFoundException("Article introuvable avec l'id : " + id);
        }
        articleRepository.deleteById(id);
    }

    public Comment addComment(Long articleId, Comment comment) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new EntityNotFoundException("Article introuvable avec l'id : " + articleId));
        comment.setArticle(article);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Commentaire introuvable avec l'id : " + commentId);
        }
        commentRepository.deleteById(commentId);
    }
}
