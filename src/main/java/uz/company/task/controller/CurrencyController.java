package uz.company.task.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.company.task.dto.CurrencyDTO;
import uz.company.task.service.CurrencyService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<CurrencyDTO>> getAll() {
        return ResponseEntity.ok(currencyService.getAll());
    }

    @GetMapping("/get/{code}")
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.getCurrency(code));
    }
}
