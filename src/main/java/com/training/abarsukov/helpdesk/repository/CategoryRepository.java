package com.training.abarsukov.helpdesk.repository;

import com.training.abarsukov.helpdesk.model.Category;
import com.training.abarsukov.helpdesk.repository.generic.Repository;

import java.util.List;

public interface CategoryRepository extends Repository<Category, Long> {
  List<Category> findAll();
}
