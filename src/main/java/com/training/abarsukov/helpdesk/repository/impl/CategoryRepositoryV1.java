package com.training.abarsukov.helpdesk.repository.impl;

import com.training.abarsukov.helpdesk.model.Category;
import com.training.abarsukov.helpdesk.repository.CategoryRepository;
import com.training.abarsukov.helpdesk.repository.generic.abstracts.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepositoryV1 extends AbstractRepository<Category, Long>
    implements CategoryRepository {

  private static final String QUERY_TO_GET_CATEGORIES = "from Category";

  @Override
  public List<Category> findAll() {
    return super.find(QUERY_TO_GET_CATEGORIES);
  }
}
