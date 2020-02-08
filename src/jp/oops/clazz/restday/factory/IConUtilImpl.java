package jp.oops.clazz.restday.factory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import javax.servlet.http.HttpServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <div>Class to get Connection to database</div>
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class IConUtilImpl implements IConUtil {

    private final Logger LOG = LogManager.getLogger(IConUtilImpl.class);

    HttpServlet thisServlet;
    Properties prop = null;

    public IConUtilImpl(HttpServlet servlet) {
        thisServlet = servlet;
    }

    private Properties getConfigProperties(HttpServlet servlet) {

        Properties dbProp = new Properties();
        try {
            InputStream is = servlet.getServletContext().getResourceAsStream("WEB-INF/restrest.properties");
            try {
                dbProp.load(is);
            } finally {
                is.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return dbProp;
    }

    HashMap<String, String> loadedDriverMap = new HashMap<>();

    private void loadDriver(String driverClassName) throws SQLException {

        if (loadedDriverMap.get(driverClassName) != null) {
            LOG.debug("--------- already loaded " + driverClassName);
            return;
        }

        try {
//           Class.forName("com.mysql.jdbc.Driver");
//           Class.forName("org.postgresql.Driver");
            Class.forName(driverClassName);
            LOG.info("JDBC driver loaded = " + driverClassName);

            loadedDriverMap.put(driverClassName, driverClassName);

        } catch (ClassNotFoundException ex) {
            throw new SQLException("can't load driver :" + driverClassName);
        }
    }

    @Override
    public synchronized Connection getConnection(String prefix) throws SQLException {

        if (prop == null) {
            prop = getConfigProperties(thisServlet);
        }
        String driverClassName = prop.getProperty(prefix + ".driver");

        loadDriver(driverClassName);

        String base = prop.getProperty(prefix + ".url.base");
        String user = prop.getProperty(prefix + ".user");
        String pass = prop.getProperty(prefix + ".password");
        String url = base + "user=" + user + "&password=" + pass;

        Connection conn = DriverManager.getConnection(url);
        return conn;
    }
}
