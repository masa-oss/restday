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
public class BasicFrameworkServletCustomizeTest {

    private final static Logger LOG = LogManager.getLogger(BasicFrameworkServletCustomizeTest.class);

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

    public BasicFrameworkServletCustomizeTest() {

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

    @Test
    public void testCaseCustomize01() throws Exception {

        LOG.info("******************************testCaseCustomize01");
        BeanFactoryEx bfe = new BeanFactoryEx(conUtil, testJsonPlace.getAbsolutePath(), simpleDao, vali);
        msc.setAttribute(Const.KEY_BEAN_FACTORY, bfe);

        MockHttpServletRequest req = new MockHttpServletRequest(msc);
        req.setMethod(THIS_METHOD);
        req.setPathInfo("/customizeGet/11");
        req.setBufferedReader(getReader(""));

        MockHttpServletResponse res = new MockHttpServletResponse();

        BasicFrameworkServlet instance = new BasicFrameworkServlet();
        instance.init(new MockServletConfig(msc));
        // Test 
        instance.doGet(req, res);

        LOG.info("getStatus = " + res.getStatus());
        assertEquals(200, res.getStatus());
        assertEquals(Const.JSON_CONTENT_TYPE, res.getContentType());

        String ws = res.getWrittedString();
        LOG.info("writtedString = " + ws);

    }


}
