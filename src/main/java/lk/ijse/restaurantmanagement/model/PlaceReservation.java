package lk.ijse.restaurantmanagement.model;

import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class PlaceReservation {
    private Reservation reservation;
    private List<reservationDetails> reservationDetailsList;

}
