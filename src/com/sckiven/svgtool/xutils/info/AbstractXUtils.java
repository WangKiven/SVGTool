package com.sckiven.svgtool.xutils.info;

import java.util.regex.Pattern;

public abstract class AbstractXUtils implements IXUtils {
    private static final String mPackageName = "org.xutils.view.annotation";
    private final Pattern mFieldAnnotationPattern = Pattern.compile("^@" + getFieldAnnotationSimpleName() + "\\(([^\\)]+)\\)$", Pattern.CASE_INSENSITIVE);
    private final String mFieldAnnotationCanonicalName = getPackageName() + "." + getFieldAnnotationSimpleName();
    private final String mCanonicalBindStatement = /*getPackageName() + "." + */getSimpleBindStatement();// 构建启用注解使用的代码
    private final String mCanonicalUnbindStatement = getPackageName() + "." + getSimpleUnbindStatement();
    private final String mOnClickCanonicalName = getPackageName() + ".OnClick";
    private final String mUnbinderClassCanonicalName = getPackageName() + "." + getUnbinderClassSimpleName();


    @Override
    public Pattern getFieldAnnotationPattern() {
        return mFieldAnnotationPattern;
    }

    @Override
    public String getPackageName() {
        return mPackageName;
    }

    @Override
    public String getFieldAnnotationCanonicalName() {
        return mFieldAnnotationCanonicalName;
    }

    @Override
    public String getOnClickAnnotationCanonicalName() {
        return mOnClickCanonicalName;
    }

    @Override
    public String getCanonicalBindStatement() {
        return mCanonicalBindStatement;
    }

    @Override
    public boolean isUnbindSupported() {
        return true;
    }

    @Override
    public boolean isUsingUnbinder() {
        // Let's assume that this is going to stay after ButterKnife 8.
        return true;
    }

    @Override
    public String getCanonicalUnbindStatement() {
        return mCanonicalUnbindStatement;
    }

    @Override
    public String getUnbinderClassCanonicalName() {
        return mUnbinderClassCanonicalName;
    }
}
