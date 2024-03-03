package persistence.sql.entity;

public interface EntityManager {

    <T> T find(Class<T> clazz, Long Id);

    Object persist(Object entity);

    Object merge(Object entity);

    void remove(Object entity);

    boolean isDirty(Object entity);
}
