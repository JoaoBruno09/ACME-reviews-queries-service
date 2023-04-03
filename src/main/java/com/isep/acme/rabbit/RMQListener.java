package com.isep.acme.rabbit;

import com.isep.acme.model.Review;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RMQListener {

    @RabbitListener(queues = RMQConfig.RQQUEUE)
    public void listener(Review review){
        System.out.println("Review" + review);
        System.out.println("Product SKU" + review.getIdReview());
    }
}