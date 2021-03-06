package net.janvsmachine.pgwordvecs

import java.nio.file.Paths

import doobie._
import doobie.implicits._
import cats.implicits._
import cats.effect.IO


/**
  * Command line utility for loading GloVe word vectors into PostgreSQL.
  */
object LoadVectors {

  def main(args: Array[String]): Unit = {
    // TODO:
    // Read needed args, e.g. DB host + post, username & password [optional], schema name, table name.
    // And gloVe file path.

    // A transactor that gets connections from java.sql.DriverManager
    implicit val xa: Transactor.Aux[IO, Unit] = Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver",
      url = "jdbc:postgresql:postgres",
      user = "postgres",
      pass = ""
    )

    val repo = new WordVectorsRepo("glove_6b_50d")
    repo.init()
    repo.loadFromFile(Paths.get("/Users/jan/data/wordvecs/glove/glove.6B.50d.txt"))
  }


  def insertExamples(repo: WordVectorsRepo)(implicit xa: Transactor.Aux[IO, Unit]): Unit = {
    val i1: ConnectionIO[Int] = repo.insert(WordVector("is", Vector(0.6185, 0.64254, -0.46552, 0.3757, 0.74838, 0.53739, 0.0022239, -0.60577, 0.26408, 0.11703, 0.43722, 0.20092, -0.057859, -0.34589, 0.21664, 0.58573, 0.53919, 0.6949, -0.15618, 0.05583, -0.60515, -0.28997, -0.025594, 0.55593, 0.25356, -1.9612, -0.51381, 0.69096, 0.066246, -0.054224, 3.7871, -0.77403, -0.12689, -0.51465, 0.066705, -0.32933, 0.13483, 0.19049, 0.13812, -0.21503, -0.016573, 0.312, -0.33189, -0.026001, -0.38203, 0.19403, -0.12466, -0.27557, 0.30899, 0.48497)))
    val i2: ConnectionIO[Int] = repo.insert(WordVector("was", Vector(0.086888, -0.19416, -0.24267, -0.33391, 0.56731, 0.39783, -0.97809, 0.03159, -0.61469, -0.31406, 0.56145, 0.12886, -0.84193, -0.46992, 0.47097, 0.023012, -0.59609, 0.22291, -1.1614, 0.3865, 0.067412, 0.44883, 0.17394, -0.53574, 0.17909, -2.1647, -0.12827, 0.29036, -0.15061, 0.35242, 3.124, -0.90085, -0.02567, -0.41709, 0.40565, -0.22703, 0.76829, 0.60982, 0.070068, -0.13271, -0.1201, 0.096132, -0.43998, -0.48531, -0.5188, -0.3077, -0.75028, -0.77, 0.3945, -0.16937)))
    val res = (i1, i2).mapN(_ + _).transact(xa).unsafeRunSync
    println(s"Insert examples result: $res")
  }

}
