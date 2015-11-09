package com.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by titanic on 15-11-9.
 */
public class FlatMapValues
{
    public static void main(String[] args)
    {
        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("FlatMapValues");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-hadoop-spark/target/com-hadoop-spark-1.0-SNAPSHOT.jar");

        JavaRDD<String> dataRDD = jsc.textFile("file:///home/titanic/soft/spark-1.5.0/README.md");
        JavaRDD<String> pairRDD = dataRDD.flatMap(new FlatMapFunction<String, String>()
        {
            public Iterable<String> call(String s) throws Exception
            {
                return Arrays.asList(s.split(" "));
            }
        });
        JavaPairRDD<String,Integer> pairRDDxx = pairRDD.mapToPair(new PairFunction<String, String, Integer>()
        {
            public Tuple2<String, Integer> call(String s) throws Exception
            {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        JavaPairRDD<String,Integer> flatMapVaules= pairRDDxx.flatMapValues(new Function<Integer, Iterable<Integer>>()
        {
            public Iterable<Integer> call(Integer integer) throws Exception
            {
                return Arrays.asList(integer + 1);
            }
        });

        Map<String,Integer> map = flatMapVaules.collectAsMap();
        System.out.println(map);

    }
}