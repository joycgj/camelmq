package camel.tc.cmq.store;

// done
public interface Visitable<T> {

    AbstractLogVisitor<T> newVisitor(long iterateFrom);

    long getMinOffset();

    long getMaxOffset();
}