package net.janvsmachine.pgwordvecs

import doobie._
import cats.effect._

/**
  * Command line utility for loading GloVe word vectors into PostgreSQL.
  */
object LoadVectors extends App {

  // TODO:
  // Read needed args, e.g. DB host + post, username & password [optional], schema name, table name.
  // And gloVe file path.
  // Stream the gloVe file (using FS2??) into the DB

  // A transactor that gets connections from java.sql.DriverManager
  implicit val xa: Transactor.Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql:postgres",
    user = "postgres",
    pass = ""
  )

  populateTable()

  val repo = new WordVectorsRepo("glove_6b_50d")
  val wordVec: Option[WordVector] = repo.vectorForWord("the")
  println(s"Vector for 'the': $wordVec")

  val mostSimilar: Option[Vector[WordVector]] = wordVec.map(wv => repo.mostSimilarVectors(wv.vector))
  println(s"Most similar vectors for 'the':")
  mostSimilar.foreach(_.foreach(println))

  def populateTable()(implicit tx: Transactor.Aux[IO, Unit]): Unit = {
    // TODO: Create schema here if it doesn't exist already? Might be a simple way to do things?
    // TODO!
  }

}