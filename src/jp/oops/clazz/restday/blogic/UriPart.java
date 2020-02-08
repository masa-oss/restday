package jp.oops.clazz.restday.blogic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class UriPart {

    public static List<String> removeFirstSlash(String str) {

        ArrayList<String> part = new ArrayList<>();
        String str2 = str.substring(1);

        int n = str2.indexOf("/");

        if (n < 0) {
            part.add(str2);
            return part;
        }
        part.add(str2.substring(0, n));

        for (;;) {
            str2 = str2.substring(n + 1);
            n = str2.indexOf("/");
            if (n < 0) {
                part.add(str2);
                break;
            }
            part.add(str2.substring(0, n));
        }
        return part;
    }
}
