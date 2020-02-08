package jp.oops.clazz.restday.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;

import jp.oops.clazz.restday.Const;
import jp.oops.clazz.restday.dao.ValidatorImpl;
import jp.oops.clazz.restday.factory.BeanFactoryEx;
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
public class BasicFrameworkServletOtherTest {

    private final static Logger LOG = LogManager.getLogger(BasicFrameworkServletOtherTest.class);

    MockServletContext msc;
    MockSimpleDao simpleDao;
    ValidatorImpl vali;
    MockConnection mCon;
    MockConUtil conUtil;
    File testJsonPlace = new File("unit_test");

    
    public BasicFrameworkServletOtherTest() {

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

    // 正常終了は、201が返る事
    // Normal completion is returned by 201
    @Test
    public void testCasePost01() throws Exception {

        LOG.info("******************************testCasePost01");
        String THIS_METHOD = Const.METHOD_POST;
        
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/post02/11");
        req.setBufferedReader(getReader("{ \"aa\" : 111, \"bb\" : \"xyz\"    }"));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doPost(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(201, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
    }

    // Daoの中で、SQLExceptionが発生したら、409が返る事
    // If an SQLException occurs in Dao, 409 is returned
    @Test
    public void testCasePost02() throws Exception {

        LOG.info("******************************testCasePost02");
        String THIS_METHOD = Const.METHOD_POST;
        simpleDao.setSQLException("testCasePost02");  // cause SQLException
        
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/post02/11");
        req.setBufferedReader(getReader("{ \"aa\" : 111, \"bb\" : \"xyz\"    }"));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doPost(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(409, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
    }
    
    @Test
    public void testCaseDelete01() throws Exception {

        LOG.info("******************************testCaseDelete01");
        String THIS_METHOD = Const.METHOD_DELETE;
        
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/post02/11");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doDelete(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(204, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
    }


    @Test
    public void testCaseDelete02() throws Exception {

        LOG.info("******************************testCaseDelete02");
        String THIS_METHOD = Const.METHOD_DELETE;
        simpleDao.setSQLException("testCaseDelete02");  // cause SQLException

        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/post02/11");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doDelete(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(409, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
    }

    @Test
    public void testCasePut01() throws Exception {

        LOG.info("******************************testCasePut01");
        String THIS_METHOD = Const.METHOD_PUT;

        
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/post02/11");
        req.setBufferedReader(getReader("{ \"aa\" : 111, \"bb\" : \"xyz\"    }"));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doPut(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(204, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
    }

    @Test
    public void testCasePut02() throws Exception {

        LOG.info("******************************testCasePut02");
        String THIS_METHOD = Const.METHOD_PUT;
        simpleDao.setSQLException("testCasePut02");  // cause SQLException
        
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/post02/11");
        req.setBufferedReader(getReader("{ \"aa\" : 111, \"bb\" : \"xyz\"    }"));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doPut(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(409, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
    }

    @Test
    public void testCasePatch01() throws Exception {

        LOG.info("******************************testCasePatch01");
        String THIS_METHOD = Const.METHOD_PATCH;

        
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/post02/11");
        req.setBufferedReader(getReader("{ \"aa\" : 111, \"bb\" : \"xyz\"    }"));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doPatch(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(404, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);
    }

}
