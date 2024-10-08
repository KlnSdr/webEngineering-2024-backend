package com.sks.gateway.fridges.rest;

import com.sks.fridge.api.FridgeDTO;
import com.sks.fridge.api.FridgeRequestMessage;
import com.sks.fridge.api.FridgeResponseMessage;
import com.sks.fridge.api.FridgeSender;
import com.sks.gateway.auth.JwtUtil;
import com.sks.gateway.util.AccessVerifier;
import com.sks.products.api.ProductDTO;
import com.sks.products.api.ProductsRequestMessage;
import com.sks.products.api.ProductsResponseMessage;
import com.sks.products.api.ProductsSender;
import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FridgesResourceRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductsSender productsSender;

    @MockBean
    private FridgeSender fridgeSender;

    @MockBean
    private AccessVerifier accessVerifier;

    private final OAuth2User mockPrincipal = new OAuth2User() {
        @Override
        public Map<String, Object> getAttributes() {
            return Map.of("sub", "user");
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public String getName() {
            return "user";
        }
    };

    @MockBean
    private UsersSender usersSender;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    private UserDTO user;

    @BeforeEach
    public void setup() {
        when(accessVerifier.verifyAccessesSelf(any(Long.class), any(Principal.class))).thenReturn(true);

        user = new UserDTO();
        user.setUserId(1L);
        user.setUserName("user");

        UsersResponseMessage usersResponseMessage = new UsersResponseMessage();
        usersResponseMessage.setKnownToken(true);
        when(usersSender.sendRequest(any())).thenReturn(usersResponseMessage);
        token = jwtUtil.generateToken(mockPrincipal, user);
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetFridgeItems_Success() throws Exception {
        FridgeDTO fridgeDTO = new FridgeDTO();
        fridgeDTO.setProducts(Map.of("/products/1", 2));

        FridgeResponseMessage fridgeResponse = new FridgeResponseMessage();
        fridgeResponse.setFridgeContent(fridgeDTO);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);

        ProductDTO product = new ProductDTO(1L, "Milk", "liters");
        ProductsResponseMessage productsResponse = new ProductsResponseMessage(new ProductDTO[]{product});
        when(productsSender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(productsResponse);

        mockMvc.perform(get("/fridge/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'name':'Milk','id':1,'unit':'liters','quantity':2}]"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetFridgeItems_FridgesNotFound() throws Exception {
        FridgeResponseMessage fridgeResponse = new FridgeResponseMessage();
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);

        mockMvc.perform(get("/fridge/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    public void testAddOrUpdateFridgeItems_Success() throws Exception {
        FridgeDTO fridgeDTO = new FridgeDTO();
        fridgeDTO.setProducts(Map.of("/products/1", 5));

        FridgeResponseMessage fridgeResponse = new FridgeResponseMessage();
        fridgeResponse.setFridgeContent(fridgeDTO);
        fridgeResponse.setWasSuccess(true);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);

        ProductDTO product = new ProductDTO(1L, "Milk", "liters");
        ProductsResponseMessage productsResponse = new ProductsResponseMessage(new ProductDTO[]{product});
        when(productsSender.sendRequest(any(ProductsRequestMessage.class))).thenReturn(productsResponse);

        String requestContent = "[{\"productId\":1,\"quantity\":5}]";

        mockMvc.perform(put("/fridge/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'name':'Milk','id':1,'unit':'liters','quantity':5}]"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testAddOrUpdateFridgeItems_Failure() throws Exception {
        FridgeResponseMessage fridgeResponse = new FridgeResponseMessage();
        fridgeResponse.setWasSuccess(false);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);

        String requestContent = "[{\"productId\":1,\"quantity\":5}]";

        mockMvc.perform(put("/fridge/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user")
    public void testDeleteFridgeItem_Success() throws Exception {
        FridgeResponseMessage fridgeResponse = new FridgeResponseMessage();
        fridgeResponse.setWasSuccess(true);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);

        mockMvc.perform(delete("/fridge/1/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user")
    public void testDeleteFridgeItem_Failure() throws Exception {
        FridgeResponseMessage fridgeResponse = new FridgeResponseMessage();
        fridgeResponse.setWasSuccess(false);
        when(fridgeSender.sendRequest(any(FridgeRequestMessage.class))).thenReturn(fridgeResponse);

        mockMvc.perform(delete("/fridge/1/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
