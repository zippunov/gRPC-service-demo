package xcorp.parking.business.exceptions;

public class NoFreeSlotsException extends BusinessException {

    public NoFreeSlotsException(String msg, Throwable err) {
        super(ExceptionStatus.NO_FREE_SLOTS, msg, err);
    }

    public NoFreeSlotsException(String msg) {
        super(ExceptionStatus.NO_FREE_SLOTS, msg);
    }

    public NoFreeSlotsException(Throwable err) {
        super(ExceptionStatus.NO_FREE_SLOTS, err);
    }

    public NoFreeSlotsException() {
        super(ExceptionStatus.NO_FREE_SLOTS);
    }
}
