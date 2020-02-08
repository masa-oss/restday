package jp.oops.clazz.restday.dao;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <h3>Conversion and Validation Process</h3>
 * 
 * <div>Copyright (c) 2019, 2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class ValidatorImpl implements IValidator {
    
    private static final Logger LOG = LogManager.getLogger(ValidatorImpl.class);

    @Override
    public DataAfterVerification doConversionAndValidation(Map<String, String> inputMap, JsonArray  ruleJson) throws ConfigurationSyntaxErrorException {

        DataAfterVerification result = new DataAfterVerification();

        procMaster(ruleJson, inputMap, result);

        return result;
    }

    private final int MAX_ROWS = 100;

    // 現在、未使用
    // Processing details (Currently unused)
    private boolean procDetail(JsonArray checkNode, int row, Map<String, String> inputMap, DataAfterVerification parent) throws ConfigurationSyntaxErrorException {

        // 行ごとの結果を作る
        // Make line-by-line results
        DataAfterVerification perRow = new DataAfterVerification();

        int n = checkNode.size();
        for (int i = 0; i < n; i++) {
            JsonValue temp = checkNode.get(i);

            JsonObject jo = JsonUtil.asJsonObjectStrictly(temp);
            List<String> listStr = jo.names();

            for (String right : listStr) {

                String head = String.format("row%d:", row);

                String key = head + right;
                LOG.info("procDetail     key:" + key);

                String value = inputMap.get(key);
                if (value == null) {

                    return false;

                } else {
                    JsonObject jo2 = JsonUtil.asJsonObjectStrictly(temp);
                    JsonValue options = jo2.get(right);
                    String removedKey = removeRowColon(key);
                    SQLValue sv = processRule(removedKey, JsonUtil.asArrayStrictly(options), value);
                    perRow.put(removedKey, sv);
                }
            }
        }
        parent.putChildData(perRow);
        return true;
    }

    void procMaster(JsonArray validateNode, Map<String, String> inputMap, DataAfterVerification result) throws ConfigurationSyntaxErrorException {

        int n = validateNode.size();
        for (int i = 0; i < n; i++) {

            // tempの例 は { "bb" : [ "string"   ]   ]  
            JsonValue temp = validateNode.get(i);

            JsonObject jo = JsonUtil.asJsonObjectStrictly(temp);
            List<String> it = jo.names();
            String key = it.get(0);

            String value = inputMap.get(key);
            JsonObject jo3 = JsonUtil.asJsonObjectStrictly(temp);

            JsonValue options =  jo3.get(key);
            
            SQLValue sv = processRule(key,   JsonUtil.asArrayStrictly(options) , value);
            result.put(key, sv);
        }
    }

    String removeRowColon(String inp) {
        int n = inp.indexOf(':');
        if (n < 0) {
            return inp;
        } else {
            return inp.substring(n + 1);
        }
    }

    /**
     * <div>テスト用入り口</div>
     * <div>JUnit testing entrance</div>
     */
    SQLValue processRule(String key, JsonArray ruleArray, String inputValue) throws ConfigurationSyntaxErrorException {

        // add 2020/02/02
        if (inputValue == null) {
            SQLValue    sv0 = SQLValue.createNull();
            sv0.setErrorMsg(ErrorMsg.build(E900, key, inputValue));
            return sv0;
        }
        
        // スタックに変換する
        // Convert to stack
        LinkedList<JsonValue> stack = new LinkedList<>();
        int sz = ruleArray.size();
        for (int k = 0; k < sz; k++) {
            stack.add(ruleArray.get(k));
        }

        SQLValue sv;

        if (sz == 0) {
            sv = SQLValue.createNull();
            sv.setErrorMsg(ErrorMsg.build(E900, "Config json error -  empty"));
            return sv;
        }

        JsonValue convertTo = stack.pop();
        String jsType = JsonUtil.asStringStrictly(convertTo);

        if ("int".equals(jsType)) {
            int v;
            try {
                v = Integer.parseInt(inputValue);
            } catch (Exception ex1) {
                sv = SQLValue.createNull();
                sv.setErrorMsg(ErrorMsg.build(E904, key, inputValue));
                return sv;
            }
            sv = SQLValue.createInt(v);
        } else if ("string".equals(jsType)) {
            sv = SQLValue.createString(inputValue);
        } else if ("long".equals(jsType)) {

            long ll;
            try {
                ll = Long.parseLong(inputValue);
            } catch (Exception ex) {
                sv = SQLValue.createNull();
                sv.setErrorMsg(ErrorMsg.build(E904, key, inputValue));
                return sv;
            }

            sv = SQLValue.createLong(ll);

        } else if ("BigDecimal".equals(jsType)) {

            BigDecimal bb;
            try {
                bb = new BigDecimal(inputValue);
            } catch (Exception ex1) {
                sv = SQLValue.createNull();
                sv.setErrorMsg(ErrorMsg.build(E904, key, inputValue));
                return sv;
            }

            sv = SQLValue.createBigDecimal(bb);
        } else if ("Date".equals(jsType)) {

            java.sql.Date dt;
            try {
                dt = java.sql.Date.valueOf(inputValue);
            } catch (Exception ex1) {
                sv = SQLValue.createNull();
                sv.setErrorMsg(ErrorMsg.build(E904, key, inputValue));
                return sv;
            }

            sv = SQLValue.createDate(dt);
        } else if ("Timestamp".equals(jsType)) {
            if (stack.isEmpty()) {
                sv = SQLValue.createNull();
                sv.setErrorMsg(ErrorMsg.build(E902, "Timestamp's format not found, config file syntax error  "));
                return sv;
            }

            JsonValue fmt = stack.pop();
            String sfmt = JsonUtil.asStringStrictly(fmt);

            java.sql.Timestamp ts;
            try {
                LOG.info("sfmt="+ sfmt);
                SimpleDateFormat sdf = new SimpleDateFormat(sfmt);
                LOG.info("inputValue="+ inputValue);
                // inputValueがnullの時どうするか？
                // What to do when inputValue is null?
                ts = new java.sql.Timestamp(sdf.parse(inputValue).getTime());
            } catch (Exception ex1) {
                LOG.warn("434)", ex1);
                sv = SQLValue.createNull();
                sv.setErrorMsg(ErrorMsg.build(E904, key, inputValue));
                return sv;
            }

            sv = SQLValue.createTimestamp(ts);

        } else if ("double".equals(jsType)) {
            double d;
            try {
                d = Double.parseDouble(inputValue);
            } catch (Exception ex1) {
                sv = SQLValue.createNull();
                sv.setErrorMsg(ErrorMsg.build(E904, key, inputValue));
                return sv;
            }
            sv = SQLValue.createDouble(d);

        } else {
            throw new ConfigurationSyntaxErrorException("unknown keyword '" + jsType + "'");
        }

        if (stack.isEmpty()) {
            return sv;
        }

        JsonValue v1 = stack.pop();
        String ruleName = JsonUtil.asStringStrictly(v1);
        if ("rename".equals(ruleName)) {

            JsonValue renameTo = stack.pop();
            sv.setSpecifiedColumnName(JsonUtil.asStringStrictly(renameTo));

            v1 = stack.pop();
            ruleName = JsonUtil.asStringStrictly(v1);
        }

        if ("min".equals(ruleName)) {

            if (stack.isEmpty()) {
                sv.setErrorMsg(ErrorMsg.build(E902, "Config json syntax  error - min "));
                return sv;
            } else {
                JsonValue minArg = stack.pop();

                if (!compareMin(minArg, sv)) {
                    sv.setErrorMsg(ErrorMsg.build(E901, key));
                    return sv;
                }

                if (stack.isEmpty()) {
                    return sv;
                }
            }
        } else if ("max".equals(ruleName)) {
            
            if (stack.isEmpty()) {
                sv.setErrorMsg(ErrorMsg.build(E902, "Config json syntax  error - min "));
                return sv;
            } else {
                JsonValue maxArg = stack.pop();

                if (!compareMax(maxArg, sv)) {
                    sv.setErrorMsg(ErrorMsg.build(E901, key));
                    return sv;
                }

                if (stack.isEmpty()) {
                    return sv;
                }
            }
        } else if ("min-max".equals(ruleName)) {
            if (stack.isEmpty()) {
                sv.setErrorMsg(ErrorMsg.build(E902, "Config json syntax  error - min-max "));
                return sv;
            } else {
                JsonValue minArg = stack.pop();
                if (stack.isEmpty()) {
                    sv.setErrorMsg(ErrorMsg.build(E902, "Config json syntax  error - min-max "));
                    return sv;
                }
                JsonValue maxArg = stack.pop();
                
                if (!betweenMinMax(minArg, maxArg, sv)) {
                    sv.setErrorMsg(ErrorMsg.build("min max error", key));
                    return sv;
                }
                if (stack.isEmpty()) {
                    return sv;
                }
            }
        }

        return sv;
    }
    
    // return true - ok,  false - validation error
    boolean compareMin(JsonValue jv, SQLValue sv) throws ConfigurationSyntaxErrorException {
        
        int svType = sv.getType();
        switch (svType) {
            case SQLValue.INTEGER:
            {
                int iSv = sv.intValue();
                int iMin = JsonUtil.asIntStrictly(jv);
                return (iMin <= iSv);
            }
            case SQLValue.LONG:
            {
                long lSv = sv.longValue();
                long lMin = JsonUtil.asLongStrictly(jv);
                return (lMin <= lSv);
            }
            case SQLValue.STRING:
            {
                String sSv = sv.stringValue();
                int iMin = JsonUtil.asIntStrictly(jv);
                
                if (sSv == null) return false;
                return (sSv.length() >= iMin);
            }
            default:
                return false;
        }
    }
    
    // return true - ok,  false - validation error
    boolean compareMax(JsonValue jv, SQLValue sv) throws ConfigurationSyntaxErrorException {
        
        int svType = sv.getType();
        switch (svType) {
            case SQLValue.INTEGER:
            {
                int iSv = sv.intValue();
                int iMax = JsonUtil.asIntStrictly(jv);
                return (iSv <= iMax);
            }
            case SQLValue.LONG:
            {
                long lSv = sv.longValue();
                long lMax = JsonUtil.asLongStrictly(jv);
                return (lSv <= lMax);
            }
            case SQLValue.STRING:
            {
                String sSv = sv.stringValue();
                int iMax = JsonUtil.asIntStrictly(jv);
                
                if (sSv == null) return false;
                return (sSv.length() <= iMax);
            }
            default:
                return false;
        }
    }
    
    // return true - ok,  false - validation error
    boolean betweenMinMax(JsonValue min, JsonValue max, SQLValue sv) throws ConfigurationSyntaxErrorException {
        
        int svType = sv.getType();
        switch (svType) {
            case SQLValue.INTEGER:
            {
                int iSv = sv.intValue();
                int iMin = JsonUtil.asIntStrictly(min);
                int iMax = JsonUtil.asIntStrictly(max);
                return (iMin <= iSv && iSv <= iMax);
            }
            case SQLValue.LONG:
            {
                long lSv = sv.longValue();
                long lMin = JsonUtil.asLongStrictly(min);
                long lMax = JsonUtil.asLongStrictly(max);
                return (lMin <= lSv && lSv <= lMax);
            }
            case SQLValue.STRING:
            {
                String sSv = sv.stringValue();
                int iMin = JsonUtil.asIntStrictly(min);
                int iMax = JsonUtil.asIntStrictly(max);
                
                if (sSv == null) return false;
                return (iMin <= sSv.length() && sSv.length() <= iMax);
            }
            default:
                return false;
        }
    }
    
    // TODO: longValue, bigDecimal, etc

    private static final String E901 = "E901";
    private static final String E902 = "E902";
    // "Config json error - rename"
    private static final String E903 = "E903";
    private static final String E904 = "E904";

    private static final String E900 = "E900";
}
