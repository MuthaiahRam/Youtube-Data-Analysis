package com.cs267.youtube.task3;

import java.util.ArrayList;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class LikesVsViews {

	public static JavaPairRDD<String, String> getLikesViews(
			JavaRDD<String> input) {

		JavaRDD<String> likesViews = input
				.flatMap(new FlatMapFunction<String, String>() {
					private static final long serialVersionUID = 1L;

					public Iterable<String> call(String x) {
						ArrayList<String> arrayList = new ArrayList<String>();
						if (x.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)").length > 8) {
							arrayList.add(x
									.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[7]
									+ ":"
									+ x.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[8]);

						}
						return arrayList;
					}

				});

		JavaPairRDD<String, String> likesViewsMap = likesViews
				.mapToPair(new PairFunction<String, String, String>() {
					private static final long serialVersionUID = 1L;

					public Tuple2<String, String> call(String s) {

						return new Tuple2<String, String>(s.split(":")[0], s
								.split(":")[1]);

					}
				});

		return likesViewsMap;
	}

}
