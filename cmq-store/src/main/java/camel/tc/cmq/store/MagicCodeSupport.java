package camel.tc.cmq.store;

// done
public final class MagicCodeSupport {

    private MagicCodeSupport() {
    }

    public static boolean isValidMessageLogMagicCode(final int magicCode) {
        return magicCode == MagicCode.MESSAGE_LOG_MAGIC_V1
                || magicCode == MagicCode.MESSAGE_LOG_MAGIC_V2 || magicCode == MagicCode.MESSAGE_LOG_MAGIC_V3;
    }
}
