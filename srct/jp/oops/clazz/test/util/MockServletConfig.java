package jp.oops.clazz.test.util;

import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 *
 * <div> Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div> This software is released under the MIT License.</div>
 */
public class MockServletConfig implements ServletConfig {

    ServletContext theSc;
    
    public MockServletConfig(ServletContext sc) {
        theSc = sc;
    }
    
    @Override
    public String getServletName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ServletContext getServletContext() {
        return theSc;
    }

    @Override
    public String getInitParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
