package xcorp.parking.domain.factory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperBuilder;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface EntityMapper {

    static MapperBuilder<EntityMapper> builder(CqlSession session) {
        return new EntityMapperBuilder(session);
    }

    @DaoFactory
    CassandraSlotDao cassandraSlotDao();

    @DaoFactory
    CassandraLedgerDao cassandraLedgerDao();
}
