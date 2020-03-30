package xcorp.parking.business.exceptions;

public class BusinessException extends Exception {

    private final ExceptionStatus status;

    public BusinessException(ExceptionStatus status, String msg, Throwable err) {
        super(msg, err);
        this.status = status;
    }

    public BusinessException(ExceptionStatus status, String msg) {
        super(msg);
        this.status = status;
    }

    public BusinessException(ExceptionStatus status, Throwable err) {
        super(err);
        this.status = status;
    }

    public BusinessException(ExceptionStatus status) {
        super();
        this.status = status;
    }

    public ExceptionStatus getStatus() {
        return status;
    }
}
