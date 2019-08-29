package spring_with_netty.netty;

import org.apache.log4j.Logger;
import spring_with_netty.netty.handler.RadarSerialHandler;

import gnu.io.*;

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
    private String filePath = "/Users/wutiancheng/Documents/项目/雷达/77G _Radar/radar.cfg";

    private volatile static int packsNum = 0;
    public static boolean isConnected = false;

    private static boolean uploadFlag = false;

    public static void setUploadFlag(boolean flag){
        uploadFlag = flag;
    }

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

        try {
            Thread.currentThread().sleep(500);
        } catch (Exception e){
            logger.error(e.getMessage());
        }

        Thread uploadThread = new Thread(()->{
            Thread.currentThread().setName("uploadThread");
            //Gets an instance
            RadarSerialHandler uploadPort = new RadarSerialHandler();
            //Gets all ports
            ArrayList<String> ports = uploadPort.listSerialPorts();
            System.out.println("=========================================");
            System.out.println("Avaible Serial Port List: ");
            while (true){
                try {
                    ports.forEach(s-> System.out.println(s));
                    System.out.println("=========================================");
                    System.out.println("Input Upload Serial Port Name:");
                    Scanner sc = new Scanner(System.in);
                    String port = sc.nextLine();
                    uploadPort.openPort(port, uploadBuadRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    File cfgFile = new File(filePath);
                    uploadPort.sendFileToComPort(cfgFile);
                    break;
                } catch (Exception e){
                    logger.error(e.toString());
                }

            }
            uploadPort.disconnect();
            Thread.currentThread().interrupted();

//                1.初始化串口 2.设置输入输出流以及监听器 3.上传文件 4.接收数据

        });
        Thread downloadThread = new Thread(()->{
            Thread.currentThread().setName("downloadThread");
            RadarSerialHandler downloadPort = new RadarSerialHandler();
            ArrayList<String> ports = downloadPort.listSerialPorts();
            System.out.println("=========================================");
            System.out.println("Avaible Serial Port List: ");
            while (true){
                try {
                    ports.forEach(s-> System.out.println(s));
                    System.out.println("=========================================");
                    System.out.println("Input Download Serial Port Name:");
                    Scanner sc = new Scanner(System.in);
                    String port = sc.nextLine();
                    downloadPort.openPort(port, downloadBuadRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    break;
                } catch (Exception e){
                    logger.error(e.toString());
                }
            }

        });

        downloadThread.start();
        while (true){
            if (uploadFlag){
                uploadThread.start();
                break;
            }
            try {
                Thread.currentThread().sleep(500);
            }catch (Exception e){
                logger.error(e.toString());
            }
        }



    }

    public void start () {
        logger.info("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }





}
