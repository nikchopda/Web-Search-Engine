/**
 * @version Dec 2, 2020
 * @author Kavisha Solanki
 * Purpose: Developing the functionality of web crawler
**/

package WebCrawler;


import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utilities.Util;



public class web_crawler {
	
	//Maintaining visited links in a list (queue)
	private LinkedList<String> linksToVisit = new LinkedList<String>();
	
	//confirming no link is visited twice by setting this storing already visited links
	// Creates a SET to maintain the list of links that were visited in order to guarantee no double visit
	private HashSet<String> visitedLinks = new HashSet<String>();

	//confirming no link is visited twice by storing links yet to be visited
	private HashSet<String>  linksToVisitCheckSet = new HashSet<String>();
	
	//Contains list of already visited nodes and already built nodes
	private Collection<WebCrawlerNode> nodesInWebCrawler = new LinkedList<WebCrawlerNode>();
	
	
	private final String STANDARD_URL_REGEX = "\\b(https?)://[-a-zA-Z0-9|.]+[-a-zA-Z0-9#/_|.:]*(asp|aspx|asx|asmx|cfm|html|htm|xhtml|jhtml|jsp|jspx|wss|do|php|php4|php3|phtml|py|shtml){1}$";
	private final String STANDARD_URLFOLDER_REGEX = "\\b(https?)://[-a-zA-Z0-9|.]+[-a-zA-Z0-9#/_|.:]*(/){1}$";
	private final String STANDARD_URLOTHER_REGEX = "\\b(https?)://[-a-zA-Z0-9|.]+[-a-zA-Z0-9/_:]*";
	
	//Number of URLs to be visited
	private int MAX_URL_VISITS = 10000;
	
	// Number of urls in the queue to be visited while crawling 
	private int QtyQueuedLinks = 3;
	
	
	// To setup if debug messages should be shown
	private boolean isPrintDebug = true;
	
	
	/**
	
	A constructor for web_crawler. 
	 * @param maximumVisitofURL
	 */
	public web_crawler(int maximumVisitofURL) {
		this.MAX_URL_VISITS = maximumVisitofURL;
	}

	
	/**
	 * 	Returns a Collections of WebCrawlerNodes 
	 * 
	 * @return Collections of WebCrawlerNodes
	 */
	public Collection<WebCrawlerNode> getnodesInWebCrawler() {
		return nodesInWebCrawler;
	}

    
	/**
	 * 
	 * 	web crawling of URL starts and stores it in list
	 * 
	 * @param urlToVisit
	 */
    public void buildWebCrawl(String url)  {
    	WebCrawlerNode crawlerNode = new WebCrawlerNode(url);
    	try {
			//Connection to url 
	    	Connection urlConnection = Jsoup.connect(url);

		    Document jSoupDoc = urlConnection.get();
			
		    // Obtaining all tags of HREF type
		    Elements hreflinks = jSoupDoc.select("a[href]");
		    

	    	crawlerNode = new WebCrawlerNode(url);
	    	String htmlTextFormat = jSoupDoc.text();
	    	StringTokenizer tokenizer = new StringTokenizer(htmlTextFormat);
	    	
	    	//Converting text from parsed HTML files
			while (tokenizer.hasMoreTokens()) {
				String string_token = tokenizer.nextToken();
				// removing unnecessary strings
				String token_clear = string_token.replaceAll("[+.^:,?;!�()\n\r\"]","");
				crawlerNode.addTextContentToken(token_clear);
			}

			//Saving found links in HTML files 
		    for (int i = 0; i < hreflinks.size(); i++) {
	    		Element link = hreflinks.get(i);
	    		crawlerNode.addNodeUrlLink(link.attr("abs:href"));
		    	if (QtyQueuedLinks <= this.MAX_URL_VISITS) {
		    		String hrefURL = link.attr("abs:href");
		    		if (validadeURLWebCrawler(hrefURL)) {
		    			if (!linksToVisitCheckSet.contains(hrefURL)) {
					    	linksToVisit.add(hrefURL);
					    	linksToVisitCheckSet.add(hrefURL);
					    	QtyQueuedLinks++;
					    	Util.printDebug(isPrintDebug, "####### Adding visited links: " + hrefURL + "Index to link: " + QtyQueuedLinks);
		    			} else {
		    				Util.printDebug(isPrintDebug, "####### Link not added to visited, in QUEUE: " + hrefURL);
		    			}
		    		} else {
		    			Util.printDebug(isPrintDebug, "####### Link not added to visited: " + hrefURL);
		    		}
		    	}
		    }
    	} catch (IOException ex) {
    		crawlerNode.setBadURL(true);
    	}
    	
    	nodesInWebCrawler.add(crawlerNode);
    	
	    // Recursively visits the first Sub-URL that was Queued
	    if (!linksToVisit.isEmpty()) {
	    	String linkToVisit = null;
	    	do {
	    		linkToVisit = linksToVisit.removeFirst();
	    		if (visitedLinks.contains(linkToVisit)) {
	    			Util.printDebug(isPrintDebug, "###### Already visited : " + linkToVisit);
	    		}
	    	} while (!linksToVisit.isEmpty() && visitedLinks.contains(linkToVisit));
	    	
	    	Util.printDebug(isPrintDebug, "Link to be removed from set:" + linkToVisit);
	    	Util.printDebug(isPrintDebug, "Number of links to be visited: " + linksToVisit.size());
	    	Util.printDebug(isPrintDebug, "Links yet to be visited: " + linkToVisit + " will be adding to visited link");
			visitedLinks.add(linkToVisit);
			Util.printDebug(isPrintDebug, ">>>>>>>>> Visiting link recursevelly now: " + linkToVisit);
			buildWebCrawl(linkToVisit);
	    }
    }
    
    /**
     *   Uses REGEX functionalities 
     */
    public boolean validadeURLWebCrawler(String ValidatingURL) {
    	boolean URLMatches = false;
    	if (ValidatingURL != null) {
    		URLMatches = URLMatches || ValidatingURL.matches(STANDARD_URL_REGEX);
    		URLMatches = URLMatches || ValidatingURL.matches(STANDARD_URLFOLDER_REGEX);
    		URLMatches = URLMatches || ValidatingURL.matches(STANDARD_URLOTHER_REGEX);
    	}
		return URLMatches;
    }   

    /**
     * 
     * 
     * @param args
     */
    public static void main (String[] args) {
		web_crawler webCrawler = new web_crawler(10000);
		System.out.println("1 - " + webCrawler.validadeURLWebCrawler("https://en.wikipedia.org/wiki/Franks"));
    }
    
}