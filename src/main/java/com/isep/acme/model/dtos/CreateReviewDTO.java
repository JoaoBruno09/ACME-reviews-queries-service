package com.isep.acme.model.dtos;

public class CreateReviewDTO {

    private String reviewText;

    private Long userID;

    public CreateReviewDTO(){}

    public CreateReviewDTO(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
