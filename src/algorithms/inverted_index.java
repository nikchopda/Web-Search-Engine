package algorithms;

/**
 * class name : Tries.java
 * @vesrion Dec 11, 2020
 * @author Nikunj Chopda
 * 
 */

import java.util.LinkedList;
import java.util.Map;

import WebCrawler.WebCrawlerNode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

//Class Trie.java
class Tries implements Serializable  {
	char dt;
	int cnt;
	boolean end;
	int w_number;
	LinkedList<Tries> child;

	// Parameterized Constructor
	public Tries(char n) {
		dt = n;
		cnt = 0;
		end = false;
		w_number = -1;
		child = new LinkedList<Tries>();
	}

	// getChild
	public Tries getChild(char c) {
		if (child != null) {
			for (Tries child : child) {
				if (child.dt == c) {
					return child;
				}
			}
		}
		return null;
	}
}

/**
 * class name : inverted_index.java
 * @vesrion Dec 11, 2020
 * @author Nikunj Chopda
 *
 */
public class inverted_index implements Serializable {

	private static final boolean String = false;
	public static int c_w_num;
	public static Tries r;
	public static HashMap<Integer, HashMap<String, Integer>> i_Id_Array;

	public inverted_index() {
		r = new Tries(' ');
		i_Id_Array = new HashMap<Integer, HashMap<java.lang.String, Integer>>();
		c_w_num = 1;
	}

	
	// It will update the word occurrence in HashMap
	public void updateWordOccurrence(int number, String url) {

		// Document is present
		if (i_Id_Array.get(number) != null) {

			// URL is already visited
			if (i_Id_Array.get(number).get(url) != null) {

				// It will update the occurrence
				i_Id_Array.get(number).put(url, i_Id_Array.get(number).get(url) + 1);
			} else {

				// First time finding of word in URL
				i_Id_Array.get(number).put(url, 1);
			}
		} else {

			// Word found first time
			HashMap<String, Integer> url_map = new HashMap<java.lang.String, Integer>();
			url_map.put(url, 1);
			i_Id_Array.put(number, url_map);
		}
	}


	// Inserting a wrod in Tries for operations
	public void insertWord(String word, String url) {

		// Update occurrence after finding of a word
		int word_number = search(word);
		
		if (word_number != -1) {
			updateWordOccurrence(word_number, url);
			return;
		}

		// If word is not found then it will add a word in Tries
		Tries current = r;
		for (char c : word.toCharArray()) {
			Tries child = current.getChild(c);
			if (child != null) {
				current = child;
			} else {
				current.child.add(new Tries(c));
				current = current.getChild(c);
			}
			current.cnt++;
		}

		// InvertedIndex will be updated
		current.end = true;
		current.w_number = c_w_num;
		updateWordOccurrence(current.w_number, url);
		c_w_num++;
	}


	// Fetching InvertedIndex List
	public void getAllInvertedIndexList() {

		System.out.println("InvertedIndex List : ");
		for (Map.Entry<Integer, HashMap<String, Integer>> index : i_Id_Array.entrySet()) {
			System.out.println(index);
		}
	}

	
	// Return w_number if word is found
	public int search(String word) {
		Tries current = r;
		for (char c : word.toCharArray()) {
			if (current.getChild(c) == null) {
				return -1;
			} else {
				current = current.getChild(c);
			}
		}
		if (current.end) {
			return current.w_number;
		}

		return -1;
	}


	// It will remove word from Tries
	public void remove(String word, String url) {

		// Checking for word is present or not
		int word_number = search(word);
		if (word_number == -1) {
			System.out.println("Word is not found");
			return;
		}

		// Handle InvertedIndex
		i_Id_Array.get(word_number).remove(url);

		// Handle Tries
		Tries current = r;
		for (char c : word.toCharArray()) {
			Tries child = current.getChild(c);
			if (child.cnt == 1) {
				current.child.remove();
				return;
			} else {
				child.cnt--;
				current = child;
			}
		}
		current.end = false;
	}


	// Dynamic method to find edit editdistance for two words
	public int findEditDistance(String string1, String string2) {
		int editdistance[][] = new int[string1.length() + 1][string2.length() + 1];
		for (int i = 0; i <= string1.length(); i++) {
			editdistance[i][0] = i;
		}
		for (int i = 0; i <= string2.length(); i++) {
			editdistance[0][i] = i;
		}
		for (int i = 1; i < string1.length(); i++) {
			for (int j = 1; j < string2.length(); j++) {
				if (string1.charAt(i) == string2.charAt(j)) {
					editdistance[i][j] = Math.min(Math.min((editdistance[i - 1][j]) + 1, (editdistance[i][j - 1]) + 1),
							(editdistance[i - 1][j - 1]));
				} else {
					editdistance[i][j] = Math.min(Math.min((editdistance[i - 1][j]) + 1, (editdistance[i][j - 1]) + 1),
							(editdistance[i - 1][j - 1]) + 1);
				}
			}
		}
		return editdistance[string1.length() - 1][string2.length() - 1];
	}


