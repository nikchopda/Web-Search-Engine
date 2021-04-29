package algorithms;
/**
 @version December 2, 2020
 * @author Yash Anghan
 * Purpose: This class is used to get the kth largest elements from the list of array
 */
public class quickselect {
	 public int findKthLargest(int[] numbers, int j) {
	        int initial = 0, ended = numbers.length - 1, index = numbers.length - j;
	        while (initial < ended) {
	            int pivot_1 = partion(numbers, initial, ended);
	            if (pivot_1 < index) initial = pivot_1 + 1; 
	            else if (pivot_1 > index) ended = pivot_1 - 1;
	            else return numbers[pivot_1];
	        }
	        return numbers[initial];
	    }
	    
	    private int partion(int[] numbers, int initial, int ended) {
	        int pivot_1 = initial, temp;
	        while (initial <= ended) {
	            while (initial <= ended && numbers[initial] <= numbers[pivot_1]) initial++;
	            while (initial <= ended && numbers[ended] > numbers[pivot_1]) ended--;
	            if (initial > ended) break;
	            temp = numbers[initial];
	            numbers[initial] = numbers[ended];
	            numbers[ended] = temp;
	        }
	        temp = numbers[ended];
	        numbers[ended] = numbers[pivot_1];
	        numbers[pivot_1] = temp;
	        return ended;
	    }
	    
	    public static void main(String[] arr) {
		
	    	// This class is created for testing
		}
}
