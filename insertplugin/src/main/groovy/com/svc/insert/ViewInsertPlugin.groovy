package com.svc.insert

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by Administrator on 2016/11/19.
 */
public class ViewInsertPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
//        project.task("testTask") <<{
//            println "Hello Insert Plugin"
//        }

        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            def android = project.extensions.getByType(AppExtension)
            def transform = new ViewInsertTransform()
            //这里加dependence可以决定在什么时候修改
            android.registerTransform(transform)
        }
    }
}
