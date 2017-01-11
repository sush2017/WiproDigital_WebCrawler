package com.wiprodigitial.web.crawler;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class WebCrawlerTest {
	
	private WebCrawlerImpl webCrawler = null;
	String url = "http://wiprodigital.com/";

	@Before
	public void init(){
		webCrawler = new WebCrawlerImpl(url);
	}
	
	
	@Test
	public void testCrawlPage() {
		Set<String> allUrls = webCrawler.crawlPage(url);
		Assert.assertNotNull(allUrls);
		Assert.assertTrue(allUrls.size() > 100);
		
	}
	
}
