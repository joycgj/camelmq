package camel.tc.cmq.store;

import camel.tc.cmq.monitor.QMon;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

// done
abstract class ReferenceObject {

    private static final AtomicIntegerFieldUpdater<ReferenceObject> REF_CNT_UPDATER;

    static {
        AtomicIntegerFieldUpdater<ReferenceObject> updater = AtomicIntegerFieldUpdater.newUpdater(ReferenceObject.class, "refCnt");
        REF_CNT_UPDATER = updater;
    }

    private volatile int refCnt = 1;

    public boolean retain() {
        for (; ; ) {
            final int refCnt = this.refCnt;
            if (refCnt < 1) {
                return false;
            }

            if (REF_CNT_UPDATER.compareAndSet(this, refCnt, refCnt + 1)) {
                QMon.logSegmentTotalRefCountInc();
                return true;
            }
        }
    }

    public boolean release() {
        for (; ; ) {
            final int refCnt = this.refCnt;
            if (refCnt < 1) {
                return true;
            }

            if (REF_CNT_UPDATER.compareAndSet(this, refCnt, refCnt - 1)) {
                QMon.logSegmentTotalRefCountDec();
                return true;
            }
        }
    }

    public boolean disable() {
        final int refCnt = this.refCnt;
        if (refCnt < 1) {
            return true;
        }
        if (refCnt > 1) {
            return false;
        }

        return REF_CNT_UPDATER.compareAndSet(this, refCnt, refCnt - 1);
    }
}
