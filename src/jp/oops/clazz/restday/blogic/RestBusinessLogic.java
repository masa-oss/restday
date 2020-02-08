package jp.oops.clazz.restday.blogic;

import com.eclipsesource.json.ParseException;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import jp.oops.clazz.restday.Const;
import jp.oops.clazz.restday.exception.BeanNotFoundException;
import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;
import jp.oops.clazz.restday.exception.ClazzCastException;
import jp.oops.clazz.restday.exception.RestdayException;

import jp.oops.clazz.restday.dao.DataAfterVerification;
import jp.oops.clazz.restday.dao.SQLValue;
import jp.oops.clazz.restday.dao.ISimpleDao;
import jp.oops.clazz.restday.dao.JsonUtil;
import jp.oops.clazz.restday.dao.DaoResult;
import jp.oops.clazz.restday.dao.GetParams;
import jp.oops.clazz.restday.dao.IValidator;
import jp.oops.clazz.restday.dao.Node;
import jp.oops.clazz.restday.dao.TemplateV2;
import jp.oops.clazz.restday.exception.ConfigurationException;
import jp.oops.clazz.restday.exception.ConfigurationFileException;
import jp.oops.clazz.restday.factory.IConUtil;
import jp.oops.clazz.restday.factory.ConUtilAware;
import jp.oops.clazz.restday.factory.ISimpleDaoAware;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <div> Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div> This software is released under the MIT License.</div>
 */
public final class RestBusinessLogic implements ConUtilAware, ISimpleDaoAware {

    private final static Logger LOG = LogManager.getLogger(RestBusinessLogic.class);

    private IConUtil aConUtil;

    @Override
    public void setConUtil(IConUtil conUtil) {
        this.aConUtil = conUtil;
    }

    private ISimpleDao simpleDao;

    @Override
    public void setISimpleDao(ISimpleDao dao) {
        this.simpleDao = dao;
        LOG.debug("setISimpleDao = " + dao);
    }

    private IValidator iValidator;

    public void setIValidator(IValidator vali) {
        iValidator = vali;
    }

    private String appPath;

    @Override
    public void setAppPath(String path) {
        appPath = path;
    }

    private File associatedJson;

    public void setJsonFileName(String jfn) {

        File f1 = new File(appPath, "/WEB-INF/json.cfg/");

        associatedJson = new File(f1, jfn);
    }

    public String getAbsolutePathOfJson() {
        return associatedJson.getAbsolutePath();
    }

    private boolean developerMode;

    public void setDeveloperMode(boolean b) {
        developerMode = b;
    }

    public boolean getDeveloperMode() {
        return developerMode;
    }

    private JsonValue readJsonFile(String encoding) throws IOException {

        JsonValue jv = null;
        try {
            try ( FileInputStream fis = new FileInputStream(associatedJson);  InputStreamReader isr = new InputStreamReader(fis, encoding)) {

                jv = Json.parse(isr);
            }
        } catch (FileNotFoundException e) {
            LOG.error("File Not Found = " + associatedJson);
            throw e;
        } catch (UnsupportedEncodingException e3) {
            LOG.error("UnsupportedEncodingException");
            throw e3;
        } catch (IOException e2) {
            LOG.error("IOException", e2);
            throw e2;
        }
        return jv;
    }

    JsonObject cache = null;

    public synchronized void clearJsonObject() {
        cache = null;
        LOG.debug("config cache cleard!");
    }

