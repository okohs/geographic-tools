import org.scalatest._

class GeographicConverterSpec extends FlatSpec{
  val geographicConverter = new GeographicConverter

  val gLatitude:String = "35.670392"
  val gLongitude:String = "139.775846"
  val jLatitude:String = "35.667153"
  val jLongitude:String = "139.779080"
  val dmsDegree:String = "139/41/30.3"
  val degDegree:String = "139.691749"

  "convertCoordinates method" should "convert latitude & longitude geographic coordinates" in{
    assert(
      geographicConverter.convertCoordinates(Array("j2g", jLatitude, jLongitude))
        === Map("latitude" -> gLatitude, "longitude" -> gLatitude))
    assert(
      geographicConverter.convertCoordinates(Array("g2j", gLatitude, gLongitude))
        === Map("latitude" -> jLatitude, "longitude" -> jLatitude))
  }

  "convertDegree method" should "convert degree format" in {
    assert(
      geographicConverter.convertDegree(Array("dms2deg", dmsDegree))
      === Map("DMS" -> dmsDegree, "DEG" -> degDegree))
    assert(
      geographicConverter.convertDegree(Array("deg2dms", degDegree))
        === Map("DMS" -> dmsDegree, "DEG" -> degDegree))
  }

  "buildConverterUrl method" should "build URL to call convert coordinates API" in {
    assert(
      geographicConverter.buildConverterUrl(Array("j2g", jLatitude, jLongitude)) ===
      "http://vldb.gsi.go.jp/sokuchi/surveycalc/tky2jgd/tky2jgd.pl?outputType=json&sokuti=1&Place=1&latitude="+jLatitude+"&longitude="+jLongitude)
    assert(
      geographicConverter.buildConverterUrl(Array("g2j", gLatitude, gLongitude)) ===
      "http://vldb.gsi.go.jp/sokuchi/surveycalc/tky2jgd/tky2jgd.pl?outputType=json&sokuti=2&Place=1&latitude="+gLatitude+"&longitude="+gLongitude)
  }

  "convertDegToDms method" should "convert Degree from DEG format to DMS format" in {
    assert(
      geographicConverter.convertDegToDms(degDegree) === dmsDegree
    )
  }

  "convertDmsToDeg method" should "convert Degree from DEG format to DMS format" in {
    assert(
      geographicConverter.convertDmsToDeg(dmsDegree) === degDegree
    )
  }
}
