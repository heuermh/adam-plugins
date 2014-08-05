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

import org.apache.avro.Schema;

import org.apache.spark.SparkContext;

import org.apache.spark.rdd.RDD;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import org.bdgenomics.adam.apis.java.JavaADAMContext;

import org.bdgenomics.adam.plugins.ADAMPlugin;

import scala.Function1;
import scala.Option;

/**
 * Java ADAM plugin.
 *
 * @param <Input> input RDD type
 * @param <Output> output RDD type
 * @author  Michael Heuer
 */
public abstract class JavaADAMPlugin<Input, Output> implements ADAMPlugin<Input, Output> {
    private final Option<Schema> projection;
    // todo:  would be nice if this were org.apache.spark.api.java.function.Function<Input, Boolean> instead
    private final Option<Function1<Input, Object>> predicate;

    protected JavaADAMPlugin() {
        this.projection = Option.empty();
        this.predicate = Option.empty();
    }

    protected JavaADAMPlugin(final Option<Schema> projection,
                             final Option<Function1<Input, Object>> predicate) {
        if (projection == null) {
            throw new NullPointerException("projection must not be null");
        }
        if (predicate == null) {
            throw new NullPointerException("predicate must not be null");
        }
        this.projection = projection;
        this.predicate = predicate;
    }

    @Override
    public final Option<Schema> projection() {
        return projection;
    }

    @Override
    public final Option<Function1<Input, Object>> predicate() {
        return predicate;
    }

    @Override
    public final RDD<Output> run(final SparkContext sparkContext, final RDD<Input> recs, final String args) {
        // todo:  is fromRDD(.., null) valid?
        return run(new JavaADAMContext(sparkContext), JavaRDD.fromRDD(recs, null), args).rdd();
    }

    protected abstract JavaRDD<Output> run(JavaADAMContext ac, JavaRDD<Input> recs, String args);
}
