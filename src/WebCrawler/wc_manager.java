package WebCrawler;

/**
 @version December 2, 2020
 * @author Yash Anghan
 * Purpose: This class is created to crawl given urls
 */

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import algorithms.inverted_index;
import utilities.Util;

public class wc_manager {
	
	private static final String WC_NODE = "WebCrawlerNodes";
	private static final String TYPE = ".ser";

	public static boolean saveWebCrawlerNode (String nameOfGroup, Collection<WebCrawlerNode> nodes) throws IOException {
		boolean saveData = false; 
		FileOutputStream fileOutput = new FileOutputStream(WC_NODE + nameOfGroup + TYPE);
		ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);
		outputStream.writeObject(nodes);
		outputStream.close();
		fileOutput.close();
		saveData = true;
		Util.printDebug(true, "Done Serialization at: " + fileOutput.toString());
		return saveData;
	}
   
   public static Collection<WebCrawlerNode> loadWebCrawlerNodes (String nameOfGroup) throws IOException, ClassNotFoundException {
	   Collection<WebCrawlerNode> nodes = null;
	   FileInputStream fileInput = new FileInputStream(WC_NODE + nameOfGroup + TYPE);
	   ObjectInputStream inputStream = new ObjectInputStream(fileInput);
	   nodes = (Collection<WebCrawlerNode>) inputStream.readObject();
	   inputStream.close();
	   fileInput.close();    	  
       return nodes;
    }

	public static boolean saveSerializableObject(String suffixName, Object objectToSave) throws IOException {
		boolean saveData = false; 
		FileOutputStream fileOutput = new FileOutputStream("E:\\UWinStudy\\websearchengine\\"+objectToSave.getClass().getSimpleName() + "-" + suffixName + TYPE);
		ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);
		outputStream.writeObject(objectToSave);
		outputStream.close();
		fileOutput.close();
		saveData = true;
		Util.printDebug(true, " Done Serialization at: " + fileOutput.toString());
		return saveData;
	}
	

	public static Object loadSerializedObject (String suffixName, String className) throws IOException, ClassNotFoundException {
		Object objectLoaded = null;
		System.outputStream.println("Serialized Class File to open is: " + className + "-" + suffixName + TYPE);
		FileInputStream fileInput = new FileInputStream("E:\\UWinStudy\\websearchengine\\"+className + "-" + suffixName + TYPE);
		System.outputStream.println("Next");
		ObjectInputStream inputStream = new ObjectInputStream(fileInput);
		objectLoaded = inputStream.readObject();
		System.outputStream.println("loaded Object => " + objectLoaded);
		inputStream.close();
		fileInput.close();    	  
	    return objectLoaded;
	}   

 
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		
		 
		/**
		 *   Based on the Constructor object I have created an object web_crawler and built a list of links size N times
		 *  After this links will be visited, their HTML will be parsed to TEXT and saved as tokens.
		 *  then it will QUEUE all links of this page and The limit of pages QUEUED is N
		 *  No link is visited Twice, it is guaranteed by a HashSet of links
		 */
		web_crawler webCrawler = new web_crawler(10);
		// crawling links based on urls 
			
		webCrawler.buildWebCrawl("https://www.cricbuzz.com/profiles/265/ms-dhoni");
		
		// number of links and its text parsed into Tokens,  Final number of nodes
		System.outputStream.println("List size : " + webCrawler.getWebCrawledNodes().size());
		
		Collection<WebCrawlerNode> nodesMemory = webCrawler.getWebCrawledNodes();
		Iterator<WebCrawlerNode> it = nodesMemory.iterator();
		
		System.outputStream.println(">>>>>>>>>> Will save Nodes to file!");
		
		wc_manager.saveSerializableObject("urls", webCrawler.getWebCrawledNodes());	
		
		System.outputStream.println("############## Nodes save to file! #########################");

		// Nodes with the collection of loads from searlised files directly into memory
		
		System.outputStream.println("Now will load from file ...........");
		Collection<WebCrawlerNode> nodesSaved = (Collection<WebCrawlerNode>)wc_manager.loadSerializedObject("urls", "local");
		inverted_index obj = new inverted_index();
		System.outputStream.println("Next Next");
		obj.updatedloadData(nodesSaved);
   }
   
}

