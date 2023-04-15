package com.isep.acme.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.model.dtos.ReviewDTO;
import com.isep.acme.model.mappers.ReviewMapper;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Component
public class RpcClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    UserRepository uRepository;
    public RpcClient(){

    }
    public void getProducts() throws Exception {
        String correlationId = UUID.randomUUID().toString();

        MessageProperties props = new MessageProperties();
        props.setReplyTo("#{queueRPC.name}");
        props.setCorrelationId(correlationId);

        String message = "getProducts";
        Message requestMessage = new Message(message.getBytes(), props);

        ArrayList<Product> productList = (ArrayList<Product>) rabbitTemplate.convertSendAndReceive("rpc_reviews_queue", requestMessage);


        System.out.println(productList);

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < productList.size(); i++) {
            System.out.println(productList.get(i));
            Product product = objectMapper.convertValue(productList.get(i), Product.class);
            System.out.println("ProductSKU " + product.getSku());
            productRepository.save(product);
            System.out.println("Product added to database");
        }
        getReviews();
        getVotes();

    }
    public void getReviews() throws Exception{
        String correlationId = UUID.randomUUID().toString();

        MessageProperties props = new MessageProperties();
        props.setReplyTo("#{queueRPC.name}");
        props.setCorrelationId(correlationId);

        String message = "getReviews";
        Message requestMessage = new Message(message.getBytes(), props);
        ArrayList<ReviewDTO> reviewsList = (ArrayList<ReviewDTO>) rabbitTemplate.convertSendAndReceive("rpc_reviews_queue", requestMessage);


        System.out.println(reviewsList);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        for (int i = 0; i < reviewsList.size(); i++) {
            System.out.println(reviewsList.get(i));
            ReviewDTO reviewDTO = objectMapper.convertValue(reviewsList.get(i), ReviewDTO.class);
            System.out.println("Review RID " + reviewDTO.getRID());
            Review review = getProductAndUserForReview(reviewDTO);
            reviewRepository.save(review);
            System.out.println("Review added to database");
        }
    }
    public void getVotes() throws Exception{

    }
    private Review getProductAndUserForReview(ReviewDTO reviewDTO){
        final Optional<Product> product = productRepository.findBySku(reviewDTO.getProductSku());
        final Optional<User> user = uRepository.findById(reviewDTO.getUserId());
        Review review = ReviewMapper.toReview(reviewDTO);
        review.setUser(user.get());
        review.setProduct(product.get());
        return review;
    }
}
