package jp.oops.clazz.restday.dao;

import java.util.HashMap;
import java.util.Map;
import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;
import jp.oops.clazz.restday.exception.RestdayException;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi . </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class SqlTemplateResults {
    
    private final Map<String, Integer> map;
    private final String template;
    
    SqlTemplateResults(String template, Map<String, Integer> map) {
        this.template = template;
        this.map = map;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SqlTemplateResults[");
        sb.append("'" + template + "', ");
        if (map != null) {
            sb.append(map.toString());
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * @return the map
     */
    public Map<String, Integer> getMap() {
        return map;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }



    public static SqlTemplateResults parseTemplate(String sqlTempl) throws RestdayException {

        StringBuilder sb = new StringBuilder();
        int start = 0;
        HashMap<String, Integer> map = new HashMap<>();

        // ? が発見された数のカウント
        // Count the number of times?
        int count = 1;

        for (;;) {
            int foundPos = sqlTempl.indexOf("?", start);

            if (foundPos < 0) {
                sb.append(sqlTempl.substring(start));
                break;
            }

            int findPos2 = searchEnd(sqlTempl, foundPos + 1);
            if (findPos2 < 0) {
                throw new ConfigurationSyntaxErrorException("bad sql template", null);
            }

            String keyword = sqlTempl.substring(foundPos + 1, findPos2);

            map.put(keyword, count);

            sb.append(sqlTempl.substring(start, foundPos + 1));
            start = findPos2;
            count++;
        }

        return new SqlTemplateResults(sb.toString(), map);
    }

    // ? の後に、識別子として許可される文字の時　falseを返す
    // Returns false if the character is allowed as an identifier after '?'.
    //
    //  0 - 9
    //  a - z
    //  A - Z
    //  _
    //
    private static boolean isInvalidChar(char ch) {

        if ('0' <= ch && ch <= '9') {
            return false;
        }
        if ('a' <= ch && ch <= 'z') {
            return false;
        }
        if ('A' <= ch && ch <= 'Z') {
            return false;
        }
        if (ch == '_') {
            return false;
        }
        return true;
    }

    // ?以降のワードのindex
    //  Returns the index of the word following '?'.
    public static int searchEnd(String str, int startPos) {

        int pos = startPos;
        int end = str.length();
        while (pos < end) {
            char ch = str.charAt(pos);

            if (isInvalidChar(ch)) {
                break;
            }
            if (ch <= ' ') {
                break;
            }
            pos++;
        }
        return pos;
    }
}
