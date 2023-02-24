package uz.company.task.service;

import uz.company.task.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    CurrencyDTO getCurrency(String code);

    List<CurrencyDTO> getAll();
}
