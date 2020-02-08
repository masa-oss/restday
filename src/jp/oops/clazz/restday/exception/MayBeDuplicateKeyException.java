package jp.oops.clazz.restday.exception;

/**
 *
 */
public class MayBeDuplicateKeyException extends RestdayException {
 
    public MayBeDuplicateKeyException(String msg, Throwable th) {
        super(msg, th);
    }
    
}
