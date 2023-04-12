package com.isep.acme.services;

import com.isep.acme.model.dtos.CreateReviewDTO;
import com.isep.acme.model.Review;
import com.isep.acme.model.dtos.ReviewDTO;

import java.util.List;

public interface ReviewService {

    Iterable<Review> getAll();

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);

    List<ReviewDTO> findPendingReview();

    List<ReviewDTO> findReviewsByUser(Long userID);
}
