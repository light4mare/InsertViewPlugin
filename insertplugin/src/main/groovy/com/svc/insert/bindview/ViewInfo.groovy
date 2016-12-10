package com.svc.insert.bindview;

/**
 * Created by Ino on 2016/11/22.
 */
public class ViewInfo {
    String fieldName;//变量名
    String fieldType;//类型
    int id;//注入的id

    ViewInfo(String fieldName, String fieldType) {
        this.fieldName = fieldName
        this.fieldType = fieldType
    }
}