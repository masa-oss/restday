package jp.oops.clazz.restday.factory;

import jp.oops.clazz.restday.dao.ISimpleDao;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public interface ISimpleDaoAware {
    
    void setISimpleDao(ISimpleDao dao);
    
    void setAppPath(String path);
}
