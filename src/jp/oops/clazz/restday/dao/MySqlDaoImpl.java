package jp.oops.clazz.restday.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.oops.clazz.restday.exception.MayBeDuplicateKeyException;
import jp.oops.clazz.restday.exception.RestdayException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi . </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class MySqlDaoImpl implements ISimpleDao {

    private static final Logger LOG = LogManager.getLogger(MySqlDaoImpl.class);

    
    @Override
    public DaoResult doGet(Connection connection, String sqlTempl, Map<String, SQLValue> validated ) throws SQLException, RestdayException {

        LOG.debug("sqlTempl = '" + sqlTempl + "'" );
        
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        
        SqlTemplateResults sqlTemplResult =SqlTemplateResults. parseTemplate(sqlTempl);

        LOG.debug("sqlTemplResult = " + sqlTemplResult + "" );

        PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
        try {
            fillData(ps, sqlTemplResult.getMap(), validated);
            LOG.debug("42) ps = " + ps);

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();

            ArrayList<String> columnNameList = new ArrayList<>();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                columnNameList.add(metaData.getColumnName(i));
            }

            while (rs.next()) {

                HashMap<String, Object> row = new HashMap<>();
                for (int i = 1; i <= count; i++) {
                    Object obj = rs.getObject(i);

                    row.put(columnNameList.get(i - 1), obj);
                }
                resultList.add(row);
            }
            LOG.info("--------------- sql exec done.");

        } finally {
            ps.close();
        }

        DaoResult dr = new DaoResult();

        dr.setGetResults(resultList);
        return dr;
    }


    @Override
    public DaoResult doDelete(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {

        int nUpdate;
        SqlTemplateResults sqlTemplResult =SqlTemplateResults. parseTemplate(sqlTempl);
        PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
        try {
            fillData(ps, sqlTemplResult.getMap(), validated);

            LOG.debug("86) ps=" + ps);
            nUpdate = ps.executeUpdate();
            LOG.debug("88) executeUpdate end");
        } finally {
            ps.close();
        }
        return new DaoResult();
    }

    @Override
    public DaoResult doPost(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {

        SqlTemplateResults sqlTemplResult =SqlTemplateResults. parseTemplate(sqlTempl);

        try {
            PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
            try {
                fillData(ps, sqlTemplResult.getMap(), validated);

                LOG.debug("105) ps=" + ps);
                ps.execute();
                LOG.debug("107) execute end");
            } finally {
                ps.close();
            }
        } catch (SQLException sqle) {

            String errMsg = sqle.getMessage();
            if (errMsg.indexOf(DUPLICATE_ERR) >= 0) {

                throw new MayBeDuplicateKeyException("may be duplicate key", null);
            } else {
                throw sqle;
            }
        }

        // autoIncrementで発番されたキーを取得する
        // Get key issued by autoIncrement
        Object autoIncrement = null;
        try {
            PreparedStatement ps2 = connection.prepareStatement(SELECT_LAST);
            try {
                ResultSet rs = ps2.executeQuery();
                if (rs.next()) {
                    autoIncrement = rs.getObject("LAST");
                }
            } finally {
                ps2.close();
            }
        } catch (SQLException sqle2) {
            throw sqle2;
        }
        DaoResult dr = new DaoResult();
        dr.setObject(autoIncrement);
        return dr;
    }

    public static String SELECT_LAST = "SELECT LAST_INSERT_ID() as LAST";

    public static String DUPLICATE_ERR = "uplicate";

    @Override
    public DaoResult doPut(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {

        SqlTemplateResults sqlTemplResult =SqlTemplateResults. parseTemplate(sqlTempl);

        PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
        try {
            fillData(ps, sqlTemplResult.getMap(), validated);

            LOG.debug("156) ps=" + ps);
            ps.execute();
            LOG.debug("158) execute end");
        } finally {
            ps.close();
        }
        return new DaoResult();
    }

    // 暫定
    // Provisional
    @Override
    public DaoResult doPatch(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {
     
        SqlTemplateResults sqlTemplResult =SqlTemplateResults. parseTemplate(sqlTempl);

        PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
        try {
            fillData(ps, sqlTemplResult.getMap(), validated);

            LOG.debug("176) ps=" + ps);
            ps.execute();
            LOG.debug("178) execute end");
        } finally {
            ps.close();
        }
        return new DaoResult();
    }
    
    void fillData(PreparedStatement ps, Map<String, Integer> map, Map<String, SQLValue> validated) throws SQLException, RestdayException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("188)" + validated );
        }
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();

            SQLValue sqlValue = validated.get(key);

            if (sqlValue == null) {

                RestdayException me = new RestdayException("'" + key + "' not found", "Is the configuration file or request wrong?");
                throw me;
            }

            Integer integer = map.get(key);
            sqlValue.setTo(ps, integer);
        }
    }
}
