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

import org.bdgenomics.adam.plugins.ADAMPlugin;

import org.bdgenomics.formats.avro.ADAMRecord;

import scala.Function1;
import scala.Option;
import scala.Tuple2;

/**
 * Count alignments implemented in Java.
 *
 * @author  Michael Heuer
 */
public final class JavaCountAlignments implements ADAMPlugin<ADAMRecord, Tuple2<CharSequence, Integer>>, Serializable {

    @Override
    public Option<Function1<ADAMRecord, Object>> predicate() {
        return Option.empty();
    }

    @Override
    public Option<Schema> projection() {
        return Option.empty();
    }

    @Override
    public RDD<Tuple2<CharSequence, Integer>> run(final SparkContext sparkContext, final RDD<ADAMRecord> recs, final String args) {
        JavaRDD<ADAMRecord> javaRecs = JavaRDD.fromRDD(recs, null);

        JavaRDD<CharSequence> contigNames = javaRecs.map(new Function<ADAMRecord, CharSequence>() {
                @Override
                public CharSequence call(final ADAMRecord rec) {
                    return rec.getReadMapped() ? rec.getContig().getContigName() : "unmapped";
                }
            });

        JavaPairRDD<CharSequence, Integer> counts = contigNames.mapToPair(new PairFunction<CharSequence, CharSequence, Integer>() {
                @Override
                public Tuple2<CharSequence, Integer> call(final CharSequence contigName) {
                    return new Tuple2<CharSequence, Integer>(contigName, Integer.valueOf(1));
                }
            });

        JavaPairRDD<CharSequence, Integer> reducedCounts = counts.reduceByKey(new Function2<Integer, Integer, Integer>() {
                @Override
                public Integer call(final Integer value0, final Integer value1) {
                    return Integer.valueOf(value0.intValue() + value1.intValue());
                }
            });

        return reducedCounts.rdd();
    }
}
