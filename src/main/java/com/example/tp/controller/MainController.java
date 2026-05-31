package com.example.tp.controller;

import com.example.tp.model.Article;
import com.example.tp.model.Comment;
import com.example.tp.model.Product;
import com.example.tp.service.BlogService;
import com.example.tp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BlogService blogService;

    // ===================== INDEX =====================

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ===================== PRODUITS =====================

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("newProduct", new Product());
        return "products";
    }

    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute("newProduct") Product product,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "products";
        }
        productService.createProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Produit ajouté avec succès !");
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(p -> model.addAttribute("product", p));
        return "product-edit";
    }

    @PostMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id,
                              @Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "product-edit";
        }
        productService.updateProduct(id, product);
        redirectAttributes.addFlashAttribute("successMessage", "Produit modifié avec succès !");
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Produit supprimé avec succès !");
        return "redirect:/products";
    }

    // ===================== BLOG =====================

    @GetMapping("/blog")
    public String listArticles(Model model) {
        model.addAttribute("articles", blogService.getAllArticles());
        model.addAttribute("newArticle", new Article());
        return "blog";
    }

    @PostMapping("/blog/add")
    public String addArticle(@Valid @ModelAttribute("newArticle") Article article,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("articles", blogService.getAllArticles());
            return "blog";
        }
        blogService.createArticle(article);
        redirectAttributes.addFlashAttribute("successMessage", "Article publié avec succès !");
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String viewArticle(@PathVariable Long id, Model model) {
        model.addAttribute("newComment", new Comment());
        blogService.getArticleById(id).ifPresent(article -> model.addAttribute("article", article));
        return "article-detail";
    }

    @PostMapping("/blog/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @Valid @ModelAttribute("newComment") Comment comment,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            blogService.getArticleById(id).ifPresent(article -> model.addAttribute("article", article));
            return "article-detail";
        }
        blogService.addComment(id, comment);
        redirectAttributes.addFlashAttribute("successMessage", "Commentaire ajouté !");
        return "redirect:/blog/" + id;
    }

    @PostMapping("/blog/{id}/delete")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        blogService.deleteArticle(id);
        redirectAttributes.addFlashAttribute("successMessage", "Article supprimé avec succès !");
        return "redirect:/blog";
    }
}
