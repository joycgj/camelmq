package camel.tc.cmq.protocol;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import camel.tc.cmq.base.BaseMessage;
import camel.tc.cmq.base.MessageHeader;
import camel.tc.cmq.base.RawMessage;
import camel.tc.cmq.protocol.producer.SendResult;
import camel.tc.cmq.utils.CharsetUtils;
import camel.tc.cmq.utils.Crc32;
import camel.tc.cmq.utils.Flags;
import camel.tc.cmq.utils.PayloadHolderUtils;

import java.nio.ByteBuffer;
import java.util.*;

// done
public class QMQSerializer {

    private static final int VALUE_SIZE_NEGATIVE_COMPENSATE = 1 << 16;

    public static RawMessage deserializeRawMessage(ByteBuf body) {
        int headerStart = body.readerIndex();
        body.markReaderIndex();
        MessageHeader header = deserializeMessageHeader(body);
        int bodyLen = body.readInt();
        int headerLen = body.readerIndex() - headerStart;

        int totalLen = headerLen + bodyLen;
        body.resetReaderIndex();
        byte[] data = new byte[totalLen];
        body.readBytes(data);
        header.setBodyCrc(Crc32.crc32(data));
        return new RawMessage(header, Unpooled.wrappedBuffer(data), totalLen);
    }

    public static MessageHeader deserializeMessageHeader(ByteBuf body) {
        byte flag = body.readByte();
        long createdTime = body.readLong();
        long expiredTime = body.readLong();
        String subject = PayloadHolderUtils.readString(body);
        String messageId = PayloadHolderUtils.readString(body);
        MessageHeader header = new MessageHeader();
        if (Flags.hasTags(flag)) {
            final Set<String> tags = new HashSet<>();
            final byte tagsSize = body.readByte();
            for (int i = 0; i < tagsSize; i++) {
                String tag = PayloadHolderUtils.readString(body);
                tags.add(tag);
            }
            header.setTags(tags);
        }

        header.setFlag(flag);
        header.setCreateTime(createdTime);
        header.setExpireTime(expiredTime);
        header.setSubject(subject);
        header.setMessageId(messageId);
        return header;
    }

    public static Map<String, SendResult> deserializeSendResultMap(ByteBuf buf) {
        Map<String, SendResult> result = Maps.newHashMap();
        while (buf.isReadable()) {
            String messageId = PayloadHolderUtils.readString(buf);
            int code = buf.readInt();
            String remark = PayloadHolderUtils.readString(buf);
            result.put(messageId, new SendResult(code, remark));
        }
        return result;
    }


    public static HashMap<String, Object> deserializeMap(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) return null;

        HashMap<String, Object> map = new HashMap<>();
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        try {
            while (byteBuffer.hasRemaining()) {
                short keySize = byteBuffer.getShort();
                byte[] keyBs = new byte[keySize];
                byteBuffer.get(keyBs);

                int valSize = byteBuffer.getShort();
                if (valSize < 0) {
                    valSize += VALUE_SIZE_NEGATIVE_COMPENSATE;
                }
                byte[] valBs = new byte[valSize];
                byteBuffer.get(valBs);
                map.put(CharsetUtils.toUTF8String(keyBs), CharsetUtils.toUTF8String(valBs));
            }
            return map;
        } catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put(BaseMessage.keys.qmq_corruptData.name(), "true");
            result.put(BaseMessage.keys.qmq_createTime.name(), new Date().getTime());
            return result;
        }
    }
}
