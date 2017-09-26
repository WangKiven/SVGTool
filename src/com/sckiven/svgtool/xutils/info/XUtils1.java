package com.sckiven.svgtool.xutils.info;

public class XUtils1 extends AbstractXUtils {

    private static final String mFieldAnnotationSimpleName = "ViewInject";
    private static final String mSimpleBindStatement = "x.view().inject";
    private static final String mSimpleUnbindStatement = "ButterKnife.reset";


    @Override
    public String getVersion() {
        return "3.5.0";
    }

    @Override
    public String getDistinctClassName() {
        return getFieldAnnotationCanonicalName();
    }

    @Override
    public String getFieldAnnotationSimpleName() {
        return mFieldAnnotationSimpleName;
    }

    @Override
    public String getSimpleBindStatement() {
        return mSimpleBindStatement;
    }

    @Override
    public String getSimpleUnbindStatement() {
        return mSimpleUnbindStatement;
    }

    @Override
    public boolean isUsingUnbinder() {
        return false;
    }

    @Override
    public String getUnbinderClassSimpleName() {
        return null;
    }
}
