package camel.tc.cmq.store;

// done
public final class MagicCode {

    public static final short CONSUMER_LOG_MAGIC_V2 = (short) 0xA3B2;

    public static final int MESSAGE_LOG_MAGIC_V1 = 0xA1B2C300;

    // add crc field for message body
    public static final int MESSAGE_LOG_MAGIC_V2 = 0xA1B2C301;

    //add tags field for message header
    public static final int MESSAGE_LOG_MAGIC_V3 = 0xA1B2C302;

    public static final int PULL_LOG_MAGIC_V1 = 0xC1B2A300;

    public static final int ACTION_LOG_MAGIC_V1 = 0xC3B2A100;

    public static final int SORTED_MESSAGES_TABLE_MAGIC_V1 = 0xA2B100;
}
