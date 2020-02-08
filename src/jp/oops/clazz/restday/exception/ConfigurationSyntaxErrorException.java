package jp.oops.clazz.restday.exception;

/**
 * <div>設定ファイルのJsonを解釈中に発生したエラーを表現するクラス</div>
 * <div>Class that represents the error that occurred while interpreting Json in
 * the configuration file.</div>
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class ConfigurationSyntaxErrorException extends ConfigurationException {

    public ConfigurationSyntaxErrorException(String msg) {
        super(msg);
    }

    public ConfigurationSyntaxErrorException(String msg, Throwable th) {
        super(msg, th);
    }

    public ConfigurationSyntaxErrorException(String msg, Throwable th, String detail) {
        super(msg, th);
        super.messageForDevelopers = detail;
    }

    @Override
    public String getMessage() {

        return super.getMessage();
    }
}