    // 設定JSONを読み込む
    // Read configuration JSON
    public synchronized JsonObject getJsonObject() throws ConfigurationException {

        if (cache != null) {
            //LOG.debug("found in cache");
            return cache;
        }

        JsonValue jv0;
        try {
            jv0 = readJsonFile("UTF-8");
        } catch (IOException ex) {
            throw new ConfigurationFileException("IOException", ex);
        } catch (ParseException ex2) {
            // com.eclipsesource.json.ParseException
            ConfigurationFileException cje = new ConfigurationFileException(ex2.getMessage(), ex2);
            if (this.associatedJson != null) {
                cje.setMessageForDevelopers(associatedJson.getAbsolutePath());
            }
            throw cje;
        }

        if (jv0 == null) {
            throw new ConfigurationFileException("null");
        }

        JsonObject wk = JsonUtil.asJsonObjectStrictly(jv0);

        cache = wk;

        // JSONをロードした時に、カスタマイズも処理する
        // Handle customization when loading JSON
        {
            JsonValue getNode = wk.get(Const.METHOD_GET);
            if (getNode != null) {
                procCustomizeV2(  JsonUtil.asJsonObjectStrictly(getNode), Const.METHOD_GET);
            }
            JsonValue postNode = wk.get(Const.METHOD_POST);
            if (postNode != null) {
                procCustomizeV2(  JsonUtil.asJsonObjectStrictly(postNode), Const.METHOD_POST);
            }
            JsonValue putNode = wk.get(Const.METHOD_PUT);
            if (putNode != null) {
                procCustomizeV2(  JsonUtil.asJsonObjectStrictly(putNode), Const.METHOD_PUT);
            }
            JsonValue deleteNode = wk.get(Const.METHOD_DELETE);
            if (deleteNode != null) {
                procCustomizeV2(  JsonUtil.asJsonObjectStrictly(deleteNode), Const.METHOD_DELETE);
            }
            JsonValue patchNode = wk.get(Const.METHOD_PATCH);
            if (patchNode != null) {
                procCustomizeV2(  JsonUtil.asJsonObjectStrictly(patchNode), Const.METHOD_PATCH);
            }
        }
        return wk;
    }

    private void procCustomizeV2(JsonObject configJson, String mode) throws ConfigurationSyntaxErrorException {

        JsonValue jv = configJson.get("customization");
        // カスタマイズの指定がなければ、終了
        // Exit if no customization specified
        if (jv == null) {
            setCustomization(mode, new BaseCustomizePoint());
            return;
        }

        String str = JsonUtil.asStringStrictly(jv);
        LOG.debug("str = " + str);
        Object obj;
        try {
            obj = SimpleFactory.createInstance(str);
        } catch (BeanNotFoundException ex) {
            throw new ConfigurationSyntaxErrorException("BeanNotFoundException");
        }

        BaseCustomizePoint bcp;
        try {
            bcp = SimpleFactory.staticcast(obj);
        } catch (ClazzCastException ex) {
            throw new ConfigurationSyntaxErrorException("ClazzCastException");
        }

        if (bcp != null) {
            setCustomization(mode, bcp);
        }
    }

    private BaseCustomizePoint customizeGet = null;

    private void setCustomization(String method, BaseCustomizePoint newValue) {

        switch (method) {
            case Const.METHOD_GET:
                customizeGet = newValue;
                LOG.debug(Const.METHOD_GET + newValue.getClass());
                break;
            case Const.METHOD_PUT:
                customizePut = newValue;
                LOG.debug(Const.METHOD_PUT + newValue.getClass());
                break;
            case Const.METHOD_POST:
                customizePost = newValue;
                LOG.debug(Const.METHOD_POST + newValue.getClass());
                break;
            case Const.METHOD_DELETE:
                customizeDelete = newValue;
                LOG.debug(Const.METHOD_DELETE + newValue.getClass());
                break;
            case Const.METHOD_PATCH:
                customizePatch = newValue;
                LOG.debug(Const.METHOD_PATCH + newValue.getClass());
                break;
        }
    }

    private BaseCustomizePoint customizePut = null;

    private BaseCustomizePoint customizePost = null;
    private BaseCustomizePoint customizeDelete = null;
    private BaseCustomizePoint customizePatch = null;

    // 暫定
    // Provisional
    static final String DATA_SOURCE = "mysql";

