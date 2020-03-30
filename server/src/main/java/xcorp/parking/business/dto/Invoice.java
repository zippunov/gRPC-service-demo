package xcorp.parking.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Invoice {
    private String id;
    private Date startTime;
    private Date issuedTime;
    private Date endTime;
    private int sum;
}
