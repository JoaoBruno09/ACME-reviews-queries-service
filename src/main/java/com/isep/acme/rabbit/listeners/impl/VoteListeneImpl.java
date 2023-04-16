package com.isep.acme.rabbit.listeners.impl;

import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.model.dtos.VoteReviewDTO;
import com.isep.acme.model.mappers.VoteMapper;
import com.isep.acme.rabbit.listeners.VoteListener;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.VoteRepository;
import com.isep.acme.services.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@AllArgsConstructor
@Component
public class VoteListeneImpl implements VoteListener {

    @Autowired
    private final VoteRepository voteRepository;
    @Autowired
    ReviewRepository reviewRepository;
    ReviewService reviewService;

    private static final VoteMapper VOTE_MAPPER = VoteMapper.INSTANCE;

    @Override
    public void listenedVote(VoteReviewDTO voteReviewDTO) {
        if(voteReviewDTO != null){
            Vote vote = VOTE_MAPPER.toVote(voteReviewDTO);
            Vote vote2 = voteRepository.save(vote);
            if (reviewService.addVoteToReview(voteReviewDTO, vote2)) {
                System.out.println("Vote Added ");
            }
        }
    }
}