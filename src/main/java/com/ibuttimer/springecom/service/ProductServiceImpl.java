package com.ibuttimer.springecom.service;

import com.ibuttimer.springecom.dao.ProductRepository;
import com.ibuttimer.springecom.entity.Order;
import com.ibuttimer.springecom.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public void updateStock(Order order) {
        AtomicBoolean result = new AtomicBoolean(false);

        order.getOrderItems().forEach(orderItem -> {
            Product product = productRepository.getById(orderItem.getProductId());
            int unitsInStock = product.getUnitsInStock() - orderItem.getQuantity();
            if (unitsInStock < 0) {
                unitsInStock = 0;
            }
            product.setUnitsInStock(unitsInStock);

            // Save to database
            productRepository.save(product);

            result.set(true);
        });

        result.get();
    }
}