	// It will load the data
	public void loadData(Collection collect, String url) {

		//	It will take all data and insert in to Tries
		Iterator<String> i = collect.iterator();
		while (i.hasNext()) {
			insertWord(i.next(), url);
		}
	}

	// It will load the data
	public void updatedloadData(Collection<WebCrawlerNode> collect) {

			// It will take all data and insert in to Tries
			Iterator<WebCrawlerNode> i = collect.iterator();
			WebCrawlerNode wc_nodes= null;
			while (i.hasNext()) {
				
				wc_nodes = i.next();
				
				Collection<String> single = wc_nodes.getTextContentsTokens();
				Iterator<String> itr1 = single.iterator();
				while(itr1.hasNext()){
					
					String in= itr1.next();
					//System.out.println(in);
					insertWord(in,wc_nodes.getNodeBaseUrl());
				}
			}
		}

	// It will return array of String with top urls with match
	public String[] getTopUrls(String word) {
		
		int document_number = search(word);
		System.out.println("Word occurrence at " + document_number);
		if (document_number != -1) {

			int top_url_count = 5;
			int i = 0;

			// It will store all url with word match
			HashMap<String, Integer> matched_urls = i_Id_Array.get(document_number);

			// Array creation of freq for QuickSelect 
			final int[] freq = new int[matched_urls.size()];
			for (final int value : matched_urls.values()) {
				freq[i++] = value;
			}

			// It will call QuickSelect for finding of top 5 urls
			quickselect qs = new quickselect();
			final int kthLargestFreq = qs.findKthLargest(freq, top_url_count);

			// Creation of string array whose freq count is greater than kth largest element
			final String[] topkitems = new String[top_url_count];
			i = 0;
			for (final java.util.Map.Entry<String, Integer> trie : matched_urls.entrySet()) {
				if (trie.getValue().intValue() >= kthLargestFreq) {
					topkitems[i++] = trie.getKey();
					if (i == top_url_count) {
						break;
					}
				}
			}
			return topkitems;
		} else {
			System.out.println("No occurrence of word");
			return null;
		}
	}

	// guessing the word
	public String[] guessWord(String pre) {
		Tries current = r;
		int length = 0;
		String guessedword[] = null;
		
		// It will get the count of available words
		for (int i = 0; i < pre.length(); i++) {
			if (current.getChild(pre.charAt(i)) == null) {
				System.out.println("Suggestions not found");
				return null;
			} else if (i == (pre.length() - 1)) {
				current = current.getChild(pre.charAt(i));
				System.out.println("Char reading = "+ pre.charAt(i));
				System.out.println("Current value =" + current.dt + "===Current count= " + current.cnt);
				length = current.cnt;
			} else {
				current = current.getChild(pre.charAt(i));
			}
		}
		System.out.println("Number of words =" + length);

		// Output creation for guessWord
		guessedword = new String[length];
		for (int i = 0; i < guessedword.length; i++) {
			guessedword[i] = pre;
		}

		// Array list to find children
		java.util.ArrayList<Tries> currentchild = new java.util.ArrayList<Tries>();
		java.util.ArrayList<Tries> upcomingchild = new java.util.ArrayList<Tries>();
		HashMap<Integer, String> completion_of_words = new HashMap<Integer, String>();

		// Getting prefix child
		int counter = 0;
		if (current.child != null) {
			for (Tries trie : current.child) {
				currentchild.add(trie);
			}
		}

		// Children iteration
		while (currentchild.size() != 0) {
			for (Tries trie : currentchild) {

				// String word generation
				while (completion_of_words.get(counter) != null) {
					counter++;
				}
				for (int j = 0; j < trie.cnt; j++) {
				
					 
					 // Finding for correct word
					if (trie.end && j == (trie.cnt-1)) {
						completion_of_words.put(counter, "done");
					}
					 System.out.println("counter " + counter);
					guessedword[counter] = guessedword[counter] + trie.dt;
					counter++;
				}

				// All Character iteration for child
				for (Tries e1 : trie.child) {
					upcomingchild.add(e1);
				}
			}

			counter = 0;

			currentchild = new java.util.ArrayList<Tries>();
			if (upcomingchild.size() > 0) {
				currentchild = upcomingchild;
				upcomingchild = new java.util.ArrayList<Tries>();
			}
		}

		// Output of guessed word
		for (String s : guessedword) {
			System.out.println("Guessed word =" + s);
		}

		return guessedword;
	}

	// It will find correct words if word is not found in database
	public String[] findCorrection(String word) {
		String gueesedwordlist[] = guessWord(word.substring(0, 1));
		ArrayList<String> correction = new ArrayList<String>();
		for (String s : gueesedwordlist) {
			if (findEditDistance(word, s) == 1) {
				correction.add(s);
			}
		}

		String suggestedWord[] = (String[]) correction.toArray(new String[0]);
		System.out.println("----- Showing result of corrected word -----");
		for (String s : suggestedWord) {
			System.out.println(s);
		}

		return suggestedWord;

	}

	public static void main(String[] arr) {
		// Main method of this class
	}

}
