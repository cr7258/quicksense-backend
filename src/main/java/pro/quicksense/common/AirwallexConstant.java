package pro.quicksense.common;

public class AirwallexConstant {

    public static final String API_AIRWALLEX_ROOT = "https://api-demo.airwallex.com/api/v1";
    public static final String API_AUTHENTICATION_LOGIN = API_AIRWALLEX_ROOT + "/authentication/login";
    public static final String API_PAYMENT_LINK_CREATE = API_AIRWALLEX_ROOT + "/pa/payment_links/create";
    public static final String API_GET_BALANCES = API_AIRWALLEX_ROOT + "/balances/current";
    public static final String API_CREATE_PAYMENT = API_AIRWALLEX_ROOT + "/payments/create";
    public static final String API_LIST_PAYMENTS = API_AIRWALLEX_ROOT + "/payments";
    public static final String API_CREATE_QUOTE = API_AIRWALLEX_ROOT + "/fx/quotes/create";

    public static final String CLIENT_ID_TEST = "doemvUe8TSq0eDglXCfhQg";
    public static final String API_KEY_TEST = "ba74c1edb5d018e9c9c7827ec2e8268f99d7bdedcc75a59798c955e7359eda51dc526aa6d1512e060203ffe8d0695f7d";

    /**
     * The client needs to be authorized before calling all other Airwallex APIs,
     * and a token will be returned to the response body if auth process succeeds.
     */
    public static final String TOKEN = "token";

    /**
     * The timestamp the token will be expired at.
     */
    public static final String TOKEN_EXPIRED_AT = "expires_at";
}
