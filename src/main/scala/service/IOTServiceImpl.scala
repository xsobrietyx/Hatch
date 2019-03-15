package service

import java.time.LocalDateTime

import service.abstarctions.RequestedInformation.RequestedInformation
import service.abstarctions.{DeviceType, IOTService, RequestedInformation}

/**
  * Created by xsobrietyx on 12-March-2019 time 14:14
  */
object IOTServiceImpl extends IOTService {
  private var startTime: LocalDateTime = _
  var thermostatDevice: Device = _
  private var thermostatDataStream: Stream[Device] = _

  private def getAverageData(frozen: Stream[Device]): BigDecimal = {
    var result: BigDecimal = 0
    frozen.foreach(Device => result += Device.data)
    result / frozen.size
  }

  private def getMinData(frozen: Stream[Device]): BigDecimal = frozen.head.data

  private def getMaxData(frozen: Stream[Device]): BigDecimal = frozen.last.data

  private def getMedianData(frozen: Stream[Device]): BigDecimal = {
    frozen.take(frozen.size / 2).last.data
  }


  override def getData(typeOfData: RequestedInformation): BigDecimal = {
    val frozen: Stream[Device] = thermostatDataStream.takeWhile(Device => Device.time.isBefore(LocalDateTime.now()))
    typeOfData match {
      case RequestedInformation.average => getAverageData(frozen)
      case RequestedInformation.median => getMedianData(frozen)
      case RequestedInformation.min => getMinData(frozen)
      case RequestedInformation.max => getMaxData(frozen)
      case _ => -1
    }
  }

  override def init(): Unit = {
    startTime = LocalDateTime.now()
    thermostatDevice = Device(DeviceType.thermostat, 0, startTime)
    thermostatDataStream = Stream.continually({
      thermostatDevice = Device(thermostatDevice.deviceType, thermostatDevice.data + .1, thermostatDevice.time.plusSeconds(1))
      thermostatDevice
    })
  }

  override def healthCheck(): Boolean = !thermostatDataStream.hasDefiniteSize
}

case class IOTServiceImpl()