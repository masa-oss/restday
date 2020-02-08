package jp.oops.clazz.restday.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  <div>Result type after conversion and validation process.</div>
 *
 * <div>Copyright (c) 2019, 2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class DataAfterVerification {

    private final Map<String, SQLValue> converted = new HashMap<>();

    private final ArrayList<Map<String, SQLValue>> children = new ArrayList<>();

    private final ArrayList<ErrorMsg> errorList = new ArrayList<>();
    
    public boolean isError() {
        return errorList.size() > 0;
    }

    public List<ErrorMsg>  getErrorList() {
        return Collections.unmodifiableList(errorList);
    }
    
    public void put(String key, SQLValue obj) {
        converted.put(key, obj);
        if (obj.hasError()) {
            errorList.add(obj.getErrorMsg());
        }
    }

    public Map<String, SQLValue> getMap() {
        return Collections.unmodifiableMap(converted);
    }

    public void putChildData(DataAfterVerification child) {

        children.add(Collections.unmodifiableMap(child.converted));
    }

    public List<Map<String, SQLValue>> getListOfChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("DataAfterVerification[");
        
        sb.append(errorList.toString());
        
        sb.append(", ");
        sb.append(converted.toString());

        sb.append("]");
        return sb.toString();
    }
    
/*    
    public String toString() {
        if (isError()) {
            return "DataAfterVerification[error]";
        } else {
            return "DataAfterVerification[" + converted + ", # of children =" + children.size() + "]";
        }
    }
*/
}
