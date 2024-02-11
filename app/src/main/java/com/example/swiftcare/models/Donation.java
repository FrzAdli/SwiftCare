package com.example.swiftcare.models;

import java.util.Date;

public class Donation {
    public String donationId, fundraiserId, fundraiserName, donationDesc, donationTitle, donationBanner, imageUrl1, imageUrl2, imageUrl3, donationDuration, status;
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

    public String getDonationBanner() {
        return donationBanner;
    }

    public void setDonationBanner(String donationBanner) {
        this.donationBanner = donationBanner;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getDonationDuration() {
        return donationDuration;
    }

    public void setDonationDuration(String donationDuration) {
        this.donationDuration = donationDuration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
