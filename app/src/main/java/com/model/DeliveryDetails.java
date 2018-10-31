package com.model;

import java.io.Serializable;

/**
 * Created by Admin on 20-02-2017.
 */
public class DeliveryDetails implements Serializable {
    String recipientName,recipientAddress,recipientPhoneNumber,recipientPhoneCode,recipientEmailAddress,recipientvLatitude,recipientvLongitude;
    String deleteDeliverDetailLbl,yesLbl,noLbl;

    public String getYesLbl() {
        return yesLbl;
    }

    public void setYesLbl(String yesLbl) {
        this.yesLbl = yesLbl;
    }

    public String getRecipientvLatitude() {
        return recipientvLatitude;
    }

    public void setRecipientvLatitude(String recipientvLatitude) {
        this.recipientvLatitude = recipientvLatitude;
    }

    public String getRecipientvLongitude() {
        return recipientvLongitude;
    }

    public void setRecipientvLongitude(String recipientvLongitude) {
        this.recipientvLongitude = recipientvLongitude;
    }

    public String getNoLbl() {
        return noLbl;
    }

    public void setNoLbl(String noLbl) {
        this.noLbl = noLbl;
    }

    public String getDeleteDeliverDetailLbl() {
        return deleteDeliverDetailLbl;
    }

    public void setDeleteDeliverDetailLbl(String deleteDeliverDetailLbl) {
        this.deleteDeliverDetailLbl = deleteDeliverDetailLbl;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }



    public String getRecipientId() {
        return recipientId.trim();
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getShippmentDetailTxt() {
        return shippmentDetailTxt;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public String getRecipientPhoneCode() {
        return recipientPhoneCode;
    }

    public void setRecipientPhoneCode(String recipientPhoneCode) {
        this.recipientPhoneCode = recipientPhoneCode;
    }

    public String getRecipientEmailAddress() {
        return recipientEmailAddress;
    }

    public void setRecipientEmailAddress(String recipientEmailAddress) {
        this.recipientEmailAddress = recipientEmailAddress;
    }

    public String getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(String packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public void setShippmentDetailTxt(String shippmentDetailTxt) {
        this.shippmentDetailTxt = shippmentDetailTxt;


    }

    public String getvPackageTypeName() {
        return vPackageTypeName;
    }

    public void setvPackageTypeName(String vPackageTypeName) {
        this.vPackageTypeName = vPackageTypeName;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }


    String recipientId,shippmentDetailTxt,additionalNotes,packageTypeId,vPackageTypeName;


}
