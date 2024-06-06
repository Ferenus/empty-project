package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ShortenerService {
    private static final Logger log = LoggerFactory.getLogger(ShortenerService.class);

    private static String DOMAIN = "http://short.com/";
    private static int MAX_KEYWORD_SIZE = 20;
    private static int RANDOM_STRING_SIZE = 4;

    private static int DEFAULT_RETRY = 4;

    private Map<String, String> cache = new ConcurrentHashMap<>();

    public String shorten(String url) {
        return shorten(url, DEFAULT_RETRY);
    }

    public String shorten(String url, int retryCount) {
        log.info("url = {}", url);

        if (retryCount <= 0) {
            throw new IllegalArgumentException("Multiple retries failed!");
        }

        if (!isURLValid(url)) {
            throw new IllegalArgumentException("Provided url is not a valid url!");
        }

        String keyword = generateRandomString(RANDOM_STRING_SIZE);

        String cachedUrl = cache.putIfAbsent(keyword, url);

        if (cachedUrl != null) {
            return shorten(url, retryCount - 1);
        }

        StringBuilder sb = new StringBuilder(DOMAIN);
        sb.append(keyword);
        log.info("Generated URL {}", sb);
        return sb.toString();
    }

    private String generateRandomString(int size) {
        /*Random is thread safe from Java 1.7*/
        Random random = new Random();
        String charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++) {
            int idx = random.nextInt(charSet.length());
            char ch = charSet.charAt(idx);
            sb.append(ch);
        }

        return sb.toString();
    }

    public String shorten(String url, String keyword) {
        log.info("url = {}", url);

        if (keyword.length() > MAX_KEYWORD_SIZE) {
            throw new IllegalArgumentException("Keyword is too long!");
        }

        if (!isURLValid(url)) {
            throw new IllegalArgumentException("Provided url is not a valid url!");
        }

        String cachedUrl = cache.putIfAbsent(keyword, url);

        if (cachedUrl != null) {
            throw new IllegalArgumentException("Keyword is already reserved!");
        }

        StringBuilder sb = new StringBuilder(DOMAIN);
        sb.append(keyword);
        return sb.toString();
    }

    private boolean isURLValid(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException ex) {
            log.warn("Not a valid URL {}", url);
            return false;
        }
        log.info("Url is valid {}", url);
        return true;
    }
}
