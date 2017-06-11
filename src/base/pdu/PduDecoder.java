package base.pdu;


import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 将字节流按照包的大小进行划分，并进行封装成PduBase包
 * 并通过netty处理管道传递给下一个管道处理程序
 * @see PduBase
 * Created by Administrator on 2016/12/8 0008.
 */
public class PduDecoder extends ByteToMessageDecoder {

    private static Logger logger = Logger.getLogger(PduDecoder.class);

    public PduDecoder(){

    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int bufferLen = byteBuf.readableBytes();
        logger.debug("处理客户端请求的......"+bufferLen+",rederIndex:"+byteBuf.readerIndex());
        while(byteBuf.readableBytes() >= RMCommons.PDU_HEADER_LEN){ //判定byteBuf中是否包含完整包头
            byteBuf.markReaderIndex(); // 标记起始位置
            bufferLen = byteBuf.readableBytes();
            int pduLen = byteBuf.readInt();
            logger.debug("====处理客户端请求的......"+bufferLen+",rederIndex:"+byteBuf.readerIndex()+","+pduLen);
            if(bufferLen < pduLen ){
                byteBuf.resetReaderIndex(); // 如果当前的bytebuf的大小没有包含一个完成的数据包，那么再重新读取
                return;
            }
            PduHeader header = new PduHeader();
            header.setLength(pduLen);
            fillHeader(header, byteBuf);
            logger.debug("客户端请求的类型："+header.getCommandId());

            PduBase pduBase = new PduBase();
            pduBase.setChannel(channelHandlerContext.channel());
            pduBase.setHeader(header);
            if((pduLen - RMCommons.PDU_HEADER_LEN) > 0) {
                fillBody(pduBase, byteBuf, pduLen - RMCommons.PDU_HEADER_LEN, channelHandlerContext.channel());
                list.add(pduBase);
            }else if((pduLen - RMCommons.PDU_HEADER_LEN) == 0){
                list.add(pduBase);
            }
        }
    }

    /**
     * 填充包头的数据
     * @param header
     * @param byteBuf
     */
    private void fillHeader(PduHeader header, ByteBuf byteBuf){
        header.setVersion(byteBuf.readShort());
        header.setFlag(byteBuf.readShort());
        header.setServiceId(byteBuf.readShort());
        header.setCommandId(byteBuf.readShort());
        header.setSeNum(byteBuf.readShort());
        header.setReserved(byteBuf.readShort());
    }

    /**
     * 从netty的字节流中读取包体的字节并加入到PduBase中
     * @param pduBase
     * @param byteBuf
     * @param bodyLen
     * @param channel
     */
    private void fillBody(PduBase pduBase, ByteBuf byteBuf, int bodyLen, Channel channel){
        ByteBuf bodyBuffer = byteBuf.readBytes(bodyLen); // 读取body到buffer
        byte bodyArray[];
        int offset = 0; // 暂时未用
        int bufferLen = bodyBuffer.readableBytes();
        bodyArray = new byte[bufferLen];
        bodyBuffer.readBytes(bodyArray, bodyBuffer.readerIndex(), bufferLen);
        // 填充pdu的body
        pduBase.setBodyArray(bodyArray);
        pduBase.setBodyLen(bufferLen);
        pduBase.setBodyOffset(offset);
    }
}
