package com.dw.imximeng.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hjd
 * @Created_Time 2018\7\3 0003
 */
public class BankCard implements Parcelable{
    private int id;
    private String code;
    private String bankName;
    private String username;
    private String telephone;
    private String cnumber;
    private String lastNumber;
    private String showCnumber;
    private String icon;

    protected BankCard(Parcel in) {
        id = in.readInt();
        code = in.readString();
        bankName = in.readString();
        username = in.readString();
        telephone = in.readString();
        cnumber = in.readString();
        lastNumber = in.readString();
        showCnumber = in.readString();
        icon = in.readString();
    }

    public static final Creator<BankCard> CREATOR = new Creator<BankCard>() {
        @Override
        public BankCard createFromParcel(Parcel in) {
            return new BankCard(in);
        }

        @Override
        public BankCard[] newArray(int size) {
            return new BankCard[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCnumber() {
        return cnumber;
    }

    public void setCnumber(String cnumber) {
        this.cnumber = cnumber;
    }

    public String getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(String lastNumber) {
        this.lastNumber = lastNumber;
    }

    public String getShowCnumber() {
        return showCnumber;
    }

    public void setShowCnumber(String showCnumber) {
        this.showCnumber = showCnumber;
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
        dest.writeInt(id);
        dest.writeString(code);
        dest.writeString(bankName);
        dest.writeString(username);
        dest.writeString(telephone);
        dest.writeString(cnumber);
        dest.writeString(lastNumber);
        dest.writeString(showCnumber);
        dest.writeString(icon);
    }
}
