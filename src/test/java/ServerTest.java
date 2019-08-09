import gnu.io.SerialPort;
import io.netty.buffer.Unpooled;
import org.junit.Test;
import spring_with_netty.netty.ForwardingServer;
import spring_with_netty.netty.TCPRadarServer;
import spring_with_netty.netty.UDPRadarServer;
import spring_with_netty.netty.util.DataProcessor;
import spring_with_netty.netty.util.ForwardingThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author: Wu
 * @Date: 2019/8/1 9:21 PM
 */
public class ServerTest {

    @Test
    public void WebSocketTest(){
        String message = "{'dispatch': 'workers', 'emit': {'event': 'transection', 'args': {'area': 'A8', 'time': '2019-03-24 00:10:29.272017', 'action': 1}}}\n";
        ForwardingThread.send2WebSocket(Unpooled.wrappedBuffer(message.getBytes()));
    }


    @Test
    public void FordwardServerTest(){
        ForwardingServer server = new ForwardingServer();
        server.run();
    }

    @Test
    public void TCPRadarServerTest(){
        TCPRadarServer server = new TCPRadarServer();
        server.start();
    }

    @Test
    public void UDPRadarServerTest(){
        UDPRadarServer server = new UDPRadarServer();
        server.start();
    }

    @Test
    public void DataProcessorTest(){
        DataProcessor p = new DataProcessor();
        String hex_string = "55aa1201ff02000002000a8d000001ff0000001a0044ec00000694116b0000065e000001a80057e2000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000053";
        p.on_Target_Data(hex_string);
    }

    @Test
    public void sendFileToComPort() {
        File file = new File("/Users/wutiancheng/Documents/项目/雷达/77G _Radar/radar.cfg");
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
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
                char[] array = (lines.trim() + "\n").toCharArray();
                System.out.println(array);
                System.out.println(lines);
                line = (lines.trim()+"\n").getBytes(StandardCharsets.UTF_8);
                System.out.println(new String(line,StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void javalibrarypath(){
        System.out.println(System.getProperty("java.library.path"));
    }

}
