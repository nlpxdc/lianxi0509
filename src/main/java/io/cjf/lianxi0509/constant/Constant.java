package io.cjf.lianxi0509.constant;

public class Constant {
    public static final String passwordSeperator = "$";
    public static final Integer saltLength = 4;
    public static final String[] passUrls = {
            "/user/getCaptcha",
            "/user/login",
            "/menu/getTree",
            "/menu/getUserTree"
    };
}
