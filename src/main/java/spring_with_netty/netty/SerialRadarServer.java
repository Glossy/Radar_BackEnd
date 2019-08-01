package spring_with_netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import spring_with_netty.netty.handler.RadarSerialHandler;

import gnu.io.*;
import org.springframework.util.StringUtils;
import sun.jvm.hotspot.runtime.Bytes;

import java.io.*;
import java.util.*;


/**
 * @Author: Wu
 * @Date: 2019/7/24 3:21 PM
 */
public class SerialRadarServer implements Runnable{
    public static Logger logger = Logger.getLogger("SerialRadarServer");
    private Thread t;
    private String threadName = "SerialRadarServer-Thread";
    private int uploadBuadRate = 115200;  //上传波特率115200
    private int downloadBuadRate = 921600; //接收数据波特率921600
    private String filePath = "./radar.cfg";

    private volatile static int packsNum = 0;
    public static boolean isConnected = false;


    public void setThreadName(String name){
        this.threadName = name;
    }

    public static void resetPacksNum(){
        packsNum = 0;
    }

    public static void addPacksNum(){
        packsNum++;
    }

    public static int getPacksNum(){
        return packsNum;
    }

    public void run(){

        Thread uploadThread = new Thread(()->{
            //Gets an instance
            RadarSerialHandler uploadPort = new RadarSerialHandler();
            //Gets all ports
            ArrayList<String> ports = uploadPort.listSerialPorts();
            //Prints the ports
            ports.forEach(s-> System.out.println(s));
            try {
                //Opens the port by name
                System.out.println("Input Upload Serial Port Name:");
                Scanner sc = new Scanner(System.in);
                String port = sc.nextLine();
                uploadPort.openPort(port, uploadBuadRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                File cfgFile = new File(filePath);
                uploadPort.sendFileToComPort(cfgFile);
//                uploadPort.openPort("/dev/cu.Bluetooth-Incoming-Port");
//                1.初始化串口 2.设置输入输出流以及监听器 3.上传文件 4.接收数据
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
        Thread downloadThread = new Thread(()->{
            RadarSerialHandler downloadPort = new RadarSerialHandler();
            ArrayList<String> ports = downloadPort.listSerialPorts();
            ports.forEach(s-> System.out.println(s));
            try {
                System.out.println("Input Download Serial Port Name:");
                Scanner sc = new Scanner(System.in);
                String port = sc.nextLine();
                downloadPort.openPort(port, downloadBuadRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                downloadPort.addSerialEventListener(downloadPort);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });




    }

    public void start () {
        logger.info("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }





}
