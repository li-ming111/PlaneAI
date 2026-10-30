package com.zmx.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmx.domaim.Booking;
import com.zmx.service.BookingService;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BookingConfig {

    @Autowired
    private BookingService bookingService;

    //机票查询
    @Tool(name = "queryBooking",description = "机票查询、查询预订信息，查询预订机票，预订机票信息查询")
    public String queryBooking(@ToolParam(description = "航班预订日期、出发日期") String bookingTo,
                               @ToolParam(description = "航班预订人身份证号码") String customer){
        System.out.println("----------->>机票查询");
        Booking booking = bookingService.getOne(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getBookingTo, bookingTo)
                .eq(Booking::getCustomer, customer));
        if (booking != null){
            return "航班预订信息为：" + JSON.toJSONString(booking);
        }
        return "未找到该航班预订信息";
    }

    @Tool(name = "cancelBooking",description = "预订取消、取消预订、取消机票预订、退订机票")
    public String cancelBooking(@ToolParam(description = "航班预订日期，出发日期")
                                String bookingTo,
                                @ToolParam(description = "航班预订人身份证号码")
                                String customer,
                                @ToolParam(description = "航班号") String bookingNumber)
    {
        Booking booking = bookingService.getOne(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getBookingTo, bookingTo)
                .eq(Booking::getCustomer, customer));
        //判断记录是否存在
        if (booking !=null) {
            bookingService.removeById(booking.getId());
            return "取消成功";
        }
        return "您没有预订该航班";
    }

    @Tool(name = "saveBooking",description = "预定机票、预订机票，订购机票")
    public String saveBooking(@ToolParam(description = "航班预订日期，出发日期")
                              String bookingTo,
                              @ToolParam(description = "航班预订人身份证号码")
                              String customer,
                              @ToolParam(description = "航班预出发地，出发城市")
                              String from,
                              @ToolParam(description = "航班预订目的地，到达城市")
                              String to,
                              @ToolParam(description = "航班预订舱位，经济舱、高端经济舱、商务舱、头等舱")
                                      String bookClass,
                              @ToolParam(description = "航班号")
                                  String bookingNumber) {
        System.out.println("----------------->>>saveBooking");
        //判断记录是否存在
        Booking booking1 = bookingService.getOne(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getBookingTo, bookingTo)
                .eq(Booking::getCustomer, customer)
                .eq(Booking::getFromCity, from)
                .eq(Booking::getToCity, to)
                .eq(Booking::getBookingClass, bookClass)
        );
        if (booking1!=null) {
            return "你已经预定过此次航班";
        }
        Booking booking = new Booking();
        booking.setBookingTo(bookingTo);
        booking.setCustomer(customer);
        booking.setFromCity(from);
        booking.setToCity(to);
        booking.setBookingClass(bookClass);
        //航班号
        booking.setBookingNumber(bookingNumber);
        bookingService.save(booking);
        return "预定成功";
    }

    @Autowired
    private RedisVectorStore redisVectorStore;
    @Tool(name = "searchRedis",description = "查询机票，机票信息查询，航班信息查询")
    public List<Document> search(@ToolParam(description = "请输入查询条件") String query) {
        return redisVectorStore.similaritySearch(
                SearchRequest
                        .builder()
                        .query(query)
                        .topK(5)
                        .build()
        );
    }
}






































