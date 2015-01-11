package com.zhangyan.contacts.strcut;

/**
 * Created by ku on 2015/1/10.
 */
public class Attribute {
    private String value; // 值
    private int type; // 类型

    public Attribute() {
        setType(0);
        setValue("");
    }
    public Attribute(String value, int type){
        setValue(value);
        setType(type);
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
