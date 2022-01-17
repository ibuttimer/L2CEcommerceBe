package com.ibuttimer.springecom.service;

import com.ibuttimer.springecom.dto.PaymentInfo;
import com.ibuttimer.springecom.dto.Purchase;
import com.ibuttimer.springecom.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
