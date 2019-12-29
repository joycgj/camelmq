package camel.tc.cmq.netty.exception;

// done
public class RemoteException extends Exception {

    private static final long serialVersionUID = -7144986221917657039L;

    public RemoteException() {
    }

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }
}
