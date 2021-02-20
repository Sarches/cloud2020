package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 端口号
     * 查看负载均衡的效果
     */
    @Value("${server.port}")
    private String serverPort;

    /**
     *  @Autowired 和 @Resource 的区别
     * 1. @Autowired    由spring提供，只按照byType注入
     * 2. @Resource     由J2EE提供，默认是按照byName自动注入
     */
    //服务发现获取服务信息
    @Autowired
    private DiscoveryClient discoveryClient;


    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment) {

        int result = paymentService.create(payment);
        log.info("**插入结果：" + result);
        if (result > 0) {
            return new CommonResult(200, "插入数据库成功"+serverPort, result);
        } else {
            return new CommonResult(201, "插入数据失败", null);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment != null) {
            return new CommonResult(200, "数据查询成功"+serverPort, payment);
        } else {
            return new CommonResult(201, "没有该条数据,查询id：" + id, null);
        }
    }

    @GetMapping("/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String item : services) {
            log.info(item);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }
}
