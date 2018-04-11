package com.cs267.youtube.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;


public class Test {
	
	public static void main(String[] args) {
		String dates[] = {"17.08.12","17.07.12","17.06.12"};
        Arrays.sort(dates, new Comparator<String>() {
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

        for (String date : dates) {
            System.out.println(date);
        }
        int max=0,i=0,current=1;
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yy.dd.MM");//18.09.02
        while(i<dates.length-1){
        	
        	
        	try {
				if(getDateDiff(sdf.parse(dates[i]),sdf.parse(dates[i+1]),TimeUnit.DAYS)==1){
					current++;
					System.out.println(current);
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
        System.out.println(max);
        



}
	public static long getDateDiff(java.util.Date date, java.util.Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date.getTime();
	   // System.out.println(timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS));
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	

}
