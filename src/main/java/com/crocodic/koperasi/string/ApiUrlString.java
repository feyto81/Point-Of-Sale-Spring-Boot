package com.crocodic.koperasi.string;

public class ApiUrlString {

    public static final String tokenName = "Access-Token";

    public static final String apiBase = "/api/v1";
    public static final String apiAuthBase = apiBase+"/auth";
    public static final String apiToolsBase = apiBase+"/tools";
    public static final String getToken = "get-token";
    public static final String renewToken = "token-renew";
    public static final String getTokenFull = apiAuthBase+"/get-token";
    public static final String renewTokenFull = apiAuthBase+"/token-renew";

    /*
    auth api url -> whit out require request token
     */
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String forgotPassword = "forgot-password";

    public static final String CART = "/cart";

    public static final String CARTPEMBELIAN = "/cartv2";

}
