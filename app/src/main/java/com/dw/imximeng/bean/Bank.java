package com.dw.imximeng.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hjd
 * @create-time 2018-07-04 11:05:24
 */
public class Bank implements Parcelable{
    private String code;
    private String name;
    private String icon;

    protected Bank(Parcel in) {
        code = in.readString();
        name = in.readString();
        icon = in.readString();
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(icon);
    }
}
