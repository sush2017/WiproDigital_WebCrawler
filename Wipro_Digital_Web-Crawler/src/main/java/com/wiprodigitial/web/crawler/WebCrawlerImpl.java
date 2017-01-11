package com.wiprodigitial.web.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawlerImpl implements WebCrawler {

	private static Logger LOG = Logger.getLogger(WebCrawlerImpl.class.getCanonicalName()) ;
	private String urlToCrawl;
	private static final List<String> IMAGE_TYPES = new ArrayList<String>();
	private Set<String> locationSet = new HashSet<String>();

	public WebCrawlerImpl(String url) {
		this.urlToCrawl = url;
	}

	static{
    	IMAGE_TYPES.add(".*?\\.jpg");
    	IMAGE_TYPES.add(".*?\\.jpeg");
    	IMAGE_TYPES.add(".*?\\.png");
    	IMAGE_TYPES.add(".*?\\.gif");	
    }


	
	/**
     *This is the main function which will crawler URLs, static images etc.
     * and adds it to the Set of url's
     * 
     * @param doc HTML document
     * @param locationSet
     */
	
	public Set<String> crawlPage(String url) {
		
		locationSet.add(url);
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            LOG.log(Level.SEVERE,"\nUnable to read Page at [" + url + "]", e);
            locationSet.add(url);
            return locationSet;
        }
        addImagesToSet(doc);
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String linkUrl = link.attr("abs:href");
            if (linkUrl.startsWith(urlToCrawl)) { 
            	// check link does not end with a '#'
                if (!linkUrl.matches("#")) { 
                    // Is link a file?
                    for (String regex : IMAGE_TYPES) {
                        if (linkUrl.matches(regex)) {
                            // Image file, add to set and dont crawl
                        	locationSet.add(linkUrl);
                            return locationSet;
                        }
                    }
                    if (linkUrl.length()>0 && !linkUrl.startsWith("mailto:") && !locationSet.contains(linkUrl)) {	
                    	crawlPage(linkUrl);	
                    }
                }
            } 
        }
        
        return locationSet;
    }
	
	
	/**
     * This method finds all image urls via html 'img' attrinute 
     * and adds it to the Set of url's
     * 
     * @param doc HTML document
     * @param locationSet
     */
	private void addImagesToSet(Document doc) {
		// Get every image from that page
        Elements images = doc.select("img");
        for (Element image : images) {
            String imageUrl = image.attr("abs:src");
            // It is an image --> add to list
            locationSet.add(imageUrl);
        }
	}	
	
	public static void main (String args[]) {
		WebCrawlerImpl crawler = new WebCrawlerImpl("http://wiprodigital.com");
		Set<String> allUrls = crawler.crawlPage("http://wiprodigital.com");
		
		for (String url : allUrls) {
		System.out.println(url);
		}
	}
}
