package proto

import proto.workers.Extractor

object Main {
  def main(args: Array[String]): Unit = {
    val extractor : Extractor = new Extractor
    extractor.extractData()
  }
}
