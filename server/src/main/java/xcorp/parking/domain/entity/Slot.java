package xcorp.parking.domain.entity;

import com.datastax.oss.driver.api.mapper.annotations.*;
import lombok.Data;

import java.util.Date;

import static com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention.SNAKE_CASE_INSENSITIVE;

@Data
@Entity
@CqlName("slot")
@NamingStrategy(convention = SNAKE_CASE_INSENSITIVE)
public class Slot {
    @PartitionKey
    private String plate;
    private Date booked;
    private String ledgerId;
}
