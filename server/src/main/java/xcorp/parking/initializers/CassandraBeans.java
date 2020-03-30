package xcorp.parking.initializers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.ProgrammaticDriverConfigLoaderBuilder;
import com.datastax.oss.driver.internal.core.auth.PlainTextAuthProvider;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Property;
import xcorp.parking.domain.LedgerDao;
import xcorp.parking.domain.SlotDao;
import xcorp.parking.domain.factory.EntityMapper;

import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static io.micronaut.core.util.StringUtils.isNotEmpty;

@Factory
public class CassandraBeans {

    @Property(name = "cassandra.contact-point")
    private List<String> contactPoints;

    @Property(name = "cassandra.keyspace")
    private String keyspace;

    @Property(name = "cassandra.datacenter")
    private String datacenter;

    @Property(name = "cassandra.username")
    private String username;

    @Property(name = "cassandra.password")
    private String password;

    @Singleton
    CqlSession cqlSession() {
        ProgrammaticDriverConfigLoaderBuilder configBuilder = DriverConfigLoader.programmaticBuilder();
        configBuilder.withString(DefaultDriverOption.REQUEST_CONSISTENCY, "LOCAL_QUORUM");
        if (isNotEmpty(username) && isNotEmpty(password)) {
            configBuilder = configBuilder
                    .withString(DefaultDriverOption.AUTH_PROVIDER_CLASS, PlainTextAuthProvider.class.getCanonicalName())
                    .withString(DefaultDriverOption.AUTH_PROVIDER_USER_NAME, username)
                    .withString(DefaultDriverOption.AUTH_PROVIDER_PASSWORD, password);
        }
        DriverConfigLoader loader = configBuilder.build();

        CqlSessionBuilder builder =  CqlSession.builder()
                .withConfigLoader(loader)
                .withKeyspace(keyspace)
                .withLocalDatacenter(datacenter);
        contactPoints.stream()
                .map(this::string2socketAddr)
                .forEach(opt -> opt.ifPresent(builder::addContactPoint));
        return builder.build();
    }

    @Singleton
    EntityMapper entityMapper(CqlSession cqlSession) {
        return EntityMapper.builder(cqlSession).build();
    }

    @Singleton
    SlotDao slotDao(EntityMapper mapper) {
        return new SlotDao(mapper.cassandraSlotDao());
    }

    @Singleton
    LedgerDao ledgerDao(EntityMapper mapper) {
        return new LedgerDao(mapper.cassandraLedgerDao());
    }

    private Optional<InetSocketAddress> string2socketAddr(String addr) {
        try {
            URI uri = new URI("my://" + addr);
            String host = uri.getHost();
            int port = uri.getPort() == -1 ? 9042 : uri.getPort();
            if (uri.getHost() == null) {
                throw new URISyntaxException(uri.toString(), "URI " + addr + "must have a host part");
            }
            return Optional.of(new InetSocketAddress (host, port));
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }
}
