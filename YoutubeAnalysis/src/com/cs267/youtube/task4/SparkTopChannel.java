package com.cs267.youtube.task4;

import java.util.ArrayList;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class SparkTopChannel {
	public static JavaPairRDD<String, Integer> getTopChannels(
			JavaRDD<String> input) {

		JavaRDD<String> likesViews = input
				.flatMap(new FlatMapFunction<String, String>() {
					private static final long serialVersionUID = 1L;
					String pattern="[A-Za-z0-9]+";
					
					public Iterable<String> call(String x) {
						ArrayList<String> arrayList = new ArrayList<String>();
						if (x.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)").length > 3) {
							String channel=x.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[3];
							if(channel.matches(pattern))
							arrayList.add(channel);
						}
						return arrayList;
					}

				});

		JavaPairRDD<String, Integer> likesViewsMap = likesViews
				.mapToPair(new PairFunction<String, String, Integer>() {
					private static final long serialVersionUID = 1L;

					public Tuple2<String, Integer> call(String s) {

						return new Tuple2<String, Integer>(s, 1);

					}
				});
		JavaPairRDD<String, Integer> totalLikesViewsMap = likesViewsMap
				.reduceByKey(new Function2<Integer, Integer, Integer>() {
					private static final long serialVersionUID = 1L;

					public Integer call(Integer x, Integer y) {
						return x + y;
					}
				});

		return totalLikesViewsMap;
	}

}
