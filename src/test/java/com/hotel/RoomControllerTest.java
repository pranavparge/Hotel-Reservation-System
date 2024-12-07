package com.hotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.request.RoomCreateRequest;
import com.hotel.dto.request.RoomPriceRequest;
import com.hotel.dto.response.RoomCreateResponse;
import com.hotel.dto.response.RoomPriceResponse;
import com.hotel.enums.ProgramType;
import com.hotel.enums.RoomType;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.StaffRepository;
import com.hotel.room.service.IRoomService;
import com.hotel.user.entity.Customer;
import com.hotel.user.entity.Staff;
import com.hotel.util.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {
    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private StaffRepository staffRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IRoomService roomService;

    @MockBean
    private JwtUtility jwtUtility;

    @BeforeEach
    public void setupMocks() {
        when(customerRepository.findFirstByEmail("customer@gmail.com")).thenReturn(Optional.of(
                new Customer(ProgramType.MEMBER, "CustomerName", "customer@gmail.com", "{noop}password")
        ));
        when(staffRepository.findFirstByEmail("staff@gmail.com")).thenReturn(Optional.of(
                new Staff("StaffName", "staff@gmail.com", "{noop}password")
        ));
    }

    @Test
    public void testCreateRoom_Success() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("staff@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("STAFF");

        RoomCreateRequest request = new RoomCreateRequest("1", 2, RoomType.SINGLE.toString(), "AVAILABLE");
        RoomCreateResponse response = new RoomCreateResponse("1", 2, RoomType.SINGLE, 100.0);

        when(roomService.createRoom(any(RoomCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/staff/rooms/create")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("1"))
                .andExpect(jsonPath("$.roomType").value(RoomType.SINGLE.toString()))
                .andExpect(jsonPath("$.roomCapacity").value(2))
                .andExpect(jsonPath("$.roomPrice").value(100.0));
    }

    @Test
    public void testCreateRoom_InvalidInput() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("staff@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("STAFF");

        RoomCreateRequest request = new RoomCreateRequest("1", 2, RoomType.SINGLE.toString(), "AVAILABLE");

        when(roomService.createRoom(any(RoomCreateRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid room details"));

        mockMvc.perform(post("/staff/rooms/create")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input!"));
    }

    @Test
    public void testCreateRoom_InternalError() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("staff@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("STAFF");

        RoomCreateRequest request = new RoomCreateRequest("1", 2, RoomType.SINGLE.toString(), "AVAILABLE");

        when(roomService.createRoom(any(RoomCreateRequest.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        mockMvc.perform(post("/staff/rooms/create")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Room not created!"));
    }

    @Test
    public void testViewRooms_Success() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        RoomCreateResponse room1 = new RoomCreateResponse("1", 2, RoomType.SINGLE, 100.0);
        RoomCreateResponse room2 = new RoomCreateResponse("2", 2, RoomType.DOUBLE, 150.0);

        when(roomService.viewRooms()).thenReturn(Arrays.asList(room1, room2));

        mockMvc.perform(get("/customer/rooms")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").value("1"))
                .andExpect(jsonPath("$[0].roomType").value(RoomType.SINGLE.toString()))
                .andExpect(jsonPath("$[1].roomNumber").value("2"))
                .andExpect(jsonPath("$[1].roomType").value(RoomType.DOUBLE.toString()));
    }

    @Test
    public void testViewRooms_InternalError() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        when(roomService.viewRooms()).thenThrow(new RuntimeException("Database connection error"));

        mockMvc.perform(get("/customer/rooms")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Unable to fetch rooms!"));
    }

    @Test
    public void testGetRoomPrice_Success() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        RoomPriceRequest request = new RoomPriceRequest("2024-12-01");
        RoomPriceResponse response = new RoomPriceResponse(100.0, 150.0, 200.0);

        when(roomService.getRoomPrice(any(RoomPriceRequest.class))).thenReturn(response);

        mockMvc.perform(get("/customer/rooms/price")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.singleRoomPrice").value(100.0))
                .andExpect(jsonPath("$.doubleRoomPrice").value(150.0))
                .andExpect(jsonPath("$.suiteRoomPrice").value(200.0));
    }

    @Test
    public void testGetRoomPrice_InvalidInput() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        RoomPriceRequest request = new RoomPriceRequest("2024-12-01");

        when(roomService.getRoomPrice(any(RoomPriceRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid room details"));

        mockMvc.perform(get("/customer/rooms/price")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input!"));
    }

    @Test
    public void testGetRoomPrice_InternalError() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        RoomPriceRequest request = new RoomPriceRequest("2024-12-01");

        when(roomService.getRoomPrice(any(RoomPriceRequest.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        mockMvc.perform(get("/customer/rooms/price")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Unable to fetch room prices!"));
    }
}
