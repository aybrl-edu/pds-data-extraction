package proto.workers

import proto.models.TwitterSearchResponse

import scala.util.{Failure, Success}

class Extractor {

  def extractData(): Unit = {
    val queryParam = Map(
      "query" -> "highway (crash OR carcrash)",
      "max_results" -> "100",
      "tweet.fields" -> "public_metrics",
      "user.fields" -> "name"
    )

    var counter : Int = 0
    val maxReqPerFile : Int = 5
    var listTweets = List[TwitterSearchResponse]()

    while(true) {
      // HTTP Request
      HttpRequester
        .getTwitterSearchQuery("https://api.twitter.com/2/tweets/search/recent", queryParam) match {
        case Success(res) => {
          listTweets = res :: listTweets

          // Write CSV
          if (counter % maxReqPerFile == 0) {
            WriterCSV.writeJsonToCSV(listTweets)
          }


          // Wait
          Thread.sleep(2000)

          // Update
          counter += 2

          // Log
          println(s"${counter} seconds have passed...")
        }
        case Failure(exception) => {}
          println(exception)
      }

    }
  }
}
