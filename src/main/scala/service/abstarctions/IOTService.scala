package service.abstarctions

import service.abstarctions.DeviceType.DeviceType
import service.abstarctions.RequestedInformation.RequestedInformation

/**
  * Created by xsobrietyx on 12-March-2019 time 16:43
  */
trait IOTService {
  def init(): Unit
  def getData(typeOfDevice: DeviceType,typeOfData: RequestedInformation): BigDecimal
}
