package jp.oops.clazz.restday.dao;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;
import jp.oops.clazz.restday.exception.RequestBodyJsonException;

/**
 * <div>使っているjsonライブラリは、ほとんどRuntimeExceptionを発生させる。</div>
 * <div>検査例外にしたかったので、変換するユーティリティクラスを作った。</div>
 *
 * <div> The json library (minimal-json) I use, mostly raises RuntimeException.</div>
 * <div> I wanted to make a check exception, so I created a utility class to convert.</div>
 * 
 * <div>Copyright (c) 2020 Masahito Hemmi</div> 
 * <div>This software is released under the MIT License.</div>
 */
public final class JsonUtil {


    public static String    asStringStrictly(JsonValue jv) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to String");
        String str;
        try {
            str = jv.asString();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to String");
        }
        return str;
    }

    public static JsonObject    asJsonObjectStrictly(JsonValue jv) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to JsonObject");
        JsonObject str;
        try {
            str = jv.asObject();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to JsonObject");
        }
        return str;
    }
    
    public static JsonArray   asArrayStrictly(JsonValue jv) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to JsonArray");
        JsonArray arr;
        try {
            arr = jv.asArray();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to JsonArray");
        }
        return arr;
    }
    
    public static int   asIntStrictly(JsonValue jv) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to int");
        int val;
        try {
            val = jv.asInt();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to int");
        }
        return val;
    }
    
    public static long   asLongStrictly(JsonValue jv) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to long");
        long val;
        try {
            val = jv.asLong();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to long");
        }
        return val;
    }
    
    // --------------------------------------------------------
    
    public static JsonObject    asJsonObjectForBody(JsonValue jv) throws RequestBodyJsonException {

        if (jv == null) throw new RequestBodyJsonException("can't convert NULL to JsonObject");
        JsonObject str;
        try {
            str = jv.asObject();
        } catch (Exception ex) {
            throw new RequestBodyJsonException("can't convert to JsonObject");
        }
        return str;
    }
    
    
    private JsonUtil() {
    }
}
