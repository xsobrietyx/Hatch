package service.abstarctions

/**
  * Created by xsobrietyx on 12-March-2019 time 20:46
  */
object RequestedInformation extends Enumeration {
  type RequestedInformation = Value
  val average, median, max, min = Value
}
