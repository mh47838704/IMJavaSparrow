package loginserver.vo;

/**
 * Created by Administrator on 2017/5/15 0015.
 */
public class MsgServerInfo {
    private String ipAddr1; // 电信ip
    private String ipAddr2; // 网通ip
    private int port;
    private int maxConnCnt;
    private int curConnCnt;
    private String hostname;

    public String getIpAddr1() {
        return ipAddr1;
    }

    public void setIpAddr1(String ipAddr1) {
        this.ipAddr1 = ipAddr1;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpAddr2() {
        return ipAddr2;
    }

    public void setIpAddr2(String ipAddr2) {
        this.ipAddr2 = ipAddr2;
    }

    public int getMaxConnCnt() {
        return maxConnCnt;
    }

    public void setMaxConnCnt(int maxConnCnt) {
        this.maxConnCnt = maxConnCnt;
    }

    public int getCurConnCnt() {
        return curConnCnt;
    }

    public void setCurConnCnt(int curConnCnt) {
        this.curConnCnt = curConnCnt;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
