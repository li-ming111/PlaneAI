package com.zmx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.domaim.Booking;
import com.zmx.mapper.BookingMapper;
import com.zmx.service.BookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements BookingService {
}
