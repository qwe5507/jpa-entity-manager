package persistence.sql.dml;

import persistence.sql.meta.Columns;
import persistence.sql.meta.EntityMetaCreator;
import persistence.sql.meta.PrimaryKey;
import persistence.sql.meta.TableName;

public class SelectQueryBuilder {

    public static final String SELECT_FIND_ALL_DEFAULT_DML = "select %s from %s";
    public static final String SELECT_FIND_ID_DEFAULT_DML = "%s where %s";
    private final TableName tableName;
    private final PrimaryKey primaryKey;
    private final Columns columns;

    public SelectQueryBuilder(EntityMetaCreator entityMetaCreator) {
        this.tableName = entityMetaCreator.createTableName();
        this.primaryKey = entityMetaCreator.createPrimaryKey();
        this.columns = entityMetaCreator.createColumns();
    }

    public String createFindAllQuery() {
        return String.format(SELECT_FIND_ALL_DEFAULT_DML, select(), this.tableName.name());
    }

    public String createFindByIdQuery(Long id) {
        return String.format(SELECT_FIND_ID_DEFAULT_DML, createFindAllQuery(), selectWhere(id));
    }

    private String select() {
        return String.format("%s, %s", this.primaryKey.name(), String.join(", ", this.columns.names()));
    }

    private String selectWhere(Long id) {
        return String.format("%s = %dL", this.primaryKey.name(), id);
    }
}
