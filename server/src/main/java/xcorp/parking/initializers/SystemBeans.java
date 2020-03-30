package xcorp.parking.initializers;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import xcorp.parking.util.SequenceGenerator;

import javax.inject.Named;

@Factory
public class SystemBeans {
    @Bean
    @Named("ledgerIdSeq")
    SequenceGenerator sequenceGenerator() {
        return new SequenceGenerator();
    }
}
