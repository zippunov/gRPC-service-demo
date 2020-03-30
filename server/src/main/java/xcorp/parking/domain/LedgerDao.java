package xcorp.parking.domain;

import reactor.core.publisher.Mono;
import xcorp.parking.domain.entity.Ledger;
import xcorp.parking.domain.factory.CassandraLedgerDao;

public class LedgerDao {
    private final CassandraLedgerDao ledgerDao;

    public LedgerDao(CassandraLedgerDao ledgerDao) {
        this.ledgerDao = ledgerDao;
    }

    public Mono<Void> save(Ledger ledger) {
        return Mono.fromCompletionStage(ledgerDao.update(ledger));
    }

    public Mono<Ledger> find(String dateCode, String id) {
        return Mono.fromCompletionStage(ledgerDao.geRecord(dateCode, id));
    }
}
