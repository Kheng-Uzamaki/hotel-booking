package com.kheng.hotelbooking.service;

import com.kheng.hotelbooking.entity.BookingReference;
import com.kheng.hotelbooking.repository.BookingReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class BookingCodeGenerator {
    private final BookingReferenceRepository bookingReferenceRepository;

    @Value("${characters}")
    String characters;

    public String generateBookingReference() {
        String bookingReference;
        do {
            bookingReference = GenerateRandomAlphaNumericCode(10);
        }while (isBookingReferenceExist(bookingReference));

        saveBookingReference(bookingReference);
        return bookingReference;
    }

    private String GenerateRandomAlphaNumericCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    private boolean isBookingReferenceExist(String bookingReference) {
        return bookingReferenceRepository.findByReferenceNo(bookingReference).isPresent();
    }

    private void saveBookingReference(String bookingReference) {
        BookingReference newBookingReference = BookingReference.builder()
                .referenceNo(bookingReference)
                .build();
        bookingReferenceRepository.save(newBookingReference);
    }
}
