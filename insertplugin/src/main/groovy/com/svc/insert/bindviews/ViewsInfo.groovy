package com.svc.insert.bindviews;

/**
 * Created by Ino on 2016/11/24.
 */
public class ViewsInfo {
    String fieldName;//变量名
    String fieldType;//类型
    String genericity;//泛型
    int[] id;//注入的id
    ViewsInfo(String fieldName, String fieldType, String signature) {
        this.fieldName = fieldName
        this.fieldType = fieldType
        this.genericity = signature
    }
}