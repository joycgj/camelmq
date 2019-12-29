package camel.tc.cmq.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import camel.tc.cmq.protocol.RemotingCommand;
import camel.tc.cmq.protocol.RemotingHeader;

import java.io.IOException;
import java.util.List;

import static camel.tc.cmq.protocol.RemotingHeader.DEFAULT_MAGIC_CODE;

// done
public class DecodeHandler extends ByteToMessageDecoder {

    private final boolean isServer;

    public DecodeHandler(boolean isServer) {
        this.isServer = isServer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        if (in.readableBytes() < RemotingHeader.MIN_HEADER_SIZE + RemotingHeader.LENGTH_FIELD) return;

        int magicCode = in.getInt(in.readerIndex() + RemotingHeader.LENGTH_FIELD);
        if (DEFAULT_MAGIC_CODE != magicCode) {
            throw new IOException("Illegal Data, MagicCode=" + Integer.toHexString(magicCode));
        }

        in.markReaderIndex();
        int total = in.readInt();
        if (in.readableBytes() < total) {
            in.resetReaderIndex();
            return;
        }

        short headerSize = in.readShort();
        RemotingHeader remotingHeader = decodeHeader(in);

        int bodyLength = total - headerSize - RemotingHeader.HEADER_SIZE_LEN;

        RemotingCommand remotingCommand = new RemotingCommand();
        //because netty(lower version) has memory leak when ByteBuf cross thread
        //We can ensure server use high version netty, bu we can't ensure client
        if (isServer) {
            ByteBuf bodyData = in.readSlice(bodyLength);
            bodyData.retain();
            remotingCommand.setBody(bodyData);
        } else {
            ByteBuf bodyData = Unpooled.buffer(bodyLength, bodyLength);
            in.readBytes(bodyData, bodyLength);
            remotingCommand.setBody(bodyData);
        }
        remotingCommand.setHeader(remotingHeader);
        list.add(remotingCommand);
    }

    private RemotingHeader decodeHeader(ByteBuf in) {
        RemotingHeader remotingHeader = new RemotingHeader();
        // int magicCode(4 bytes)
        remotingHeader.setMagicCode(in.readInt());
        // short code
        remotingHeader.setCode(in.readShort());
        // short version
        remotingHeader.setVersion(in.readShort());
        // int opaque
        remotingHeader.setOpaque(in.readInt());
        // int flag
        remotingHeader.setFlag(in.readInt());
        // int requestCode
        remotingHeader.setRequestCode(in.readShort());
        return remotingHeader;
    }
}
