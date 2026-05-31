package com.example.tp.service;

import com.example.tp.model.Article;
import com.example.tp.model.Comment;
import com.example.tp.repository.ArticleRepository;
import com.example.tp.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du BlogService")
class BlogServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private BlogService blogService;

    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        article = new Article(1L, "Mon premier article", "Contenu de l'article", null);
        comment = new Comment(1L, "Super article !", "Alice", null);
    }

    @Test
    @DisplayName("getAllArticles() retourne tous les articles")
    void getAllArticles_shouldReturnAll() {
        when(articleRepository.findAll()).thenReturn(List.of(article));

        List<Article> result = blogService.getAllArticles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Mon premier article");
    }

    @Test
    @DisplayName("createArticle() sauvegarde l'article")
    void createArticle_shouldSave() {
        when(articleRepository.save(any())).thenReturn(article);

        Article result = blogService.createArticle(article);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Mon premier article");
    }

    @Test
    @DisplayName("addComment() ajoute le commentaire à l'article")
    void addComment_whenArticleExists_shouldAddComment() {
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(commentRepository.save(any())).thenReturn(comment);

        Comment result = blogService.addComment(1L, comment);

        assertThat(result).isNotNull();
        assertThat(result.getAuthor()).isEqualTo("Alice");
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("addComment() lève exception si article inexistant")
    void addComment_whenArticleNotExists_shouldThrowException() {
        when(articleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> blogService.addComment(99L, comment))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("99");
    }

    @Test
    @DisplayName("deleteArticle() supprime l'article existant")
    void deleteArticle_whenExists_shouldDelete() {
        when(articleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(articleRepository).deleteById(1L);

        blogService.deleteArticle(1L);

        verify(articleRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteArticle() lève exception si article inexistant")
    void deleteArticle_whenNotExists_shouldThrowException() {
        when(articleRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> blogService.deleteArticle(99L))
            .isInstanceOf(EntityNotFoundException.class);
        verify(articleRepository, never()).deleteById(any());
    }
}
