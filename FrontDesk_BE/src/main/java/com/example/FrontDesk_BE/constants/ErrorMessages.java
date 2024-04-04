package com.example.FrontDesk_BE.constants;

public class ErrorMessages {
    public static final String ZOHO_OAUTH_TOKEN_FAILURE = "Failed Zoho Oauth Service call";
    public static final String ZOHO_TOKEN_CACHE ="Emptying Zoho OauthToken Cache";
    public static final String ZOHO_AUTH_TOKEN_MSG ="Zoho AuthToken call initiated";
    public static final String ZOHO_REFRESH_TOKEN_MSG ="Zoho Refresh Token call initiated";
    public static final String HTTP_CLIENT_EXCEPTION =" Exception Occurred in getClientHttpRequestFactory";
    public static final String ZOHO_SEARCH_SERVICE_FAILURE = "Failed Zoho Employee Search Service call";
    public static final String ZOHO_UNAUTHORIZED_ERROR = "Unauthorized error,Token Refresh Process invoked";
    public static final String ZOHO_SEARCH_SERVICE_ERROR = "Error Occurred in Zoho Employee Search Service";

    public static final String ID_CARD_INTERNAL_ERROR="Unexpected Error Occurred while saving ID Card, Couldn't save!";
    public static final String ID_CARD_RETURN_ERROR="Unexpected Error Occurred while returning ID Card, Couldn't return!";
    public static final String ID_CARD_EDIT_ERROR="Unexpected Error Occurred while saving the Edited changes, Couldn't save the edited changes! ";
    public static final String VISITOR_INTERNAL_ERROR="Unexpected Error Occurred while saving Visitor, Couldn't save!";
    public static final String VISITOR_CLOCK_OUT_ERROR="Unexpected Error Occurred while clocking out visitor, Couldn't clockOut!";
    public static final String VISITOR_EDIT_ERROR="Unexpected Error Occurred while saving the Edited changes, Couldn't save the edited changes!";
    public static final String LOGIN_ERROR="Unexpected Error Occurred while logging in, please try again! ";
    public static final String UNAUTHORIZED_LOGIN="Unauthorized Login access!";
}
