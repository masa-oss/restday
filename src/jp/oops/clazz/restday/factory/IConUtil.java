package jp.oops.clazz.restday.factory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public interface IConUtil {

    public Connection getConnection(String prefix)throws SQLException;

}
