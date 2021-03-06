import sbt.Keys.scalaVersion

organization := "org.edmcouncil"

organizationName := "Enterprise Data Management Council"

name := "rdf-toolkit"

version := "2.0.0-SNAPSHOT"

startYear := Some(2015)

developers := List(
  Developer(id = "coates", name = "Anthony Coates", email = "", url = new URL("https://github.com/abcoates")),
  Developer(id = "jgeluk", name = "Jacobus Geluk", email = "", url = new URL("https://github.com/jgeluk"))
)

licenses += ("mit", new URL("https://opensource.org/licenses/MIT"))

scalaVersion := "2.12.1"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

javacOptions ++= Seq("-Xlint:unchecked")

Seq(bintrayResolverSettings:_*)

libraryDependencies += "org.scala-lang" % "scala-library" % scalaVersion.value

val owlApiVersion = "5.0.5"

val rdf4jVersion = "2.2"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.5"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.5" withSources()

libraryDependencies += "commons-validator" % "commons-validator" % "1.5.1"

//
// OWLAPI Model Interfaces And Utilities
//
libraryDependencies += ("net.sourceforge.owlapi" % "owlapi-api" % owlApiVersion withSources())
                        .exclude("com.fasterxml.jackson.core", "jackson-core")

libraryDependencies += "net.sourceforge.owlapi" % "owlapi-apibinding" % owlApiVersion withSources()

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "1.3.0" withSources()

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.22" withSources()

libraryDependencies += "org.clapper" %% "avsl" % "1.0.13" withSources()

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test" withSources()

//libraryDependencies += "org.ow2.easywsdl" % "easywsdl-tool-java2wsdl" % "2.3"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.5.0"

//
// Explicit loading of jackson-core to prevent merge issue in sbt-assembly
//
//libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.5.1"

//
// RDF4F Binding And Config
//
libraryDependencies += "org.eclipse.rdf4j" % "rdf4j-runtime" % rdf4jVersion

//
// JSON-LD Java Binding & Config
//
libraryDependencies += "com.github.jsonld-java" % "jsonld-java" % "0.10.0"

//
// Apache Command-line Argument Handling Library used in Tony's Java code
//
libraryDependencies += "commons-cli" % "commons-cli" % "1.4"

//
// jline console utilities
//
libraryDependencies += "jline" % "jline" % "2.14.3"

//
// Argot Command-Line Argument Handling used in the Scala code
//
//libraryDependencies += "org.clapper" %% "argot" % "1.0.3"

//
// Command Line Interface Scala Toolkit
//
// https://github.com/backuity/clist
//
//libraryDependencies += "org.backuity.clist" %% "clist-core"   % "3.2.2"
//libraryDependencies += "org.backuity.clist" %% "clist-macros" % "3.2.2" % "provided"

//
// Scallop CLI processing
//
// https://github.com/scallop/scallop
//
//libraryDependencies += "org.rogach" %% "scallop" % "2.0.6"

//
// Java library JGit used to read git log for the input file and determine what default versionIRI to use when
// publishing.
//
libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.6.1.201703071140-r"

//
// Scala ARM library
//
// https://github.com/jsuereth/scala-arm
//
libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0"

//
// Generate booter.properties, see class org.edmcouncil.main.BooterProperties
//
resourceGenerators in Compile += Def.task {
  BooterPropertiesGenerator(
    (resourceManaged in Compile).value,
    licenses.value,
    organization.value,
    name.value,
    version.value,
    scalaVersion.value
  )
}.taskValue
//resourceGenerators in Compile <+= (
//  resourceManaged in Compile, licenses, organization, name, version, scalaVersion
//) map {
//  (dir, l, o, n, v, s) => BooterPropertiesGenerator(dir, l, o, n, v, s)
//}

fork in run := true

resolvers += JavaNet2Repository

resolvers += "http://weblab.ow2.org/" at "http://weblab.ow2.org/release-repository"

//
// Select the main class. Let it be the Scala main class, not the Java main (SesameRdfFormatter), which can still
// be selected on the command line separately. This prevents the following prompt:
//
// Multiple main classes detected, select one to run:
//
// [1] org.edmcouncil.rdf-toolkit.SesameRdfFormatter
// [2] org.edmcouncil.rdf-toolkit.Main
//
//mainClass in Compile := Some("org.edmcouncil.main.Main")
mainClass in Compile := Some("org.edmcouncil.rdf_toolkit.SesameRdfFormatter")

lazy val depProject = RootProject(uri("https://github.com/modelfabric/sparql-dl-api.git#master"))

lazy val `rdf-toolkit` = project
  .in(file("."))
  .dependsOn(depProject)
  .enablePlugins(AutomateHeaderPlugin)
