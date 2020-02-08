package jp.oops.clazz.restday.blogic;

import jp.oops.clazz.restday.exception.BeanNotFoundException;
import jp.oops.clazz.restday.exception.ClazzCastException;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class SimpleFactory {

    public static Object createInstance(String clazzName) throws BeanNotFoundException {

        Class<?> clazz;
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException cnfe) {
            throw new BeanNotFoundException("ClassNotFound:" + clazzName, cnfe);
        }

        Object temp;
        try {
            temp = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new BeanNotFoundException("Exception:" + clazzName, ex);
        }

        return (temp);
    }

    @SuppressWarnings("unchecked")
    public static <T> T staticcast(Object obj) throws ClazzCastException {

        T castObj;
        try {
            castObj = (T) obj;
        } catch (Exception ex) {
            throw new ClazzCastException(ex);
        }
        return castObj;
    }
    
    private SimpleFactory() {
    }
}
