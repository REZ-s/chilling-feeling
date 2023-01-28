package com.joolove.core.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Collection;

public class CoreAssert extends Assert {

    private static final String ASSERT_NULL = "[ASSERT_MESSAGE] this object or collection is null !";
    private static final String ASSERT_EMPTY = "[ASSERT_MESSAGE] this object or collection is empty (null or length = 0) !";
    private static final String ASSERT_NO_TEXT = "[ASSERT_MESSAGE] no text (null or length = 0 or whitespace) !";
    private static final String ASSERT_ZERO_LENGTH = "[ASSERT_MESSAGE] length = 0 (or null) !";

    public static void notNull(@Nullable Object object) {
        Assert.notNull(object, ASSERT_NULL);
    }

    public static void notNull(@Nullable Object[] objects) {
        Assert.notNull(objects, ASSERT_NULL);
    }

    public static void notNull(@Nullable Collection<?> collection) {
        Assert.notNull(collection, ASSERT_NULL);
    }

    public static void notEmpty(@Nullable Object[] objects) {
        Assert.notEmpty(objects, ASSERT_EMPTY);
    }

    public static void notEmpty(@Nullable Collection<?> collection) {
        Assert.notEmpty(collection, ASSERT_EMPTY);
    }

    public static void hasLength(@Nullable String text) {
        Assert.hasLength(text, ASSERT_ZERO_LENGTH);
    }

    public static void hasText(@Nullable String text) {
        Assert.hasText(text, ASSERT_NO_TEXT);
    }


}
