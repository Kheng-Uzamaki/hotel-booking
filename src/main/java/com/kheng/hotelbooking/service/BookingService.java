package com.kheng.hotelbooking.service;

import com.kheng.hotelbooking.dto.BookingDTO;
import com.kheng.hotelbooking.dto.Response;

public interface BookingService {
    Response getAllBookings();
    Response createBooking(BookingDTO bookingDTO);
    Response findBookingByReferenceNo(String bookingReferenceNo);
    Response updateBooking(BookingDTO bookingDTO);
}
