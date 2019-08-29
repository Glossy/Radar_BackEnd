package spring_with_netty.netty.handler;

import com.sun.xml.internal.bind.v2.TODO;
import gnu.io.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import org.apache.log4j.Logger;
import spring_with_netty.netty.SerialRadarServer;
import spring_with_netty.netty.util.DataProcessor;

import java.io.*;
import java.util.*;

/**
 * @Author: Wu
 * @Date: 2019/7/31 3:25 PM
 */
public class RadarSerialHandler implements SerialPortEventListener{
    private Logger logger = SerialRadarServer.logger;
    private static DataProcessor processor = new DataProcessor();
    private int readLength = 8;
    private SerialPort serialPort;
    private OutputStream outStream;
    private InputStream inStream;

    /**
     * The filed with a DataRate value.
     */
    private int dataRate;

    /**
     * The value of TIME OUT constant.
     */
    public static final int DEFAULT_TIME_OUT = 2000;

    /**
     * The filed with a TimeOut value.
     */
    private int timeOut;

//    /**
//     * Static member class member that holds only one instance of the
//     * <code>StaticHolder</code> class.
//     *
//     */
//    private static class StaticHolder {
//        static final RadarSerialHandler INSTANCE = new RadarSerialHandler();
//    }
//
//    /**
//     * Providing Global point of access.
//     *
//     * @return {@link StaticHolder} object.
//     */
//    public static  RadarSerialHandler getSingleton() {
//        return StaticHolder.INSTANCE;
//    }
//
//    /**
//     * A private constructor, which prevents any other class from instantiating.
//     */
//    private  RadarSerialHandler() {
//    }

