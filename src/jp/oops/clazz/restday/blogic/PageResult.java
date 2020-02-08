package jp.oops.clazz.restday.blogic;

import jp.oops.clazz.restday.dao.ErrorMsg;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class PageResult {

    /**
     * <div>jsonで戻すかhtmlでもどすか</div>
     * <div>Return with json or html</div>
     */
    public String viewType = "json";

    /**
     * <div>戻り値</div>
     */
    public Object returnValue;

    private List<String> pathList;

    private final ArrayList<ErrorMsg> errorList = new ArrayList<>();

    public void addAll(List<ErrorMsg> toAdd) {
        errorList.addAll(toAdd);
    }

    public List<ErrorMsg> getErrorList() {
        return errorList;
    }

    /**
     * <div>タイムリーフ用(現在未使用です)</div>
     * <div>For Thymeleaf (Currently unused)</div>
     */
    public Map<String, Object> thymeleafMap;

    /**
     * <div>タイムリーフ用(現在未使用です)</div>
     * <div>For Thymeleaf (Currently unused)</div>
     */
    public String htmlPath;

    /**
     * @return the pathList
     */
    public List<String> getPathList() {
        return pathList;
    }

    /**
     * @param pathList the pathList to set
     */
    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }
}
