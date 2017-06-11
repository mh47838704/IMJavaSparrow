package base.pdu;


import io.netty.channel.Channel;

import java.nio.ByteBuffer;

/**
 * 通信包在业务层的表示
 * Created by Administrator on 2016/12/8 0008.
 */
public class PduBase {
    private Channel channel; // 包来自的socket管道（引用）
    private PduHeader header; // 包的头部
    private byte[] bodyArray; // 包体的字节流
    private int bodyOffset;
    private int bodyLen = 0;

    public PduHeader getHeader() {
        return header;
    }

    public void setHeader(PduHeader header) {
        this.header = header;

    }

    public byte[] getBodyArray() {
        return bodyArray;
    }

    public void setBodyArray(byte[] bodyArray) {

        this.bodyArray = bodyArray;
    }

    public int getBodyOffset() {
        return bodyOffset;
    }

    public void setBodyOffset(int bodyOffset) {
        this.bodyOffset = bodyOffset;
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public void setBodyLen(int bodyLen) {
        this.bodyLen = bodyLen;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * 封装整个包到byte数组
     * @return
     */
    public byte[] getByteArray(){
        if((this.header != null)){
            // 计算整个包长度
            int bodyLen = 0;
            if(this.bodyArray != null){
                bodyLen = bodyArray.length;
            }
            int pduLen = RMCommons.PDU_HEADER_LEN + bodyLen;
            // 把包封装成为字节流
            ByteBuffer byteBuffer = ByteBuffer.allocate(pduLen);
            byteBuffer.putInt(header.getLength());
            byteBuffer.putShort(header.getVersion());
            byteBuffer.putShort(header.getFlag());
            byteBuffer.putShort(header.getServiceId());
            byteBuffer.putShort(header.getCommandId());
            byteBuffer.putShort(header.getSeNum());
            byteBuffer.putShort(header.getReserved());
            if(this.bodyArray != null){
                byteBuffer.put(bodyArray);
            }
            return byteBuffer.array();
        }
        return null;
    }
}
