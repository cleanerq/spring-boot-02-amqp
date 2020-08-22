package com.qby.springboot;

import com.qby.springboot.bean.Book;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
class SpringBoot02AmqpApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 单播：点对点消息
     */
    @Test
    void contextLoads() {

        Map<String, Object> map = new HashMap<>();
        map.put("msg", "第一个消息");
        map.put("data", Arrays.asList("helloworld", 123, true));
        // 只需要传入要发送的对象，自动序列化发送给rabbitmq object默认当成消息体
        // 对象被默认序列化以后发送出去
        rabbitTemplate.convertAndSend("exchange.direct", "qby.news", map);
    }

    @Test
    public void receive() {
        Object o = rabbitTemplate.receiveAndConvert("qby.news");
        System.out.println(o.getClass());
        System.out.println(o);
    }

    @Test
    public void test01() {
        Book book = new Book();
        book.setBookName("西游记");
        book.setAuthor("吴承恩");
        rabbitTemplate.convertAndSend("exchange.direct", "qby.news", book);
    }

    /**
     * 广播
     */
    @Test
    public void test02() {
        rabbitTemplate.convertAndSend("exchange.fanout", "", new Book("三国演义", "罗贯中 "));
    }

}
