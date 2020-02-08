package jp.oops.clazz.restday.exception;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class ConfigurationFileException extends ConfigurationException {
    
    public ConfigurationFileException(String msg) {
        super(msg);
    }

    public ConfigurationFileException(String msg, Throwable th) {
        super(msg, th);
    }
}
