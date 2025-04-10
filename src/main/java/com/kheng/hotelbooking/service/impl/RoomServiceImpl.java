package com.kheng.hotelbooking.service.impl;

import com.kheng.hotelbooking.dto.Response;
import com.kheng.hotelbooking.dto.RoomDTO;
import com.kheng.hotelbooking.entity.Room;
import com.kheng.hotelbooking.enums.RoomType;
import com.kheng.hotelbooking.exception.InvalidBookingStateAndDateException;
import com.kheng.hotelbooking.exception.NotFoundException;
import com.kheng.hotelbooking.repository.RoomRepository;
import com.kheng.hotelbooking.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final static String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-images/";

    @Override
    public Response addRoom(RoomDTO roomDTO, MultipartFile imageFile) {
        Room roomToSave = modelMapper.map(roomDTO, Room.class);

        // Save room without image first
        Room savedRoom;
        try {
            savedRoom = roomRepository.save(roomToSave);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save room data: " + e.getMessage());
        }

        // Save image only if room is saved successfully
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            savedRoom.setImageUrl(imagePath);
            roomRepository.save(savedRoom); // Update image URL after image saved
        }

        return Response.builder()
                .status(200)
                .message("Successfully added room")
                .build();
    }


    @Override
    public Response updateRoom(RoomDTO roomDTO, MultipartFile imageFile) {
        Room existingRoom = roomRepository.findById(roomDTO.getId())
                .orElseThrow(() -> new NotFoundException("Room not found"));

        // Only update fields if valid
        if (roomDTO.getRoomNumber() != null && roomDTO.getRoomNumber() >= 0) {
            existingRoom.setRoomNumber(roomDTO.getRoomNumber());
        }
        if (roomDTO.getPricePerNight() != null && roomDTO.getPricePerNight().compareTo(BigDecimal.ZERO) >= 0) {
            existingRoom.setPricePerNight(roomDTO.getPricePerNight());
        }
        if (roomDTO.getCapacity() != null && roomDTO.getCapacity() >= 0) {
            existingRoom.setCapacity(roomDTO.getCapacity());
        }
        if (roomDTO.getType() != null) {
            existingRoom.setType(roomDTO.getType());
        }
        if (roomDTO.getDescription() != null) {
            existingRoom.setDescription(roomDTO.getDescription());
        }

        try {
            roomRepository.save(existingRoom);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update room: " + e.getMessage());
        }

        // Save image after successful update
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            existingRoom.setImageUrl(imagePath);
            roomRepository.save(existingRoom);
        }

        return Response.builder()
                .status(200)
                .message("Successfully updated room")
                .build();
    }


    @Override
    public Response getAllRooms() {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<RoomDTO> roomDTOs = rooms.stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .toList();
        return Response.builder()
                .status(200)
                .message("Success")
                .rooms(roomDTOs)
                .build();
    }

    @Override
    public Response getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Room not found"));
        RoomDTO roomDTO = modelMapper.map(room, RoomDTO.class);

        return Response.builder()
                .status(200)
                .message("Success")
                .room(roomDTO)
                .build();
    }

    @Override
    public Response deleteRoom(Long id) {
        if(!roomRepository.existsById(id)) throw new NotFoundException("Room not found");
        roomRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("Room deleted successfully")
                .build();
    }

    @Override
    public Response getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {

        //validation: Ensure check-in date is not before today
        if(checkInDate.isBefore(LocalDate.now())){
            throw new InvalidBookingStateAndDateException("check-in date cannot be before today");
        }

        //validation: Ensure check-out date is not before check-in
        if(checkOutDate.isBefore(checkInDate)){
            throw new InvalidBookingStateAndDateException("check-out date cannot be before check-in date");
        }

        //validation: Ensure check-in date is not equal check-out date
        if(checkInDate.isEqual(checkOutDate)){
            throw new InvalidBookingStateAndDateException("check-in date cannot be equal to check-out date");
        }

        List<Room> rooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomDTO> roomDTOList = modelMapper.map(
                rooms, new TypeToken<List<RoomDTO>>() {}.getType()
        );
        return Response.builder()
                .status(200)
                .message("Success")
                .rooms(roomDTOList)
                .build();
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomRepository.getAllRoomTypes();
    }

    @Override
    public Response searchRoom(String input) {
        List<Room> rooms = roomRepository.searchRooms(input);
        List<RoomDTO> roomDTOList = modelMapper.map(rooms, new TypeToken<List<RoomDTO>>() {}.getType());
        return Response.builder()
                .status(200)
                .message("Success")
                .rooms(roomDTOList)
                .build();
    }


    private String saveImage(MultipartFile imageFile) {
        if(!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image type");
        }
        //create Dir to store img if not exist
        File dir = new File(IMAGE_DIRECTORY);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        //Generate unique ID for file name
        String uniqueFileName = UUID.randomUUID()+ "_" + imageFile.getOriginalFilename();
        //Get absolute path
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return imagePath;
    }
}
