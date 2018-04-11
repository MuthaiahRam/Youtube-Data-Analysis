package com.cs267.youtube.task1;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class SparkWordCloud {

	public static JavaPairRDD<String, Integer> getWordCounts(
			JavaRDD<String> input) {

		JavaRDD<String> RFwords = input
				.flatMap(new FlatMapFunction<String, String>() {
					private static final long serialVersionUID = 1L;

					public Iterable<String> call(String x) {
						ArrayList<String> arrayList = new ArrayList<String>();
						String pattern="[A-Za-z0-9]+";
						Pattern patternn=Pattern.compile(pattern);
						
						if (x.split(",").length > 2) {
							String[] words = x.split(",")[2].trim().split(" ");
							for (String word : words) {
								Matcher matcher=patternn.matcher(word);
								while(matcher.find()) {
									arrayList.add(word.substring(matcher.start(),matcher.end()));
								}
							}
							

						}
						return arrayList;
					}
				});

		JavaPairRDD<String, Integer> wordCounts = RFwords
				.mapToPair(new PairFunction<String, String, Integer>() {
					private static final long serialVersionUID = 1L;

					public Tuple2<String, Integer> call(String s) {

						return new Tuple2<String, Integer>(s, 1);

					}
				});

		JavaPairRDD<String, Integer> totalCount = wordCounts
				.reduceByKey(new Function2<Integer, Integer, Integer>() {
					private static final long serialVersionUID = 1L;

					public Integer call(Integer x, Integer y) {
						return x + y;
					}
				});

		return totalCount;
	}
}
