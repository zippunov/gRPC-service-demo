package xcorp.parking.business.exceptions;

public class InternalExeption extends BusinessException{

    public InternalExeption(String msg, Throwable err) {
        super(ExceptionStatus.INTERNAL, msg, err);
    }

    public InternalExeption(String msg) {
        super(ExceptionStatus.INTERNAL, msg);
    }

    public InternalExeption(Throwable err) {
        super(ExceptionStatus.INTERNAL, err);
    }

    public InternalExeption() {
        super(ExceptionStatus.INTERNAL);
    }
}