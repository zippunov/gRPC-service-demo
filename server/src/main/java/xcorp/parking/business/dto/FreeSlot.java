package xcorp.parking.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FreeSlot {
    String plate;
    private Invoice invoice;
}