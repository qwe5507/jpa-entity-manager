package persistence.sql.entity;

import persistence.sql.entity.impl.EntityEntry;
import persistence.sql.entity.impl.EntityKey;

public interface PersistenceContext {
    Object getEntity(EntityKey key);

    void addEntity(EntityKey key, Object entity);

    void addEntity(EntityKey key, Object entity, EntityEntry entityEntry);

    void removeEntity(EntityKey key);

    boolean isDirty(EntityKey key, Object entity);

    EntityEntry getEntityEntry(EntityKey key);

    void addEntityEntry(EntityKey key, EntityEntry entityEntry);
}
