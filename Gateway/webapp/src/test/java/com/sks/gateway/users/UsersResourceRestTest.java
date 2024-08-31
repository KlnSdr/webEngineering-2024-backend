package com.sks.gateway.users;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersResourceRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersSender sender;

    @BeforeEach
    public void setup() {
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetCurrentUser_ReturnsPrincipalWhenNotNull() throws Exception {
        mockMvc.perform(get("/users/current")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'authorities':[{'authority':'ROLE_USER'}],'details':null,'authenticated':true,'principal':{'password':'password','username':'user','authorities':[{'authority':'ROLE_USER'}],'accountNonExpired':true,'accountNonLocked':true,'credentialsNonExpired':true,'enabled':true},'credentials':'password','name':'user'}"));
    }

    @Test
    public void testGetCurrentUser_ReturnsNullWhenPrincipalIsNull() throws Exception {
        mockMvc.perform(get("/users/current")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testGetUserById_ReturnsUserWhenUserExists() throws Exception {
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        user.setUserName("John Doe");
        UsersResponseMessage responseMessage = new UsersResponseMessage();
        responseMessage.setUser(user);
        when(sender.sendRequest(any(UsersRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/users/id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'userId':1,'userName':'John Doe'}"));
    }

    @Test
    public void testGetUserById_ThrowsExceptionWhenUserDoesNotExist() throws Exception {
        UsersResponseMessage responseMessage = new UsersResponseMessage();
        when(sender.sendRequest(any(UsersRequestMessage.class))).thenReturn(responseMessage);

        mockMvc.perform(get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}