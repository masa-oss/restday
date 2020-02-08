package jp.oops.clazz.restday.blogic;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jp.oops.clazz.restday.dao.DataAfterVerification;
import jp.oops.clazz.restday.dao.JsonUtil;
import jp.oops.clazz.restday.exception.RequestBodyJsonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class BaseCustomizePoint {

    private final static Logger LOG = LogManager.getLogger(BaseCustomizePoint.class);
    
    
    /**
     * <div>型変換と検証の後よばれるカスタマイズ箇所</div>
     * <div>Customized place called after type conversion and validation</div>
     * 
     * @param validated 
     */
    public void afterValidationAndConvert(DataAfterVerification validated) {
    }

    /**
     * <div>GETのDB呼び出しの後、呼ばれるカスタマイズ箇所</div>
     * <div>Customization location called after GET DB call</div>
     * 
     * @param obj
     * @return 
     */
    public JsonObject convToJson(List<Map<String, Object>> obj) {

        JsonArray array = new JsonArray();

        int siz = obj.size();
        for (int i = 0; i < siz; i++) {
            Map<String, Object> map = obj.get(i);

            JsonObject jmap = new JsonObject();
            Set<String> keys = map.keySet();
            for (String key : keys) {
                Object o2 = map.get(key);

                addTo(jmap, key, o2);
            }

            array.add(jmap);
        }

        JsonObject ret = new JsonObject();
        ret.add("success", array);
        return ret;
    }

    protected void addTo(JsonObject jmap, String key, Object o) {

        if (o == null) {
            jmap.add(key, Json.NULL);
        } else if (o instanceof Integer) {
            jmap.add(key, ((Integer) o));
        } else if (o instanceof String) {
            jmap.add(key, ((String) o));
        } else if (o instanceof java.sql.Date) {
            jmap.add(key, ((java.sql.Date) o).toString());
        } else if (o instanceof java.sql.Timestamp) {
            jmap.add(key, ((java.sql.Timestamp) o).toString());
        } else {
            LOG.debug("else:" + o);
            jmap.add(key, o.toString());
        }
    }
    
    /**
     * <div>POSTされたHTML-BODYのJSONをMAPに変換するカスタマイズ箇所</div>
     * <div>Customized part that converts JSON of POSTed HTML-BODY to MAP</div>
     * 
     * @param postedBody
     * @return
     * @throws RequestBodyJsonException 
     */
    public Map<String, String> convertPOSTedBodyToMap(JsonValue postedBody) throws RequestBodyJsonException {
        
        JsonObject inputMap =  JsonUtil.asJsonObjectForBody(postedBody);  

        HashMap<String, String> map = new HashMap<>();
        List<String> keySet = inputMap.names();

        for (String s : keySet) {
            JsonValue jv = inputMap.get(s);

            if (jv.isString()) {
                map.put(s, jv.asString());

            } else {
                map.put(s, jv.toString());
            }
        }

        return map;
    }
    
}