    void procViewType(PageResult result, JsonObject node) throws ConfigurationSyntaxErrorException {

        JsonValue viewTypeNode = node.get("viewType");
        if (viewTypeNode == null) {
            result.viewType = "json";
        } else {
            result.viewType = JsonUtil.asStringStrictly(viewTypeNode);
        }
    }

    //
    // http://localhost:8080/restday/v1/aaa?master1_sqno=1
    //
    // select
    public void doGet(PageResult result, Map<String, String> actionForm, JsonObject getNode, GetParams getParams)
            throws RestdayException, SQLException {

        LOG.info("start doGet ");

        if (getNode == null) {
            throw new NullPointerException();
        }
        procViewType(result, getNode);

        if (simpleDao == null) {
            throw new NullPointerException();
        }

        JsonValue validateNode = getNode.get("validate");

        if (validateNode == null) {
            throw new ConfigurationSyntaxErrorException("validate not found", null, this.getAbsolutePathOfJson());
        }

        // 文字列を型に変換し、検証する
        // Convert string to type and validate
        DataAfterVerification validated = iValidator.doConversionAndValidation(actionForm, JsonUtil.asArrayStrictly(validateNode));

        if (validated.isError()) {
            result.addAll(validated.getErrorList());
            return;
        }
        // 相関チェック等あれば、Javaで記述する
        // If there is a correlation check etc., describe in Java
        if (customizeGet != null) {
            customizeGet.afterValidationAndConvert(validated);
            if (validated.isError()) {
                result.addAll(validated.getErrorList());
                return;
            }
        }

        JsonValue queryNode = getNode.get("sql");
        if (queryNode == null) {
            throw new ConfigurationSyntaxErrorException("sql not found");
        }

        String sqlTempl = JsonUtil.asStringStrictly(queryNode);

        if (aConUtil != null) {
            Connection conn = aConUtil.getConnection(DATA_SOURCE);
            try {
                LOG.debug("sql =" + sqlTempl);
                LOG.debug("validated=" + validated);

                Node node = tVer2.parseWhere(sqlTempl);

                Map<String, SQLValue> mapmap = validated.getMap();

                if (node.getType() == Node.TRIPLE) {

                    sqlTempl = tVer2.procGetParams(node, getParams);
                    LOG.debug("311) sql = " + sqlTempl);

                    JsonValue whereNode = getNode.get("where");  // where を取得
                    if (whereNode == null) {
                        throw new ConfigurationSyntaxErrorException("where not found of GET");
                    }
                    DataAfterVerification valiWhere = iValidator.doConversionAndValidation(getParams.getReqMap(), JsonUtil.asArrayStrictly(whereNode));
                    LOG.debug("320) valiWhere = " + valiWhere);

                    HashMap<String, SQLValue> tempMap = new HashMap<>();
                    tempMap.putAll(validated.getMap());
                    tempMap.putAll(valiWhere.getMap());
                    mapmap = tempMap;

                } else if (node.getType() != Node.STRING) {
                    throw new RestdayException("bad sqlTemplate");
                }

                DaoResult dr = simpleDao.doGet(conn, sqlTempl, mapmap);
                List<Map<String, Object>> obj = dr.getGetResults();
                
                if (customizeGet != null) {
                    result.returnValue = customizeGet.convToJson(obj);
                }
            } finally {
                closeIgnoreError(conn);
            }
        }
    }

    TemplateV2 tVer2 = new TemplateV2();

    void closeIgnoreError(Connection conn) {

        //  avoid mySQL's
        //  javax.net.ssl.SSLException: closing inbound before receiving peer's close_notify 
        try {
            if (conn.isClosed()) {
                LOG.warn("already connection closed");
            } else {
                conn.close();
            }
        } catch (Exception ex) {

        }
    }

