package com.zmx;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmx.domaim.Booking;
import com.zmx.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlaneApplicationTests {
    @Autowired
    private BookingService bookingService;

    @Test
    public void testSave() {
        Booking booking = bookingService.getOne(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getBookingTo, "2025-06-02")
                .eq(Booking::getCustomer, "12345678912"));
        if (booking != null){
            System.out.println("航班预订信息为：" + JSON.toJSONString(booking));
        }
        else System.out.println("未找到该航班预订信息");
    }
}
