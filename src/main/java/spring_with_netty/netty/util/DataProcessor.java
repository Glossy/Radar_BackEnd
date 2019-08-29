package spring_with_netty.netty.util;

//import com.mongodb.async.SingleResultCallback;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
//import org.bson.Document;
import spring_with_netty.netty.TCPRadarServer;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 从Handler接收数据的处理类
 * @Author: Wu
 * @Date: 2019/3/24 9:46 AM
 */
public class DataProcessor {
    //private MongoDB mongodb;

//    public void setMongodb(MongoDB mongodb){
//        this.mongodb = mongodb;
//    }

//    SingleResultCallback<Void> callback;

    /**
     * hexstring to Int
     * @param string
     * @return
     */
    public int string2Int(String string){
        return  Integer.parseInt(string, 16);
    }

//    /**
//     * 貌似是小端16进制字符串转byte
//     * @param hexstring
//     * @return
//     */
//    public byte[] hexstring2bytes(String hexstring){
//        byte[] destByte = new byte[hexstring.length()/2];
//        int j=0;
//        for(int i=0;i<destByte.length;i++) {
//            byte high = (byte) (Character.digit(hexstring.charAt(j), 16) & 0xff);
//            byte low = (byte) (Character.digit(hexstring.charAt(j + 1), 16) & 0xff);
//            destByte[i] = (byte) (high << 4 | low);
//            j+=2;
//        }
//        return destByte;
//    }

    /**
     * 四字节（8位16进制）字符串转long 小端
     * @param hexstring
     * @return
     * @throws Exception
     */
    public long hexstring2Unit32(String hexstring) throws IOException{
        BigInteger unit32;
//        String endianstring = new StringBuffer(hexstring).reverse().toString();
        String endianstring = littleEndianString2HexString(hexstring);
        if (hexstring.length() == 8){
            unit32 = new BigInteger(endianstring, 16);
            return unit32.longValue();
        }
        throw new IOException("Wrong length of input string");
    }


    /**
     * 二字节（4位16进制）字符串转int 小端
     * @param hexstring
     * @return
     * @throws Exception
     */
    public int hexstring2Unit16(String hexstring) throws IOException{
        BigInteger unit16;
        String endianstring = littleEndianString2HexString(hexstring);
        if (hexstring.length() == 4){
            unit16 = new BigInteger(endianstring, 16);
            return unit16.intValue();
        }
        throw new IOException("Wrong length of input string");
    }
    /**
     * java实现C中 uint32_t
     * @param l
     * @return
     */
    public long getUint32(long l){
        return l & 0x00000000ffffffff;
    }

//    /**
//     * bytes转为unit32_t(报错）
//     * @param bytes
//     * @param start
//     * @param length
//     * @return
//     */
//    public long bytes2long(byte[] bytes, int start, int length){
//        byte[] parseByte = new byte[4];
//        System.arraycopy(bytes, start, parseByte, 0, length);
//
//        ByteBuffer buf=ByteBuffer.allocateDirect(4); //无额外内存的直接缓存
//        buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
//        buf.put(parseByte);
//        buf.rewind();
//        long a = buf.getLong();
//        return getUint32(a);
//    }

    /**
     * 将bytes[] 数组中的（四字节）数据转为float
     * @param data
     * @param start
     * @param length
     * @return
     */
    public float bytes2Float(byte[] data, int start, int length){


        byte[] parseByte = new byte[4];
        System.arraycopy(data, start, parseByte, 0, length);

        ByteBuffer buf=ByteBuffer.allocateDirect(4); //无额外内存的直接缓存
        buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
        buf.put(parseByte);
        buf.rewind();
        return buf.getFloat();

//        int l;
//        l = b[0];
//        l &= 0xff;
//        l |= ((long) b[1] << 8);
//        l &= 0xffff;
//        l |= ((long) b[2] << 16);
//        l &= 0xffffff;
//        l |= ((long) b[3] << 24);
//        return Float.intBitsToFloat(l);
    }

    public String bytes2String(byte[] bytes){
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }


    public void parse(ByteBuf in){
        if(!in.hasArray()){
            int len =  in.readableBytes();
            byte[] bytes = new byte[len];
            in.getBytes(0, bytes);
            String hex_string = bytes2String(bytes);
            on_Data(hex_string);
        } else {
            byte[] bytes = in.array();
            String hex_string = bytes2String(bytes);
            on_Data(hex_string);
        }
    }