    // insert
    public void doPost(PageResult result, JsonValue postedBody, JsonObject configJson, Map<String, String> actionForm)
            throws RestdayException, SQLException {

        LOG.info("------------- start doPost ");
        LOG.info("postedBody " + postedBody);
        LOG.info("configJson " + configJson.toString());

        if (configJson == null) {
            throw new NullPointerException("configJson");
        }
        procViewType(result, configJson);

        if (simpleDao == null) {
            throw new NullPointerException("simpleDao");
        }
        if (aConUtil == null) {
            throw new NullPointerException("aConUtil");
        }

        Map<String, String> input = this.customizePost.convertPOSTedBodyToMap(postedBody);
        input.putAll(actionForm);

        LOG.info("414) input = " + input);

        JsonValue checkNode = configJson.get("validate");

        if (checkNode == null) {
            throw new ConfigurationSyntaxErrorException("validate not found", null, this.getAbsolutePathOfJson());
        }

        DataAfterVerification validated = iValidator.doConversionAndValidation(input, JsonUtil.asArrayStrictly(checkNode));

        if (validated.isError()) {
            LOG.info("466) --------------- isError " + validated);
            result.addAll(validated.getErrorList());
            return;
        }

        if (customizePost != null) {
            customizePost.afterValidationAndConvert(validated);
            if (validated.isError()) {
                result.addAll(validated.getErrorList());
                return;
            }
        }

        JsonValue queryNode = configJson.get("sql");
        if (queryNode == null) {
            throw new ConfigurationSyntaxErrorException("sql not found");
        }
        String query = JsonUtil.asStringStrictly(queryNode);

        JsonValue queryNode2 = configJson.get("sql-rows");
        String query2 = null;
        if (queryNode2 != null) {
            query2 = JsonUtil.asStringStrictly(queryNode2);
        }

        DaoResult daoResult;
        Connection conn = aConUtil.getConnection(DATA_SOURCE);
        try {
            LOG.debug("sql=" + query);
            LOG.debug("validated=" + validated);
            daoResult = simpleDao.doPost(conn, query, validated.getMap());

            if (queryNode2 != null) {

                LOG.info("query2=" + query2);
                List<Map<String, SQLValue>> childList = validated.getListOfChildren();
                for (Map<String, SQLValue> mmap : childList) {

                    simpleDao.doPost(conn, query2, mmap);
                }
            }
        } finally {
            closeIgnoreError(conn);
        }

        result.returnValue = daoResult;
    }

    // update
    public void doPut(PageResult result, JsonValue postedBody, Map<String, String> actionForm, JsonObject putNode)
            throws RestdayException, SQLException {

        LOG.info("start doPut ");
        LOG.info("postedBody= " + postedBody);
        LOG.info("actionForm= " + actionForm);
        if (simpleDao == null) {
            throw new NullPointerException();
        }
        if (putNode == null) {
            throw new ConfigurationSyntaxErrorException("put not found", null);
        }

        procViewType(result, putNode);

        Map<String, String> input = this.customizePut.convertPOSTedBodyToMap(postedBody);
        input.putAll(actionForm);
        LOG.info("input= " + input);

        JsonValue checkNode = putNode.get("validate");

        if (checkNode == null) {
            throw new ConfigurationSyntaxErrorException("validate not found");
        }

        DataAfterVerification validated = iValidator.doConversionAndValidation(input, JsonUtil.asArrayStrictly(checkNode));

        if (validated.isError()) {
            LOG.warn("487) validation Error");
            result.returnValue = "";
            result.addAll(validated.getErrorList());
            return;
        }
        // 相関チェック等あれば、Javaで記述する
        // If there is a correlation check etc., describe in Java
        if (customizePut != null) {
            customizePut.afterValidationAndConvert(validated);
        }
        if (validated.isError()) {
            result.addAll(validated.getErrorList());
            return;
        }

        JsonValue queryNode = putNode.get("sql");
        String query = JsonUtil.asStringStrictly(queryNode);

        if (aConUtil != null) {
            Connection conn = aConUtil.getConnection(DATA_SOURCE);
            try {
                LOG.debug("599)sql=" + query);
                LOG.debug("600)validated=" + validated);
                simpleDao.doPut(conn, query, validated.getMap());
            } finally {
                this.closeIgnoreError(conn);
            }
        }
    }

