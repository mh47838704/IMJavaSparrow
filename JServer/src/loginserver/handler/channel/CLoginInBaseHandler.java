package loginserver.handler.channel;

import base.pdu.PduBase;
import base.pdu.PduHandler;
import base.pdu.PduHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * 自定义的netty管道的handler
 * 初始化业务的handler，并根据包体的类型选择相应的handler进行处理
 * Created by Administrator on 2016/12/8 0008.
 */
public class CLoginInBaseHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger(CLoginInBaseHandler.class);
    private HashMap<Integer, PduHandler> handlerMap;

    public CLoginInBaseHandler(HashMap<Integer, PduHandler> handlerMap){
        // 初始化HandlerMap
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 根据commandID找到handler，并处理pdu
        PduBase pduBase = (PduBase) msg;
        PduHeader header = pduBase.getHeader();
        PduHandler handler = handlerMap.get((int)header.getCommandId());
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
