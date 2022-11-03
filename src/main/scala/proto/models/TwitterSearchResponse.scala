package proto.models


case class TwitterSearchResponse(data : List[ResponseElement])

case class ResponseElement(text : String, id : String, public_metrics : PublicMetricResponse)

case class PublicMetricResponse(retweet_count : Int, reply_count : Int, like_count : Int, quote_count : Int)



