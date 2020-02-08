package jp.oops.clazz.restday.blogic;

import jp.oops.clazz.restday.dao.ErrorMsg;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  <div>現在未使用のはず</div>
 *  <div> Currently unused</div>
 * 
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class MessageBuilder {

    public String getMessage(ErrorMsg errorMsg) {

        String key = errorMsg.getErrorNumber();
        
        String templ = ResourceBundle.getBundle("Message", Locale.getDefault()).getString(key);

        System.out.println("key = " + key + ", templ = " + templ  );

        Object[] params = { errorMsg.getArg1(), errorMsg.getArg2()   };
        
        String message = MessageFormat.format(templ, params);
        
        return message;
    }

}
