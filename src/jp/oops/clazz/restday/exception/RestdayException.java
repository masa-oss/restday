package jp.oops.clazz.restday.exception;
/**
 * 
 * <div>Copyright (c) 2019-2020 Masahito Hemmi</div> 
 * <div>This software is released under the MIT License.</div>
 */
public class RestdayException extends Exception {
    
    public RestdayException(String msg) {
        super(msg);
    }
    
    public RestdayException(String msg, Throwable th) {
        super(msg, th);
    }

    public RestdayException(String msg, String forDev) {
        super(msg);
        messageForDevelopers = forDev;
    }
    
    
    protected String messageForDevelopers = "";
    
    public String getMessageForDevelopers() {
        return messageForDevelopers;
    }
    
    public void setMessageForDevelopers(String msg) {
        messageForDevelopers = msg;
    }
}
