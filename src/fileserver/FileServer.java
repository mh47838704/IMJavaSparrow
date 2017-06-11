package fileserver;

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
import loginserver.LoginServer;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/15 0015.
 */
public class FileServer {
    private HashMap<Integer, PduHandler> handlerHashMap = new HashMap<>();
    private static Logger logger = Logger.getLogger(LoginServer.class);
    public FileServer(){
        initHandlerMap(this.handlerHashMap);
        initLog();
    }
    public void bind(int clientListenPort) throws Exception {
        logger.debug("负载均衡服务器：启动客户端监听，端口号："+clientListenPort);
        // 配置服务端的NIO线程组
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
                            // 注册相应的handler和encoder和decoder
                            ch.pipeline().addLast(new PduDecoder());
                            ch.pipeline().addLast(new PduEncoder());
                            // 添加file相关的handler
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture f = bootstrap.bind(clientListenPort).sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 初始化业务handlerMap
     * @return
     */
    private HashMap<Integer, PduHandler> initHandlerMap(HashMap<Integer, PduHandler> handlerHashMap){

        return handlerHashMap;
    }

    private void initLog(){
        PropertyConfigurator.configure("config/log4j.properties");
    }

    public static void main(String[] args) throws Exception {
        // 用于监听客户端的端口，从配置文件中读取（10010是默认值）
        int clientListenPort = 10010;
        // 配置文件名字
        String fileName = "FileServer.properties";
        /* 解析配置文件 */
        String relativePath = "\\src\\fileserver\\";
        String fullPath = ConfigUtil.getAbsolateConfPath(relativePath, fileName);
        ConfigReader configReader = new ConfigReader(fullPath);
        // 获取配置文件中的端口号
        clientListenPort = configReader.getInt("ClientListenPort");
        /* 初始化LoginServer服务器*/
        LoginServer server = new LoginServer();
        server.bind(clientListenPort);
    }
}
