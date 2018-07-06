package com.dw.imximeng.bean;

import java.io.Serializable;

/**
 * @author hjd
 * @create-time 2018-07-06 16:43:28
 */
public class Selection implements Serializable{
    private String id;
    private String name;
    private boolean check;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
