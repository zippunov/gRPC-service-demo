package xcorp.parking.business.dto;

import lombok.Data;

@Data
public class BookingCount {
    private int totalSlots;
    private int takenSlots;
    private int reservedSlots;
    private int availableSlots;
}
