package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Currency {
    private String CcyNm_EN; // in English
    private String CcyNm_UZC;
    private String Diff; // how changed
    private String Rate; // money in this date
    private String Ccy;
    private String CcyNm_RU;
    private Integer id;
    private String CcyNm_UZ;
    private String Code;
    private String Nominal;
    private String Date;
}

