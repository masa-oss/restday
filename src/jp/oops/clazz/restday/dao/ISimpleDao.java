package jp.oops.clazz.restday.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import jp.oops.clazz.restday.exception.RestdayException;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public interface ISimpleDao {

    DaoResult doPost(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException;

    DaoResult doGet(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException;

    DaoResult doPut(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException;

    DaoResult doDelete(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException;

    DaoResult doPatch(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException;
}
