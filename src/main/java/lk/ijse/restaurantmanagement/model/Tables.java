package lk.ijse.restaurantmanagement.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Tables {
    private String tableId;
    private String description;
    private int noOfTables;
    private int noOfSeats;
}
