package jp.oops.clazz.restday.servlet;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.oops.clazz.restday.Const;
import jp.oops.clazz.restday.factory.BeanFactoryEx;
import jp.oops.clazz.restday.blogic.PageResult;
import jp.oops.clazz.restday.blogic.RestBusinessLogic;
import jp.oops.clazz.restday.blogic.UriPart;
import jp.oops.clazz.restday.dao.DaoResult;
import jp.oops.clazz.restday.dao.ErrorMsg;
import jp.oops.clazz.restday.exception.ConfigurationException;
import jp.oops.clazz.restday.exception.RestdayException;
import jp.oops.clazz.restday.exception.RequestBodyJsonException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public abstract class BaseFrameworkServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(BaseFrameworkServlet.class);

    void exceptionReturn(HttpServletResponse resp, Exception ex) {

        LOG.error("42) ----", ex);

        if (ex instanceof RequestBodyJsonException) {
            // 400番台は「クライアント側のエラー」
            // 400s are "client-side errors"
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else if (ex instanceof ConfigurationException) {

            LOG.debug("ConfigurationSyntaxErrorException", ex);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType(Const.JSON_CONTENT_TYPE);
            try {
                PrintWriter out = resp.getWriter();
                errorPage3(out, (RestdayException) ex);
                out.flush();
            } catch (IOException ioe) {
                LOG.error("can't recover", ioe);
            }

        } else if (ex instanceof RestdayException) {

            // add 2020/02/04
            resp.setStatus(HttpServletResponse.SC_CONFLICT); //409
            resp.setContentType(Const.JSON_CONTENT_TYPE);
            try {
                PrintWriter out = resp.getWriter();
                errorPage3(out, (RestdayException) ex);
                out.flush();
            } catch (IOException ioe) {
                LOG.error("can't recover", ioe);
            }
        } else if (ex instanceof FileNotFoundException) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType(Const.JSON_CONTENT_TYPE);
            LOG.error("74) " + ex.getMessage());
        } else {
            resp.setContentType(Const.JSON_CONTENT_TYPE);
            Throwable th = ex.getCause();
            if (th != null) {
                LOG.error("89)", th);
            } else {
                LOG.error("91)", ex);
            }
            resp.setStatus(HttpServletResponse.SC_CONFLICT); //409
        }
    }

    void errorPage3(PrintWriter out, RestdayException me) throws IOException {

        JsonObject map = new JsonObject();
        map.add("error", me.getMessage());
        map.add("MessageForDevelopers", me.getMessageForDevelopers());
        out.print(map.toString());
        out.flush();
    }

    void printJsonError(PrintWriter out, List<ErrorMsg> errorMsgList) {

        int sz = errorMsgList.size();

        JsonObject map = new JsonObject();

        map.add("error", Json.TRUE);

        for (int i = 0; i < sz; i++) {
            ErrorMsg msg = errorMsgList.get(i);
            map.add(String.valueOf(i), msg.toString());
        }

        out.print(map.toString());
        out.flush();
    }

    private PageResult lifecycleImpl(HttpServletRequest req) throws IOException, RestdayException {

        String pathInfo = req.getPathInfo();
        LOG.info("pathInfo = " + pathInfo);   // ex /aaa

        List<String> uri = UriPart.removeFirstSlash(pathInfo);

        BeanFactoryEx beanFactory = getBeanFactory();
        RestBusinessLogic bl = beanFactory.getNamedBean(uri.get(0));

        bl.setJsonFileName(uri.get(0) + ".json");

        PageResult pr;
        try {
            // （オーバーライドの）ライフサイクルを実行
            // Execute Override Lifecycle
            pr = doLifecycle(bl, req, beanFactory, uri);

            pr.setPathList(uri);
        } finally {

            if (bl.getDeveloperMode()) {
                bl.clearJsonObject();
                beanFactory.clearNamedBean();
            }
        }
        return pr;
    }

    BeanFactoryEx getBeanFactory() {

        // RestdayInitServletが、あらかじめ作成しているBeanを取得
        // RestdayInitServlet gets the bean created in advance
        Object dicon = getServletContext().getAttribute(Const.KEY_BEAN_FACTORY);
        BeanFactoryEx beanFactory = (BeanFactoryEx) dicon;
        return beanFactory;
    }

    Map<String, String> getServletConfigMap() {

        // RestdayInitServletが、あらかじめ作成しているマップを取得
        // RestdayInitServlet gets the map created in advance
        Object dicon = getServletContext().getAttribute(Const.KEY_SERV_PARAMS);
        Map<String, String> beanFactory = (Map<String, String>) dicon;
        return beanFactory;
    }

    /**
     * <div>オーバーライドする箇所</div>
     * <div>Where to override</div>
     *
     * @param bl
     * @param req
     * @param beanFactory
     * @param pathInfo
     * @return
     * @throws jp.oops.clazz.restday.exception.RestdayException
     */
    public abstract PageResult doLifecycle(RestBusinessLogic bl, HttpServletRequest req, BeanFactoryEx beanFactory, List<String> pathInfo)
            throws RestdayException;

    void renderViewGet(String viewType, HttpServletRequest req, HttpServletResponse resp, PageResult result, List<String> pathInfo) throws IOException {

        if ("json".equals(viewType)) {
            resp.setContentType(Const.JSON_CONTENT_TYPE);
            // 上の行のあとに、getWriterを実行する必要がある
            // You need to get getWriter after executing setContentType
            PrintWriter out = resp.getWriter();

            List<ErrorMsg> errorMsgList = result.getErrorList();
            if (!errorMsgList.isEmpty()) {
                // エラーメッセージを出力
                // Output error message
                printJsonError(out, errorMsgList);
                return;
            }

            Object obj = result.returnValue;
            if (obj == null) {
                LOG.error("172) --- result object is null");

                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

            } else if (obj instanceof JsonValue) {
                JsonValue jv = (JsonValue) obj;
                out.println(jv.toString());
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    void renderViewPostDeletePut(String viewType, HttpServletRequest req, HttpServletResponse resp, PageResult result, int respCode) throws IOException {

        if ("json".equals(viewType)) {

            if (Const.METHOD_POST.equals(req.getMethod())) {
                Map<String, String> servConfMap = getServletConfigMap();
                String left = "http://";
                if (servConfMap != null) {
                    left = servConfMap.get(Const.URL_BASE);
                }
                left = left + result.getPathList().get(0);

                DaoResult dr = (DaoResult) result.returnValue;
                if (dr != null) {
                    LOG.info("308) result.returnValue= null");
                    left = left + "/" + dr.getObject().toString();
                }
                resp.setHeader("Link", left);
            }
            resp.setContentType(Const.JSON_CONTENT_TYPE);

            // add 2020.02.02
            List<ErrorMsg> errorList = result.getErrorList();
            if (errorList.size() > 0) {

                LOG.info("235) errorList = " + errorList.toString());

                resp.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            } else {
                resp.setStatus(respCode);
            }
        } else {
            resp.setContentType(Const.JSON_CONTENT_TYPE);
            resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    void renderViewPatch(String viewType, HttpServletRequest req, HttpServletResponse resp, PageResult result, BeanFactoryEx beanFactory) throws IOException {

        if ("json".equals(viewType)) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.debug("------------------------------- doGet start");
        PageResult result;
        try {
            result = lifecycleImpl(req);
        } catch (IOException | RestdayException | RuntimeException pe) {
            exceptionReturn(resp, pe);
            return;
        }
        try {
            renderViewGet(result.viewType, req, resp, result, null);
        } catch (IOException | RuntimeException ex) {
            LOG.error("renderView", ex);
        }
        LOG.debug("------------------------------- doGet end");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.debug("------------------------------- doPost start");
        PageResult result;
        try {
            result = lifecycleImpl(req);
        } catch (IOException | RestdayException | RuntimeException pe) {
            exceptionReturn(resp, pe);
            return;
        }

        DaoResult dr = (DaoResult) result.returnValue;
        if (dr == null) {
            LOG.info("308) result.returnValue= null");

        } else {
            LOG.info("311) result.returnValue= " + dr.getObject());

        }

        try {
            renderViewPostDeletePut(result.viewType, req, resp, result, 201);
        } catch (IOException | RuntimeException ex) {
            LOG.error("renderView", ex);
        }
        LOG.debug("------------------------------- doPost end");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.debug("------------------------------- doDelete start");
        PageResult result;
        try {
            result = lifecycleImpl(req);
        } catch (IOException | RestdayException | RuntimeException pe) {
            exceptionReturn(resp, pe);
            return;
        }
        try {
            renderViewPostDeletePut(result.viewType, req, resp, result, HttpServletResponse.SC_NO_CONTENT);
        } catch (IOException | RuntimeException ex) {
            LOG.error("renderView", ex);
        }

        LOG.debug("------------------------------- doDelete end");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.debug("------------------------------- doPut start");
        PageResult result;
        try {
            result = lifecycleImpl(req);
        } catch (IOException | RestdayException | RuntimeException pe) {
            exceptionReturn(resp, pe);
            return;
        }
        try {
            renderViewPostDeletePut(result.viewType, req, resp, result, HttpServletResponse.SC_NO_CONTENT);
        } catch (IOException | RuntimeException ex) {
            LOG.error("renderView", ex);
        }

        LOG.debug("------------------------------- doPut end");
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.debug("------------------------------- doPatch start");
        PageResult result;
        try {
            result = lifecycleImpl(req);
        } catch (IOException | RestdayException | RuntimeException pe) {
            exceptionReturn(resp, pe);
            return;
        }
        try {
            renderViewPatch(result.viewType, req, resp, result, null);
        } catch (IOException | RuntimeException ex) {
            LOG.error("renderView", ex);
        }

        LOG.debug("------------------------------- doPatch end");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }
}
