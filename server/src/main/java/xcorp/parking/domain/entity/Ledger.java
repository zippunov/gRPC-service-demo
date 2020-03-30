package xcorp.parking.domain.entity;

import com.datastax.oss.driver.api.mapper.annotations.*;
import lombok.Data;

import java.util.Date;

import static com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention.SNAKE_CASE_INSENSITIVE;

@Data
@Entity
@CqlName("ledger")
@NamingStrategy(convention = SNAKE_CASE_INSENSITIVE)
public class Ledger {
    @PartitionKey
    private String dayCode;
    @ClusteringColumn(1)
    private String id;
    private String plate;
    private Date booked;
    private Date released;
    private Integer minuteRate;
    private Integer total;
}
