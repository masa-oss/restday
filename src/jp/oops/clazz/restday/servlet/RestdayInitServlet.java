package jp.oops.clazz.restday.servlet;

import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;

import jp.oops.clazz.restday.Const;
import jp.oops.clazz.restday.dao.MySqlDaoImpl;
import jp.oops.clazz.restday.dao.ValidatorImpl;
import jp.oops.clazz.restday.factory.BeanFactoryEx;
import jp.oops.clazz.restday.factory.IConUtil;
import jp.oops.clazz.restday.factory.IConUtilImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class RestdayInitServlet extends HttpServlet {

    private final static Logger LOG = LogManager.getLogger(RestdayInitServlet.class);

    final IConUtil conUtil;

    public RestdayInitServlet() {
        conUtil = new IConUtilImpl(this);
    }

    HashMap<String, String> servletParamMap = new HashMap<>();
    
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        LOG.info("------------------- START -------------------");
        
//        servletParamMap.put("URL_BASE", "http://127.0.0.1:8080/restday/v1/");

        String url = this.getInitParameter("url");
        servletParamMap.put("URL_BASE", url);
        
        
        String path = getServletContext().getRealPath("/");

        LOG.info("path = '" + path + "'");

        String mode = this.getInitParameter("develop");

        LOG.info("develop mode = '" + mode + "'");

        MySqlDaoImpl simpleDaoImpl = new MySqlDaoImpl();
        ValidatorImpl vali = new ValidatorImpl();

        // initParameterにdevelopタグがあれば、開発者モードにする。
        // If there is a develop tag in initParameter, change to developer mode.
        //
        // 本番モードでは、initParameterにdevelopタグを書かないでください
        // In production mode, do not write a develop tag in initParameter
        BeanFactoryEx factory = (mode != null) ? new BeanFactoryEx(conUtil, path, simpleDaoImpl, vali, true) : new BeanFactoryEx(conUtil, path, simpleDaoImpl, vali);

        getServletContext().setAttribute(Const.KEY_BEAN_FACTORY, factory);
        
        getServletContext().setAttribute(Const.KEY_SERV_PARAMS, servletParamMap);

        LOG.info("-------------------  END  -------------------");
    }

}
