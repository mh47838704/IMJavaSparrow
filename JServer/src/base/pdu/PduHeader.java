package base.pdu;

/**
 * 包头的信息
 * Created by Administrator on 2016/12/8 0008.
 */
public class PduHeader implements Cloneable {

    private int length; // 整个包的长度（包头包体）
    private short version = 0; // 包的版本号
    private short flag = 0; //
    private short serviceId = 0;
    private short commandId = 0; // 包体类型
    private short seNum = 0; // 序列号
    private short reserved = 0;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public short getFlag() {
        return flag;
    }

    public void setFlag(short flag) {
        this.flag = flag;
    }

    public short getServiceId() {
        return serviceId;
    }

    public void setServiceId(short serviceId) {
        this.serviceId = serviceId;
    }

    public short getCommandId() {
        return commandId;
    }

    public void setCommandId(short commandId) {
        this.commandId = commandId;
    }

    public short getSeNum() {
        return seNum;
    }

    public void setSeNum(short seNum) {
        this.seNum = seNum;
    }

    public short getReserved() {
        return reserved;
    }

    public void setReserved(short reserved) {
        this.reserved = reserved;
    }

    @Override
    protected PduHeader clone() throws CloneNotSupportedException {
        return (PduHeader)super.clone();
    }
}