    /**
     * 串口雷达报头检测
     * @param hex_string
     * @return
     * @throws IOException
     */
    public List<Object> on_V2Data(String hex_string) throws IOException{
//        if (hex_string.substring(0, 16).equals("0201040306050807")){
//            System.out.println("合法报头");

//            double version = string2Int(hex_string.substring(16, 24)) ; //Version
//            double total_length = string2Int(hex_string.substring(24, 32)) ; //total packet length
//            double platform = string2Int(hex_string.substring(32, 40)) ;
//            double frameNum = string2Int(hex_string.substring(40, 48)) ; //Frame Number
//            double time = string2Int(hex_string.substring(48, 56)) ; //CPU Cycles
//
//            String message = " \"Version\":" + version + ", \"total packet length\":" + total_length + "\"Platform\":"
//                    + platform + ", \"Frame Number\":"+ frameNum + "\"Time\":"+ time;
//            on_V2Target_Data(hex_string,message);
//        } else{
//            TCPRadarServer.LOG.error("Unknown Header");
//        }
        if (hex_string.substring(0, 16).equals("0201040306050807")){
            List<Object> list = new ArrayList<>();
            long version = hexstring2Unit32(hex_string.substring(16, 24)) ; //Version
            long total_length = hexstring2Unit32(hex_string.substring(24, 32)) ; //total packet length
            long platform = hexstring2Unit32(hex_string.substring(32, 40)) ;
            long frameNum = hexstring2Unit32(hex_string.substring(40, 48)) ; //Frame Number
            long timeCpuCycles = hexstring2Unit32(hex_string.substring(48, 56)) ; //CPU Cycles
            long numDetectedObj = hexstring2Unit32(hex_string.substring(56, 64)); //Detected Objects
            long numTLVs = hexstring2Unit32(hex_string.substring(64, 72));//Number TLV’s

            String message = " \"Version\":" + version + ", \"total packet length\":" + total_length + "\"Platform\":"
                    + platform + ", \"Frame Number\":"+ frameNum + ", \"TimeCpuCycles\":"+ timeCpuCycles
                    + ", \"Detected Objects\":" + numDetectedObj + ", \"Number TLV’s\":" + numTLVs;

            if (total_length%32 != 0){ //雷达会自动补齐缺失位数
                total_length = (32 - total_length%32) + total_length;
            }
            list.add(total_length);
            list.add(numDetectedObj);
            list.add(message);
            return list;
        }
        return null;
    }

    public void on_V2Target_Data(String hex_string, long targetNum, String message) throws IOException{
//        long detectedObjectNum = hexstring2Unit32(hex_string.substring(16, 24));
        String description = littleEndianString2HexString(hex_string.substring(16, 24));
        int detectedObj = hexstring2Unit16(hex_string.substring(16, 20));
        long Qformat = hexstring2Unit16(hex_string.substring(20, 24));
        long target_count = targetNum;
        String header = message;

//        if(target_count != 0){
//            System.out.println("非0目标");
//        }
        for(int i = 0; i < target_count; i++){
            int start = 24 + 24 * i;
            int end = start +  24;

            int range = hexstring2Unit16(hex_string.substring(start, start+4)) ; //Range Index
            int doppler = hexstring2Unit16(hex_string.substring(start+4, start+8)); //Doppler Index
            int peak = hexstring2Unit16(hex_string.substring(start+8, start + 12)); //Peak Value
            int x = hexstring2Unit16(hex_string.substring(start+12, start+16)); //X轴距离
            int y = hexstring2Unit16(hex_string.substring(start+16, start+20)); //Y轴距离
            int z = hexstring2Unit16(hex_string.substring(start+20, start+24)); //Z轴距离

//            message = "{\"dispatch\":\"workers\", \"emit\":{\"event\":\"dot\", \"args\": {\"x\":" + x + ", \"y\":" + y + ", \"z\":" + z
//                    + ", \"range\":" + range + ", \"doppler\":" + doppler + ", \"peak\":" + peak + ", \"Q-Format Description:\":"
//                    + description + message + "}}}";
            message = "{\"dispatch\":\"workers\", \"emit\":{\"event\":\"dot\", \"args\": {\"x\":" + x + ", \"y\":" + y + ", \"z\":" + z
                    + ", \"range\":" + range + ", \"doppler\":" + doppler + ", \"peak\":" + peak + ", \"Q-Format Description:\":"
                    + description + ", \"Q-DetectedObj:\":" + detectedObj + ", \"Q-Format :\":" + Qformat + message + "}}}";

//            System.out.println(message);

            ForwardingThread.send2WebSocket(Unpooled.wrappedBuffer(message.getBytes()));
            message = header;

//            数据库相关操作
//            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//            Document doc = new Document("time", date)
//                    .append("x", _x)
//                    .append("y", _y)
//                    .append("id", i);
//
//            mongodb.collection.insertOne(doc, new SingleResultCallback<Void>() {
//                public void onResult(final Void result, final Throwable t) {
//                    TCPRadarServer.LOG.info("Document inserted!");
//                }});

        }

        //return "{'dispatch': 'workers', 'emit': {'event': 'transection', 'args': {'area': 'A8', 'time': '2019-03-24 00:10:29.272017', 'action': 1}}}\n";


    }

    static byte[] firstPacket; //暂存第一个数据包

