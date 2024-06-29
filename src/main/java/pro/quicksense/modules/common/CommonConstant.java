package pro.quicksense.modules.common;


public interface CommonConstant {
    Integer USER_STATUS_FROZEN = 0;
    Integer USER_STATUS_NORMAL = 1;

    String X_ACCESS_TOKEN = "X-Access-Token";

    Integer SUCCESS_CODE = 200;
    Integer BAD_REQUEST_CODE = 400;
    Integer INTERNAL_SERVER_ERROR_CODE = 500;
    String KEY_PREFIX = "email_code_";
}
