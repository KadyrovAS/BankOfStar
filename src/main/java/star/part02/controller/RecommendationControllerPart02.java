package star.part02.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star.part02.model.Recommendation;
import star.part02.model.Transaction;
import star.part02.service.RecommendationRuleSet;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/part02")
public class RecommendationControllerPart02 {
    private final RecommendationRuleSet service;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationControllerPart02.class);

    public RecommendationControllerPart02(@Qualifier("servicePart02") RecommendationRuleSet service) {
        this.service = service;
    }

    @GetMapping(value = "/recommendation/{id}")
    public ResponseEntity<List<Recommendation>> findRecommendationById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findRecommendationById(id).orElse(null));
    }

    @GetMapping(value = "/allRecommendations")
    public List<Recommendation>findAllRecommendations(){
        return service.findAllRecommendations();
    }

    @PutMapping
    public void insertRecommendation(@RequestBody Recommendation recommendation) {
        logger.info("insertRecommendation: {}", recommendation);
        service.addRecommendation(recommendation);
    }

    @DeleteMapping
    public void deleteRecommendation(@RequestBody UUID id) {
        logger.info("deleteRecommendation: {}", id);
        service.deleteRecommendation(id);
    }

    @GetMapping(value = "/transactions/{id}")
    public List<Transaction> findAllTransactionsById(@PathVariable UUID id) {
        return service.getTransactionsByUserId(id);
    }

}
