package jp.oops.clazz.test.util;

import java.sql.Connection;
import java.sql.SQLException;
import jp.oops.clazz.restday.factory.IConUtil;

/**
 * <div> Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div> This software is released under the MIT License.</div>
 */
public class MockConUtil implements IConUtil {

    Connection conn;
    
    public MockConUtil(Connection con) {
        conn = con;
    }
    
    
    @Override
    public Connection getConnection(String prefix) throws SQLException {
        return conn;
    }
    
}
