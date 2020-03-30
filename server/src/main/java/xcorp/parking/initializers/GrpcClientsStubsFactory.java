package xcorp.parking.initializers;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Property;
import lombok.extern.slf4j.Slf4j;
import xcorp.parking.api.ParkingServiceGrpc;

import javax.inject.Singleton;

@Slf4j
@Factory
public class GrpcClientsStubsFactory {
    @Property(name = "grpc.channels.parking.address")
    private String parkingAddress;


    @Singleton
    ParkingServiceGrpc.ParkingServiceFutureStub accountServiceFutureStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(parkingAddress)
                .usePlaintext()
                .build();
        log.info("Parking gRPC stub address {}", parkingAddress);
        return ParkingServiceGrpc.newFutureStub(channel);
    }
}
