import play.api.libs.json._

/**
  * Geographic Tool 1.Converter
  *
  * This converter has following 2 features.
  * Feature 1. Convert latitude & longitude
  *   Convert latitude and longitude geographic coordinates ( Japanese / Global )
  * Feature 2. Convert Degree
  *   Convert degree format ( DMS (139/41/30.3) / DEG (139.691749) )
  */
class GeographicConverter {
  def main(args: Array[String]): Unit = {
    if (args.length > 2 && (args(0) == "g2j" || args(0) == "j2g")) {
      println(convertCoordinates(args))
    } else if (args.length > 1 && (args(0) == "dms2deg" || args(0) == "deg2dms")) {
      println(convertDegree(args))
    } else {
      println("your input data is wrong.please input following parameter.")
      println("Case1. convert latitude & longitude")
      println("  <j2g/g2j> latitude longitude")
      println("  'j2g' means converting latitude and longitude from Japanese geographic coordinates to Global geographic coordinates.")
      println("Case2. convert degree")
      println("  <dms2deg/deg2dms> degree")
      println("  'dms2deg' means converting degree from DMS format (139/41/30.3) to DEG format (139.691749).")
    }
  }

  /**
    * convert Geographic Coordinates
    *
    * @param params
    * @return String
    */
  def convertCoordinates(params: Array[String]): Map[String, Double] = {
    val convertUrl = buildConverterUrl(params)
    val json = Json.parse(scala.io.Source.fromURL(convertUrl).mkString)
    val a = (json \ "OutputData" \ "latitude").get.asInstanceOf[JsString].value
    val b = (json \ "OutputData" \ "longitude").get.asInstanceOf[JsString].value
    Map("latitude" -> a.toDouble, "longitude" -> b.toDouble)
  }

  /**
    * build Convert Coordinates API URL
    *
    * @param args
    * @return String
    */
  def buildConverterUrl(args: Array[String]): String = {
    val baseUrl: String = "http://vldb.gsi.go.jp/sokuchi/surveycalc/tky2jgd/tky2jgd.pl?"
    var params: Map[String, String] = Map("outputType" -> "json", "sokuti" -> "1", "Place" -> "1")

    if (args.length > 0 && args(0) == "g2j") {
      params += ("sokuti" -> "2")
    }
    if (args.length > 1) {
      params += ("latitude" -> args(1))
    }
    if (args.length > 2) {
      params += ("longitude" -> args(2))
    }
    val paramsToString = params.map { case (key, value) => s"${key}=${value}" }.mkString("&")

    baseUrl + paramsToString
  }

  /**
    * Convert Degree Format (DEG/DMS)
    *
    * @param args
    * @return
    */
  def convertDegree(args: Array[String]): Map[String, String] = {
    val convertMode: String = args(0)
    val baseDegree: String = args(1)

    var result: Map[String, String] = Map("DMS" -> baseDegree, "DEG" -> baseDegree)
    if (args.length > 0 && convertMode == "dms2deg") {
      result += ("DMS" -> convertDmsToDeg(baseDegree))
    } else if (args.length > 0 && convertMode == "deg2dms") {
      result += ("DEG" -> convertDegToDms(baseDegree))
    }

    result
  }

  /**
    * Convert Degree DEG format(139.691749) to DMS format (139/41/30.3)
    *
    * @param baseDegree
    * @return String
    */
  def convertDegToDms(baseDegree: String): String = {
    val dmsDegree: Double = baseDegree.toDouble

    val degree = Math.floor(dmsDegree)
    val minute = Math.floor((dmsDegree - degree) * 60)
    val second = Math.floor((((dmsDegree - degree) * 60) - minute) * 60)

    s"${degree.toInt}/${minute.toInt}/${second}"
  }

  /**
    * Convert Degree DMS format (139/41/30.3) to DEG format (139.691749)
    *
    * @param baseDegree
    * @return String
    */
  def convertDmsToDeg(baseDegree: String): String = {
    val degreeArray: Array[String] = baseDegree.split("/")

    val degree= degreeArray(0).toDouble
    val minute= degreeArray(1).toDouble / 60
    val second= degreeArray(2).toDouble / 60 / 60

    s"${degree + minute + second}"
  }
}
