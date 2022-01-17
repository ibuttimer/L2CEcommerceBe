package com.ibuttimer.springecom.dto;

import com.ibuttimer.springecom.entity.Address;
import com.ibuttimer.springecom.entity.Customer;
import com.ibuttimer.springecom.entity.Order;
import com.ibuttimer.springecom.entity.OrderItem;
import lombok.Data;

import java.util.Collection;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Collection<OrderItem> orderItems;
}
