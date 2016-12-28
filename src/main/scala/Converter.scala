import play.api.libs.json._

object Converter {
  def main(args: Array[String]): Unit = {
    val args = Array("g2j", "35.6703917", "139.7758458")
    if (args.length > 2 && (args(0) == "g2j" || args(0) == "j2g")) {
      println(convertCoordinates(args))
    }else if(args.length > 1 && (args(0) == "dms2deg" || args(0) == "deg2dms")){
      println(convertDegree(args))
    }else{
      println("your input data is wrong.please input following parameter.")
      println("Case1. convert latitude & longitude")
      println("  <j2g/g2j> latitude longitude")
      println("  'j2g' means converting Japanese geographic coordinates to Global geographic coordinates.")
      println("Case2. convert degree")
      println("  <dms2deg/deg2dms> degree")
      println("  'dms2deg' means converting degree from DMS format (139/41/30.3) to DEG format (139.691749).")
    }
  }

  /**
    * convert Geographic Coordinates
    * @param params
    * @return String
    */
  def convertCoordinates(params: Array[String]): Map[String, Double] = {
    var result: Array[Double] = new Array[Double](2)
    val convertUrl = buildConverterUrl(params)
    val json = Json.parse(scala.io.Source.fromURL(convertUrl).mkString)
    val a = (json \ "OutputData" \ "latitude").get.asInstanceOf[JsString].value
    val b =  (json \ "OutputData" \ "longitude").get.asInstanceOf[JsString].value
    Map("latitude" -> a.toDouble, "longitude" -> b.toDouble)
  }

  /**
    * build Convert Coordinates API URL
    * @param args
    * @return String
    */
  def buildConverterUrl(args: Array[String]): String = {
    val baseUrl:String = "http://vldb.gsi.go.jp/sokuchi/surveycalc/tky2jgd/tky2jgd.pl?"
    var params:Map[String, String] = Map("outputType" -> "json", "sokuti" -> "1", "Place" -> "1")

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
}
