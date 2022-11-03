package proto.workers

import scalaj.http._
import org.json4s._
import org.json4s.native.JsonMethods._
import proto.models.TwitterSearchResponse

import scala.util.{Failure, Success, Try}

object HttpRequester {

  val authToken : String = ""

  def getTwitterSearchQuery(endpoint : String, query : Map[String, String]): Try[TwitterSearchResponse] = {

    var response : HttpResponse[String] = null

    try {
      response = Http(endpoint)
        .header("Authorization", "Bearer " + authToken)
        .params(query)
        .asString

    } catch {
      case e: Exception => Failure(new Throwable(e.getMessage()))
    }

    implicit val formats = DefaultFormats
    val formattedRes = parse(response.body).extract[TwitterSearchResponse]
    Success(formattedRes)

  }
}
