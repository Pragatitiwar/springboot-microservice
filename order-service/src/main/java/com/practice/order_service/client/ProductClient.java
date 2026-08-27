package com.practice.order_service.client;

import com.practice.order_service.dto.ProductResponse;
import com.practice.order_service.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ProductResponse getProduct(Long productId) {

      try {
          return restClient.get()
                  .uri("/products/{id}", productId)
                  .retrieve()
                  .body(ProductResponse.class);
      } catch (HttpClientErrorException.NotFound ex) {
          throw new ProductNotFoundException("Product not found with id : " + productId);
      }
    }
}
