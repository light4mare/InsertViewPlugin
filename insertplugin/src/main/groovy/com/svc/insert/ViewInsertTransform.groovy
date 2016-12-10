package com.svc.insert

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import javax.xml.crypto.dsig.TransformException

/**
 * Created by Administrator on 2016/11/19.
 */
public class ViewInsertTransform extends Transform {
    @Override
    String getName() {
        return "ViewInsertTransform"
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        return Collections.singleton(QualifiedContent.Scope.PROJECT)
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        println "----------------insertView begin----------------"

        def outDir = transformInvocation.outputProvider.getContentLocation("insertView", outputTypes, scopes, Format.DIRECTORY)
        outDir.deleteDir()
        outDir.mkdirs()

        String dir = outDir.toString();

        println("insertViewPath :" + outDir)

        transformInvocation.inputs.each {
            it.directoryInputs.each {
//                println "directory: " + it.toString()
                int length = it.file.toString().length()
                it.file.traverse {
                    String className = it.toString().substring(length);
                    if (it.isDirectory()) {
//                        println "directory: " + dir + className
                        new File(dir + className).mkdirs();
                    } else {
//                        if (!className.startsWith("android")){//android\support\v7\appcompat
                        if (!it.name.startsWith("BuildConfig")) {
//                                println "className: " + className
//                                println "file.fieldName: " + it.fieldName

                            File transFile = new File(dir + className);


                            ClassReader classReader = new ClassReader(it.bytes);
                            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                            BindClassVisitor viewVisitor = new BindClassVisitor(Opcodes.ASM4, classWriter, dir, className);

                            classReader.accept(viewVisitor, Opcodes.ASM4);

                            transFile.bytes = classWriter.toByteArray();
                        }
//                        }
                    }
                }
            }
        }

        println "----------------insertView end----------------"
    }
}
