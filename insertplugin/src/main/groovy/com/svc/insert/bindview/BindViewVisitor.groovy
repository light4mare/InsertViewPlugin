package com.svc.insert.bindview

import com.svc.insert.ClassInfo
import com.svc.insert.Common
import com.svc.insert.bindviews.BindViewsAnnoVisitor
import com.svc.insert.bindviews.ViewsInfo
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by Ino on 2016/11/22.
 */
public class BindViewVisitor extends FieldVisitor {
    private ClassInfo classInfo;

    private String name;
    private String type;
    private String signature;

    BindViewVisitor(int api, FieldVisitor fv, ClassInfo classInfo, String name, String desc, String signature) {
        super(api, fv)
        this.classInfo = classInfo;
        this.name = name;
        this.type = desc;
        this.signature = signature;
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        if (Common.BIND_VIEW_ANNO.equals(desc)) {
            if (classInfo.viewInfoList == null) {
                classInfo.viewInfoList = new ArrayList<>();
            }
            ViewInfo viewInfo = new ViewInfo(name, type)
            BindViewAnnoVisitor visitor = new BindViewAnnoVisitor(Opcodes.ASM4, annotationVisitor, viewInfo);
            classInfo.viewInfoList.add(viewInfo);
            return visitor;
        } else if (Common.BIND_VIEWS_ANNO.equals(desc)) {
            if (type.equals(Common.TYPE_CLASS_LIST)) {
                if (classInfo.viewsInfoList == null) {
                    classInfo.viewsInfoList = new ArrayList<>()
                }
                if (signature) {
                    ViewsInfo viewsInfo = new ViewsInfo(name, type, signature)
                    BindViewsAnnoVisitor visitor = new BindViewsAnnoVisitor(Opcodes.ASM4, annotationVisitor, viewsInfo)
                    classInfo.viewsInfoList.add(viewsInfo)
                    return visitor
                }
            } else {
                println "error: Field: " + classInfo.className + "." + name + "'s fieldType dose not match with 'List', skip binding"
            }
        }
        return annotationVisitor;
    }
}