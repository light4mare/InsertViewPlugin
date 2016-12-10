package com.svc.insert

import com.svc.insert.bindview.ViewInfo
import com.svc.insert.bindviews.ViewsInfo
import com.svc.insert.onClick.OnClickMethodInfo;

/**
 * Created by Ino on 2016/11/25.
 */
public class ClassInfo {
    public String className

    List<ViewInfo> viewInfoList;
    List<ViewsInfo> viewsInfoList;
    List<OnClickMethodInfo> clickMethodInfoList;

    int stackDepth = 2;

    ClassInfo(String className) {
        this.className = className
    }

    public boolean needInsert() {
        return viewInfoList || viewsInfoList || clickMethodInfoList
    }
}