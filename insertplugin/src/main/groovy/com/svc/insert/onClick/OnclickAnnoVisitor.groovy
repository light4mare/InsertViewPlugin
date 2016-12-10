package com.svc.insert.onClick

import org.objectweb.asm.AnnotationVisitor;

/**
 * Created by Ino on 2016/11/25.
 */
public class OnclickAnnoVisitor extends AnnotationVisitor {
    private OnClickMethodInfo methodInfo;

    OnclickAnnoVisitor(int api, AnnotationVisitor av, OnClickMethodInfo methodInfo) {
        super(api, av)

        this.methodInfo = methodInfo;
    }

    @Override
    void visit(String name, Object value) {
        methodInfo.id = (int[]) value;
        super.visit(name, value)
    }
}