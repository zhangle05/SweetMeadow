/**
 * 
 */
package com.sweetmeadow.api.bridge.utils;

/**
 * @author zhangle
 *
 */
public class SharedConstants {

    public static final String ARRAY_STR_SPLITTER = "¶";

    public static final String DURATION_TUPLE_SPLITTER = "=>";

    public static final String CLIP_URL_PLACEHOLDER = "‡idx‡";

    public static final int FORECAST_SIZE = 4;

    public static final int DEFAULT_DISCUSS_PAGE_SIZE = 10;

    public static final int DEFAULT_CACHE_SECONDS = 7200; // cache for 2 hours
                                                          // by default

    public static final int ONEDAY_SECONDS = 3600 * 24; // seconds for one day

    public static final String URL_PREFIX_KEY = "url-prefix";

    public static final int AUTH_EXPIRE_SECONDS = 600;

    public static final String TIMESTAMP_PARAM_NAME = "ts";

    public static final String CLIENT_NAME_PARAM_NAME = "client";

    public static final String CLIENT_SIGN_PARAM_NAME = "sign";

    public static final int TIMESTAMP_TORLERRANCE = 1000 * 60 * 5; // 5 minutes

    public static final String ERROR_SIGN_TIMESTAMP_EMPTY = "请求时间戳为空，请重试";

    public static final String ERROR_SIGN_CLIENT_NOT_FOUND = "client not found";

    public static final String ERROR_SIGN_TIMESTAMP_EXPIRED = "本机时间错误，请先调整时间";
}
