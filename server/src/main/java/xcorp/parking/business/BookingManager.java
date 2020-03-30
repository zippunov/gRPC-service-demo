package xcorp.parking.business;

import reactor.core.publisher.Mono;
import xcorp.parking.business.dto.BookingCount;
import xcorp.parking.business.dto.BookingResult;
import xcorp.parking.business.dto.FreeSlot;
import xcorp.parking.business.dto.Invoice;
import xcorp.parking.business.exceptions.AlreadyExistsException;
import xcorp.parking.business.exceptions.NoFreeSlotsException;
import xcorp.parking.business.exceptions.NotFoundException;
import xcorp.parking.domain.LedgerDao;
import xcorp.parking.domain.SlotDao;
import xcorp.parking.domain.entity.Ledger;
import xcorp.parking.domain.entity.Slot;
import xcorp.parking.util.SequenceGenerator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class BookingManager {

    private static final int TOTAL_SLOTS = 100;

    @Inject
    LedgerDao ledgerDao;

    @Inject
    SlotDao slotDao;

    @Inject @Named("ledgerIdSeq")
    SequenceGenerator sequenceGenerator;

    public Mono<BookingCount> getBookingCount() {
        return slotDao
                .getBookedCount()
                .map(taken -> {
                    BookingCount rslt = new BookingCount();
                    rslt.setTotalSlots(TOTAL_SLOTS);
                    rslt.setTakenSlots(taken.intValue());
                    rslt.setReservedSlots(0);
                    rslt.setAvailableSlots(TOTAL_SLOTS - taken.intValue());
                    return rslt;
                });
    }

    public Mono<BookingResult> takeSlot(String plate) {
        BookingResult rslt = new BookingResult();
        rslt.setStartTime(new Date());
        rslt.setSuccess(true);
        rslt.setReason("");
        return ensureCapacity()
                .then(ensureNoDoubleBooking(plate))
                .then(createLedger(plate))
                .flatMap(ledger -> slotDao.bookSlot(plate, ledger.getId()))
                .then(Mono.just(rslt));
    }

    public Mono<FreeSlot> freeSlot(String plate) {
        FreeSlot fs = new FreeSlot();
        fs.setPlate(plate);
        Invoice inv = new Invoice();
        fs.setInvoice(inv);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        slotDao.getSlot(plate)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(slot -> {
                    String dayCode = sdf.format(slot.getBooked());
                    return ledgerDao.find(dayCode, slot.getLedgerId());
                })
                .flatMap(ledger -> {
                    ledger.setReleased(new Date());
                    inv.setId(ledger.getId());
                    inv.setIssuedTime(ledger.getBooked());
                    inv.setStartTime(ledger.getBooked());
                    inv.setSum(ledger.getTotal());
                    inv.setEndTime(ledger.getReleased());
                    return ledgerDao.save(ledger);
                })
                .then(slotDao.releaseSlot(plate))
                .then(Mono.just(fs));


        return Mono.empty();
    }

    private Mono<Void> ensureCapacity() {
        return slotDao
                .getBookedCount()
                .flatMap(taken -> {
                    if (taken >= TOTAL_SLOTS) {
                        return  Mono.error(new NoFreeSlotsException());
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> ensureNoDoubleBooking(String plate) {
        return Mono.just(plate)
                .flatMap(slotDao::getSlot)
                .switchIfEmpty(Mono.just(new Slot()))
                .flatMap(slot -> {
                    if(slot.getPlate().equals(plate)) {
                        return  Mono.error(new AlreadyExistsException());
                    }
                    return Mono.empty();
                });
    }

    private Mono<Ledger> createLedger(String plate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date now = new Date();
        String dayCode = sdf.format(now);
        Ledger rslt = new Ledger();
        rslt.setId(sequenceGenerator.nextStringId());
        rslt.setDayCode(dayCode);
        rslt.setBooked(now);
        rslt.setPlate(plate);
        rslt.setMinuteRate(0);
        rslt.setTotal(200);
        return ledgerDao.save(rslt)
                .then(Mono.just(rslt));
    }
}
