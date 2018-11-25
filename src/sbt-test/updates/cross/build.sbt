import com.timushev.sbt.updates.versions.Version

scalaVersion := "2.10.4"
crossScalaVersions := Seq("2.10.4", "2.11.5")

libraryDependencies += "org.specs2" %% "specs2" % "3.1"

TaskKey[Unit]("check") := {
  val updates = dependencyUpdatesData.value
  val found = updates.keys.exists {
    case m if m.organization == "org.scala-lang" => false
    case m if m.organization == "org.specs2" =>
      val versions = updates(m)
      if (versions.contains(Version("3.7")))
        sys.error(s"Wrong update versions: $versions")
      true
    case other =>
      if (true) // SBT bug workaround: The setting/task `X / y` is not being invoked inside the task definition
        sys.error(s"Wrong update key: $other")
      false
  }
  if (!found)
    sys.error("No updates for specs2 found")
  ()
}
