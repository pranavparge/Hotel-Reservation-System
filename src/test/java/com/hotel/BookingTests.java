package com.hotel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.booking.controller.BookingController;
import com.hotel.booking.service.BookingService;
import com.hotel.dto.request.BookingCreateRequest;
import com.hotel.dto.request.BookingUpdateRequest;
import com.hotel.dto.response.BookingCreateResponse;
import com.hotel.dto.response.RoomCreateResponse;
import com.hotel.enums.ProgramType;
import com.hotel.enums.RoomType;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.StaffRepository;
import com.hotel.user.entity.Customer;
import com.hotel.user.entity.Staff;
import com.hotel.util.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class BookingTests {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private StaffRepository staffRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    public void testCreateBooking_Success() throws Exception {

        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        // Prepare the mock response
        BookingCreateResponse bookingCreateResponse = new BookingCreateResponse();
        bookingCreateResponse.setBookingId(1L);
        bookingCreateResponse.setCustomerId(1L);
        bookingCreateResponse.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-01"));
        bookingCreateResponse.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-03"));
        bookingCreateResponse.setTotalNumberOfGuests(2);
        bookingCreateResponse.setTotalPrice(90.0);
        bookingCreateResponse.setAdditionalServices(Arrays.asList("Breakfast", "Dinner"));

        RoomCreateResponse roomCreateResponse = new RoomCreateResponse();
        roomCreateResponse.setRoomNumber("101");
        roomCreateResponse.setRoomCapacity(2);
        roomCreateResponse.setRoomType(RoomType.SINGLE);
        roomCreateResponse.setRoomPrice(100.0);

        bookingCreateResponse.setTotalRooms(Collections.singletonList(roomCreateResponse));
        bookingCreateResponse.setTimeStamp(new Date());

        // Prepare the mock request
        BookingCreateRequest bookingCreateRequest = new BookingCreateRequest();
        bookingCreateRequest.setCustomerId(1L);
        bookingCreateRequest.setRoomTypeToQuantity(Map.of(RoomType.SINGLE, 1));
        bookingCreateRequest.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-01"));
        bookingCreateRequest.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-03"));
        bookingCreateRequest.setTotalNumberOfGuests(2);
        bookingCreateRequest.setAddBreakfast(true);
        bookingCreateRequest.setAddLunch(false);
        bookingCreateRequest.setAddDinner(true);

        // Mock the service method call
        when(bookingService.createBooking(any(BookingCreateRequest.class))).thenReturn(bookingCreateResponse);

        // Convert the request to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(bookingCreateRequest);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/customer/bookings/create")
                        .header("Authorization", "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.startDate").value("2024-12-01T00:00:00.000+00:00"))
                .andExpect(jsonPath("$.endDate").value("2024-12-03T00:00:00.000+00:00"))
                .andExpect(jsonPath("$.totalPrice").value(90.0))
                .andExpect(jsonPath("$.additionalServices").isArray())
                .andExpect(jsonPath("$.additionalServices[0]").value("Breakfast"))
                .andExpect(jsonPath("$.additionalServices[1]").value("Dinner"))
                .andExpect(jsonPath("$.totalRooms[0].roomNumber").value("101"));

        // Verify that the service method was called
        verify(bookingService, times(1)).createBooking(any(BookingCreateRequest.class));
    }

    @Test
    public void testViewBookingById_Success() throws Exception {

        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("staff@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("STAFF");

        // Prepare mock data
        BookingCreateResponse bookingCreateResponse = new BookingCreateResponse();
        bookingCreateResponse.setBookingId(1L);
        bookingCreateResponse.setCustomerId(1L);
        bookingCreateResponse.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-01"));
        bookingCreateResponse.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-03"));
        bookingCreateResponse.setTotalNumberOfGuests(2);
        bookingCreateResponse.setTotalPrice(90.0);
        bookingCreateResponse.setAdditionalServices(Arrays.asList("Breakfast", "Dinner"));

        RoomCreateResponse roomCreateResponse = new RoomCreateResponse();
        roomCreateResponse.setRoomNumber("101");
        roomCreateResponse.setRoomCapacity(2);
        roomCreateResponse.setRoomType(RoomType.SINGLE);
        roomCreateResponse.setRoomPrice(100.0);

        bookingCreateResponse.setTotalRooms(Collections.singletonList(roomCreateResponse));

        // Mock the service
        when(bookingService.viewBooking(1L)).thenReturn(bookingCreateResponse);

        // Perform the GET request
        mockMvc.perform(get("/staff/bookings")
                        .param("bookingID", "1")
                        .header("Authorization", "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.startDate").value("2024-12-01T00:00:00.000+00:00"))
                .andExpect(jsonPath("$.endDate").value("2024-12-03T00:00:00.000+00:00"))
                .andExpect(jsonPath("$.totalPrice").value(90.0))
                .andExpect(jsonPath("$.additionalServices").isArray())
                .andExpect(jsonPath("$.additionalServices[0]").value("Breakfast"));

        // Verify service call
        verify(bookingService, times(1)).viewBooking(1L);
    }

    @Test
    public void testUpdateBooking_Success() throws Exception {

        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("staff@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("STAFF");

        // Mock data
        BookingUpdateRequest updateRequest = new BookingUpdateRequest();
        updateRequest.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-01"));
        updateRequest.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-03"));
        updateRequest.setTotalNumberOfGuests(3);
        updateRequest.setRoomTypeToQuantity(Collections.singletonMap(RoomType.SINGLE, 1));
        updateRequest.setAdditionalServices(Arrays.asList("Breakfast"));

        BookingCreateResponse response = new BookingCreateResponse();
        response.setBookingId(1L);
        response.setCustomerId(1L);
        response.setStartDate(updateRequest.getStartDate());
        response.setEndDate(updateRequest.getEndDate());
        response.setTotalNumberOfGuests(updateRequest.getTotalNumberOfGuests());
        response.setTotalPrice(90.0);
        response.setAdditionalServices(updateRequest.getAdditionalServices());

        RoomCreateResponse roomCreateResponse = new RoomCreateResponse();
        roomCreateResponse.setRoomNumber("101");
        roomCreateResponse.setRoomCapacity(2);
        roomCreateResponse.setRoomType(RoomType.SINGLE);
        roomCreateResponse.setRoomPrice(100.0);

        response.setTotalRooms(Collections.singletonList(roomCreateResponse));
        response.setTimeStamp(new Date());

        // Mock service
        when(bookingService.updateBooking(eq(1L), any(BookingUpdateRequest.class))).thenReturn(response);

        // Perform PUT request
        mockMvc.perform(put("/staff/bookings/update")
                        .param("bookingID", "1")
                        .header("Authorization", "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.totalPrice").value(90.0))
                .andExpect(jsonPath("$.totalRooms").isArray());

        // Verify service interaction
        verify(bookingService, times(1)).updateBooking(eq(1L), any(BookingUpdateRequest.class));
    }

    @Test
    void testDeleteBookingSuccess() throws Exception {

        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("staff@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("STAFF");

        Long bookingId = 1L;

        // Mock service behavior
        when(bookingService.deleteBooking(bookingId)).thenReturn(true);

        // Perform the DELETE request and validate
        mockMvc.perform(delete("/staff/bookings/delete")
                        .param("bookingID", bookingId.toString())
                        .header("Authorization", "Bearer mockedToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking deleted successfully"));
    }

}
