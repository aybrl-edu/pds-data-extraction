package proto.workers

import org.apache.spark.sql.SparkSession

object SaveCSV {
  val sparkSession = SparkSession.builder().appName("psd-data-extraction").getOrCreate()

  def saveCSVToHDFS(): Unit = {

  }
}
