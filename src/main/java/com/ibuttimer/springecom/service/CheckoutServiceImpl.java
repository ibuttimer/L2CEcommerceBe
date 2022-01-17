package com.ibuttimer.springecom.service;

import com.ibuttimer.springecom.dao.CustomerRepository;
import com.ibuttimer.springecom.dto.PaymentInfo;
import com.ibuttimer.springecom.dto.Purchase;
import com.ibuttimer.springecom.dto.PurchaseResponse;
import com.ibuttimer.springecom.entity.Address;
import com.ibuttimer.springecom.entity.Customer;
import com.ibuttimer.springecom.entity.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;
    private final ProductService productService;


    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               ProductService productService,
                               @Value("${stripe.key.secret}") String stripeSecretKey) {
        this.customerRepository = customerRepository;
        this.productService = productService;

        // initialise stripe
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // Retrieve the order info from dto
        Order order = purchase.getOrder();

        // Generate order number
        String orderTrackingNumber = generateTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // Populate order with orderItems
        purchase.getOrderItems().forEach(order::add);

        // Populate order addresses
        Address billingAddress = purchase.getBillingAddress();
        Address shippingAddress = purchase.getShippingAddress();
        if (billingAddress.equals(shippingAddress)) {
            billingAddress = shippingAddress;
        }
        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);

        // Populate customer with order
        Customer customer = purchase.getCustomer();
        Customer repoCustomer = customerRepository.findByEmail(
                customer.getEmail()
        );

        if (repoCustomer != null) {
            customer = repoCustomer;
        }

        customer.add(order);

        // Save to database
        customerRepository.save(customer);

        // update stock levels
        productService.updateStock(order);

        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

        List<String> paymentMethodTypes = List.of("card");

        Map<String, Object> params = Map.of(
                "amount", paymentInfo.getAmount(),
                "currency", paymentInfo.getCurrency(),
                "payment_method_types", paymentMethodTypes,
//                "description", "Purchase",
                "receipt_email", paymentInfo.getReceiptEmail()
        );

        return PaymentIntent.create(params);
    }

    private String generateTrackingNumber() {
        return UUID.randomUUID().toString();
    }



}
