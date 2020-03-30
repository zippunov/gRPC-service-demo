package xcorp.parking.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookingResult {
    boolean success;
    Date startTime;
    String  reason;
}
