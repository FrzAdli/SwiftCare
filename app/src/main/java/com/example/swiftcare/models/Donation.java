package com.example.swiftcare.models;

import java.util.Date;

public class Donation {
    public String donationId, fundraiserId, fundraiserName, donationDesc, donationTitle;
    public Date donationStart, donationEnd;
    public Long donationTarget;

    public Donation() {

    }

    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public String getFundraiserId() {
        return fundraiserId;
    }

    public void setFundraiserId(String fundraiserId) {
        this.fundraiserId = fundraiserId;
    }

    public String getFundraiserName() {
        return fundraiserName;
    }

    public void setFundraiserName(String fundraiserName) {
        this.fundraiserName = fundraiserName;
    }

    public String getDonationDesc() {
        return donationDesc;
    }

    public void setDonationDesc(String donationDesc) {
        this.donationDesc = donationDesc;
    }

    public String getDonationTitle() {
        return donationTitle;
    }

    public void setDonationTitle(String donationTitle) {
        this.donationTitle = donationTitle;
    }

    public Date getDonationStart() {
        return donationStart;
    }

    public void setDonationStart(Date donationStart) {
        this.donationStart = donationStart;
    }

    public Date getDonationEnd() {
        return donationEnd;
    }

    public void setDonationEnd(Date donationEnd) {
        this.donationEnd = donationEnd;
    }

    public Long getDonationTarget() {
        return donationTarget;
    }

    public void setDonationTarget(Long donationTarget) {
        this.donationTarget = donationTarget;
    }
}
