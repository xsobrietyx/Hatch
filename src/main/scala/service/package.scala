/**
  * Created by xsobrietyx on 29-March-2019 time 22:27
  */
package object service {

  import service.interfaces.Device
  import service.interfaces.DeviceType.DeviceType
  import service.interfaces.RequestedInformation.RequestedInformation

  import scala.collection.mutable.ListBuffer

  /**
    * Created by xsobrietyx on 12-March-2019 time 16:43
    */
  abstract class IOTService[D <: Device, T <: DeviceType, R <: RequestedInformation] {

    private[service] val devices: ListBuffer[D] = new ListBuffer[D]

    def getData(typeOfDevice: T, typeOfData: R): BigDecimal

    /**
      * Method to add an additional device to the application. Current implementation assumes that device can be added
      * but the stream of data of the device of that type will be replaced (if already exists). To be able to add additional
      * streams of the same device type multi map can be used.
      *
      * @param device device that should be added
      */
    def addDevice(device: D): Unit = {
      val sizeBefore = devices.length
      devices += device
      if (sizeBefore >= devices.length) throw new RuntimeException("Device not added.")
    }
  }

}
