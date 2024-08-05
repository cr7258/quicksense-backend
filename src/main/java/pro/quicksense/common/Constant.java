package pro.quicksense.common;

public interface Constant {
    Integer USER_STATUS_FROZEN = 0;
    Integer USER_STATUS_NORMAL = 1;
    String X_ACCESS_TOKEN = "X-Access-Token";

    // Email verification code key prefix in Redis
    String EMAIL_LOGIN = "login";
}
