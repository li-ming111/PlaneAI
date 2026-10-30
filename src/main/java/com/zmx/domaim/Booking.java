package com.zmx.domaim;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("booking")
public class Booking {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String bookingNumber;//航班预订号
    private String date;//预订日期
    private String bookingTo;//预订到达日期
    private String customer;//预订人
    private String fromCity;//出发地
    private String toCity;//目的地
    private String bookingStatus;//预订状态
    private String bookingClass;//预订舱位
}