package spring_with_netty.service;

import org.springframework.stereotype.Service;

/**
 * @Author: Wu
 * @Date: 2019/3/11 7:07 PM
 */
@Service
public interface TestService {
    //返回json格式的String
    String CPU_Test(int request_id, String content) throws Exception;

    String Memory_Test(int request_id, String content) throws Exception;

    String Network_Test(int request_id, String content) throws Exception;


}
