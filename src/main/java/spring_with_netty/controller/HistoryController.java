package spring_with_netty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring_with_netty.service.HistoryService;

/**
 * @Author: Wu
 * @Date: 2019/5/27 8:07 PM
 */

@RestController
@RequestMapping(value = "/history")
public class HistoryController {
    @Autowired
    HistoryService historyService;
}
