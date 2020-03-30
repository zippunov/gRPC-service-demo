package xcorp.parking.domain;

import reactor.core.publisher.Mono;
import xcorp.parking.domain.entity.Slot;
import xcorp.parking.domain.factory.CassandraSlotDao;

import java.util.Date;

public class SlotDao {

    private final CassandraSlotDao slotDao;

    public SlotDao(CassandraSlotDao slotDao) {
        this.slotDao = slotDao;
    }

    public Mono<Slot> getSlot(String plate) {
        return Mono.fromCompletionStage(slotDao.findById(plate));
    }

    public Mono<Void> bookSlot(String plate, String ledgerId) {
        Slot s = new Slot();
        s.setPlate(plate);
        s.setBooked(new Date());
        s.setLedgerId(ledgerId);
        return Mono.fromCompletionStage(slotDao.update(s));
    }

    public Mono<Void> releaseSlot(String plate) {
        return Mono.fromCompletionStage(slotDao.deleteById(plate));
    }

    public Mono<Long> getBookedCount() {
        return  Mono.fromCompletionStage(slotDao.slotsBooked());
    }
}