    /**
     * List the available serial ports.
     *
     * @return the {@link ArrayList} with all available serial port names.
     */
    public ArrayList<String> listSerialPorts() {
        Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

        ArrayList<String> ports = new ArrayList<String>();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) portEnum.nextElement();
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                ports.add(port.getName());
            }
        }
        return ports;
    }

    /**
     * Opens a CommPort using {@link CommPortIdentifier} object. If the port is
     * owned by some other application the method throws {@link IOException}.
     *
     * @param portName
     *            the {@link CommPortIdentifier} object.
     * @throws IOException
     *             if {@link IOException} occurs.
     */
    public void openPort(final String portName, int b, int d, int s, int p) throws IOException {
        try {
            if (portName == null) {
                throw new NullPointerException("Com port is null");
            }
            // Obtain a CommPortIdentifier object for the port you want to open
            CommPortIdentifier portId;

            portId = CommPortIdentifier.getPortIdentifier(portName);

            if (!(portId.getCurrentOwner()==null) && portId.isCurrentlyOwned()) {
                throw new IllegalStateException("Com port in use");
            }

            // Get the port's ownership
            this.serialPort = (SerialPort) portId.open( RadarSerialHandler.class.getName(), this.DEFAULT_TIME_OUT);

            // Set the parameters of the connection.
            this.serialPort.setSerialPortParams(b, d, s, p);

            this.serialPort.notifyOnDataAvailable(true);

            // Open the input and output streams for the connection.
            // If they won't open, close the port before throwing an
            // exception.
            outStream = serialPort.getOutputStream();
            inStream = serialPort.getInputStream();

            logger.info(this.serialPort.getName() + " is open");
            if (Thread.currentThread().getName().equals("downloadThread")){
                this.addSerialEventListener(this);
                byte[] headbuffer = new byte[72];

                //TODO
                //每次读36字节[72]，然后判断是否为有效帧头。是的话调用函数进一步处理，动态读取剩余大小
                //如 if(process.onV2Data(hexString) != -1) { do... }
                //TODO
                //帧头判断有问题 每次读36字节可能造成永远收不到数据

                int dataSize = 0;
                SerialRadarServer.setUploadFlag(true);
                while(true){
                    if (inStream.available() > 0) {
                        dataSize = inStream.read(headbuffer);
                        String hexString = processor.bytes2String(headbuffer);
//                        System.out.println(hexString);
                        List<Object> returnList = processor.on_V2Data(hexString);
                        if (returnList == null){
                            continue;
                        }
                        long length = (long)returnList.get(0);
                        long targetNum = (long)returnList.get(1);
                        String message = (String)returnList.get(2);
                        if(length != -1){
                            int l = Integer.parseInt(String.valueOf((length-36)*2));
                            byte[] databuffer = new byte[l];
                            inStream.read(databuffer);
                            processor.on_V2Target_Data(processor.bytes2String(databuffer), targetNum, message);
                        }
                    }else {
                        Thread.sleep(500);
                        logger.info(Thread.currentThread().getName() + " wait 500 ms");
                    }
                }
            }
        } catch (NoSuchPortException e1) {
            this.serialPort.close();
            throw new IOException(e1.toString()+"\n No such Port Exception");
        } catch (PortInUseException e) {
            this.serialPort.close();
            throw new IOException(e.toString()+"\n Port in use Exception");
        } catch (IOException e) {
            this.serialPort.close();
            throw e;
        } catch (UnsupportedCommOperationException e) {
            this.serialPort.close();
            throw new IOException("Unsupported serial port parameter");
        }  catch (InterruptedException e){
            this.serialPort.close();
            logger.error(e.toString());
        }
        catch (TooManyListenersException e){
            this.serialPort.close();
            logger.error(e.toString());
        }

    }

    /**
     * Check for available data.
     *
     * @return <code>true<code> if data is available; <code>false</code>
     *         otherwise.
     */
    public boolean isDataAvailable() {
        boolean flag = false;
        try {
            if (this.inStream.available() != 0) {
                flag = true;
            }
        } catch (IOException ex) {

        }

        return flag;
    }

    /**
     * Disconnect the serial port.
     */
    public void disconnect() {
        if (serialPort != null) {
            try {
                // close the i/o streams.
                outStream.close();
                inStream.close();
            } catch (IOException ex) {
                // don't care
            }
            // Close the port.
            serialPort.close();
            serialPort = null;
        }
    }

    /**
     * Sets TimeOut of serial connection
     *
     * @param timeOut
     *            the TimeOut value.
     */
    public void setTimeOut(final int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * Sets DataRade of the serial connection.
     *
     * @param dataRate
     *            the DataRade value.
     */
    public void setDataRate(final int dataRate) {
        this.dataRate = dataRate;
    }

    /**
     * Gets the {@link OutputStream} object.
     *
     * @return the {@link OutputStream} object.
     */
    public OutputStream getOutStream() {
        return outStream;
    }

    /**
     * Gets the {@link InputStream} object.
     *
     * @return the {@link InputStream} object.
     */
    public InputStream getInStream() {
        return inStream;
    }


    /**
     * Adds the specified {@link SerialPortEventListener} listener to receive
     * {@link  RadarSerialHandler} events. If listener deviceListener is null, no
     * exception is thrown and no action is performed.
     *
     * @param listener
     *            the {@link SerialPortEventListener} object.
     * @throws TooManyListenersException
     *             if {@link TooManyListenersException} occurs.
     */
    public void addSerialEventListener(final SerialPortEventListener listener) throws TooManyListenersException {
        this.serialPort.addEventListener(listener);
    }

    /**
     * This method reads block of data with size.
     *
     * @param len
     *            the number of bytes to read.
     * @return the bytes of array.
     * @throws IOException
     *             if {@link IOException} occurs.
     */
    public byte[] readBlocked(final int len) throws IOException {
        byte[] buff = new byte[len];

        DataInputStream dInputStream = new DataInputStream(this.inStream);
        dInputStream.readFully(buff, 0, len);

        return buff;
    }

    /*
     * Writes the specified byte to this byte array output stream.
     *
     * @param bytes the byte to be written.
     *
     * @throws IOException if {@link IOException} occurs.
     */
    public void writeBlock(final byte[] bytes) throws IOException {
        if (bytes == null) {
            throw new NullPointerException("Byte buffer is empty");
        }
        this.outStream.write(bytes);
    }

    /**
     * Gets all available data from input stream.
     *
     * @return the available data.
     * @throws IOException
     *             if {@link IOException} occurs.
     */
    public int getAvailableBytes() throws IOException {
        return this.inStream.available();

    }

    /**
     * 往串口发送数据
     *
     * @param orders      待发送数据
     */
    public void sendDataToComPort(byte[] orders) {
        try {
            if (this.serialPort != null) {
                outStream = this.serialPort.getOutputStream();
                outStream.write(orders);
                outStream.flush();
                logger.info("往串口 " + this.serialPort.getName() + " 发送数据：" + Arrays.toString(orders) + " 完成...");
            } else {
                logger.error("gnu.io.SerialPort 为null，取消数据发送...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 往串口发送文件
     *
     * @param file      配置文件
     */
    public void sendFileToComPort(File file) {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            if (this.serialPort != null) {
                FileReader fr = new FileReader(file.getPath());
                BufferedReader bf = new BufferedReader(fr);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                    str = str.trim();
                    arrayList.add(str + '\n');
                }
                bf.close();
                fr.close();
                byte[] line;
                for(Iterator iterator = arrayList.iterator(); iterator.hasNext();){
                    String lines = (String)iterator.next();
                    line = (lines.trim()+'\n').getBytes("UTF-8");
                    sendDataToComPort(line);
                    Thread.sleep(50);
                }
            } else {
                logger.error("gnu.io.SerialPort 为null，取消数据发送...");
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE://获取到串口返回信息
                byte[] buffer = new byte[2048];
                int dataSize = 0;
//
//                do{
//                    try{
//                        while(inStream.available() > 0){
//                            dataSize = inStream.read(buffer);
//                            String hexString = processor.bytes2String(buffer);
//                            System.out.println(hexString);
//                            processor.on_V2Data(hexString);
//                        }
//                    }catch(Exception e){
//                        return;
//                    }
//                }
//                while(dataSize != -1);
//
//                serialPort.close();//这里一定要用close()方法关闭串口，释放资源
//                logger.info(serialPort.getName() + " closed");

                break;
            default:
                break;
        }

    }
}
