package com.akhil.service.imp;

import com.akhil.DTO.ReviewRequest;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.UserDTO;
import com.akhil.model.Review;
import com.akhil.repo.ReviewRepo;
import com.akhil.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepo repo;
    @Override
    public Review createReview(ReviewRequest request, UserDTO userDTO, SaloonDTO saloonDTO) {
        Review review=new Review();
        review.setReviewText(request.getReviewText());
        review.setRating(request.getRating());
        review.setUserId(userDTO.getId());
        review.setSalonId(saloonDTO.getId());

        return repo.save(review);
    }

    @Override
    public List<Review> getReviewBySalonId(Long salonId) {
        return repo.findBySalonId(salonId);
    }

    private Review getReviewById(Long Id){
        return  repo.findById(Id).orElseThrow(()->new RuntimeException("Review Not Exist"));
    }
    @Override
    public Review updateReview(ReviewRequest request, Long userId, Long reviewId) {
        Review review=getReviewById(reviewId);
        if(!review.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this review");
        }
        review.setReviewText(review.getReviewText());
        review.setRating(review.getRating());


        return repo.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review=getReviewById(reviewId);
        if(!review.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this review");
        }
        repo.delete(review);


    }
}
