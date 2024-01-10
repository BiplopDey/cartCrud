package com.assignment.cartCrud.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartCrudIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateAndGetCart() throws Exception {
        String jsonResponse = mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String cartId = JsonPath.parse(jsonResponse).read("$.id", String.class);

        String productJson = "{\"id\":1,\"description\":\"Sample Product\",\"amount\":10.0}";
        mockMvc.perform(post("/cart/" + cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cart/" + cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].id").value(1))
                .andExpect(jsonPath("$.products[0].description").value("Sample Product"))
                .andExpect(jsonPath("$.products[0].amount").value(10.0));

        mockMvc.perform(delete("/cart/" + cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
