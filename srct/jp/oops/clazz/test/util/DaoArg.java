package jp.oops.clazz.test.util;

import java.sql.Connection;
import java.util.Map;
import jp.oops.clazz.restday.dao.SQLValue;

/**
 * <div> Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div> This software is released under the MIT License.</div>
 */
public final class DaoArg {

    private final Connection connection;
    private final String sqlTempl;
    private final Map<String, SQLValue> validated;

    
    public DaoArg(Connection connection, String sqlTempl, Map<String, SQLValue> validated) {
        this.connection = connection;
        this.sqlTempl = sqlTempl;
        this.validated = validated;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return the sqlTempl
     */
    public String getSqlTempl() {
        return sqlTempl;
    }

    /**
     * @return the validated
     */
    public Map<String, SQLValue> getValidated() {
        return validated;
    }

}
