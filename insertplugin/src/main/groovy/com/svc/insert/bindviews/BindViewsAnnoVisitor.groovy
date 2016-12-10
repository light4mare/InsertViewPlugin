package com.svc.insert.bindviews

import com.svc.insert.bindview.ViewInfo
import org.objectweb.asm.AnnotationVisitor;

/**
 * Created by Ino on 2016/11/22.
 */
public class BindViewsAnnoVisitor extends AnnotationVisitor {
    private ViewsInfo viewInfo;

    BindViewsAnnoVisitor(int api, AnnotationVisitor av, ViewsInfo info) {
        super(api, av)
        viewInfo = info;
    }

    @Override
    void visit(String name, Object value) {
        viewInfo.id = (int[]) value;
        super.visit(name, value)
    }
}