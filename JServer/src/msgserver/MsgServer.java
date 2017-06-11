package msgserver;

import base.config.ConfigReader;
import base.config.ConfigUtil;
import base.pdu.PduDecoder;
import base.pdu.PduEncoder;
import base.pdu.PduHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import msgserver.global.MsgGlobal;
import msgserver.handler.channel.CMsgServerInBaseHandler;
import msgserver.handler.channel.CMsgServerOutBaseHandler;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/15 0015.
 */
public class MsgServer {

    private static Logger logger = Logger.getLogger(MsgServer.class);

    public MsgServer(){
        // 完成服务器的初始化工作
        initHandlerMap();
        initLog();
    }

    public void bind(int clientListenPort) throws Exception {
        logger.debug("消息服务器：启动客户端监听，端口号："+clientListenPort);
        // 参考netty主从多线程模型
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            logger.debug("有新的客户端连接进来");
                            ch.pipeline().addLast(new PduDecoder());
                            ch.pipeline().addLast(new PduEncoder());
                            ch.pipeline().addLast(new CMsgServerInBaseHandler());
                            ch.pipeline().addLast(new CMsgServerOutBaseHandler());
                        }
                    });
            ChannelFuture f = bootstrap.bind(clientListenPort).sync();
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 初始化handlerMap
     * 不同的通信数据报文对应不同的handler
     * @return
     */
    private HashMap<Integer, PduHandler> initHandlerMap(){
        return  MsgGlobal.handlerMap;
    }

    /**
     * 初始化log4j日志配置
     */
    private void initLog(){
        PropertyConfigurator.configure("config/log4j.properties");
    }

    public static void main(String[] args) throws Exception {
        // 客户端监听端口，配置文件读取（默认10011）
        int clientListenPort = 10011;
        // 解析配置文件
        String fileName = "MsgServer.properties";
        String relativePath = "\\src\\msgserver\\";
        String fullPath = ConfigUtil.getAbsolateConfPath(relativePath, fileName);
        ConfigReader configReader = new ConfigReader(fullPath);
        // 获取配置文件中的端口号
        clientListenPort = configReader.getInt("ListenPort");
        MsgServer server = new MsgServer();
        server.bind(clientListenPort);
    }
}
