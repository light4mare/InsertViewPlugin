package com.svc.insert.onClick

import com.svc.insert.ClassInfo
import com.svc.insert.Common
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes;

/**
 * Created by Ino on 2016/11/25.
 */
public class BindMethodVisitor extends MethodVisitor {
    private ClassInfo classInfo;
    private int access;
    private String methodName;
    private String methodDesc;

    BindMethodVisitor(int api, MethodVisitor mv, ClassInfo classInfo, int access, String methodName, String methodDesc) {
        super(api, mv)

        this.classInfo = classInfo;
        this.access = access;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        if (Common.BIND_CLICK_ANNO.equals(desc)) {
            if (classInfo.clickMethodInfoList == null) {
                classInfo.clickMethodInfoList = new ArrayList<>();
            }
            OnClickMethodInfo methodInfo = new OnClickMethodInfo(methodName, methodDesc, false);
            boolean canInvoke = true;
            String error = "";
            if (access == Opcodes.ACC_PUBLIC) {
                if (methodDesc.equals("(" + Common.TYPE_CLASS_VIEW + ")V")) {
                    methodInfo.hasParams = true;
                } else if (methodDesc.equals("()V")) {
                    methodInfo.hasParams = false;
                } else {
                    canInvoke = false;
                    error = " need more parameters which can not provide, skip binding"
                }
            } else {
                canInvoke = false;
                error = " must be public, skip binding"
            }
            if (canInvoke) {
                classInfo.clickMethodInfoList.add(methodInfo)
                OnclickAnnoVisitor onclickAnnoVisitor = new OnclickAnnoVisitor(Opcodes.ASM4, annotationVisitor, methodInfo);
                return onclickAnnoVisitor
            } else {
                println "error: Method: " + classInfo.className + "." + methodName + error
            }
        }
        return annotationVisitor
    }
}