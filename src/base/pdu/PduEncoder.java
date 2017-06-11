package base.pdu;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class PduEncoder extends MessageToByteEncoder<PduBase> {
    private static Logger logger = Logger.getLogger(PduEncoder.class);
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, PduBase rmPduBase, ByteBuf byteBuf) throws Exception {
        PduHeader header = rmPduBase.getHeader();
        logger.debug("正在编码消息，消息类型："+header.getCommandId());
        byteBuf.writeBytes(rmPduBase.getByteArray());
    }
}
