/**
 * Copyright 2014 held jointly by the individual authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.heuermh.adam.plugins

import org.bdgenomics.adam.plugins.ADAMPlugin
import org.bdgenomics.formats.avro.AlignmentRecord
import org.apache.avro.Schema
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

/**
 * Count alignments.
 * 
 * @author  Michael Heuer
 */
class CountAlignments extends ADAMPlugin[AlignmentRecord, Tuple2[String, Int]] with Serializable {
   override def projection: Option[Schema] = None
   override def predicate: Option[(AlignmentRecord) => Boolean] = None

   override def run(sc: SparkContext, recs: RDD[AlignmentRecord], args: String): RDD[Tuple2[String, Int]] = {
     recs.map(rec => if (rec.getReadMapped) rec.getContig.getContigName else "unmapped")
       .map(contigName => (contigName, 1))
       .reduceByKey(_ + _)
   }
}
