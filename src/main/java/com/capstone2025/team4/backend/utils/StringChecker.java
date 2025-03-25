package com.capstone2025.team4.backend.utils;

import org.springframework.util.StringUtils;

public abstract class StringChecker {
    static public boolean stringsAreEmpty(String... strings) {
        for (String string : strings) {
            if (!StringUtils.hasText(string)) {
                return true;
            }
        }
        return false;
    }
}