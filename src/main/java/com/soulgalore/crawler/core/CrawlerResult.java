/******************************************************
 * Web crawler
 * 
 *
 * Copyright (C) 2012 by Peter Hedenskog (http://peterhedenskog.com)
 *
 ******************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License. You may obtain a copy of the License at
 * 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is 
 * distributed  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 * See the License for the specific language governing permissions and limitations under the License.
 *
 *******************************************************
 */
package com.soulgalore.crawler.core;

import java.util.Collections;
import java.util.Set;


/**
 * The result of a crawl.
 * 
 */
public class CrawlerResult {

	private final Set<PageURL> urls;
	private final Set<HTMLPageResponse> nonWorkingResponses;
	private final String startPoint;

	/**
	 * Create the result from a crawl.
	 * 
	 * @param theStartPoint
	 *            where the crawl was started
	 * @param theUrls
	 *            the urls that was fetched
	 * @param theNonWorkingResponses
	 *            the non working urls
	 */
	public CrawlerResult(String theStartPoint, Set<PageURL> theUrls,
			Set<HTMLPageResponse> theNonWorkingResponses) {
		startPoint = theStartPoint;
		urls = theUrls;
		nonWorkingResponses = theNonWorkingResponses;
	}

	/**
	 * Get the non working urls.
	 * 
	 * @return non working urls.
	 */
	public Set<HTMLPageResponse> getNonWorkingUrls() {
		return Collections.unmodifiableSet(nonWorkingResponses);
	}

	/**
	 * Get the start point of the crawl.
	 * 
	 * @return the start point of the crawl.
	 */
	public String getTheStartPoint() {
		return startPoint;
	}

	/**
	 * Get the fetched urls.
	 * 
	 * @return the fetched urls. Contains only working urls.
	 */
	public Set<PageURL> getUrls() {
		return Collections.unmodifiableSet(urls);
	}

}
