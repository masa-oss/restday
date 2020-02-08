package jp.oops.clazz.restday.servlet;

import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.ParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import jp.oops.clazz.restday.Const;
import jp.oops.clazz.restday.blogic.PageResult;
import jp.oops.clazz.restday.blogic.RestBusinessLogic;
import jp.oops.clazz.restday.dao.GetParams;
import jp.oops.clazz.restday.dao.JsonUtil;
import jp.oops.clazz.restday.exception.ConfigurationException;
import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;
import jp.oops.clazz.restday.exception.RestdayException;
import jp.oops.clazz.restday.exception.RequestBodyJsonException;
import jp.oops.clazz.restday.factory.BeanFactoryEx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi .</div>
 * <div>This software is released under the MIT License. </div>
 */
public class BasicFrameworkServlet extends BaseFrameworkServlet {

    private static final Logger LOG = LogManager.getLogger(BasicFrameworkServlet.class);

    @Override
    public PageResult doLifecycle(RestBusinessLogic restBL, HttpServletRequest req, BeanFactoryEx beanFactory, List<String> pathInfo) throws RestdayException {

        if (pathInfo == null || pathInfo.size() == 0) {
            throw new RestdayException("invalid pathInfo", "invalid pathInfo");
        }

        String method = req.getMethod();

        Map<String, String> inputMap = new HashMap<>();

        JsonValue jv = cookHttpRequest(req, method, pathInfo, inputMap);

        LOG.debug("51) inputMap============" + inputMap);

        PageResult result = new PageResult();

        try {
            if (Const.METHOD_GET.equals(method)) {

                Map<String, String> reqMaps = new HashMap<>();
                try {
                    getParameters(reqMaps, req);
                } catch (IOException ioe) {
                    throw new RuntimeException("getParameters", ioe);
                }
                LOG.debug("64) ********* get parameters  " + reqMaps);

                execGet(result, restBL, inputMap, new GetParams(reqMaps, pathInfo));
            } else if (Const.METHOD_POST.equals(method)) {
                execPost(result, restBL, jv, inputMap);
            } else if (Const.METHOD_PUT.equals(method)) {
                execPut(result, restBL, jv, inputMap);
            } else if (Const.METHOD_DELETE.equals(method)) {
                execDelete(result, restBL, inputMap);
            } else if (Const.METHOD_PATCH.equals(method)) {
                execPatch(result, restBL, jv, inputMap);
            } else {
                throw new IllegalArgumentException("Unknown method name  " + method);
            }
        } catch (ConfigurationException cfse) {

            cfse.setMessageForDevelopers(pathInfo.get(0) + ".json");
            throw cfse;

        } catch (RestdayException me) {
            throw me;

        } catch (SQLException ex1) {

            throw new RuntimeException("SQLException: " + ex1.getMessage(), ex1);

        } catch (Exception ex2) {
            throw new RuntimeException("unkown error", ex2);
        }
        return result;
    }

    protected JsonValue cookHttpRequest(HttpServletRequest req, String method, List<String> pathInfo, Map<String, String> actionForm) throws RequestBodyJsonException {

        JsonValue jv = Json.NULL;
        try {
            jv = parseJsonOfRequest(req, jv);
        } catch (IOException | ParseException ex2) {
            // com.eclipsesource.json.ParseException
            // Java SE7 and later , multiple catch
            RequestBodyJsonException rbje = new RequestBodyJsonException(RequestBodyJsonException.MSG + ex2.getMessage(), ex2);
            rbje.setDetail(req.getPathInfo() + " - " + method);
            throw rbje;
        }

        int sz = pathInfo.size();
        for (int i = 1; i < sz; i++) {
            actionForm.put("pkey" + i, pathInfo.get(i));
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        actionForm.put("current_time", timestamp.toString());

        String ip = req.getRemoteAddr();  // IP V6 ???
        actionForm.put("remote_addr", ip);

        return jv;
    }

    private void getParameters(Map<String, String> af, HttpServletRequest req) throws IOException {

        Enumeration e = req.getParameterNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String[] values = req.getParameterValues(key);
            //af.put(  key  , new String(values[0].getBytes("8859_1"), "MS932"));
            af.put(key, values[0]);
        }
    }

    JsonValue parseJsonOfRequest(HttpServletRequest req, JsonValue jv) throws IOException {

        String body = doPostedBody(req.getReader());
        if (body.length() > 1) {
            jv = Json.parse(new StringReader(body));
        }
        return jv;
    }

    void execGet(PageResult result, RestBusinessLogic bl, Map<String, String> actionForm, GetParams reqMap) throws Exception {

        JsonValue getNode = bl.getJsonObject().get(Const.METHOD_GET);
        if (getNode == null) {
            throw new ConfigurationSyntaxErrorException("GET not found", null, bl.getAbsolutePathOfJson());
        }
        bl.doGet(result, actionForm, JsonUtil.asJsonObjectStrictly(getNode), reqMap);
    }

    void execDelete(PageResult result, RestBusinessLogic bl, Map<String, String> actionForm) throws Exception {

        JsonValue deleteNode = bl.getJsonObject().get(Const.METHOD_DELETE);
        if (deleteNode == null) {
            throw new ConfigurationSyntaxErrorException("DELETE not found", null);
        }

        bl.doDelete(result, actionForm, JsonUtil.asJsonObjectStrictly(deleteNode));
    }

    // update
    void execPut(PageResult result, RestBusinessLogic bl, JsonValue jv, Map<String, String> actionForm) throws Exception {

        JsonValue putNode = bl.getJsonObject().get(Const.METHOD_PUT);
        if (putNode == null) {
            throw new ConfigurationSyntaxErrorException("PUT not found", null);
        }
        bl.doPut(result, jv, actionForm, JsonUtil.asJsonObjectStrictly(putNode));
    }

    // update
    void execPatch(PageResult result, RestBusinessLogic bl, JsonValue jv, Map<String, String> actionForm) throws Exception {

        JsonValue patchNode = bl.getJsonObject().get(Const.METHOD_PATCH);

        if (patchNode == null) {
            throw new ConfigurationSyntaxErrorException("PATCH not found", null);
        }
        bl.doPatch(result, jv, actionForm, JsonUtil.asJsonObjectStrictly(patchNode));
    }

    // insert
    void execPost(PageResult result, RestBusinessLogic bl, JsonValue jv, Map<String, String> actionForm) throws Exception {

        JsonValue postNode = bl.getJsonObject().get(Const.METHOD_POST);
        if (postNode == null) {
            throw new ConfigurationSyntaxErrorException("POST not found", null);
        }
        bl.doPost(result, jv, JsonUtil.asJsonObjectStrictly(postNode), actionForm);
    }

    String doPostedBody(Reader r) throws IOException {

        StringBuilder sb = new StringBuilder();
        int in;
        while ((in = r.read()) >= 0) {
            sb.append((char) in);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("posted body = " + sb.toString());
        }
        return sb.toString();
    }
}
