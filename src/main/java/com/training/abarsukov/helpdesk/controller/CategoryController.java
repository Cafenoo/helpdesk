package com.training.abarsukov.helpdesk.controller;

import com.training.abarsukov.helpdesk.model.Category;
import com.training.abarsukov.helpdesk.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<Category>> getCategories() {
    final List<Category> categories = categoryService.findAll();
    return ResponseEntity.ok(categories);
  }
}
