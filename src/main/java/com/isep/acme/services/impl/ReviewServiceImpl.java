package com.isep.acme.services.impl;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.model.Vote;
import com.isep.acme.model.dtos.ReviewDTO;
import com.isep.acme.model.dtos.VoteReviewDTO;
import com.isep.acme.model.mappers.ReviewMapper;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import com.isep.acme.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repository;
    @Autowired
    ProductRepository pRepository;
    @Autowired
    UserRepository uRepository;

    @Override
    public Iterable<Review> getAll() {
        return repository.findAll();
    }

    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {
        Optional<Product> product = pRepository.findBySku(sku);
        if(!product.isEmpty()){
            Optional<List<Review>> r = repository.findByProductIdStatus(product.get(), status);
            if (!r.isEmpty()){
                return ReviewMapper.toDtoList(r.get());
            }
        }

        return null;
    }

    @Override
    public List<ReviewDTO> findPendingReview(){
        Optional<List<Review>> r = repository.findPendingReviews();
        if(!r.isEmpty()){
            return ReviewMapper.toDtoList(r.get());
        }

        return Collections.emptyList();
    }

    @Override
    public List<ReviewDTO> findReviewsByUser(Long userID) {
        final Optional<User> user = uRepository.findById(userID);
        if(!user.isEmpty()){
            Optional<List<Review>> r = repository.findByUserId(user.get());
            if (!r.isEmpty()){
                return ReviewMapper.toDtoList(r.get());
            }
        }

        return null;
    }

    @Transactional
    @Override
    public boolean addVoteToReview(VoteReviewDTO voteReviewDTO, Vote vote) {

        Optional<Review> review = this.repository.findByRID(voteReviewDTO.getRID());

        if (review.isEmpty()) return false;

        if (voteReviewDTO.getVote().equalsIgnoreCase("upVote") && review.get().addUpVote(vote)
                || voteReviewDTO.getVote().equalsIgnoreCase("downVote") && review.get().addDownVote(vote)) {

            repository.save(review.get());
            return true;

        }
        return false;
    }
}