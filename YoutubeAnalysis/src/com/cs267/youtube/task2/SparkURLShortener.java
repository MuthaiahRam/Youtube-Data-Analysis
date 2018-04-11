package com.cs267.youtube.task2;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class SparkURLShortener {
	
			public static JavaPairRDD<String, Integer> getURLShortener(
				JavaRDD<String> input) {

			JavaRDD<String> shortURLs = input
					.flatMap(new FlatMapFunction<String, String>() {
						private static final long serialVersionUID = 1L;

						public Iterable<String> call(String x) {
							ArrayList<String> arrayList = new ArrayList<String>();
							
							String pattern="https?://[A-Za-z0-9\\.]+";
							Pattern patternn=Pattern.compile(pattern);
							
							if (x.split(",").length >=16 ) {
								String description = x.split(",")[15].trim();
								
									Matcher matcher=patternn.matcher(description);
									while(matcher.find()) {
										arrayList.add(description.substring(matcher.start(),matcher.end()));
									}
								}
								

							
							return arrayList;
						}
					});

			JavaPairRDD<String, Integer> URLCounts = shortURLs
					.mapToPair(new PairFunction<String, String, Integer>() {
						private static final long serialVersionUID = 1L;

						public Tuple2<String, Integer> call(String s) {

							return new Tuple2<String, Integer>(s, 1);

						}
					});

			JavaPairRDD<String, Integer> totalURLCount = URLCounts
					.reduceByKey(new Function2<Integer, Integer, Integer>() {
						private static final long serialVersionUID = 1L;

						public Integer call(Integer x, Integer y) {
							return x + y;
						}
					});

			return totalURLCount;
		}
	


}
