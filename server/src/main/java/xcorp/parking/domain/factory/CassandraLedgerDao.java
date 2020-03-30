package xcorp.parking.domain.factory;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.Update;
import xcorp.parking.domain.entity.Ledger;

import java.util.concurrent.CompletionStage;

@Dao
public interface CassandraLedgerDao {
    @Update
    CompletionStage<Void> update(Ledger entity);

    @Query("SELECT * FROM ledger WHERE day_code = :dateCode AND id = :id LIMIT 1")
    CompletionStage<Ledger> geRecord(String dateCode, String id);
}
