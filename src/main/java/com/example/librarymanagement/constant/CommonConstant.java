package com.example.librarymanagement.constant;

public class CommonConstant {
    public static final String REGEXP_FULL_NAME = "^\\S+(\\s+\\S+)+";
    public static final String REGEXP_USERNAME = "^[a-z][a-z0-9]{3,15}$";
    public static final String REGEXP_PASSWORD = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
    public static final String REGEXP_PHONE_NUMBER = "^(?:\\+84|0)(?:1[2689]|9[0-9]|3[2-9]|5[6-9]|7[0-9])(?:\\d{7}|\\d{8})$";
    public static final String TOKEN_TYPE = "BEARER";

    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static final String SORT_TYPE_ASC = "ASC";
    public static final String SORT_TYPE_DESC = "DESC";
    public static final Integer PAGE_NUM_DEFAULT = 1;
    public static final Integer PAGE_SIZE_DEFAULT = 10;

    public static final Long ZERO_VALUE = 0L;
    public static final Long ONE_VALUE = 1L;

    public static final Integer ZERO_INT_VALUE = 0;
    public static final Integer ONE_INT_VALUE = 1;
    public static final Integer HUNDRED_INT_VALUE = 100;

    public static final String EMPTY_STRING = "";
    public static final String BEARER_TOKEN = "Bearer";



}
