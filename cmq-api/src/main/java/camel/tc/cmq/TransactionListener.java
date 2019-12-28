package camel.tc.cmq;

// done
public interface TransactionListener {

    void beginTransaction(MessageStore store);

    void addMessage(ProduceMessage message);

    void beforeCommit();

    void afterCommit();

    void afterCompletion();

    void suspend();

    void resume();
}
