package com.training.abarsukov.helpdesk.service.impl;

import com.training.abarsukov.helpdesk.model.Category;
import com.training.abarsukov.helpdesk.repository.CategoryRepository;
import com.training.abarsukov.helpdesk.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceV1 implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public List<Category> findAll() {
    return categoryRepository.findAll();
  }
}
