package camel.tc.cmq.store;

import java.text.NumberFormat;

// done
final class StoreUtils {

    static String offset2FileName(final long offset) {
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(20);
        nf.setMaximumFractionDigits(0);
        nf.setGroupingUsed(false);
        return nf.format(offset);
    }

    static String offsetFileNameForSegment(final LogSegment segment) {
        return offsetFileNameOf(segment.getBaseOffset());
    }

    static String offsetFileNameOf(final long baseOffset) {
        return "." + StoreUtils.offset2FileName(baseOffset) + ".offset";
    }
}
