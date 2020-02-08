package jp.oops.clazz.restday.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * <div>入力された文字列を、プリペアードステートメントに渡すための値クラス</div>
 *
 * <div>Value class for passing the input string to the prepared statement.</div>
 * 
 * <div>Copyright (c) 2019, 2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class SQLValue {

    public static final int NULL = 0;
    public static final int STRING = 1;
    public static final int INTEGER = 2;
    public static final int DOUBLE = 3;
    public static final int LONG = 4;
    public static final int BIGDECIMAL = 5;
    public static final int DATE = 9;
    public static final int TIMESTAMP = 10;
            
    private int valueType;
    
    public int getType() {
        return valueType;
    }

    private int valueInt;

    private double valueDouble;

    private long valueLong;

    private String valueString;

    private BigDecimal bigDecimal;

    private java.sql.Date date;
    
    private java.sql.Timestamp timestamp;

    private ErrorMsg errorMsg = null;

    // For rename
    private String specifiedColumnName = null;

    /**
     * Forbid use of constructor
     */
    private SQLValue() {
    }

    public void setSpecifiedColumnName(String value) {
        specifiedColumnName = value;
    }

    public String getSpecifiedColumnName() {
        return specifiedColumnName;
    }

    public void setErrorMsg(ErrorMsg em) {
        errorMsg = em;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    public boolean hasError() {
        return errorMsg != null;
    }

    public int intValue() {
        if (valueType == 2) {
            return valueInt;
        }
        throw new IllegalStateException("not int value");
    }

    public long longValue() {
        if (valueType == LONG) {
            return valueLong;
        }
        throw new IllegalStateException("not long value");
    }
    
    public String stringValue() {
        if (valueType == STRING) {
            return valueString;
        }
        throw new IllegalStateException("not string value");
    }
    
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SQLValue[");

        if (errorMsg == null) {

            switch (valueType) {
                case 0:
                    sb.append("null");
                    break;
                case 1:
                    sb.append(valueString);
                    break;
                case 2:
                    sb.append(valueInt);
                    break;
                case 3:
                    sb.append(valueDouble);
                    break;
                case 4:
                    sb.append(valueLong);
                    break;
                case 5:
                    sb.append(bigDecimal);
                    break;
                case 9:
                    sb.append(date);
                    break;
                case 10:
                    sb.append(timestamp);
                    break;
            }
        } else {
            sb.append("error:");
            sb.append(this.errorMsg.toString());
        }

        sb.append("]");
        return sb.toString();
    }

    public static SQLValue createNull() {
        SQLValue inst = new SQLValue();
        inst.valueType = 0;
        return inst;
    }

    public static SQLValue createString(String value) {
        SQLValue inst = new SQLValue();
        inst.valueString = value;
        inst.valueType = 1;
        return inst;
    }

    public static SQLValue createInt(int value) {
        SQLValue inst = new SQLValue();
        inst.valueInt = value;
        inst.valueType = 2;
        return inst;
    }

    public static SQLValue createDouble(double value) {
        SQLValue inst = new SQLValue();
        inst.valueDouble = value;
        inst.valueType = 3;
        return inst;
    }

    public static SQLValue createLong(long value) {
        SQLValue inst = new SQLValue();
        inst.valueLong = value;
        inst.valueType = 4;
        return inst;
    }

    public static SQLValue createBigDecimal(BigDecimal value) {
        SQLValue inst = new SQLValue();
        inst.bigDecimal = value;
        inst.valueType = 5;
        return inst;
    }
    
    public static SQLValue createDate(java.sql.Date value) {
        SQLValue inst = new SQLValue();
        inst.date = value;
        inst.valueType = 9;
        return inst;
    }

    public static SQLValue createTimestamp(java.sql.Timestamp value) {
        SQLValue inst = new SQLValue();
        inst.timestamp = value;
        inst.valueType = 10;
        return inst;
    }

    public void setTo(PreparedStatement ps, int pos) throws SQLException {

        switch (valueType) {
            case 0:
                // Reference: http://yano.hatenadiary.jp/entry/20111220/1324390185
                ps.setNull(pos, java.sql.Types.NULL);
                break;
            case 1:
                ps.setString(pos, valueString);
                break;
            case 2:
                ps.setInt(pos, valueInt);
                break;
            case 3:
                ps.setDouble(pos, valueDouble);
                break;
            case 4:
                ps.setLong(pos, valueLong);
                break;
            case 5:
                ps.setBigDecimal(pos, bigDecimal);
                break;
            case 9:
                ps.setDate(pos, date);
                break;
            case 10:
                ps.setTimestamp(pos, this.timestamp);
                break;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    // Code generated by NetBeans
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SQLValue other = (SQLValue) obj;
        if (this.valueType != other.valueType) {
            return false;
        }
        if (this.valueInt != other.valueInt) {
            return false;
        }
        if (Double.doubleToLongBits(this.valueDouble) != Double.doubleToLongBits(other.valueDouble)) {
            return false;
        }
        if (this.valueLong != other.valueLong) {
            return false;
        }
        if (!Objects.equals(this.valueString, other.valueString)) {
            return false;
        }
        if (!Objects.equals(this.specifiedColumnName, other.specifiedColumnName)) {
            return false;
        }
        if (!Objects.equals(this.bigDecimal, other.bigDecimal)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        if (!Objects.equals(this.errorMsg, other.errorMsg)) {
            return false;
        }
        return true;
    }
}
