package tk.wurst_client.utils;

import java.math.BigDecimal;

 public class MathUtils
 {
   public static double round(double value, int places) 
   {
	   if (places < 0)
		   throw new IllegalArgumentException();
	   BigDecimal bigdecimal = new BigDecimal(value);
	   bigdecimal = bigdecimal.setScale(places, java.math.RoundingMode.HALF_UP);
	   return bigdecimal.doubleValue();
   }
 }