/**
 @version December 2, 2020
 * @author Arti Vekaria
 * 
 */

package WebCrawler;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class WebCrawlerNode implements java.io.Serializable {
	
	// Visited node's base
	private String base_node_url;
	
	// HTML file to Text
	private Collection<String> text_contents_token;
	
	// Links found in the base URL of this node from the HTML
	private Collection<String> node_url_link;
	
	// To check if the node is valid or invalid. False, means it failed to connect and get the HTML data
	private boolean is_bad_url = false;

	/**
	 * To build a node based on the URL that is visited 
	 * @param new_base_url
	 */
	public WebCrawlerNode(String new_base_url) {
		this.base_node_url = new_base_url;
		this.text_contents_token = new ArrayList<String>();
		this.node_url_link = new ArrayList<String>();		
	}
	
	
	/**
	 * 
	 * 	To return the tokens from the text file which is coverted from HTML
	 *  @return text_contents_token
	 */
	public Collection<String> gettext_contents_token() {
		return this.text_contents_token;
	}
	
	/**
	 * To return all links found in base URL. Depending on the queue size limit, it will be added to the web_crawler search 
	 * @return node_url_link
	 */
	public Collection<String> getnode_url_link() {
		return this.node_url_link;
	}
	
	/**
	 * @param string_token
	 */
	public void addTextContentToken(String string_token) {
		this.text_contents_token.add(string_token);
	}
	
	/**
	 * @param string_url_link
	 */
	public void addNodeUrlLink(String string_url_link) {
		this.node_url_link.add(string_url_link);
	}	
	

	/**
	 * @return base_node_url
	 */
	public String getNodeBaseUrl() {
		return this.base_node_url;
	}
	
	/**
	 * @param is_bad
	 */
	public void setBadURL(boolean is_bad) {
		this.is_bad_url = is_bad;
	}
	

	/**
	 * @return is_bad_url
	 */
	public boolean is_bad_url() {
		return this.is_bad_url;
	}

}

