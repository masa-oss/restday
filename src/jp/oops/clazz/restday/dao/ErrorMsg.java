package jp.oops.clazz.restday.dao;

/**
 * <div>Copyright (c) 2019, 2020 Masahito Hemmi</div> 
 * <div>This software is released under the MIT License.</div>
 */
public final class ErrorMsg {

    private final String errorNumber;
    private final String arg1;
    private final String arg2;
    private final String arg3;

    private ErrorMsg(String errorNumber, String arg1, String arg2, String arg3) {
        this.errorNumber = errorNumber;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }
    
    public static ErrorMsg build(String errorNumber, String arg1) {
        return new ErrorMsg(errorNumber, arg1, null, null);
    }
    
    public static ErrorMsg build(String errorNumber, String arg1, String arg2) {
        return new ErrorMsg(errorNumber, arg1, arg2, null);
    }
    
    /**
     * @return the arg1
     */
    public String getArg1() {
        return arg1;
    }

    /**
     * @return the arg2
     */
    public String getArg2() {
        return arg2;
    }

    /**
     * @return the arg3
     */
    public String getArg3() {
        return arg3;
    }

    /**
     * @return the errorNumber
     */
    public String getErrorNumber() {
        return errorNumber;
    }

    /**
     * <div>デバッグのため、人間にとって、わかりやすい文字列を返す。
     * この文字列は将来変更される可能性がある。
     * そのため、この文字列を使ってロジックを書くことは厳禁である。</div>
     * <div>Returns a human-friendly string for debugging.
     * This string may change in the future.
     * For this reason, writing logic using this string is strictly prohibited.</div>
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( "ErrorMsg[");
        sb.append(errorNumber);
        if (arg1 != null) {
            sb.append(", ");
            sb.append(arg1);
        }
        
        if (arg2 != null) {
            sb.append(", ");
            sb.append(arg2);
        }

        if (arg3 != null) {
            sb.append(", ");
            sb.append(arg3);
        }
        
        sb.append("]");
        return sb.toString();
    }
}
