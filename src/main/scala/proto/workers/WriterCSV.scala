package proto.workers

import com.github.tototoshi.csv.CSVWriter
import proto.models.TwitterSearchResponse

import java.io.{BufferedWriter, FileWriter}


object WriterCSV {
  var counter : Int = 0


  def writeJsonToCSV(tweetsList : List[TwitterSearchResponse]): Unit = {
    // Paths
    val linuxPath: String = s"/tmp/HDFS_TWITTER_TMP_CSV/crashes_${counter}.csv"
    val windowsPath: String = s"./HDFS_TWITTER_TMP_CSV/crashes_${counter}.csv"

    // Init
    val out = new BufferedWriter(new FileWriter(windowsPath))
    val writer = new CSVWriter(out)
    val FileSchema: Array[String] = Array("id", "text", "retweet_count", "reply_count", "like_count", "quote_count")

    // Write
    writer.writeRow(FileSchema)
    tweetsList.foreach(tweet => {
      tweet.data.foreach(element => {
        val arr = Array(element.id, element.text,
          element.public_metrics.retweet_count, element.public_metrics.reply_count, element.public_metrics.like_count,
          element.public_metrics.quote_count)

        writer.writeRow(arr)
      })
    })

    // Close
    out.close()

    // Log
    println(s"${counter} files have been written!")

    // Update
    counter += 1

  }
}
