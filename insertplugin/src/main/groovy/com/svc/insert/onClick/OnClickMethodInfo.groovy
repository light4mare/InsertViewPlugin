package com.svc.insert.onClick;

/**
 * Created by Ino on 2016/11/25.
 */
public class OnClickMethodInfo {
    String methodName;
    String methodDesc
    boolean hasParams;
    int[] id;

    OnClickMethodInfo(String methodName, String methodDesc, boolean hasParams) {
        this.methodName = methodName
        this.methodDesc = methodDesc
        this.hasParams = hasParams
    }
}