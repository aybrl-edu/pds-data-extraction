package proto.workers

import com.github.tototoshi.csv.CSVWriter
import org.apache.spark.sql.{SaveMode, SparkSession}
import proto.models.TwitterSearchResponse

import java.io.{BufferedWriter, FileWriter}


object WriterCSV {
  var counter : Int = 0

  def writeJsonToHDFS(tweetsList: List[TwitterSearchResponse]): Unit = {
    val sparkSession = SparkSession.builder().appName("psd-data-extraction").getOrCreate()
    // Paths
    val linuxHDFSPath: String = s"hdfs://172.31.250.225:9000/user/hc-name-node/twitter_data/crashes_${counter}.csv"
    val windowsPath: String = s"./HDFS_TWITTER_TMP_CSV/crashes_${counter}.csv"

    // Init
    val FileSchema: Array[String] = Array("id", "text", "retweet_count", "reply_count", "like_count", "quote_count")

    // Write
    val dataSeq: Seq[Array[String]] = Seq(FileSchema)

    tweetsList.foreach(tweet => {
      tweet.data.foreach(element => {
        val arr = Array(element.id, element.text,
          element.public_metrics.retweet_count, element.public_metrics.reply_count, element.public_metrics.like_count,
          element.public_metrics.quote_count)
        dataSeq :+ arr
      })
    })

    import sparkSession.implicits._
    val df = dataSeq.toDF().coalesce(1)

    // Writing Dataframe as csv file
    df.write.mode(SaveMode.Overwrite).csv(linuxHDFSPath)

    // Log
    println(s"${counter} files have been written to HDFS!")

    // Update
    counter += 1

  }
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
