package jp.oops.clazz.restday.factory;

import java.util.HashMap;
import jp.oops.clazz.restday.blogic.RestBusinessLogic;
import jp.oops.clazz.restday.dao.ISimpleDao;
import jp.oops.clazz.restday.dao.IValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class BeanFactoryEx {

    private final static Logger LOG = LogManager.getLogger(BeanFactoryEx.class);

    // static にするJunitがうまく動かないので、 staticにしない事
    // Do not make static because Junit does not work well
    final HashMap<String, RestBusinessLogic> OBJECTS_MAP_1 = new HashMap<>();

    private final ISimpleDao simpleDao;

    private final IConUtil util;
    private final String appPath;
    private final IValidator iValidator;

    private final boolean developModep;
    
    public BeanFactoryEx(IConUtil util, String appPath, ISimpleDao dao, IValidator vali) {

        this.util = util;
        this.appPath = appPath;
        this.simpleDao = dao;
        this.iValidator = vali;
        this.developModep = false;
    }
    public BeanFactoryEx(IConUtil util, String appPath, ISimpleDao dao, IValidator vali, boolean b) {

        this.util = util;
        this.appPath = appPath;
        this.simpleDao = dao;
        this.iValidator = vali;
        this.developModep = b;
    }

    public RestBusinessLogic getNamedBean(String compName) {

        synchronized (OBJECTS_MAP_1) {
            RestBusinessLogic obj = OBJECTS_MAP_1.get(compName);
            if (obj == null) {
                RestBusinessLogic temp = new RestBusinessLogic();
                OBJECTS_MAP_1.put(compName, temp);

                temp.setConUtil(util);
                temp.setISimpleDao(simpleDao);
                temp.setIValidator(iValidator);
                temp.setAppPath(appPath);
                temp.setDeveloperMode(developModep);

                LOG.debug(" new !! " + temp);
                return temp;
            } else {
                LOG.debug(" found in map !! " + obj);
                return obj;
            }
        }
    }

    public void clearNamedBean() {
        
        synchronized (OBJECTS_MAP_1) {
            OBJECTS_MAP_1.clear();
        }
        LOG.debug("clearNamedBean()");
    }
    
    
    public ISimpleDao getSimpleDao() {
        return this.simpleDao;
    }
}
