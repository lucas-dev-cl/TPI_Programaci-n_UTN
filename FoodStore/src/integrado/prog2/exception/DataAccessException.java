package integrado.prog2.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public DataAccessException(String mensaje) {
        super(mensaje);
    }
}
