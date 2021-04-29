package utilities;


/**
 * 
 */


import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

/**
 * @version Dec 02, 2020
 * @author Arti Vekaria
 * Purpose: General Class for common and usefull methods
 */
public class Util {
	
	public static boolean isPrint_Debug_On = false;
	
	
	/**
	 *  To print messages in console if the debug switch called isPrint_Debug_On is set to true. 
	 *  @param print_value 
	 *  String to be printed as debug message
	 */
	public static void printDebug(String print_value) {
		if (isPrint_Debug_On) {
			System.out.println(print_value);
		}
	}
	
	/**
	 * 
	 * 	To print messages in the console if the Debug switch called isPrint_Debug_On is set to true.
	 * @param print_value 
	 * String to be printed as Debug message
	 */
	public static void printDebug(boolean to_print, String print_value) {
		if (to_print) {
			System.out.println(print_value);
		}
	}

	/**
	 * 
	 * 	To print messages in the Console if the Debug switch called isPrint_Debug_On is set to true.	 * 
	 *  @param print_value 
	 *  String to be printed as Debug message
	 */
	public static void printDebug(boolean to_print, Object[] array1) {
		if (to_print) {
			if (array1 != null ) {
				System.out.println("[");
				for(Object bucket : array1) {
					if (bucket != null) {
						System.out.print(bucket + ",");	
					}
				}
				System.out.println("]");
			}
		}
	}
	
	
	/**
	 * 
	 * 	To print messages in the Console if the Debug switch called isPrint_Debug_On is set to true.
	 *  @param print_value 
	 *  String to be printed as Debug message
	 */
	public static void printDebug(boolean to_print, long[] array1) {
		if (to_print) {
			if (array1 != null ) {
				System.out.println("[");
				for(long bucket : array1) {
					System.out.print(bucket + ",");	
				}
				System.out.println("]");
			}
		}
	}
	
	
	/**
	 * 	To returns time in msec
	 *  @return Returns de Time at the momento in msec
	 */
	public static long getTimeStampMilis() {
		return System.currentTimeMillis();	
	}
	
	/**
	 * 	To return de time in nsec
	 *  @return Returns de Time at the moment in nsec
	 */
	public static long getTimeNanoTime() {
		return System.nanoTime();	
	}
	
	
	/**
	 * 	To copy an array1 of TYPE LONG into a new one of primitige type long 
	 *  @param array_long
	 *  @return newarray_long
	 */
	public static long[] makeCopy(Long[] array_long) {
		long[] newarray_long = null;
		if (array_long != null) {
			newarray_long = new long[array_long.length];
			for (int i=0; i<array_long.length ;i++) {
				newarray_long[i] = array_long[i].longValue();
			}
		}
		return newarray_long;
	}
	
	

}
