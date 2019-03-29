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
  abstract class IOTService {

    private[service] val devices: ListBuffer[Device] = new ListBuffer[Device]

    def getData(typeOfDevice: DeviceType, typeOfData: RequestedInformation): BigDecimal

    def addDevice(device: Device): Unit
  }

}
