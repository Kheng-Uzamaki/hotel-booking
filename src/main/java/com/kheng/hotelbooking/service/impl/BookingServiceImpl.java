package com.kheng.hotelbooking.service.impl;

import com.kheng.hotelbooking.dto.BookingDTO;
import com.kheng.hotelbooking.dto.NotificationDTO;
import com.kheng.hotelbooking.dto.Response;
import com.kheng.hotelbooking.entity.Booking;
import com.kheng.hotelbooking.entity.Room;
import com.kheng.hotelbooking.entity.User;
import com.kheng.hotelbooking.enums.BookingStatus;
import com.kheng.hotelbooking.enums.PaymentStatus;
import com.kheng.hotelbooking.exception.InvalidBookingStateAndDateException;
import com.kheng.hotelbooking.exception.NotFoundException;
import com.kheng.hotelbooking.repository.BookingRepository;
import com.kheng.hotelbooking.repository.RoomRepository;
import com.kheng.hotelbooking.service.BookingCodeGenerator;
import com.kheng.hotelbooking.service.BookingService;
import com.kheng.hotelbooking.service.NotificationService;
import com.kheng.hotelbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomRepository roomRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final BookingCodeGenerator bookingCodeGenerator;

    @Override
    public Response getAllBookings() {
        List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BookingDTO> bookingDTOList = modelMapper.map(bookingList, new TypeToken<List<BookingDTO>>() {}.getType());

        for (BookingDTO bookingDTO : bookingDTOList) {
            bookingDTO.setUser(null);
            bookingDTO.setRoom(null);
        }
        return Response.builder()
                .status(200)
                .message("success")
                .bookings(bookingDTOList)
                .build();
    }

    @Override
    public Response createBooking(BookingDTO bookingDTO) {
        User currentUser = userService.getCurrentLoggedInUser();

        Room room = roomRepository.findById(bookingDTO.getRoomId()).orElseThrow(
                () -> new NotFoundException("Room not found")
        );

        //validation: Ensure check-in date is not before today
        if(bookingDTO.getCheckInDate().isBefore(LocalDate.now())){
            throw new InvalidBookingStateAndDateException("check-in date cannot be before today");
        }
        //validation: Ensure check-out date is not before check-in
        if(bookingDTO.getCheckOutDate().isBefore(bookingDTO.getCheckInDate())){
            throw new InvalidBookingStateAndDateException("check-out date cannot be before check-in date");
        }
        //validation: Ensure check-in date is not equal check-out date
        if(bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())){
            throw new InvalidBookingStateAndDateException("check-in date cannot be equal to check-out date");
        }

        //validate room availability
        boolean isAvailable = bookingRepository.isRoomAvailable(room.getId(),bookingDTO.getCheckInDate(),bookingDTO.getCheckOutDate());
        if(!isAvailable){
            throw new InvalidBookingStateAndDateException("Room is not available for the selected date ranges!");
        }
        //calculate the total price needed to pay for the stay
        BigDecimal totalPrice = calculateTotalPrice(room,bookingDTO);
        String bookingReference = bookingCodeGenerator.generateBookingReference();

        // create and save the booking
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(currentUser);
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setBookingReference(bookingReference);
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        //generate the payment Url which will be sent vai mail
        String paymentUrl = "http://localhost:3000/payment/" + bookingReference + "/" + totalPrice;
        log.info("Payment URL: {}", paymentUrl);

        //send Notification vai Email
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(currentUser.getEmail())
                .subject("Booking Confirmation📝")
                .body(String.format("""
                        Your Booking has been created successfully.\
                        Please proceed with your payment using the payment Link below\
                        
                        
                         %s""", paymentUrl))
                .bookingReference(bookingReference)
                .build();
        notificationService.sendEmail(notificationDTO);
        log.info("Email sent successfully to {} 🚀", currentUser.getEmail());

        return Response.builder()
                .status(200)
                .message("Booking has been created successfully🚀")
                .booking(bookingDTO)
                .build();
    }

    @Override
    public Response findBookingByReferenceNo(String bookingReferenceNo) {
        Booking booking = bookingRepository.findByBookingReference(bookingReferenceNo)
                .orElseThrow(() -> new NotFoundException("Booking not found with ReferenceNo: " + bookingReferenceNo));
        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);

        return Response.builder()
                .status(200)
                .message("Success")
                .booking(bookingDTO)
                .build();
    }

    @Override
    public Response updateBooking(BookingDTO bookingDTO) {
        if(bookingDTO.getId() == null) throw new NotFoundException("Booing id is required");

        Booking existingBooking = bookingRepository.findById(bookingDTO.getId()).orElseThrow(
                () -> new NotFoundException("Booking not found")
        );

        if(bookingDTO.getBookingStatus() != existingBooking.getBookingStatus()){
            existingBooking.setBookingStatus(bookingDTO.getBookingStatus());
        }
        if(bookingDTO.getPaymentStatus() != existingBooking.getPaymentStatus()){
            existingBooking.setPaymentStatus(bookingDTO.getPaymentStatus());
        }
        bookingRepository.save(existingBooking);
        return Response.builder()
                .status(200)
                .message("Booking has been updated successfully")
                .build();
    }

    private BigDecimal calculateTotalPrice(Room room, BookingDTO bookingDTO) {
        BigDecimal pricePerNight = room.getPricePerNight();
        long days = ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(),bookingDTO.getCheckOutDate());
        return pricePerNight.multiply(BigDecimal.valueOf(days));
    }
}
