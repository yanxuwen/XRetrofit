//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yanxuwen.compiler.bean;

import javax.lang.model.type.TypeMirror;

/**
 * 参数，以@Param("account") String account2 为例
 * name为account
 * orginName 为 account2
 */
public class ParamMeta {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public @interface TYPE {
        int Field = 1;
        int Query= 2;
        int Body= 3;
        int Path= 4;
        int Param = 5;
        int Header = 6;
    }
    private int type;
    private TypeMirror typeMirror;
    private String name;
    private String orginName;

    public TypeMirror getTypeMirror() {
        return this.typeMirror;
    }

    public void setTypeMirror(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrginName() {
        return this.orginName;
    }

    public void setOrginName(String orginName) {
        this.orginName = orginName;
    }
}
