package com.ibuttimer.springecom.dto;

import lombok.Data;


/**
 * https://stripe.com/docs/api/payment_intents
 */
@Data
public class PaymentInfo {

    private int amount;
    private String currency;
    private String receiptEmail;
}
