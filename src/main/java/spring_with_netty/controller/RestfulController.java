package spring_with_netty.controller;

import org.springframework.web.bind.annotation.*;
import spring_with_netty.service.TestService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Wu
 * @Date: 2019/3/11 6:50 PM
 */

@RestController
@RequestMapping("/v1")
public class RestfulController {
    @Resource
    private TestService testService;

    @Resource
    private HttpServletResponse response;

    @GetMapping(value = "/{testmethod}")
    @ResponseBody
    public String getTestMethod(@PathVariable String testmethod,
                                @RequestParam("id")int requestid,
                                @RequestBody String content) throws Exception{
        String data;
        if (testmethod.equals("CPU_TEST")){
            data = testService.CPU_Test(requestid, content);
        } else if(testmethod.equals("MEMORY_TEST")){
            data = testService.Memory_Test(requestid, content);
            System.out.println(content);
        } else if (testmethod.equals("NETWORK_TEST")){
            data = testService.Network_Test(requestid, content);
        } else {
            System.out.println("Unknown Method!");
            data = "Unknown Method!";
        }
        return data;
    }


}
