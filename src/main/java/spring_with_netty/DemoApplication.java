package spring_with_netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import spring_with_netty.netty.ForwardingServer;
import spring_with_netty.netty.RadarServer_V1;
import spring_with_netty.netty.RadarServer_V2;

/**
 * @Author: Wu
 * @Date: 2019/2/27 10:19 AM
 */

@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        //RadarServer radarServer = new RadarServer();
        RadarServer_V1 radarServer = new RadarServer_V1();
//        RadarServer_V2 radarServer = new RadarServer_V2();
        ForwardingServer forwardingServer = new ForwardingServer();
        radarServer.start();
        forwardingServer.start();


    }

}

