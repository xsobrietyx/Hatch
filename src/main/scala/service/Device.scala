package service

import service.abstarctions.DeviceType.DeviceType

/**
  * Created by xsobrietyx on 12-March-2019 time 16:49
  */
case class Device(deviceType: DeviceType, data: BigDecimal, time: java.time.LocalDateTime)