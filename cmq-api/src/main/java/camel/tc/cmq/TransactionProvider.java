package camel.tc.cmq;

// done
public interface TransactionProvider {

    boolean isInTransaction();

    void setTransactionListener(TransactionListener listener);

    MessageStore messageStore();
}
