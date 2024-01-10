package com.assignment.cartCrud.integration;

import com.assignment.cartCrud.job.CartCleanupTask;
import com.assignment.cartCrud.respository.CartRepository;
import com.jayway.jsonpath.JsonPath;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartTtlIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartCleanupTask cartCleanupTask;

    @Test
    public void testCartTtl() throws Exception {
        MvcResult result = mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        String cartId = JsonPath.parse(jsonResponse).read("$.id", String.class);

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() ->
                !cartRepository.getCart(cartId).isPresent());

        assertFalse(cartRepository.getCart(cartId).isPresent());
    }
}
