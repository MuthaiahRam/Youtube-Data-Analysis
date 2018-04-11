package com.cs267.youtube.task5;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class SparkMaxTrend {

	public static JavaPairRDD<String, Integer> getMaxTrend(JavaRDD<String> input) {

		JavaRDD<String> trend = input
				.flatMap(new FlatMapFunction<String, String>() {
					private static final long serialVersionUID = 1L;

					public Iterable<String> call(String x) {
						ArrayList<String> arrayList = new ArrayList<String>();
						String[] array=x.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
						if(array.length>2){
							if(array[0].matches("[a-zA-Z0-9]+")&& array[1].matches("[a-zA-Z0-9\\.]+")){
								arrayList.add(array[0]+":"+array[1]);

							}
						}
						
						return arrayList;
					}
				});

		JavaPairRDD<String, String> trendCount = trend
				.mapToPair(new PairFunction<String, String, String>() {
					private static final long serialVersionUID = 1L;

					public Tuple2<String, String> call(String s) {

						return new Tuple2<String, String>(s.split(":")[0],s.split(":")[1]);

					}
				});
		
		JavaPairRDD<String,Iterable<String>> group=trendCount.groupByKey();
		JavaPairRDD<String, Integer> output= group.mapValues(new Function<Iterable<String>, Integer>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Integer call(Iterable<String> rs){
				Iterator<String> iterator=rs.iterator();
				ArrayList<String> dates=new ArrayList<String>();
				while(iterator.hasNext()){
					dates.add(iterator.next());
				}

		        Collections.sort(dates, new Comparator<String>() {
		            private SimpleDateFormat sdf = new java.text.SimpleDateFormat("yy.dd.MM");
		            @Override
		            public int compare(String o1, String o2) {
		                int result = -1;

		                try {
		                    result = sdf.parse(o1).compareTo(sdf.parse(o2));
		                } catch (ParseException ex) {
		                    ex.printStackTrace();
		                }

		                return result;
		            }
		        });

		        
		        int max=0,i=0,current=1;
		        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yy.dd.MM");
		        //0 1 2 3 4 -5
		        while(i<dates.size()-1){
		        	
		        	
		        	try {
						if(getDateDiff(sdf.parse(dates.get(i)),sdf.parse(dates.get(i+1)),TimeUnit.DAYS)==1){
							current++;
						}
						else{
							if(max<current)
								max=current;
							current=0;
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	i++;
		        	
		        }
		        if(max<current)
					max=current;
			return max;
			}
		});

		return output;
	}

	
	public static long getDateDiff(java.util.Date date, java.util.Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

}
