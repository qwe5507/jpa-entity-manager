package persistence.sql.entity.impl;

import persistence.sql.entity.EntityLoader;
import persistence.sql.entity.EntityManager;
import persistence.sql.entity.EntityPersister;
import persistence.sql.entity.PersistenceContext;

import java.util.Objects;

public class EntityManagerImpl implements EntityManager {

    private final EntityPersister entityPersister;
    private final EntityLoader entityLoader;
    private final PersistenceContext persistenceContext;

    public EntityManagerImpl(EntityPersister entityPersister, EntityLoader entityLoader, PersistenceContext persistenceContext) {
        this.entityPersister = entityPersister;
        this.entityLoader = entityLoader;
        this.persistenceContext = persistenceContext;
    }

    @Override
    public <T> T find(final Class<T> clazz, final Long id) {
        final EntityKey key = EntityKey.fromNameAndValue(clazz.getName(), id);

        if (Objects.isNull(persistenceContext.getEntity(key))) {
            final T instance = entityLoader.findById(clazz, id);
            persistenceContext.addEntity(key, instance);
            return instance;
        }

        return (T) persistenceContext.getEntity(key);
    }

    @Override
    public Object persist(final Object entity) {
        final EntityEntry entityEntry = EntityEntry.of(Status.SAVING);
        final EntityKey entityKey = EntityKey.fromEntity(entity);
        persistenceContext.addEntityEntry(entityKey, entityEntry);

        final Long id = entityPersister.insert(entity);
        final EntityKey key = EntityKey.fromNameAndValue(entity.getClass().getName(), id);
        persistenceContext.addEntity(key, entity, entityEntry.updateStatus(Status.MANAGED));

        return entity;
    }

    @Override
    public Object merge(final Object entity) {
        final EntityKey key = EntityKey.fromEntity(entity);

        if (persistenceContext.isDirty(key, entity)) {
            entityPersister.update(entity);
        } else {
            entityPersister.insert(entity);
        }

        persistenceContext.addEntity(key, entity);
        return entityLoader.findById(entity.getClass(), (Long) key.value());
    }


    @Override
    public void remove(final Object entity) {
        final EntityKey key = EntityKey.fromEntity(entity);

        persistenceContext.removeEntity(key);
        entityPersister.delete(entity);
    }

    public boolean isDirty(Object entity) {
        final EntityKey key = EntityKey.fromEntity(entity);

        return persistenceContext.isDirty(key, entity);
    }
}
