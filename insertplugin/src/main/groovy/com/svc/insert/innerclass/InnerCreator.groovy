package com.svc.insert.innerclass

import com.sun.xml.internal.ws.util.StringUtils
import com.svc.insert.Common
import com.svc.insert.onClick.OnClickMethodInfo
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by Administrator on 2016/11/26.
 */
public class InnerCreator {
    private static final String OBJECT = "insertObject"

    /**
     *
     * @param className "com/svc/viewinsertplugin/MainActivity"
     * @param methodName "onClick"
     * @param methodDesc "(Landroid/view/View;)V"
     * @return innerClassName
     */
    public static String create(String dir, String className, OnClickMethodInfo methodInfo) {
        String LClassName = "L" + className + ";";

        String methodName = StringUtils.capitalize(methodInfo.methodName)

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)//自动计算堆栈

        String innerClassName = className + "\$" + methodName + "Listener"
        String fileName = "\\" + innerClassName.replace("/", "\\") + ".class"
        File innerFile = new File(dir + fileName)

        classWriter.visit(Opcodes.V1_7, 0, innerClassName, null, "java/lang/Object", "android/view/View\$OnClickListener");

        FieldVisitor fieldVisitor = classWriter.visitField(Opcodes.ACC_PRIVATE, OBJECT, LClassName, null, null);
        fieldVisitor.visitEnd()

        //生成默认的构造方法
        MethodVisitor mw = classWriter.visitMethod(0, "<init>", "(" + LClassName + ")V", null, null);
        //生成构造方法的字节码指令，以外部类作为参数，才能调用外部类的方法
        mw.visitVarInsn(Opcodes.ALOAD, 0);
        mw.visitMethodInsn(Opcodes.INVOKESPECIAL, Common.TYPE_OBJECT, "<init>", "()V", false);
        mw.visitVarInsn(Opcodes.ALOAD, 0);
        mw.visitVarInsn(Opcodes.ALOAD, 1);
        mw.visitFieldInsn(Opcodes.PUTFIELD, innerClassName, OBJECT, LClassName);
        mw.visitInsn(Opcodes.RETURN);
        mw.visitMaxs(1, 1);
        mw.visitEnd();

        //生成onClick方法
        mw = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "onClick", "(" + Common.TYPE_CLASS_VIEW + ")V", null, null);
        //生成main方法中的字节码指令
        mw.visitCode()
        mw.visitVarInsn(Opcodes.ALOAD, 0);
        if (methodInfo.hasParams) {
            mw.visitFieldInsn(Opcodes.GETFIELD, innerClassName, OBJECT, LClassName)
            mw.visitVarInsn(Opcodes.ALOAD, 1);
            mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, methodInfo.methodName, methodInfo.methodDesc, false);
        } else {
            mw.visitFieldInsn(Opcodes.GETFIELD, innerClassName, OBJECT, LClassName)
            mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, methodInfo.methodName, methodInfo.methodDesc, false);
        }

        mw.visitInsn(Opcodes.RETURN);
        mw.visitMaxs(0, 0);
        //字节码生成完成
        mw.visitEnd();

        classWriter.visitEnd()

        innerFile.bytes = classWriter.toByteArray();
        return innerClassName;
    }
}
