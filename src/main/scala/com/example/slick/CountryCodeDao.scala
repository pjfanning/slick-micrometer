package com.example.slick

case class CountryCode(id: Option[Long] = None, code: String, name: String)

object CountryCodeDao {
  import slick.jdbc.H2Profile.api._

  private val CountryCodeTableQuery = TableQuery[CountryCodeTable]

  def createSchema(): DBIOAction[Unit, NoStream, Effect.All] = CountryCodeTableQuery.schema.create

  def insert(countryCode: CountryCode): DBIOAction[Option[Long], NoStream, Effect.All] =
    (CountryCodeTableQuery returning CountryCodeTableQuery.map(_.id)) += countryCode

  def all: DBIOAction[Seq[CountryCode], NoStream, Effect.Read] = CountryCodeTableQuery.result

  // scalastyle:off
  private class CountryCodeTable(tag: Tag) extends Table[CountryCode](tag, "country_code") {
    def id= column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def code = column[String]("code")
    def name = column[String]("name")
    def * = (id, code, name) <> (CountryCode.tupled, CountryCode.unapply _)
  }
}
