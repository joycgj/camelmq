package camel.tc.cmq.metainfoclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import camel.tc.cmq.meta.BrokerCluster;
import camel.tc.cmq.meta.BrokerGroup;
import camel.tc.cmq.base.OnOfflineState;
import camel.tc.cmq.meta.BrokerState;
import camel.tc.cmq.protocol.CommandCode;
import camel.tc.cmq.protocol.Datagram;
import camel.tc.cmq.protocol.consumer.MetaInfoResponse;
import camel.tc.cmq.utils.PayloadHolderUtils;

import java.util.ArrayList;
import java.util.List;

// done
@ChannelHandler.Sharable
class MetaInfoClientHandler extends SimpleChannelInboundHandler<Datagram> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaInfoClientHandler.class);

    private final ConcurrentSet<MetaInfoClient.ResponseSubscriber> responseSubscribers = new ConcurrentSet<>();

    void registerResponseSubscriber(MetaInfoClient.ResponseSubscriber subscriber) {
        responseSubscribers.add(subscriber);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Datagram msg) {
        MetaInfoResponse response = null;
        if (msg.getHeader().getCode() == CommandCode.SUCCESS) {
            response = deserializeMetaInfoResponse(msg.getBody());
        }

        if (response != null) {
            notifySubscriber(response);
        } else {
            LOG.warn("request meta info UNKNOWN. code={}", msg.getHeader().getCode());
        }
    }

    private void notifySubscriber(MetaInfoResponse response) {
        for (MetaInfoClient.ResponseSubscriber subscriber : responseSubscribers) {
            try {
                subscriber.onResponse(response);
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
    }

    private static MetaInfoResponse deserializeMetaInfoResponse(ByteBuf buf) {
        try {
            final MetaInfoResponse metaInfoResponse = new MetaInfoResponse();
            metaInfoResponse.setTimestamp(buf.readLong());
            metaInfoResponse.setSubject(PayloadHolderUtils.readString(buf));
            metaInfoResponse.setConsumerGroup(PayloadHolderUtils.readString(buf));
            metaInfoResponse.setOnOfflineState(OnOfflineState.fromCode(buf.readByte()));
            metaInfoResponse.setClientTypeCode(buf.readByte());
            metaInfoResponse.setBrokerCluster(deserializeBrokerCluster(buf));
            return metaInfoResponse;
        } catch (Exception e) {
            LOG.error("deserializeMetaInfoResponse exception", e);
        }
        return null;
    }

    private static BrokerCluster deserializeBrokerCluster(ByteBuf buf) {
        final int brokerGroupSize = buf.readShort();
        final List<BrokerGroup> brokerGroups = new ArrayList<>(brokerGroupSize);
        for (int i = 0; i < brokerGroupSize; i++) {
            final BrokerGroup brokerGroup = new BrokerGroup();
            brokerGroup.setGroupName(PayloadHolderUtils.readString(buf));
            brokerGroup.setMaster(PayloadHolderUtils.readString(buf));
            brokerGroup.setUpdateTime(buf.readLong());
            final int brokerStateCode = buf.readByte();
            final BrokerState brokerState = BrokerState.codeOf(brokerStateCode);
            brokerGroup.setBrokerState(brokerState);
            brokerGroups.add(brokerGroup);
        }
        return new BrokerCluster(brokerGroups);
    }
}
