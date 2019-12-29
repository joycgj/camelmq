package camel.tc.cmq.protocol;

// done
public interface CommandCode {
    short PLACEHOLDER = -1;

    // response code
    short SUCCESS = 0;
    short UNKNOWN_CODE = 3;
    short NO_MESSAGE = 4;

    short BROKER_ERROR = 51;
    short BROKER_REJECT = 52;
    short PARAM_ERROR = 53;

    // request code
    short SEND_MESSAGE = 10;
    short PULL_MESSAGE = 11;
    short ACK_REQUEST = 12;

    short SYNC_LOG_REQUEST = 20;
    short SYNC_CHECKPOINT_REQUEST = 21;

    short QUERY_CONSUMER_LAG = 24;
    short CONSUME_MANAGE = 25;
    short QUEUE_COUNT = 26;
    short ACTION_OFFSET_REQUEST = 27;

    short BROKER_REGISTER = 30;
    short CLIENT_REGISTER = 35;

    short BROKER_ACQUIRE_META = 40;

    short UID_ASSIGN = 50;
    short UID_ACQUIRE = 51;

    // heartbeat
    short HEARTBEAT = 100;
}
