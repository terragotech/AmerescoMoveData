package com.amereco.json;

public class FormData {
    private String action;
    private String physicalInstall;
    private String reason;
    private String newQrCode;
    private String newScan;
    private String removeReason;
    private String replaceExistingQr;
    private String replaceNewQr;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPhysicalInstall() {
        return physicalInstall;
    }

    public void setPhysicalInstall(String physicalInstall) {
        this.physicalInstall = physicalInstall;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNewQrCode() {
        return newQrCode;
    }

    public void setNewQrCode(String newQrCode) {
        this.newQrCode = newQrCode;
    }

    public String getNewScan() {
        return newScan;
    }

    public void setNewScan(String newScan) {
        this.newScan = newScan;
    }

    public String getRemoveReason() {
        return removeReason;
    }

    public void setRemoveReason(String removeReason) {
        this.removeReason = removeReason;
    }

    public String getReplaceExistingQr() {
        return replaceExistingQr;
    }

    public void setReplaceExistingQr(String replaceExistingQr) {
        this.replaceExistingQr = replaceExistingQr;
    }

    public String getReplaceNewQr() {
        return replaceNewQr;
    }

    public void setReplaceNewQr(String replaceNewQr) {
        this.replaceNewQr = replaceNewQr;
    }

    @Override
    public String toString() {
        return "FormData{" +
                "action='" + action + '\'' +
                ", physicalInstall='" + physicalInstall + '\'' +
                ", reason='" + reason + '\'' +
                ", newQrCode='" + newQrCode + '\'' +
                ", newScan='" + newScan + '\'' +
                ", removeReason='" + removeReason + '\'' +
                ", replaceExistingQr='" + replaceExistingQr + '\'' +
                ", replaceNewQr='" + replaceNewQr + '\'' +
                '}';
    }
}
