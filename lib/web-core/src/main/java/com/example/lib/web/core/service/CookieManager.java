package com.example.lib.web.core.service;

import java.util.Optional;

public interface CookieManager {

    void addCookie(String name, String value, int maxAge);

    void removeCookie(String name);

    Optional<String> getCookie(String name);
}
