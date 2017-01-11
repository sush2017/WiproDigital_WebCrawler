package com.wiprodigitial.web.crawler;

import java.util.Set;

public interface WebCrawler {

	public Set<String> crawlPage(String url);
}
