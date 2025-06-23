package star.part01.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import star.part01.service.RecommendationExampleService;

@RestController
@RequestMapping("/part01/example")
public class RecommendationExamplesController {
    private final RecommendationExampleService service;

    public RecommendationExamplesController(RecommendationExampleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> insertExampleRecords(){
        service.addExample();
        return ResponseEntity.ok("Записи добавлены");
    }

}
