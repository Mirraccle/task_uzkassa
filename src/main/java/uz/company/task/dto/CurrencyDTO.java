package uz.company.task.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CurrencyDTO {
    private String title;
    private String code;
    private String cbPrice;
    private String nbuBuyPrice;
    private String nbuCellPrice;
    private String date;
}
