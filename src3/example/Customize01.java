package example;

import jp.oops.clazz.restday.blogic.BaseCustomizePoint;
import jp.oops.clazz.restday.dao.DataAfterVerification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class Customize01 extends  BaseCustomizePoint  {
    
    private final Logger LOG = LogManager.getLogger(Customize01.class);
    
    
    @Override
    public void afterValidationAndConvert(DataAfterVerification checked) {
        LOG.info("------------------     example.Customize01  called ");
    }
}
