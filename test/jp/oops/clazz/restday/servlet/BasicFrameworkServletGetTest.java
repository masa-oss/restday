package jp.oops.clazz.restday.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import jp.oops.clazz.restday.Const;
import jp.oops.clazz.restday.dao.SQLValue;
import jp.oops.clazz.restday.dao.ValidatorImpl;
import jp.oops.clazz.restday.factory.BeanFactoryEx;

import jp.oops.clazz.test.util.DaoArg;
import jp.oops.clazz.test.util.MockConUtil;
import jp.oops.clazz.test.util.MockConnection;
import jp.oops.clazz.test.util.MockHttpServletRequest;
import jp.oops.clazz.test.util.MockHttpServletResponse;
import jp.oops.clazz.test.util.MockServletContext;
import jp.oops.clazz.test.util.MockSimpleDao;
import jp.oops.clazz.test.util.MockServletConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <div>このテストはDaoをMockSimpleDaoに差し替える事でテストをしている</div>
 * <div>This test is done by replacing Dao with MockSimpleDao</div>
 *
 * <div>そのため、MySQLは無くても、テストできる</div>
 * <div>So you can test without MySQL</div>
 *
 * <div>MySQLまでつないでテストしたい時は、tomcatを起動してindex.htmlにアクセスしてください</div>
 * <div>If you want to connect to MySQL and test, start tomcat and access index.html</div>
 *
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class BasicFrameworkServletGetTest {

    private final static Logger LOG = LogManager.getLogger(BasicFrameworkServletGetTest.class);

    MockServletContext msc;
    MockSimpleDao simpleDao;
    ValidatorImpl vali;
    MockConnection mCon;
    MockConUtil conUtil;

    // プロジェクトフォルダーにある unit_test の下にある jsonファイルを読むので注意
    // Be careful as you read the json file under unit_test in the project folder
    //
    // webapps/WEB-INF/json.cfg の下のファイルは読まないので、気を付けてください。
    // Be careful not to read the files under webapps/WEB-INF/json.cfg.
    
    File testJsonPlace = new File("unit_test");

    String THIS_METHOD = Const.METHOD_GET;

    public BasicFrameworkServletGetTest() {

        msc = new MockServletContext();
        simpleDao = new MockSimpleDao();
        vali = new ValidatorImpl();
        mCon = new MockConnection();
        conUtil = new MockConUtil(mCon);

        LOG.info("getAbsolutePath = " + testJsonPlace.getAbsolutePath());
//        LOG.info("getCanonicalPath = " + testJsonPlace.getCanonicalPath());
    }

    private BufferedReader getReader(String text) {

        StringReader sr = new StringReader(text);
        return new BufferedReader(sr);
    }

    // pqr.json
    // ファイルがみつからない時
    @Test
    public void testCase01() throws Exception {

        LOG.info("******************************testCase01");
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/pqr/11/22");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(404, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);

        assertTrue(ws.contains("error"));
        assertTrue(ws.contains("pqr.json"));
    }

    @Test
    public void testCase02() throws Exception {

        LOG.info("******************************testCase02");
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/get02/11/22");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(404, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);

        assertTrue(ws.contains("error"));
        assertTrue(ws.contains("get02.json"));
    }

    @Test
    public void testCase03() throws Exception {

        LOG.info("******************************testCase03");
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/get03/11/22");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(404, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);

        assertTrue(ws.contains("error"));
        //assertTrue(ws.contains("get03.json"));
    }

    @Test
    public void testCase04() throws Exception {

        LOG.info("******************************testCase04");
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/aaa/ab/22");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

//        assertEquals(404, res.getStatus());
        assertEquals(200, res.getStatus());   // ************* Pending
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());
        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
        assertTrue(ws.contains("pkey1"));
    }

    @Test
    public void testCase06() throws Exception {

        LOG.info("******************************testCase06");
        simpleDao.setSQLException("testCase06");
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/aaa/66/22");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        assertEquals(409, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());
        //      assertNull(res.getContentType());
        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
        assertTrue(ws.isEmpty());
    }

    // daoの中で、例外発生
    @Test
    public void testCase07() throws Exception {

        LOG.info("******************************testCase07");
        simpleDao.setRestdayException("testCase07");
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/aaa/66/22");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        assertEquals(409, res.getStatus());   
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
        assertNotNull(ws);
        assertTrue(ws.length() > 0);
    }

    @Test
    public void testCase10() throws Exception {

        LOG.info("******************************testCase10");
        LOG.info("mockDao = " + simpleDao);
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/aaa/11/22");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        assertEquals(HttpServletResponse.SC_OK, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());
        LOG.info("writtedString = " + res.getWrittedString());

        MockSimpleDao afterExec = (MockSimpleDao) bfe.getSimpleDao();
        LOG.info("mockDao(2) = " + afterExec);

        List<DaoArg> argsCalled = afterExec.getSelectCalls();
        LOG.info("# of called = " + argsCalled.size());
        assertEquals(1, argsCalled.size());

        DaoArg first = argsCalled.get(0);
        Map<String, SQLValue> validated = first.getValidated();

        LOG.info("validated = " + validated);
        assertEquals(SQLValue.createInt(11), validated.get("pkey1"));
    }

    @Test
    public void testCase11() throws Exception {

        LOG.info("******************************testCase11");
        LOG.info("mockDao = " + simpleDao);
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/get11");
        req.setBufferedReader(getReader(""));
        req.putParameter("aa", "12345");

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        assertEquals(HttpServletResponse.SC_OK, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());
        LOG.info("writtedString = " + res.getWrittedString());

        MockSimpleDao afterExec = (MockSimpleDao) bfe.getSimpleDao();
        LOG.info("mockDao(2) = " + afterExec);

        List<DaoArg> argsCalled = afterExec.getSelectCalls();
        LOG.info("# of called = " + argsCalled.size());
        assertEquals(1, argsCalled.size());

        DaoArg first = argsCalled.get(0);
        Map<String, SQLValue> validated = first.getValidated();

        LOG.info("validated = " + validated);
        assertEquals(SQLValue.createInt(12345), validated.get("aa"));

        String templ = first.getSqlTempl();
        LOG.info("SQL templ = " + templ);
        assertEquals("select * from master1  where aa=?aa", templ);
    }

    @Test
    public void testCase12() throws Exception {

        LOG.info("******************************testCase12");
        LOG.info("mockDao = " + simpleDao);
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/get11");
        req.setBufferedReader(getReader(""));
        req.putParameter("bb", "xyzww");

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        assertEquals(HttpServletResponse.SC_OK, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());
        LOG.info("writtedString = " + res.getWrittedString());

        MockSimpleDao afterExec = (MockSimpleDao) bfe.getSimpleDao();
        LOG.info("mockDao(2) = " + afterExec);

        List<DaoArg> argsCalled = afterExec.getSelectCalls();
        LOG.info("# of called = " + argsCalled.size());
        assertEquals(1, argsCalled.size());

        DaoArg first = argsCalled.get(0);
        Map<String, SQLValue> validated = first.getValidated();

        LOG.info("validated = " + validated);
        assertEquals(SQLValue.createString("xyzww"), validated.get("bb"));

        String templ = first.getSqlTempl();
        LOG.info("SQL templ = " + templ);
        assertEquals("select * from master1  where bb=?bb", templ);
    }

    @Test
    public void testCase13() throws Exception {

        LOG.info("******************************testCase13");
        LOG.info("mockDao = " + simpleDao);
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/get11");
        req.setBufferedReader(getReader(""));
        req.putParameter("aa", "1234");
        req.putParameter("bb", "xyzww");

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        assertEquals(HttpServletResponse.SC_OK, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());
        LOG.info("writtedString = " + res.getWrittedString());

        MockSimpleDao afterExec = (MockSimpleDao) bfe.getSimpleDao();
        LOG.info("mockDao(2) = " + afterExec);

        List<DaoArg> argsCalled = afterExec.getSelectCalls();
        LOG.info("# of called = " + argsCalled.size());
        assertEquals(1, argsCalled.size());

        DaoArg first = argsCalled.get(0);
        Map<String, SQLValue> validated = first.getValidated();

        LOG.info("validated = " + validated);
        assertEquals(SQLValue.createInt(1234), validated.get("aa"));
        assertEquals(SQLValue.createString("xyzww"), validated.get("bb"));

        String templ = first.getSqlTempl();
        LOG.info("SQL templ = " + templ);
        assertEquals("select * from master1  where aa=?aa AND bb=?bb", templ);
    }

}
