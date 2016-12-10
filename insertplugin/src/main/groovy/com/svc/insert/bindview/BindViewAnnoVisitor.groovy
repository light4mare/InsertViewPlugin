package com.svc.insert.bindview

import org.objectweb.asm.AnnotationVisitor;

/**
 * Created by Ino on 2016/11/22.
 */
public class BindViewAnnoVisitor extends AnnotationVisitor {
    private ViewInfo viewInfo;

    BindViewAnnoVisitor(int api, AnnotationVisitor av, ViewInfo info) {
        super(api, av)
        viewInfo = info;
    }

    @Override
    void visit(String name, Object value) {
        viewInfo.id = (int) value;
        super.visit(name, value)
    }
}