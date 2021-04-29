/**
 * @version Dec 3, 2020
 * @author Kavisha Solanki
 * Purpose: Code for implementation of servlet
**/

package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

import WebCrawler.wc_manager;
import algorithms.inverted_index;
import WebCrawler.WebCrawlerNode;

/**
 This class is designed for performing implementation of servlet
 */
@WebServlet("/WebSrcController")
public class Controller extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String CRAWLER_NODES_FILE = "urls";
	
	// Inverted Index for Web Search
	inverted_index invertedIndexObject;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Controller() {
		super();
	}

	
    /**
	 Generation of data that is to be used in entire life of servlet
     * 
     */
    public void init() throws ServletException {
    	
		Collection<WebCrawlerNode> storedNodes = null;
		try {
			System.out.println("init ### DEBUG - Initialization of servlet. ###");
			System.out.println("init ### DEBUG - Loading of serializable file of web_crawler.");
			storedNodes = (Collection<WebCrawlerNode>)wc_manager.loadSerializedObject(CRAWLER_NODES_FILE, "local");
			System.out.println("init ### DEBUG - Serialized file of web_crawler loaded Successfully!!");			
			System.out.println("init ### DEBUG - Initialization of inverted index search structure!!");
			invertedIndexObject = new inverted_index();
			System.out.println("init ### DEBUG - Initialization of inverted index search structure!!");
			invertedIndexObject.updatedloadData(storedNodes);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    }	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		
		//verifying if inverted index is initiated
		if (invertedIndexObject != null) {
			System.out.println("doGet ### DEBUG => Loading and initialization of inverted index succesful!!");
		} else {
			System.out.println("doGet ### DEBUG => Inverted Index = NULL");
		}

		if (request.getParameter("act") != null) {
			String actualString = request.getParameter("act");
			System.out.println("doGet null ###  DEBUG => ACT = " + actualString);
			if (actualString.equals("prefix")) {
				if (request.getParameter("prefix") != null) {
					String valueInPrefix = request.getParameter("prefix");
					System.out.println("doGet null ### DEBUG Prefix = " + valueInPrefix.length());
					if (valueInPrefix.length() != 0) {
						JSONArray obj = new JSONArray();
						if (invertedIndexObject.guessWord(valueInPrefix) != null) {
							for (String s : invertedIndexObject.guessWord(valueInPrefix)) {
								obj.put(s);
							}
							System.out.println("Returning " + invertedIndexObject.guessWord(valueInPrefix));
						}
						response.getWriter().print(obj);
					}
				}
			} else if (actualString.equals("getTopUrl")) {
				System.out.println("doGet getTopUrl ### DEBUG => ACT = " + actualString);
				String valueInPrefix = request.getParameter("prefix");
				System.out.println("doGet getTopUrl ### DEBUG prefix = " + valueInPrefix);
				if (valueInPrefix != null) {
					System.out.println("doGet getTopUrl ### DEBUG prefix = " + valueInPrefix.length());
					if (valueInPrefix.length() != 0) {
						ArrayList<String> e = new ArrayList<String>();
						JSONArray obj = new JSONArray();
						if (invertedIndexObject.getTopUrls(valueInPrefix) != null) {
							int i = 0;
							for (String s : invertedIndexObject.getTopUrls(valueInPrefix)) {
								if (s != null) {
									System.out.println("doGet getTopUrl ### DEBUG => getTopUrl = " + i + " => " + s);
									i++;
									obj.put(s);
								}
							}
						}
						response.getWriter().print(obj);
					}
				}
			} else if (actualString.equals("getCorWord")) {
				System.out.println("doGet getCorWord ### DEBUG => ACT = " + actualString);
				String valueInPrefix = request.getParameter("prefix");
				System.out.println("doGet getCorWord ### DEBUG prefix = " + valueInPrefix.length());
				if (valueInPrefix != null) {
					if (valueInPrefix.length() != 0) {
						ArrayList<String> e = new ArrayList<String>();
						JSONArray obj = new JSONArray();
						if (invertedIndexObject.findCorrection(valueInPrefix) != null) {
							for (String s : invertedIndexObject.findCorrection(valueInPrefix)) {
								if (s != null) {
									obj.put(s);
								}
							}
						}
						response.getWriter().print(obj);
					}
				}
			}
		}
		response.getWriter().print("");
	}	
	

	
	public static void main (String[] args) {
		Collection<WebCrawlerNode> storedNodes = null;
		try {
			System.out.println("### DEBUG - Initialization of servlet. Servlet Initialization ###");
			System.out.println("### DEBUG - Trying to load serializable file of web_crawler.");
			storedNodes = (Collection<WebCrawlerNode>)wc_manager.loadSerializedObject("urls", "local");
			System.out.println("### DEBUG - Serializable file of web_crawler loaded successfully.");			
			System.out.println("### DEBUG - Trying to initialize inverted indedx search structure!!");
			inverted_index invertedIndexObject = new inverted_index();
			System.out.println("### DEBUG - INVERTED INDEX Search Structure is being Instantiated");
			invertedIndexObject.updatedloadData(storedNodes);
			System.out.println("### DEBUG - Saving inverted index to file.");
			wc_manager.saveSerializableObject("InvertedIdxIluisRueda", invertedIndexObject);			
			System.out.println("### DEBUG - INVERTED INDEX saved to file");			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}		
	}
	

}
