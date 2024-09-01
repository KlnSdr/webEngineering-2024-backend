package com.sks.gateway.products.rest;

import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductsResourceRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductsSender sender;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetAllProducts() throws Exception {
        ProductDTO product1 = new ProductDTO(1, "Freedom", "baldEagle per oil barrel");
        ProductDTO product2 = new ProductDTO(2, "Freedom of press", "Journalists per prison cell");
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(new ProductDTO[] {product1, product2});

        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'name':'Freedom','unit':'baldEagle per oil barrel'},{'id':2,'name':'Freedom of press','unit':'Journalists per prison cell'}]"));
    }

    @Test
    public void testGetProductById() throws Exception {
        ProductDTO product = new ProductDTO(1, "Democracy", "t");
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(new ProductDTO[] {product});
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'name':'Democracy','unit':'t'}"));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(null);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetMultiple() throws Exception {
        ProductDTO product1 = new ProductDTO(1, "Productivity", "Lines of Code per Line");
        ProductDTO product2 = new ProductDTO(2, "Energy", "BPM per energy drink");
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(new ProductDTO[] {product1, product2});

        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(post("/products/get-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2]")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'name':'Productivity','unit':'Lines of Code per Line'},{'id':2,'name':'Energy','unit':'BPM per energy drink'}]"));
    }

    @Test
    public void testGetMultiple_NotFound() throws Exception {
        ProductsResponseMessage responseMessage = new ProductsResponseMessage(null);
        when(sender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(post("/products/get-multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2]")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}