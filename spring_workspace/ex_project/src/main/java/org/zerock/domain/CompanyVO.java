package org.zerock.domain;

import java.util.Date;

import lombok.Data;

@Data
public class CompanyVO {
    private String companyCode;
    private String companyName;
    private String ceoName;
    private String businessType;
    private Date regdate;
    private Date updateDate;
}