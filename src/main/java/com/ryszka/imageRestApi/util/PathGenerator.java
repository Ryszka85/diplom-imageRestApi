package com.ryszka.imageRestApi.util;

import com.ryszka.imageRestApi.security.AppConfigProperties;

public class PathGenerator {
    public static String generateFileAccessLink(String path) {
        return path != null ?
                AppConfigProperties.FILE_PATH  + path :
                null;
    }
}
