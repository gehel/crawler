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
package com.soulgalore.crawler.run;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.soulgalore.crawler.core.Crawler;
import com.soulgalore.crawler.core.CrawlerResult;
import com.soulgalore.crawler.core.PageURL;
import com.soulgalore.crawler.guice.CrawlModule;

/**
 * Crawl to File. To files will be created, one with the working urls & one with the none working urls.
 * Each url will be on one new line.
 * @author peter
 *
 */
public class CrawlToFile extends AbstractCrawl {

	public static final String DEFAULT_FILENAME = "urls.txt";
	public static final String DEFAULT_ERROR_FILENAME = "errorurls.txt";

	private final String fileName;
	private final String errorFileName;

	CrawlToFile(String[] args) throws ParseException {
		super(args);
		fileName = getLine().getOptionValue("filename", DEFAULT_FILENAME);
		errorFileName = getLine().getOptionValue("errorfilename",
				DEFAULT_ERROR_FILENAME);

	}

	/**
	 * Run.
	 * 
	 * @param args
	 *            the args
	 */
	public static void main(String[] args) {

		try {
			final CrawlToFile crawl = new CrawlToFile(args);
			crawl.crawl();

		} catch (ParseException e) {
			System.out.print(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	private void crawl() {
		final Injector injector = Guice.createInjector(new CrawlModule());
		final Crawler crawler = injector.getInstance(Crawler.class);

		final CrawlerResult result = ("".equals(getPath())) ? crawler.getUrls(
				getUrl(), getLevel(), getNoPath(), getShouldVerify()) : crawler.getUrls(
				getUrl(), getPath(), getLevel(), getNoPath(),getShouldVerify());

		final StringBuilder workingUrls = new StringBuilder();
		final StringBuilder nonWorkingUrls = new StringBuilder();

		for (PageURL workingUrl : result.getUrls()) {
			workingUrls.append(workingUrl.getUrl()).append("\n");

		}

		final File workingUrlsFile = new File(fileName);

		System.out.println("Start storing file "
				+ workingUrlsFile.getAbsolutePath());

		try {
			Files.write(workingUrls.toString().getBytes("UTF-8"),
					workingUrlsFile);

		} catch (IOException e) {
			System.err.println(e);
		}

		if (nonWorkingUrls.length() > 0) {
			for (PageURL nonWorkingUrl : result.getNonWorkingUrls()) {
				nonWorkingUrls.append(nonWorkingUrl.getUrl()).append("\n");
			}
			final File nonWorkingUrlsFile = new File(errorFileName);

			try {
				Files.write(nonWorkingUrls.toString().getBytes("UTF-8"),
						nonWorkingUrlsFile);

			} catch (IOException e) {
				System.err.println(e);
			}
		}

		crawler.shutdown();
	}

	/**
	 * Get the options.
	 * 
	 * @return the specific CrawlToCsv options
	 */
	@Override
	protected Options getOptions() {
		final Options options = super.getOptions();

		final Option filenameOption = new Option("f",
				"the name of the output file, default name is "
						+ DEFAULT_FILENAME + " [optional]");
		filenameOption.setArgName("FILENAME");
		filenameOption.setLongOpt("filename");
		filenameOption.setRequired(false);
		filenameOption.setArgs(1);

		options.addOption(filenameOption);

		final Option errorFilenameOption = new Option("ef",
				"the name of the error output file, default name is "
						+ DEFAULT_ERROR_FILENAME + " [optional]");
		errorFilenameOption.setArgName("ERRORFILENAME");
		errorFilenameOption.setLongOpt("errorfilename");
		errorFilenameOption.setRequired(false);
		errorFilenameOption.setArgs(1);

		options.addOption(errorFilenameOption);

		return options;

	}
}