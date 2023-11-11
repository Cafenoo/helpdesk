package com.training.abarsukov.helpdesk.repository.generic.abstracts;

import static java.text.MessageFormat.format;

import com.training.abarsukov.helpdesk.repository.generic.Repository;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {

  private static final String QUERY_TO_DELETE_BY_ID =
      "delete from {0} e "
          + "where e.id = :id";

  protected static final String AND = " and ";

  protected static final String WHERE = " where ";

  @PersistenceContext
  protected EntityManager entityManager;

  @Override
  public T save(T entity) {
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public Optional<T> findById(ID id) {
    final T entity = entityManager.find(getGenericClass(), id);
    return Optional.ofNullable(entity);
  }

  @Override
  public void delete(T entity) {
    entityManager.remove(entity);
  }

  @Override
  public void deleteById(ID id) {
    final String simpleClassName = getGenericClass().getSimpleName();
    final String query = format(QUERY_TO_DELETE_BY_ID, simpleClassName);
    final Map<String, Object> parameters = Map.of("id", id);

    updateByParameters(query, parameters);
  }

  @Override
  public T update(T entity) {
    return entityManager.merge(entity);
  }

  protected List<T> find(String stringQuery) {
    return createQuery(stringQuery).getResultList();
  }

  protected List<T> findByParameters(String stringQuery, Map<String, Object> parameters) {
    return createQuery(stringQuery, parameters).getResultList();
  }

  protected List<T> findByParameters(
      String stringQuery, Map<String, Object> parameters, int firstResult, int maxResult) {
    return createQuery(stringQuery, parameters, firstResult, maxResult).getResultList();
  }

  protected int updateByParameters(String stringQuery, Map<String, Object> parameters) {
    final Query query = entityManager.createQuery(stringQuery);
    parameters.forEach(query::setParameter);
    return query.executeUpdate();
  }

  private TypedQuery<T> createQuery(String stringQuery) {
    return entityManager.createQuery(stringQuery, getGenericClass());
  }

  private TypedQuery<T> createQuery(String stringQuery, Map<String, Object> parameters) {
    final TypedQuery<T> query = createQuery(stringQuery);
    parameters.forEach(query::setParameter);
    return query;
  }

  private TypedQuery<T> createQuery(
      String stringQuery, Map<String, Object> parameters, int firstResult, int maxResults) {
    return createQuery(stringQuery, parameters)
        .setFirstResult(firstResult)
        .setMaxResults(maxResults);
  }

  @SuppressWarnings("unchecked")
  private Class<T> getGenericClass() {
    return (Class<T>)
        ((ParameterizedType) getClass()
            .getGenericSuperclass())
            .getActualTypeArguments()[0];
  }
}