    /**
     * UDP服务端 解析函数
     * @param packet
     */
    public void parse(DatagramPacket packet){
        ByteBuf buf = (ByteBuf) packet.copy().content();
        byte[] pack = new byte[buf.readableBytes()]; //1024字节
        buf.readBytes(pack);

        if (pack[0]==1&&pack[1]==0&&pack[2]==1&&pack[3]==0){
            this.firstPacket = new byte[1200];
            System.arraycopy(pack, 4, this.firstPacket, 0,1200);
        } else if (pack[0]==2&&pack[1]==0&&pack[2]==2&&pack[3]==0){
            if (this.firstPacket == null){
                return;
            }
            byte[] pack2 = new byte[1200];
            System.arraycopy(pack, 4, pack2, 0, 1200);
            byte[] data = new byte[2400];
            System.arraycopy(this.firstPacket, 0, data, 0, 1200);
            System.arraycopy(pack2, 0, data, 1200, 1200);
            this.firstPacket = null;

            float selfcheck = bytes2Float(data, 0, 4);
            float type = bytes2Float(data, 4, 4);
            float version = bytes2Float(data, 8, 4);
            float curtime = bytes2Float(data, 12, 4); //单位毫秒

            int start = 16;
            for(int i = 0; bytes2Float(data, start + i*20, 4) != 0; i++){ //bytes2Float(data, start + i*20, 4) != 0
                float targetID = bytes2Float(data, start + i*20, 4);
                float x = bytes2Float(data, start + i*20 + 4, 4);
                float y = bytes2Float(data, start + i*20 + 8, 4);
                float v = bytes2Float(data, start + i*20 + 12, 4); //径向速度
                float acc = bytes2Float(data, start + i*20 + 16, 4); //加速度

                String message = "{\"dispatch\":\"workers\", \"emit\":{\"event\":\"dot\", \"args\": {\"x\":" + x + ", \"y\":" + y +
                        ", \"v\":" + v + ", \"acc\":" + acc + ", \"targetID\":" + targetID + ", \"selfcheck\":" + selfcheck +
                        ", \"type\":" + type + ", \"version\":" + version +
                        ", \"curtime\":" + curtime +  "}}}";
                ForwardingThread.send2WebSocket(Unpooled.wrappedBuffer(message.getBytes()));
            }

            //00030003c0a80102ffffffff01000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
            //00030003c0a80102ffffffff01000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
        }else {
            String hex_string = bytes2String(pack);
            String des_mac = hex_string.substring(0, 16).replace("(.{2})", "$1:");
            String mac = hex_string.substring(16, 32).replace("(.{2})", "$1:");
//            String ip = hex_string.substring();
//            UDPRadarServer.LOG.info("Radar Connected!");
//            UDPRadarServer.LOG.info("Radar MAC address:  Radar IP address: ");
        }
//        System.out.println(bytes2String(pack));

        ReferenceCountUtil.release(buf);
    }


    /**
     * 翻译自python代码 合法帧头检测
     * @param hex_string
     */
    public void on_Data(String hex_string){

        if (hex_string.substring(0, 4).equals("55aa")){
//            System.out.println("合法报头");
        } else{
            TCPRadarServer.LOG.error("Unknown Header");
        }

        if(hex_string.substring(4, 6).equals("12")){
//            System.out.println("上传指令");
            on_Target_Data(hex_string);
        }
    }

    public void on_Target_Data(String hex_string){
        int target_count = string2Int(hex_string.substring(10, 12));

//        if(target_count != 0){
//            System.out.println("非0目标");
//        }
        for(int i = 0; i < target_count; i++){
            int start = 12 + 34 * i;
            int end = start +  32;

            double distance = string2Int(hex_string.substring(start, start + 8)) * 0.01; //径向距离
            double angle= string2Int(hex_string.substring(start + 8, start + 12)) * 0.01; //方位角
            double y = string2Int(hex_string.substring(start + 12, start + 20)) * 0.01; //Y轴距离
            double x = string2Int(hex_string.substring(start + 20, start + 28)) * 0.01; //X轴距离

            double _angle = angle * 1 - 25;
            double _distance = distance * 1 + 0;

            double rad = _angle * 0.0174533;
            double _x = _distance * Math.sin(rad);
            double _y = _distance * Math.cos(rad);

            String message = "{\"dispatch\":\"workers\", \"emit\":{\"event\":\"dot\", \"args\": {\"x\":" + _x + ", \"y\":" + _y + "}}}";

            ForwardingThread.send2WebSocket(Unpooled.wrappedBuffer(message.getBytes()));
//            数据库相关操作
//            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//            Document doc = new Document("time", date)
//                    .append("x", _x)
//                    .append("y", _y)
//                    .append("id", i);
//
//            mongodb.collection.insertOne(doc, new SingleResultCallback<Void>() {
//                public void onResult(final Void result, final Throwable t) {
//                    TCPRadarServer.LOG.info("Document inserted!");
//                }});

        }

        //return "{'dispatch': 'workers', 'emit': {'event': 'transection', 'args': {'area': 'A8', 'time': '2019-03-24 00:10:29.272017', 'action': 1}}}\n";
    }

    /**
     * 两两反转小端hexString到正常十六进制字符串
     * @param hexstring
     * @return
     */
    public String littleEndianString2HexString(String hexstring){
        String littleendianstring = "";
        for(int i = hexstring.length(); i >= 2; i = i - 2){
            littleendianstring = littleendianstring + hexstring.substring(i-2, i);
        }
        return littleendianstring;
    }


}
