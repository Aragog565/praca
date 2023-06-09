package sklep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sklep.entity.User;
import sklep.service.RateService;
import sklep.service.dto.Create.CreateRateDTO;
import sklep.service.dto.RateDTO;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/api/products/{productID}/")
public class RateController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping("rate")
    public ResponseEntity<RateDTO> addRate(
            @PathVariable final Long productID,
            @RequestBody @Validated CreateRateDTO createRateDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to save: Rate {} in product {}", createRateDTO, productID);
        RateDTO rateDTO = rateService.save(createRateDTO,authenticatedUser,productID);
        return ResponseEntity.ok(rateDTO);

    }

//    @GetMapping("rate")
//    public ResponseEntity<List<RateDTO>> getRate(
//            @PathVariable final Long productID
//    ){
//        log.debug("Request to get: product {} Rate", productID);
//
//        List<RateDTO> rateDTOS = rateService.getProductRate(productID);
//        return  ResponseEntity.ok(rateDTOS);
//    }
//    @GetMapping("rate")
//    public ResponseEntity<RateDTO> getRate1(
//            @PathVariable final Long productID,
//            @RequestParam(defaultValue = "") String query
//    ){
//        log.debug("Request to get: product {} Rate", productID);
//
//        RateDTO rateDTO = rateService.getProductRate1(query, productID);
//        return ResponseEntity.ok(rateDTO);
//    }
}
