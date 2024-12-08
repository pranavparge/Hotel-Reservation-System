package com.hotel;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.request.RoomPriceRequest;
import com.hotel.dto.response.RoomPriceResponse;
import com.hotel.room.service.RoomService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomPriceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Test
    void testGetRoomPrice_Success() throws Exception {
        // Mocking service response
        RoomPriceResponse response = new RoomPriceResponse();
        response.setSingleRoomPrice(100.0);
        response.setDoubleRoomPrice(150.0);
        response.setSuiteRoomPrice(200.0);

        Mockito.when(roomService.getRoomPrice(any(RoomPriceRequest.class))).thenReturn(response);

        // Test the GET request
        mockMvc.perform(get("/customer/rooms/price")
                        .header("Authorization", "Bearer "+jwtToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\":\"2024-12-06\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"singleRoomPrice\":100.0,\"doubleRoomPrice\":150.0,\"suiteRoomPrice\":200.0}"));
    }

   public AtomicReference<String> jwtToken() throws Exception {
       AtomicReference<String> jwtToken = new AtomicReference<>("");

       String jsonPayload = "{ \"email\": \"customer@gmail.com\", \"password\": \"customer@123\" }";

       // Perform the POST request to the sign-in endpoint
       mockMvc.perform(post("/auth/customer/sign-in")
                       .contentType("application/json")
                       .content(jsonPayload))
               .andExpect(status().isOk()) // Expecting HTTP 200 OK
               .andExpect(jsonPath("$.jwt").exists()) // Ensure the JWT token is present
               .andDo(result -> {
                   // Extract the response body as a string
                   String responseBody = result.getResponse().getContentAsString();

                   // Create an ObjectMapper to parse the response body
                   ObjectMapper objectMapper = new ObjectMapper();
                   JsonNode responseJson = objectMapper.readTree(responseBody);

                   // Extract the JWT token from the response
                   jwtToken.set(responseJson.get("jwt").asText());

                   // Print or use the token as needed
                   System.out.println("Extracted JWT Token: " + jwtToken);

                   // You can store this JWT token if you need to use it for other requests
                   // For example, save it for future requests or further assertions
               });

       return jwtToken;

    }


}
