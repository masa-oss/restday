package jp.oops.clazz.restday.dao;

import java.util.List;
import java.util.Map;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class DaoResult {
 
    private Object val = null;
    private List<Map<String, Object>> getResults = null;
    
    public DaoResult() {
    }

    public Object getObject() {
        return val;
    }

    public void setObject(Object o) {
        val = o;
    }

    /**
     * @return the getResults
     */
    public List<Map<String, Object>> getGetResults() {
        return getResults;
    }

    /**
     * @param getResults the getResults to set
     */
    public void setGetResults(List<Map<String, Object>> getResults) {
        this.getResults = getResults;
    }
}
