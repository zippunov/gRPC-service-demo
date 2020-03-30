package xcorp.parking.domain.factory;

import com.datastax.oss.driver.api.mapper.annotations.*;
import xcorp.parking.domain.entity.Slot;

import java.util.concurrent.CompletionStage;

@Dao
public interface CassandraSlotDao {
    @Select
    CompletionStage<Slot> findById(String plate);

    @Update
    CompletionStage<Void> update(Slot entity);

    @Query("SELECT COUNT(*) FROM slot")
    CompletionStage<Long> slotsBooked();

    @Delete(entityClass = Slot.class)
    CompletionStage<Void> deleteById(String plate);
}
