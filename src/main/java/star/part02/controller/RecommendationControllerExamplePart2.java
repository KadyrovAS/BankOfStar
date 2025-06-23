package star.part02.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import star.part02.service.RecommendationServiceExamplePart2;

@RestController
@RequestMapping("/ExamplePart2")

public class RecommendationControllerExamplePart2 {
    private final RecommendationServiceExamplePart2 service;
    Logger logger = LoggerFactory.getLogger(RecommendationControllerExamplePart2.class);

    public RecommendationControllerExamplePart2(RecommendationServiceExamplePart2 service) {
        this.service = service;
    }

    @PostMapping
    public void createDb(){
        service.createDb();
    }
}
