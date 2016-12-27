name := "hello-scala-2_11"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "com.typesafe.play" %% "play-json" % "2.5.0"
)
