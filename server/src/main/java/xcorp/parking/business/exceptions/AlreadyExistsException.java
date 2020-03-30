package xcorp.parking.business.exceptions;

public class AlreadyExistsException extends BusinessException{

    public AlreadyExistsException(String msg, Throwable err) {
        super(ExceptionStatus.ALREADY_EXISTS, msg, err);
    }

    public AlreadyExistsException(String msg) {
        super(ExceptionStatus.ALREADY_EXISTS, msg);
    }

    public AlreadyExistsException(Throwable err) {
        super(ExceptionStatus.ALREADY_EXISTS, err);
    }

    public AlreadyExistsException() {
        super(ExceptionStatus.ALREADY_EXISTS);
    }
}