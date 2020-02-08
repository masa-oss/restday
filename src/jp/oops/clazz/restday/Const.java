package jp.oops.clazz.restday;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class Const {

    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_TRACE = "TRACE";

    public static final String METHOD_PATCH = "PATCH";
    
    /**
     * Key for storing in servlet context.
     */
    public static final String KEY_BEAN_FACTORY = "jp.oops.clazz.restday.bfactory";
    
    public static final String KEY_SERV_PARAMS = "jp.oops.clazz.restday.serv_params";

    public static final String URL_BASE = "URL_BASE";
    
    public static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

    
    private Const() {
    }
}
