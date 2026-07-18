package com.akhil.service;

import com.akhil.DTO.ReviewRequest;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.UserDTO;
import com.akhil.model.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(ReviewRequest request, UserDTO userDTO, SaloonDTO saloonDTO);
    List<Review> getReviewBySalonId(Long salonId);

    Review updateReview(ReviewRequest request,Long userId,Long reviewId);

    void deleteReview(Long reviewId,Long userId);
}
