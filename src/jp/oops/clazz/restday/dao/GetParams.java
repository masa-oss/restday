package jp.oops.clazz.restday.dao;

import java.util.List;
import java.util.Map;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class GetParams {

    private final Map<String, String> reqMap;
    private final List<String> pathInfo;

    public GetParams(Map<String, String> reqMap, List<String> pathInfo) {
        this.reqMap = reqMap;
        this.pathInfo = pathInfo;
    }

    /**
     * @return the reqMap
     */
    public Map<String, String> getReqMap() {
        return reqMap;
    }

    /**
     * @return the pathInfo
     */
    public List<String> getPathInfo() {
        return pathInfo;
    }

}
