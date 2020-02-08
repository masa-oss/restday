package jp.oops.clazz.test.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.oops.clazz.restday.dao.DaoResult;
import jp.oops.clazz.restday.dao.ISimpleDao;
import jp.oops.clazz.restday.dao.SQLValue;
import jp.oops.clazz.restday.exception.RestdayException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class MockSimpleDao implements ISimpleDao {
    
    private final static Logger LOG = LogManager.getLogger(MockSimpleDao.class);
    
    String sqlExcept;
    String mesExcept;
  
    private final ArrayList<DaoArg> postCalls = new ArrayList<>();
    private final ArrayList<DaoArg> selectCalls = new ArrayList<>();
    private final ArrayList<DaoArg> putCalls = new ArrayList<>();
    private final ArrayList<DaoArg> deleteCalls = new ArrayList<>();
    private final ArrayList<DaoArg> patchCalls = new ArrayList<>();
    
    public MockSimpleDao() {
        LOG.info("MockSimpleDao#constructor");
    }
    
    public void setSQLException(String ex) {
        sqlExcept = ex;
    }
    
    public void setRestdayException(String me) {
        mesExcept = me;
    }
    
    
    @Override
    public DaoResult doPost(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {
        
        if (sqlExcept != null) throw new SQLException(sqlExcept);
        if (mesExcept != null) throw new RestdayException(mesExcept);
        
        postCalls.add(new DaoArg( connection, sqlTempl, validated));

        Integer it = (345);
        DaoResult dr = new DaoResult();
        dr.setObject(it);
        return dr;
    }

    
    @Override
    public DaoResult doGet(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {

        if (sqlExcept != null) throw new SQLException(sqlExcept);
        if (mesExcept != null) throw new RestdayException(mesExcept);
        
        selectCalls.add(new DaoArg( connection, sqlTempl, validated));

        LOG.info("this=" + this + ", calls = " + selectCalls.size());
        
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("MockSimpleDao#doGet", "selected-value"  );        
        list.add(map1);

        DaoResult dr = new DaoResult();
        dr.setGetResults(list);
        return dr;
    }

    @Override
    public DaoResult doPut(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {

        if (sqlExcept != null) throw new SQLException(sqlExcept);
        if (mesExcept != null) throw new RestdayException(mesExcept);

        putCalls.add(new DaoArg( connection, sqlTempl, validated));
        
        return new DaoResult();

    }

    @Override
    public DaoResult doPatch(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {
        if (sqlExcept != null) throw new SQLException(sqlExcept);
        if (mesExcept != null) throw new RestdayException(mesExcept);
        
        patchCalls.add(new DaoArg( connection, sqlTempl, validated));

        return new DaoResult();
    }    
    
    @Override
    public DaoResult doDelete(Connection connection, String sqlTempl, Map<String, SQLValue> validated) throws SQLException, RestdayException {
        
        if (sqlExcept != null) throw new SQLException(sqlExcept);
        if (mesExcept != null) throw new RestdayException(mesExcept);
        
        deleteCalls.add(new DaoArg( connection, sqlTempl, validated));

        //return 234;
        return new DaoResult();
    }

    /**
     * @return the insertCalls
     */
    public List<DaoArg> getPostCalls() {
        return postCalls;
    }

    /**
     * @return the selectCalls
     */
    public List<DaoArg> getSelectCalls() {
        return selectCalls;
    }

    /**
     * @return the updateCalls
     */
    public List<DaoArg> getPutCalls() {
        return putCalls;
    }

    /**
     * @return the deleteCalls
     */
    public List<DaoArg> getDeleteCalls() {
        return deleteCalls;
    }

    public List<DaoArg> getPatchCalls() {
        return patchCalls;
    }
}
