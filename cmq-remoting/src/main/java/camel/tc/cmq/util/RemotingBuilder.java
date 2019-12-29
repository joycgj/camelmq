package camel.tc.cmq.util;

import camel.tc.cmq.protocol.Datagram;
import camel.tc.cmq.protocol.PayloadHolder;
import camel.tc.cmq.protocol.RemotingCommandType;
import camel.tc.cmq.protocol.RemotingHeader;

// done
public class RemotingBuilder {

    public static Datagram buildRequestDatagram(short code, PayloadHolder payloadHolder) {
        final Datagram datagram = new Datagram();
        datagram.setHeader(buildRemotingHeader(code, RemotingCommandType.REQUEST_COMMAND.getCode(), 0));
        datagram.setPayloadHolder(payloadHolder);
        return datagram;
    }

    private static RemotingHeader buildRemotingHeader(short code, int flag, int opaque) {
        final RemotingHeader header = new RemotingHeader();
        header.setCode(code);
        header.setFlag(flag);
        header.setOpaque(opaque);
        header.setRequestCode(code);
        header.setVersion(RemotingHeader.VERSION_9);
        return header;
    }

    public static Datagram buildResponseDatagram(final short code, final RemotingHeader requestHeader, final PayloadHolder payloadHolder) {
        final Datagram datagram = new Datagram();
        datagram.setHeader(buildResponseHeader(code, requestHeader));
        datagram.setPayloadHolder(payloadHolder);
        return datagram;
    }

    public static Datagram buildEmptyResponseDatagram(final short code, final RemotingHeader requestHeader) {
        return buildResponseDatagram(code, requestHeader, null);
    }

    public static RemotingHeader buildResponseHeader(final short code, final RemotingHeader requestHeader) {
        return buildRemotingHeader(code, RemotingCommandType.RESPONSE_COMMAND.getCode(), requestHeader);
    }

    private static RemotingHeader buildRemotingHeader(final short code, final int flag, final RemotingHeader requestHeader) {
        final RemotingHeader header = new RemotingHeader();
        header.setCode(code);
        header.setFlag(flag);
        header.setOpaque(requestHeader.getOpaque());
        header.setVersion(requestHeader.getVersion());
        header.setRequestCode(requestHeader.getCode());
        return header;
    }
}
