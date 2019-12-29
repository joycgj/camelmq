package camel.tc.cmq.utils;

import camel.tc.cmq.Message;

import java.util.Date;

// done
public class DelayUtil {

    private static final long MIN_DELAY_TIME = 500;

    public static boolean isDelayMessage(Message message) {
        Date receiveTime = message.getScheduleReceiveTime();
        return receiveTime != null && (receiveTime.getTime() - System.currentTimeMillis()) >= MIN_DELAY_TIME;
    }
}
