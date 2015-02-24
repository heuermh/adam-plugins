/**
 * Copyright 2014-2015 held jointly by the individual authors.
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
package com.github.heuermh.adam.plugins;

import java.io.Serializable;

import org.apache.avro.Schema;

import org.apache.spark.SparkContext;

import org.apache.spark.rdd.RDD;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import org.bdgenomics.adam.apis.java.JavaADAMContext;

import org.bdgenomics.adam.plugins.ADAMPlugin;

import org.bdgenomics.formats.avro.AlignmentRecord;

import scala.Function1;
import scala.Option;
import scala.Tuple2;

/**
 * Count alignments per read implemented in Java.
 *
 * @author  Michael Heuer
 */
public final class JavaCountAlignmentsPerRead extends JavaADAMPlugin<AlignmentRecord, Tuple2<String, Integer>> implements Serializable {

    @Override
    public JavaRDD<Tuple2<String, Integer>> run(final JavaADAMContext ac, final JavaRDD<AlignmentRecord> recs, final String args) {

        JavaRDD<String> contigNames = recs.map(new Function<AlignmentRecord, String>() {
                @Override
                public String call(final AlignmentRecord rec) {
                    return rec.getReadMapped() ? rec.getReadName() : "unmapped";
                }
            });

        JavaPairRDD<String, Integer> counts = contigNames.mapToPair(new PairFunction<String, String, Integer>() {
                @Override
                public Tuple2<String, Integer> call(final String readName) {
                    return new Tuple2<String, Integer>(readName, Integer.valueOf(1));
                }
            });

        JavaPairRDD<String, Integer> reducedCounts = counts.reduceByKey(new Function2<Integer, Integer, Integer>() {
                @Override
                public Integer call(final Integer value0, final Integer value1) {
                    return Integer.valueOf(value0.intValue() + value1.intValue());
                }
            });

        // todo:  seems like there should be a more direct way
        return JavaRDD.fromRDD(reducedCounts.rdd(), null);
    }
}
