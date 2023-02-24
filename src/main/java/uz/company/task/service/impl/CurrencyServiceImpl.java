package uz.company.task.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.company.task.dto.CurrencyDTO;
import uz.company.task.service.CurrencyService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final RestTemplate restTemplate;

    private static final String URL = "https://nbu.uz/uz/exchange-rates/json/";

    public CurrencyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CurrencyDTO getCurrency(String code) {
        ResponseEntity<CurrencyDTO[]> response = restTemplate.getForEntity(URL, CurrencyDTO[].class);

        Optional<CurrencyDTO> currency = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .filter(currencyDTO -> currencyDTO.getCode().equals(code))
                .findFirst();
        return currency.orElse(null);
    }

    @Override
    public List<CurrencyDTO> getAll() {
        ResponseEntity<CurrencyDTO[]> response = restTemplate.getForEntity(URL, CurrencyDTO[].class);
        return Arrays.stream(Objects.requireNonNull(response.getBody())).collect(Collectors.toList());
    }
}
