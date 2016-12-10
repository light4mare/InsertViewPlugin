package com.svc.insert

import com.svc.insert.bindview.BindViewVisitor
import com.svc.insert.innerclass.InnerCreator
import com.svc.insert.onClick.BindMethodVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by Ino on 2016/11/22.
 */
public class BindClassVisitor extends ClassVisitor {
    private ClassInfo classInfo;

    private String clazz;
    private String dir;

    BindClassVisitor(int api, ClassVisitor classVisitor, String dir, String className) {
        super(api, classVisitor)

        clazz = className.substring(1, className.length() - 6).replace("\\", "/");
//        println "className: " + className
//        println "clazz: " + clazz
//        println "dir: " + dir

        this.dir = dir;

        classInfo = new ClassInfo(clazz)
    }

//    @Override
//    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        println ("access: " + access + "____name: " + name + "____interfaces: " + interfaces + "___signature: " + signature)
//        super.visit(version, access, name, signature, superName, interfaces)
//    }

    @Override
    FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        //visitField(修饰符 , 字段名 , 字段类型 , 泛型描述 , 默认值)
//        println ("access: " + access + "____name: " + name + "____desc: " + desc + "___signature: " + signature)
        FieldVisitor fieldVisitor = super.visitField(access, name, desc, signature, value);
        BindViewVisitor viewVisitor = new BindViewVisitor(Opcodes.ASM4, fieldVisitor, classInfo, name, desc, signature);
        return viewVisitor
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//        println ("access: " + access + "____name: " + name + "____desc: " + desc + "___signature: " + signature)
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
        BindMethodVisitor onclickAnnoVisitor = new BindMethodVisitor(Opcodes.ASM4, methodVisitor, classInfo, access, name, desc)
        return onclickAnnoVisitor
    }

    @Override
    void visitEnd() {
        if (classInfo.needInsert()) {
            MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "insertView", "(" + Common.TYPE_CLASS_VIEW + ")V", null, null);
            mv.visitCode()

            insertSingleView(mv)
            insertMultipleViews(mv)
            insertOnClickMethod(mv)

            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        super.visitEnd()
    }

    private void insertSingleView(MethodVisitor mv) {
//        aload_0
//        aload_0
//        ldc           #7                  // int 2131427413
//        invokevirtual #8                  // Method findViewById:(I)Landroid/view/View;
//        checkcast     #9                  // class android/widget/LinearLayout
//        putfield      #10                 // Field linear:Landroid/widget/LinearLayout;

//        println "viewInfoList: " + classInfo.viewInfoList
        if (classInfo.viewInfoList != null && classInfo.viewInfoList.size() > 0) {
            classInfo.viewInfoList.each {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitLdcInsn(it.id)
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Common.TYPE_VIEW, "findViewById", "(I)" + Common.TYPE_CLASS_VIEW, false);
                def fieldType = it.fieldType.substring(1, it.fieldType.length() - 1)
//                println "insertViewType: " + fieldType
                mv.visitTypeInsn(Opcodes.CHECKCAST, fieldType);
                mv.visitFieldInsn(Opcodes.PUTFIELD, clazz, it.fieldName, it.fieldType);
            }
        }
    }

    private void insertMultipleViews(MethodVisitor mv) {
//        println "__viewsInfoList: " + classInfo.viewsInfoList
        if (classInfo.viewsInfoList != null && classInfo.viewsInfoList.size() > 0) {
            classInfo.viewsInfoList.each {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitTypeInsn(Opcodes.NEW, Common.TYPE_ARRAY_LIST)
                mv.visitInsn(Opcodes.DUP)
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Common.TYPE_ARRAY_LIST, "<init>", "()V", false)
                mv.visitFieldInsn(Opcodes.PUTFIELD, clazz, it.fieldName, it.fieldType);

                def fieldName = it.fieldName
                def fieldType = it.genericity

//                it.id.each {
//                    mv.visitVarInsn(Opcodes.ALOAD, 0);
//                    mv.visitVarInsn(Opcodes.ALOAD, 1);
//                    mv.visitFieldInsn(Opcodes.GETFIELD, clazz, fieldName, Common.TYPE_CLASS_LIST)
//                    mv.visitVarInsn(Opcodes.ALOAD, 1);
//                    mv.visitLdcInsn(it)
//                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Common.TYPE_VIEW, "findViewById", "(I)" + Common.TYPE_CLASS_VIEW, false);
//                    mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, Common.TYPE_LIST, "add", "(" + Common.TYPE_CLASS_OBJECT + ")Z", true)
//                    mv.visitInsn(Opcodes.POP)
//                }

                int num = 2
                it.id.each {
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitLdcInsn(it)
                    //操作符，方法所属类，方法名，方法参数及返回类型，是否为接口
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Common.TYPE_VIEW, "findViewById", "(I)" + Common.TYPE_CLASS_VIEW, false);
//                    println "original_fieldType: " + fieldType
//                    println "after_sub_fieldType: " + fieldType.substring(17, fieldType.length() - 3)
                    mv.visitTypeInsn(Opcodes.CHECKCAST, fieldType.substring(17, fieldType.length() - 3));
                    mv.visitVarInsn(Opcodes.ASTORE, num)

                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    mv.visitFieldInsn(Opcodes.GETFIELD, clazz, fieldName, Common.TYPE_CLASS_LIST)
                    mv.visitVarInsn(Opcodes.ALOAD, num);
                    mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, Common.TYPE_LIST, "add", "(" + Common.TYPE_CLASS_OBJECT + ")Z", true)
                    mv.visitInsn(Opcodes.POP)

                    num++
                }
                classInfo.stackDepth = num;
            }
        }
    }

    private void insertOnClickMethod(MethodVisitor mv) {
        if (classInfo.clickMethodInfoList != null) {
            int num = classInfo.stackDepth;

            classInfo.clickMethodInfoList.each {

                String innerClassName = InnerCreator.create(dir, clazz, it)

                mv.visitTypeInsn(Opcodes.NEW, innerClassName)
                mv.visitInsn(Opcodes.DUP)
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, innerClassName, "<init>", "(L" + clazz + ";)V", false);
                mv.visitVarInsn(Opcodes.ASTORE, num)

                int listenerDepth = num
                num++

                it.id.each {
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitLdcInsn(it)
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Common.TYPE_VIEW, "findViewById", "(I)" + Common.TYPE_CLASS_VIEW, false);
                    mv.visitTypeInsn(Opcodes.CHECKCAST, Common.TYPE_VIEW);
                    mv.visitVarInsn(Opcodes.ASTORE, num)

                    mv.visitVarInsn(Opcodes.ALOAD, num);
                    mv.visitVarInsn(Opcodes.ALOAD, listenerDepth);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Common.TYPE_VIEW, "setOnClickListener", "(" + Common.TYPE_CLASS_VIEW_ONCLICK + ")V", false);

                    num++
                }
            }
        }
    }
}