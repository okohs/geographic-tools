object Converter {
  def main(args: Array[String]): Unit = {
    val args = Array("gtoj", "35.6703917", "139.7758458")
    if(args(0) == "jtog"){
      convertGtoJ(args)
    }else if (args(0) == "gtoj"){
      convertGtoJ(args)
    }else{
      println("your input data is wrong.")
      println("please input jtog/gtoj latitude longitude")
      println("'jtog' means converting Japanese geographic coordinates to Global geographic coordinates.")
    }
  }

  def convertJtoG(params: Array[String]):Array[Double] = {
    var result: Array[Double] = null
    result = new Array[Double](2)
    val convertUrl = buildConverterUrl(params)
    val result = scala.io.Source.fromURL(convertUrl).mkString
  }

  def convertGtoJ(params: Array[String]):Array[Double] = {
    var result: Array[Double] = null
    result = new Array[Double](2)
    val convertUrl = buildConverterUrl(params)
    val result = scala.io.Source.fromURL(convertUrl).mkString
  }

  def buildConverterUrl(params:Array[String]):String = {
    val baseUrl = "http://vldb.gsi.go.jp/sokuchi/surveycalc/tky2jgd/tky2jgd.pl?"
    var params = Map('outputType -> "json", 'sokuti -> "1", 'Place -> "1", 'latitude -> "35.6730837", 'longitude -> "139.7599029")

    if (Array(0) == "gtoj") {
      params('sokuti -> "2")
    }
    params('latitude -> params(1))
    params('longitude -> params(2))

    val paramsToString = params.map { case (key, value) => s"${key}=${value}" }.mkString("&")

    val convertUrl = baseUrl + paramsToString
  }
}
