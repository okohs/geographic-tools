import play.api.libs.json._

object Converter {
  def main(args: Array[String]): Unit = {
    val args = Array("gtoj", "35.6703917", "139.7758458")
    if(args.length > 0) {
      println(convertCoordinates(args))
    }else{
      println("your input data is wrong.")
      println("please input jtog/gtoj latitude longitude")
      println("'jtog' means converting Japanese geographic coordinates to Global geographic coordinates.")
    }
  }

  def convertCoordinates(params: Array[String]): Map[String, Double] = {
    var result: Array[Double] = new Array[Double](2)
    val convertUrl = buildConverterUrl(params)
    val json = Json.parse(scala.io.Source.fromURL(convertUrl).mkString)
    val a = (json \ "OutputData" \ "latitude").get.asInstanceOf[JsString].value
    val b =  (json \ "OutputData" \ "longitude").get.asInstanceOf[JsString].value
    Map("latitude" -> a.toDouble, "longitude" -> b.toDouble)
  }

  def buildConverterUrl(args: Array[String]):String = {
    val baseUrl = "http://vldb.gsi.go.jp/sokuchi/surveycalc/tky2jgd/tky2jgd.pl?"
    var params = Map("outputType" -> "json", "sokuti" -> "1", "Place" -> "1")

    if (args.length > 0 && args(0) == "gtoj") {
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
