package camel.tc.cmq.store.result;

// done
public final class Result<S, D> {

    private final S status;
    private final D data;

    public Result(final S status, final D data) {
        this.status = status;
        this.data = data;
    }

    public S getStatus() {
        return status;
    }

    public D getData() {
        return data;
    }
}