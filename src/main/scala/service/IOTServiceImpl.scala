package service

import java.time.LocalDateTime

import service.abstarctions.DeviceType.DeviceType
import service.abstarctions.RequestedInformation.RequestedInformation
import service.abstarctions.{IOTService, RequestedInformation}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Service for data streaming simulation. Current implementation limited in device streams possible variations,
  * multi maps and additional id's logic can solve this limitation.
  * Service was created as singleton object for synchronised state in case of usage from multiple controllers across
  * the same application.
  * Created by xsobrietyx on 12-March-2019 time 14:14
  */
object IOTServiceImpl extends IOTService {
  private val devices: ListBuffer[Device] = new ListBuffer[Device]

  /**
    * Average chunk of data that was already populated.
    *
    * @param frozen part of data stream from the beginning till the moment of request
    * @return in current POC returns BigDecimal value
    */
  private def getAverageData(frozen: Stream[Device]): BigDecimal = {
    var result: BigDecimal = 0
    frozen.foreach(Device => result += Device.data)
    result / frozen.size
  }

  /**
    * Starting chunk of data that was already populated.
    *
    * @param frozen part of data stream from the beginning till the moment of request
    * @return in current POC returns BigDecimal value
    */
  private def getMinData(frozen: Stream[Device]): BigDecimal = frozen.head.data

  /**
    * Last chunk of data that was already populated.
    *
    * @param frozen part of data stream from the beginning till the moment of request
    * @return in current POC returns BigDecimal value
    */
  private def getMaxData(frozen: Stream[Device]): BigDecimal = frozen.last.data

  /**
    * Middle chunk of data that was already populated.
    *
    * @param frozen part of data stream from the beginning till the moment of request
    * @return in current POC returns BigDecimal value
    */
  private def getMedianData(frozen: Stream[Device]): BigDecimal = {
    frozen.take(frozen.size / 2).last.data
  }

  /**
    * Returns data depending on device type and type of data that requested.
    * Also performs necessary data transformations and calculations.
    * Hold's the core service's logic.
    *
    * @param typeOfDevice type of device
    * @param typeOfData   type of data
    * @return in current POC returns BigDecimal value
    */
  override def getData(typeOfDevice: DeviceType, typeOfData: RequestedInformation): BigDecimal = {
    def createDevicesStreamMap: Map[DeviceType, Future[Stream[Device]]] = {
      devices.map(device => device.deviceType -> createFutureOfStream(device)).toMap
    }

    val streams: Map[DeviceType, Future[Stream[Device]]] = createDevicesStreamMap
    val frozenStream: Stream[Device] = Await.result(streams(typeOfDevice), 2.seconds).takeWhile(Device => Device.time.isBefore(LocalDateTime.now()))

    typeOfData match {
      case RequestedInformation.average => getAverageData(frozenStream)
      case RequestedInformation.median => getMedianData(frozenStream)
      case RequestedInformation.min => getMinData(frozenStream)
      case RequestedInformation.max => getMaxData(frozenStream)
      case _ => -1
    }
  }

  /**
    * Creates a future of device's stream.
    *
    * @param device source of data
    * @return future of device's data stream
    */
  private def createFutureOfStream(device: Device): Future[Stream[Device]] = Future {
    var dev = device
    Stream.continually({
      dev = Device(dev.deviceType, dev.data + .1, dev.time.plusSeconds(1))
      dev
    })
  }

  /**
    * Method to add an additional device to the application. Current implementation assumes that device can be added
    * but the stream of data of the device of that type will be replaced (if already exists). To be able to add additional
    * streams of the same device type multi map can be used.
    *
    * @param device device that should be added
    */
  override def addDevice(device: Device): Unit = {
    val sizeBefore = devices.length
    devices += device
    if (sizeBefore >= devices.length) throw new RuntimeException("Device not added.")
  }

}
