package com.inspire12.likelionelasticsearch.module.review.infrastructure.adapter;

import com.inspire12.likelionelasticsearch.module.review.domain.Review;
import com.inspire12.likelionelasticsearch.module.review.domain.ReviewRepository;
import com.inspire12.likelionelasticsearch.module.review.infrastructure.document.ReviewDocument;
import com.inspire12.likelionelasticsearch.module.review.infrastructure.esrepoistory.ReviewEsRepository;
import com.inspire12.likelionelasticsearch.module.review.support.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReviewRepositoryAdapter implements ReviewRepository {

//    private final ReviewJpaRepository reviewRepository;
    private final ReviewEsRepository reviewEsRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public ReviewRepositoryAdapter(ReviewEsRepository reviewEsRepository) {
        this.reviewEsRepository = reviewEsRepository;
    }

    @Override
    public List<Review> getReviewsByCustomerId(Long customerId, Pageable pageable) {
        List<ReviewDocument> contents = reviewEsRepository.findAllByCustomerId(customerId, Pageable.ofSize(10))
                .getContent();
        List<Review> reviews = new ArrayList<>();
        for (ReviewDocument content : contents) {
            Review review = ReviewMapper.fromDocument(content);
            reviews.add(review);
        }
        return reviews;
    }

    @Override
    public void save(Review review) {
        ReviewDocument document = ReviewMapper.toEntity(review);
        Map<String, Object> savingObject = new HashMap<>();
        savingObject.put("id", document.getId());
        savingObject.put("orderId", document.getOrderId());
        savingObject.put("storeId", document.getStoreId());
        savingObject.put("customerId", document.getCustomerId());
        savingObject.put("content", document.getContent());
        savingObject.put("rating", document.getRating());
        savingObject.put("sentiment", document.getSentiment());
        savingObject.put("createdAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        savingObject.put("updatedAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        savingObject.putAll(document.getDynamicFilters());
        elasticsearchOperations.save(savingObject, IndexCoordinates.of("reviews"));
    }
}
