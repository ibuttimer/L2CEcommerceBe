package com.ibuttimer.springecom.controller;

import com.ibuttimer.springecom.SpringEcommerceApplication;
import com.ibuttimer.springecom.dto.PaymentInfo;
import com.ibuttimer.springecom.dto.Purchase;
import com.ibuttimer.springecom.dto.PurchaseResponse;
import com.ibuttimer.springecom.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ibuttimer.springecom.config.Config.*;


@RestController
@RequestMapping("${spring.data.rest.base-path}" + CHECKOUT_URL)
public class CheckoutController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping(PURCHASE_URL)
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase) {
        return checkoutService.placeOrder(purchase);
    }

    @PostMapping(PAYMENT_INTENT_URL)
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {

        logger.debug(paymentInfo.toString());

        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfo);
        return new ResponseEntity<>(paymentIntent.toJson(), HttpStatus.OK);
    }
}
