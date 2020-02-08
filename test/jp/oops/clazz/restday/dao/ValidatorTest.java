package jp.oops.clazz.restday.dao;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class ValidatorTest {


    @Test
    public void testProcessRule_int() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_int");

        final JsonValue json = Json.parse(" [  \"int\", \"min\" , 1, \"max\" , 10 ] ");

        System.out.println("json = " + json);

        ValidatorImpl instance = new ValidatorImpl();
        SQLValue sv = instance.processRule("aa", json.asArray(), "5");

        System.out.println("sv = " + sv);
        assertEquals(SQLValue.INTEGER, sv.getType());
        assertNull(sv.getErrorMsg());
    }

    @Test
    public void testProcessRule_string() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_string");

        final JsonValue json = Json.parse(" [  \"string\" ] ");

        System.out.println("json = " + json);

        ValidatorImpl instance = new ValidatorImpl();
        SQLValue sv = instance.processRule("aa", json.asArray(), "abc");

        System.out.println("sv = " + sv);
        assertEquals(SQLValue.STRING, sv.getType());
        assertNull(sv.getErrorMsg());
    }

    @Test
    public void testProcessRule_double() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_double");

        final JsonValue json = Json.parse(" [  \"double\" ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "123.4");

        System.out.println("sv = " + sv);
        assertEquals(SQLValue.DOUBLE, sv.getType());
        assertNull(sv.getErrorMsg());
    }

    @Test
    public void testProcessRule_long() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_long");

        final JsonValue json = Json.parse(" [  \"long\" ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "1234567");

        System.out.println("sv = " + sv);
        assertEquals(SQLValue.LONG, sv.getType());
        assertNull(sv.getErrorMsg());
    }

    @Test
    public void testProcessRule_BigDecimal() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_BigDecimal");

        final JsonValue json = Json.parse(" [  \"BigDecimal\" ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "12345.67");

        System.out.println("sv = " + sv);
        assertEquals(SQLValue.BIGDECIMAL, sv.getType());
        assertNull(sv.getErrorMsg());
    }

    @Test
    public void testProcessRule_Date() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Date");

        final JsonValue json = Json.parse(" [  \"Date\" ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "2020-01-19");

        System.out.println("sv = " + sv);
        assertEquals(SQLValue.DATE, sv.getType());
        assertNull(sv.getErrorMsg());
    }

    @Test
    public void testProcessRule_Timestamp() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Timestamp");

        final JsonValue json = Json.parse(" [  \"Timestamp\" ,     \"yyyy/MM/dd HH:mm:ss\"   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "2017/02/06 15:29:07");

        System.out.println("sv = " + sv);
        assertEquals(SQLValue.TIMESTAMP, sv.getType());
        assertNull(sv.getErrorMsg());
    }

    @Test
    public void testProcessRule_Timestamp2() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Timestamp2");

        final JsonValue json = Json.parse(" [  \"Timestamp\" ,     \"yyyy-MM-dd HH:mm:ss.S\"   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "2020-01-22 17:24:07.843");

        System.out.println("sv = " + sv);
        assertEquals(SQLValue.TIMESTAMP, sv.getType());
        assertNull(sv.getErrorMsg());
    }


    @Test
    public void testProcessRule_Bug() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Bug");

        final JsonValue json = Json.parse(" [  \"int\" ,     \"range-from\", 1, \"range-to\", 10   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "2017");

        System.out.println("sv = " + sv);
//        assertEquals(  SQLValue.TIMESTAMP,   sv.getType()         );

        assertNull(sv.getErrorMsg());
    }

    //-------- validation (min) --------

    // rule is min 1 & input value = 0 then error
    @Test
    public void testProcessRule_Min_Int_1() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Min_Int_1");

        final JsonValue json = Json.parse(" [  \"int\" ,     \"min\", 1   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "0");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNotNull(em);
        assertEquals(  "aa",   em.getArg1()  );
    }

    // rule is min 0 & input value = 0 then ok
    @Test
    public void testProcessRule_Min_Int_2() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Min_Int_2");

        final JsonValue json = Json.parse(" [  \"int\" ,     \"min\", 0   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "0");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNull(em);
    }



    // rule is min 2 & input value = "a" then error
    @Test
    public void testProcessRule_Min_String_1() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Min_String_1");

        final JsonValue json = Json.parse(" [  \"string\" ,     \"min\", 2   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "a");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNotNull(em);
        assertEquals(  "aa",   em.getArg1()  );
    }
    
    // rule is min 2 & input value = "ab" then ok
    @Test
    public void testProcessRule_Min_String_2() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Min_String_2");

        final JsonValue json = Json.parse(" [  \"string\" ,     \"min\", 2   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "ab");

        System.out.println("sv = " + sv);
        
        assertEquals( SQLValue.STRING, sv.getType()   );
        
        ErrorMsg em = sv.getErrorMsg();

        assertNull(em);
    }

    //-------- validation (max) --------

    // rule is min 1 & input value = 2 then error
    @Test
    public void testProcessRule_Max_Int_1() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Max_Int_1");

        final JsonValue json = Json.parse(" [  \"int\" ,     \"max\", 1   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "2");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNotNull(em);
        assertEquals(  "aa",   em.getArg1()  );
    }

    // rule is min 0 & input value = 0 then ok
    @Test
    public void testProcessRule_Max_Int_2() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Max_Int_2");

        final JsonValue json = Json.parse(" [  \"int\" ,     \"max\", 0   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "0");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNull(em);
    }
    
    // rule is max 2 & input value = "abc" then error
    @Test
    public void testProcessRule_Max_String_1() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Max_String_1");

        final JsonValue json = Json.parse(" [  \"string\" ,     \"max\", 2   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "abc");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNotNull(em);
        assertEquals(  "aa",   em.getArg1()  );
    }
    
    // rule is max 2 & input value = "ab" then ok
    @Test
    public void testProcessRule_Max_String_2() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_Max_String_2");

        final JsonValue json = Json.parse(" [  \"string\" ,     \"max\", 2   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "ab");

        System.out.println("sv = " + sv);
        
        assertEquals( SQLValue.STRING, sv.getType()   );
        
        ErrorMsg em = sv.getErrorMsg();

        assertNull(em);
    }
    
    
    //-------- validation (min-max) --------

    // rule is min-max 2 5  & input value = 1 then error
    @Test
    public void testProcessRule_MinMax_Int_1() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_MinMax_Int_1");

        final JsonValue json = Json.parse(" [  \"int\", \"rename\", \"cc\"  ,  \"min-max\", 2 , 5   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "1");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNotNull(em);
        assertEquals(  "aa",   em.getArg1()  );
        assertEquals("cc", sv.getSpecifiedColumnName());
    }

    // rule is min-max 2 5  & input value = 2 then ok
    @Test
    public void testProcessRule_MinMax_Int_2() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_MinMax_Int_2");

        final JsonValue json = Json.parse(" [  \"int\" ,  \"min-max\", 2 , 5    ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "2");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNull(em);
    }
    

    // rule is min-max 2 5  & input value = 5 then ok
    @Test
    public void testProcessRule_MinMax_Int_3() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_MinMax_Int_3");

        final JsonValue json = Json.parse(" [  \"int\" ,  \"min-max\", 2 , 5    ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "5");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNull(em);
    }

    // rule is min-max 2 5  & input value = 6 then error
    @Test
    public void testProcessRule_MinMax_Int_4() throws ConfigurationSyntaxErrorException {

        System.out.println("testProcessRule_MinMax_Int_1");

        final JsonValue json = Json.parse(" [  \"int\", \"rename\", \"cc\"  ,  \"min-max\", 2 , 5   ] ");
        System.out.println("json = " + json);

        SQLValue sv = new ValidatorImpl().processRule("aa", json.asArray(), "6");

        System.out.println("sv = " + sv);
        ErrorMsg em = sv.getErrorMsg();

        assertNotNull(em);
        assertEquals(  "aa",   em.getArg1()  );

        assertEquals("cc", sv.getSpecifiedColumnName());
    }

    
}
