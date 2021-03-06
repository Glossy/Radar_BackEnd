package spring_with_netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import spring_with_netty.netty.ForwardingServer;
import spring_with_netty.netty.SerialRadarServer;
import spring_with_netty.netty.UDPRadarServer;

import java.lang.reflect.Field;

/**
 * @Author: Wu
 * @Date: 2019/2/27 10:19 AM
 */

@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
        //TCPRadarServer radarServer = new TCPRadarServer();
//        UDPRadarServer radarServer = new UDPRadarServer();
        ForwardingServer forwardingServer = new ForwardingServer();
        forwardingServer.start();
        SerialRadarServer radarServer = new SerialRadarServer();
        radarServer.start();


    }

}

