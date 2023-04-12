package com.isep.acme.rabbit.listeners.impl;

import com.isep.acme.model.Vote;
import com.isep.acme.rabbit.listeners.VoteListener;
import com.isep.acme.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class VoteListeneImpl implements VoteListener {

    @Autowired
    private final VoteRepository voteRepository;

    @Override
    public void listenedVote(Vote vote) {
        if(vote != null){
            final Optional<Vote> voteToAction = voteRepository.findByVID(vote.getVID());
            if(voteToAction.isEmpty()){
                voteRepository.save(vote);
                System.out.println("Vote Added " + vote);
            }
        }
    }
}