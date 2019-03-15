package service

import java.time.LocalDateTime

import service.abstarctions.DeviceType.DeviceType
import service.abstarctions.RequestedInformation.RequestedInformation
import service.abstarctions.{DeviceType, IOTService, RequestedInformation}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by xsobrietyx on 12-March-2019 time 14:14
  */
object IOTServiceImpl extends IOTService {
  private var startTime: LocalDateTime = _
  private var devices: ListBuffer[Device] = _
  private var dataStreams: Map[DeviceType, Future[Stream[Device]]] = _

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


  override def getData(typeOfDevice: DeviceType, typeOfOperation: RequestedInformation): BigDecimal = {
    val frozenStream: Stream[Device] = Await.result(dataStreams(typeOfDevice), 5.seconds).takeWhile(Device => Device.time.isBefore(LocalDateTime.now()))

    typeOfOperation match {
      case RequestedInformation.average => getAverageData(frozenStream)
      case RequestedInformation.median => getMedianData(frozenStream)
      case RequestedInformation.min => getMinData(frozenStream)
      case RequestedInformation.max => getMaxData(frozenStream)
      case _ => -1
    }
  }

  def init(): Unit = {
    startTime = LocalDateTime.now()
    devices = new ListBuffer[Device]

    devices += Device(DeviceType.thermostat, 0, startTime)
    devices += Device(DeviceType.heartRateMeter, 50, startTime)
    devices += Device(DeviceType.musicPlayer, 100, startTime)

    dataStreams = devices.map(device => device.deviceType -> createStream(device)).toMap
  }

  private def createStream(device: Device): Future[Stream[Device]] = Future {
    var dev = device
    Stream.continually({
      dev = Device(dev.deviceType, dev.data + .1, dev.time.plusSeconds(1))
      dev
    })
  }

  override def addDevice(device: Device): Unit = {
    val sizeBefore = devices.length
    devices += device
    if (sizeBefore >= devices.length) throw new RuntimeException("Device not added.")
    else {
      dataStreams += device.deviceType -> createStream(device)
    }
  }

}

case class IOTServiceImpl()