package jp.oops.clazz.restday.dao;

import java.util.Map;
import jp.oops.clazz.restday.exception.RestdayException;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class TemplateParserTest {
    

    @Test
    public void testParseTemplate() {
        System.out.println("parseTemplate");
        //
        String sqlTempl = "select * from master1 where  master1_sqno = ?master1_sqno ";

        SqlTemplateResults result = null;
        try {
            result = SqlTemplateResults.parseTemplate(sqlTempl);
        } catch (RestdayException ex2) {
            ex2.printStackTrace();
        }
        Map<String, Integer> map = result.getMap();

        assertEquals(1, map.size());
        assertTrue(map.containsKey("master1_sqno"));
    }
    
    
}
