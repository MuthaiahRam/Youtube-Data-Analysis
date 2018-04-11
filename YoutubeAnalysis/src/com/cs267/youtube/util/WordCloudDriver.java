package com.cs267.youtube.util;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import com.cs267.youtube.task1.SparkWordCloud;

public class WordCloudDriver {
	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.err.println("usage: WordCloud <input-file> <output-dir> ");
			System.exit(1);
		}
		String inputFile = args[0];
		String outputDir = args[1];

		SparkConf conf = new SparkConf().setAppName("WordCloud");
		SparkContext spark = new SparkContext(conf);

		JavaRDD<String> input = spark.textFile(inputFile, 1).toJavaRDD();
		final String header=input.first(); input = input.filter(new Function<String,
				  Boolean>() { private static final long serialVersionUID = 1L;
				  
				 @Override public Boolean call(String string) throws Exception { return
				 !(string.contains(header)); } });

		JavaPairRDD<String, Integer> wordCounts = SparkWordCloud
				.getWordCounts(input);

		if (wordCounts != null) {

			wordCounts.saveAsTextFile(outputDir + "_wordcloud");
		}

		spark.stop();
	}

}