    public void doPatch(PageResult result, JsonValue postedBody, Map<String, String> actionForm, JsonObject patchNode)
            throws RestdayException, SQLException {

        LOG.info("start doPut ");
        LOG.info("postedBody= " + postedBody);
        LOG.info("actionForm= " + actionForm);
        if (simpleDao == null) {
            throw new NullPointerException();
        }

        if (patchNode == null) {
            throw new ConfigurationSyntaxErrorException("patch not found");
        }

        procViewType(result, patchNode);

        Map<String, String> input = this.customizePatch.convertPOSTedBodyToMap(postedBody);
        input.putAll(actionForm);
        LOG.info("input= " + input);

        JsonValue validatedNode = patchNode.get("validate");

        if (validatedNode == null) {
            throw new ConfigurationSyntaxErrorException("validate not found", null, this.getAbsolutePathOfJson());
        }

        DataAfterVerification validated = iValidator.doConversionAndValidation(input, JsonUtil.asArrayStrictly(validatedNode));

        if (validated.isError()) {
            LOG.warn("609)  Error");
            result.returnValue = "";   // Pending
            result.addAll(validated.getErrorList());
            return;
        }
        // 相関チェック等あれば、Javaで記述する
        // If there is a correlation check etc., describe in Java
        if (customizePatch != null) {
            customizePatch.afterValidationAndConvert(validated);
        }
        if (validated.isError()) {
            result.addAll(validated.getErrorList());
            return;
        }

        JsonValue queryNode = patchNode.get("sql");
        String query = JsonUtil.asStringStrictly(queryNode);

        if (aConUtil != null) {
            Connection conn = aConUtil.getConnection(DATA_SOURCE);
            try {
                LOG.info("conn=" + conn);
                LOG.info("sql=" + query);
                LOG.info("validated=" + validated);
                simpleDao.doDelete(conn, query, validated.getMap());
            } finally {
                this.closeIgnoreError(conn);
            }
        }
    }

    // delete
    public void doDelete(PageResult result, Map<String, String> actionForm, JsonObject deleteNode) throws RestdayException, SQLException {

        LOG.info("start doDelete ");
        LOG.info("actionForm= " + actionForm);

        if (deleteNode == null) {
            throw new NullPointerException();
        }
        procViewType(result, deleteNode);

        if (simpleDao == null) {
            throw new NullPointerException();
        }

        JsonValue validatedNode = deleteNode.get("validate");

        if (validatedNode == null) {
            throw new ConfigurationSyntaxErrorException("validate not found");
        }

        DataAfterVerification validated = iValidator.doConversionAndValidation(actionForm, JsonUtil.asArrayStrictly(validatedNode));

        if (validated.isError()) {
            result.addAll(validated.getErrorList());
            return;
        }
        // 相関チェック等あれば、Javaで記述する
        // If there is a correlation check etc., describe in Java
        if (customizeDelete != null) {
            customizeDelete.afterValidationAndConvert(validated);
            if (validated.isError()) {
                result.addAll(validated.getErrorList());
                return;
            }
        }

        JsonValue deleteSqlNode = deleteNode.get("sql");
        if (deleteSqlNode == null) {
            throw new ConfigurationSyntaxErrorException("sql not found");
        }
        String query = JsonUtil.asStringStrictly(deleteSqlNode);

        if (aConUtil != null) {
            Connection conn = aConUtil.getConnection(DATA_SOURCE);
            try {
                LOG.info("conn=" + conn);
                LOG.info("sql=" + query);
                LOG.info("checked=" + validated);
                simpleDao.doDelete(conn, query, validated.getMap());
            } finally {
                this.closeIgnoreError(conn);
            }
        }
    }
}
