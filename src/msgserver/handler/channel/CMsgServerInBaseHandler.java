package msgserver.handler.channel;


import base.pdu.PduBase;
import base.pdu.PduHandler;
import base.pdu.PduHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import msgserver.global.MsgGlobal;
import org.apache.log4j.Logger;

/**
 * 消息接入的handler
 * Created by Administrator on 2016/12/8 0008.
 */
public class CMsgServerInBaseHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger(CMsgServerInBaseHandler.class);

    public CMsgServerInBaseHandler(){
        // 初始化HandlerMap

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // msg是经过PduDecoder从字节流中封装成的PduBase包
        PduBase pduBase = (PduBase) msg;
        PduHeader header = pduBase.getHeader();
        PduHandler handler = MsgGlobal.handlerMap.get((int)header.getCommandId());
        if(handler != null){
            handler.handlePdu(pduBase);
        }else {
            logger.error("没有找到合适的handler");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
