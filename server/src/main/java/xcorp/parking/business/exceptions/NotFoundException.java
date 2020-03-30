package xcorp.parking.business.exceptions;

public class NotFoundException extends BusinessException {

    public NotFoundException(String msg, Throwable err) {
        super(ExceptionStatus.NOT_FOUND, msg, err);
    }

    public NotFoundException(String msg) {
        super(ExceptionStatus.NOT_FOUND, msg);
    }

    public NotFoundException(Throwable err) {
        super(ExceptionStatus.NOT_FOUND, err);
    }

    public NotFoundException() {
        super(ExceptionStatus.NOT_FOUND);
    }
}
