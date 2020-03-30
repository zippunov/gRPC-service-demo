package xcorp.parking.api.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import xcorp.billing.BillingPlan;
import xcorp.billing.Invoice;
import xcorp.parking.api.*;
import xcorp.parking.business.BookingManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ParkingServerEndpoint extends ParkingServiceGrpc.ParkingServiceImplBase {

    @Inject
    BookingManager bookingManager;

    @Override
    public void currentCapacity(Empty request, StreamObserver<CapacityResponse> responseObserver) {
        bookingManager.getBookingCount().subscribe(
                // on data
                dto ->  {
                    CapacityResponse result = CapacityResponse
                            .newBuilder()
                            .setTotalSlots(dto.getTotalSlots())
                            .setTakenSlots(dto.getTakenSlots())
                            .setReservedSlots(dto.getReservedSlots())
                            .setAvailableSlots(dto.getAvailableSlots())
                            .build();
                    responseObserver.onNext(result);
                    responseObserver.onCompleted();
                },
                // On error
                err -> {
                    responseObserver.onError(err);
                }
        );
    }

    @Override
    public void takeSlot(PlateRequest request, StreamObserver<TakeSlotResponse> responseObserver) {
        bookingManager.takeSlot(request.getPlate()).subscribe(
                // on data
                dto ->  {
                    TakeSlotResponse result = TakeSlotResponse
                            .newBuilder()
                            .setCode(true)
                            .setReason(dto.getReason())
                            .setStartTime(dto.getStartTime().getTime())
                            .setSuccess(dto.isSuccess())
                            .build();
                    responseObserver.onNext(result);
                    responseObserver.onCompleted();
                },
                // On error
                err -> {
                    responseObserver.onError(err);
                }
        );
    }

    @Override
    public void currentBilling(PlateRequest request, StreamObserver<CurrentBillingResponse> responseObserver) {
        responseObserver.onError(new NotImplementedException());
    }

    @Override
    public void freeSlot(PlateRequest request, StreamObserver<FreeSlotResponse> responseObserver) {
        bookingManager.freeSlot(request.getPlate()).subscribe(
                // on data
                dto ->  {
                    Invoice inv = Invoice
                            .newBuilder()
                            .setId(dto.getInvoice().getId())
                            .setIssuedTime(dto.getInvoice().getIssuedTime().getTime())
                            .setStartTime(dto.getInvoice().getStartTime().getTime())
                            .setEndTime(dto.getInvoice().getEndTime().getTime())
                            .setBilledTime(0)
                            .setPlan(BillingPlan.UNRECOGNIZED)
                            .setSum(dto.getInvoice().getSum())
                            .setVat(0)
                            .build();
                    FreeSlotResponse result = FreeSlotResponse
                            .newBuilder()
                            .setPlate(dto.getPlate())
                            .setInvoice(inv)
                            .build();
                    responseObserver.onNext(result);
                    responseObserver.onCompleted();
                },
                // On error
                err -> {
                    responseObserver.onError(err);
                }
        );
    }
}
