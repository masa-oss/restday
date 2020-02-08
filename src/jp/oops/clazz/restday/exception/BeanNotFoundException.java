package jp.oops.clazz.restday.exception;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class BeanNotFoundException extends Exception {
    
    public BeanNotFoundException(String msg, Throwable th) {
        super(msg, th);
    }
    
}
