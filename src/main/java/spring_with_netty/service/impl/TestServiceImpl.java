package spring_with_netty.service.impl;
import org.springframework.stereotype.Service;
import spring_with_netty.service.TestService;
import java.io.FileWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Author: Wu
 * @Date: 2019/3/11 7:07 PM
 */
@Service
public class TestServiceImpl implements TestService {
    public static final String path =  "/root/latestdata.txt";


    public void displayAvailableMemory() {

        //显示JVM总内存
        long totalMem = Runtime.getRuntime().totalMemory();
        //显示JVM尝试使用的最大内存
        long maxMem = Runtime.getRuntime().maxMemory();
        //空闲内存
        long freeMem = Runtime.getRuntime().freeMemory();
    }

    public String getId() throws Exception { //服务器IP
        return InetAddress.getLocalHost().getLocalHost().toString();
    }

    @Override
    public String CPU_Test(int request_id, String content) throws Exception {//多线程++
        System.out.println("CPU Test");
        String data = "";
        data = data + "Host:" + getId() + "  ";
        data = data + "CPU_Test";

        //double start = CPUMonitor.getInstance().getProcessCpu();
        for(int j = 0; j < 5; j++){
            new Thread(){
                private boolean isCpuOverLoad = false;
                @Override
                public void run(){ //每个Thread占用0.05%CPU
                    double next = -9999999;
                    System.out.println("start");
//                    String pid;
//                    String period;
//                    JavaSysMon monitor = new JavaSysMon();

//                    RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
//                    String name = runtime.getName();
//                    int index = name.indexOf("@");
//                    if (index != -1) {
//                        int pidNum = Integer.parseInt(name.substring(0, index));
//                        pid = String.valueOf(pidNum);
//                        period = "5000";
//
//                        while (true) {
//                            String processUsage = String.format("%.2f", Monitor.getProcessUsage(monitor, pid, period) * 100);
//                            System.out.println(processUsage);
//                            System.out.println("------------------------------------");
//                        }
//                    }


                    for(int i = 0; i < 99999; i++){
                        next = next++;

                    }
                }
            }.start();
            Thread.sleep(1000);
        }
        //double end = CPUMonitor.getInstance().getProcessCpu();
        //System.out.println("CPU Usage Change： " + (end - start));
        //data = data + String.format("  CPU Usage Change： %f", end-start);
        return data;
    }

    @Override
    public String Memory_Test(int request_id, String content) throws Exception {//申请内存
        String data = "";
        data = data + "Host:" + getId() + "  ";
        data = data + "Memory_Test";
        new Thread(){
            @Override
            public void run(){
                ArrayList<String> list = new ArrayList<String>();
                for(int i = 0; i < 10240; i++){        //10240占用2.2M
                    list.add(String.valueOf(i));
                }
            }
        }.start();

        return data;

    }

    @Override
    public String Network_Test(int request_id, String content) throws Exception {
        String data = "";
        data = data + "Host:" + getId() + "  ";
        data = data + "Network_Test";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        String hey[] = content.split(" ");
        String message = time + " times: " + 1 + " PID: " + content.split(" ")[5];

        //write(this.path, message);

        System.out.println(content);
        return data;
    }

    /***
     * 向文件写数据
     * @param path
     * @param message1
     */
    public synchronized void write(String path, String message1){
        FileWriter writer = null;
        try{
            writer = new FileWriter(path, true);
            writer.write(message1 + "\n");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


}



