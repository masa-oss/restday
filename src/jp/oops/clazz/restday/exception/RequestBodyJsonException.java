package jp.oops.clazz.restday.exception;

/**
 * <div>HttpServletRequestのBodyに添付されたJSONを解釈中に発生したエラーを表現するクラス</div>
 * 
 * <div>Class that represents the error that occurred while interpreting the JSON attached to the body of HttpServletRequest.</div>
 * 
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class RequestBodyJsonException extends RestdayException {
    
    public static final String MSG  = "Json syntax error : ";
    
    public RequestBodyJsonException(String msg) {
        super(msg);
    }
    
    public RequestBodyJsonException(String msg, Throwable th) {
        super(msg, th);
    }
    
    String detail = "";
    
    public String getDetail() {
        return detail;
    }

    public void setDetail(String val) {
        detail = val;
    }
}
