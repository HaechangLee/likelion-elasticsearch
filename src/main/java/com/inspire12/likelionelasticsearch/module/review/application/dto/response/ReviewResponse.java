package com.inspire12.likelionelasticsearch.module.review.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String review;
    private Long customerId;
    private Long orderId;
    private Long storeId;
    private Integer rating;
    private String sentiment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
