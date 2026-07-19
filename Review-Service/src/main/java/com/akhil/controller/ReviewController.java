package com.akhil.controller;

import com.akhil.DTO.ApiResponse;
import com.akhil.DTO.ReviewRequest;
import com.akhil.DTO.SaloonDTO;
import com.akhil.DTO.UserDTO;
import com.akhil.model.Review;
import com.akhil.service.ReviewService;
import com.akhil.service.client.SaloonFeignClient;
import com.akhil.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    public final ReviewService service;
    public final UserFeignClient userFeignClient;
    public final SaloonFeignClient saloonFeignClient;

    @PostMapping("/salon/{salonId}")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest request, @RequestHeader("Authorization") String jwt,@PathVariable Long salonId){

        UserDTO userDTO=userFeignClient.getUserProfile(jwt).getBody();
        SaloonDTO saloonDTO=saloonFeignClient.getSalonById(salonId).getBody();

        Review review=service.createReview(request,userDTO,saloonDTO);

        return ResponseEntity.ok(review);



    }


    @GetMapping("/salon/{salonId}")
    public ResponseEntity< List<Review>> getReviewsBySalonId(@RequestHeader("Authorization") String jwt,@PathVariable Long salonId){

        SaloonDTO saloonDTO=saloonFeignClient.getSalonById(salonId).getBody();

        List<Review> review=service.getReviewBySalonId(saloonDTO.getId());

        return ResponseEntity.ok(review);



    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateSalon(@RequestBody ReviewRequest request,@RequestHeader("Authorization") String jwt,@PathVariable Long reviewId){

        UserDTO userDTO=userFeignClient.getUserProfile(jwt).getBody();

        Review review=service.updateReview(request,reviewId,userDTO.getId());

        return ResponseEntity.ok(review);



    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@RequestHeader("Authorization") String jwt,@PathVariable Long reviewId){

        UserDTO userDTO=userFeignClient.getUserProfile(jwt).getBody();

     service.deleteReview(reviewId,userDTO.getId());

        ApiResponse response=new ApiResponse();
        response.setMessage("Review Deleted");
        return ResponseEntity.ok(response);



    }
}
