package com.training.abarsukov.helpdesk.repository.generic;

import java.util.Optional;

public interface Repository<T, ID> {
  T save(T entity);

  Optional<T> findById(ID id);

  void delete(T entity);

  void deleteById(ID id);

  T update(T entity);
}
