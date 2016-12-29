import play.api.libs.json._

object Converter {
  def main(args: Array[String]): Unit = {
//    val args = Array("j2g","35.667153","139.779080")
//    val args = Array("g2j","35.6703917", "139.7758458")
//    val args = Array("dms2deg", "139/41/30.3")
//    val args = Array("deg2dms", "139.691749")
    if (args.length > 2 && (args(0) == "g2j" || args(0) == "j2g")) {
      println(convertCoordinates(args))
    }else if(args.length > 1 && (args(0) == "dms2deg" || args(0) == "deg2dms")){
      println(convertDegree(args))
    }else{
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

  /**
    * 度十進と度分秒を変換する
    *
    * @param args
    * @return
    */
  def convertDegree(args: Array[String]): Map[String, String] = {
    val convertMode:String = args(0)
    val baseDegree:String = args(1)

    var result:Map[String, String] = Map("DMS" -> baseDegree, "DEG" -> baseDegree)
    if (args.length > 0 && convertMode == "dms2deg"){
      var convertResult:String = convertDmsToDeg(baseDegree)
      result += ("DMS" -> convertResult)
    }else if (args.length > 0 && convertMode == "deg2dms"){
      var convertResult:String =  convertDegToDms(baseDegree)
      result += ("DEG" -> convertResult)
    }

    result
  }

  /**
    * Convert Degree DEG format(139.691749) to DMS format (139/41/30.3)
    * @param baseDegree
    * @return String
    */
  def convertDegToDms(baseDegree:String):String = {
    val degreeArray:Array[String] = baseDegree.split("/")
    val convertResult:Double = degreeArray(0).toDouble +(degreeArray(1).toDouble / 60) + (degreeArray(2).toDouble / 60 / 60)

    convertResult.toString
  }

  /**
    * Convert Degree DMS format (139/41/30.3) to DEG format (139.691749)
    * @param baseDegree
    * @return String
    */
  def convertDmsToDeg(baseDegree: String):String = {
    val dmsDegree:Double = baseDegree.toDouble

    val degree:Double = Math.floor(dmsDegree)
    val minute:Double = Math.floor(degree*60)
    val second:Double = Math.floor(minute*60)

    val convertResult:String = degree.toString + "/" + minute.toString + "/" + second.toString
    convertResult
  }
}
