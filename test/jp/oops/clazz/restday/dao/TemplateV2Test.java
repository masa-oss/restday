package jp.oops.clazz.restday.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi . </div>
 * <div>This software is released under the MIT License.</div>
 */
public class TemplateV2Test {
    
    @Test
    public void testParseWhere1() throws Exception {
        
        System.out.println("testParseWhere1");
        String sqlTempl = "select * from emp {w aa=?aa && bb = ?bb} order by id desc";
        TemplateV2 instance = new TemplateV2();
        Node result = instance.parseWhere(sqlTempl);
        System.out.println("result = " + result.getType()  );
        assertEquals(0, result.getType());

        System.out.println("result = " + result.getLeft().getStr()  );
        System.out.println("result = " + result.getCurrent().getStrList()  );
        System.out.println("result = " + result.getRight().getStr() );

        
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("aa", "123");
        
        List<String> pathInfo = new ArrayList<>();
        pathInfo.add("bbb");
        
        GetParams getParams = new GetParams(reqMap, pathInfo);

        String str = instance. procGetParams( result,  getParams);

        System.out.println("str = " + str );

        assertTrue(str.contains("where aa=?"));
    }

    @Test
    public void testParseWhere2() throws Exception {
        
        System.out.println("testParseWhere2");
        String sqlTempl = "select * from emp {w aa=?aa && bb = ?bb} order by id desc";
        TemplateV2 instance = new TemplateV2();
        Node result = instance.parseWhere(sqlTempl);
        System.out.println("result = " + result.getType()  );
        assertEquals(0, result.getType());

        System.out.println("result = " + result.getLeft().getStr()  );
        System.out.println("result = " + result.getCurrent().getStrList()  );
        System.out.println("result = " + result.getRight().getStr() );

        
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("aa", "123");
        reqMap.put("bb", "123");
        
        List<String> pathInfo = new ArrayList<>();
        pathInfo.add("bbb");
        
        GetParams getParams = new GetParams(reqMap, pathInfo);

        String str = instance. procGetParams( result,  getParams);

        System.out.println("str = " + str );

        assertTrue(str.contains("where aa=?aa AND bb = ?bb"));
    }
    
    @Test
    public void testParseWhere3() throws Exception {
        
        System.out.println("testParseWhere3");
        String sqlTempl = "select * from emp {w aa=?aa && bb = ?bb} order by id desc";
        TemplateV2 instance = new TemplateV2();
        Node result = instance.parseWhere(sqlTempl);
        System.out.println("result = " + result.getType()  );
        assertEquals(0, result.getType());

        System.out.println("result = " + result.getLeft().getStr()  );
        System.out.println("result = " + result.getCurrent().getStrList()  );
        System.out.println("result = " + result.getRight().getStr() );

        
        Map<String, String> reqMap = new HashMap<>();
        
        List<String> pathInfo = new ArrayList<>();
        pathInfo.add("bbb");
        
        GetParams getParams = new GetParams(reqMap, pathInfo);

        String str = instance. procGetParams( result,  getParams);

        System.out.println("str = " + str );

        assertTrue(! str.contains("aa=?"));
    }
    
    @Test
    public void testParseWhere4() throws Exception {
    
        System.out.println("testParseWhere4");
        TemplateV2 tVer2 = new TemplateV2();
        
        String sqlTempl = "select * from user_t {w ?from <= first_visit  && first_visit <= ?to}";
        
        Node node = tVer2.parseWhere(sqlTempl);
        
        Map<String, String> reqMap = new HashMap<>();
        List<String> pathInfo = null;
        reqMap.put("to", "2019-12-30");

        GetParams getParams = new GetParams(reqMap, pathInfo);
        
        assertEquals(Node.TRIPLE, node.getType() ) ;

        sqlTempl = tVer2.procGetParams(node, getParams);

        System.out.println("311) sql = " + sqlTempl);
        
        assertEquals( "select * from user_t " +" where "+ "first_visit <= ?to" , sqlTempl  );
    }   
    
    
    @Test
    public void testParseWhere5() throws Exception {
    
        System.out.println("testParseWhere5");
        TemplateV2 tVer2 = new TemplateV2();
        
        String sqlTempl = "select * from user_t {w ?from <= first_visit  && first_visit <= ?to}";
        
        Node node = tVer2.parseWhere(sqlTempl);
        
        Map<String, String> reqMap = new HashMap<>();
        List<String> pathInfo = null;
        reqMap.put("from", "2019-12-30");

        GetParams getParams = new GetParams(reqMap, pathInfo);
        
        assertEquals(Node.TRIPLE, node.getType() ) ;

        sqlTempl = tVer2.procGetParams(node, getParams);

        System.out.println("311) sql = " + sqlTempl);
        
        assertEquals( "select * from user_t " +" where "+ "?from <= first_visit" , sqlTempl  );
    }   
    
    
}
