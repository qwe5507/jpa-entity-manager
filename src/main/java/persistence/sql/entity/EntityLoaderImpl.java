package persistence.sql.entity;

import jdbc.JdbcTemplate;
import jdbc.RowMapper;
import persistence.sql.dml.SelectQueryBuilder;

public class EntityLoaderImpl implements EntityLoader {

    private final SelectQueryBuilder selectQueryBuilder;
    private final JdbcTemplate jdbcTemplate;

    public EntityLoaderImpl(JdbcTemplate jdbcTemplate) {
        this.selectQueryBuilder = new SelectQueryBuilder();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public <T> T findById(final Class<T> clazz, final Long id) {
        final String findByIdQuery = selectQueryBuilder.createFindByIdQuery(clazz, id);
        final RowMapper<T> simpleRowMapper = new SimpleRowMapper<>(clazz);
        return jdbcTemplate.queryForObject(findByIdQuery, simpleRowMapper);
    }
}
