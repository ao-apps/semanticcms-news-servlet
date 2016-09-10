/*
 * semanticcms-news-servlet - SemanticCMS newsfeeds in a Servlet environment.
 * Copyright (C) 2016  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of semanticcms-news-servlet.
 *
 * semanticcms-news-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * semanticcms-news-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with semanticcms-news-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.semanticcms.news.servlet;

import com.semanticcms.core.model.Element;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.model.PageRef;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.CapturePage;
import com.semanticcms.news.model.News;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilities for working with news.
 */
final public class NewsUtils {

	/**
	 * Gets all the new items in the given page and below, sorted by news natural order.
	 * 
	 * @see  News#compareTo(com.semanticcms.news.model.News)
	 */
	public static List<News> findAllNews(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page
	) throws ServletException, IOException {
		List<News> found = new ArrayList<News>();
		findAllNewsRecursive(
			servletContext,
			request,
			response,
			page,
			found,
			new HashSet<PageRef>()
		);
		Collections.sort(found);
		return Collections.unmodifiableList(found);
	}

	private static void findAllNewsRecursive(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page,
		List<News> found,
		Set<PageRef> seenPages
	) throws ServletException, IOException {
		if(!seenPages.add(page.getPageRef())) throw new AssertionError();
		for(Element element : page.getElements()) {
			if(element instanceof News) {
				found.add((News)element);
			}
		}
		for(PageRef childRef : page.getChildPages()) {
			if(
				// Child not in missing book
				childRef.getBook() != null
				// Not already seen
				&& !seenPages.contains(childRef)
			) {
				findAllNewsRecursive(
					servletContext,
					request,
					response,
					CapturePage.capturePage(servletContext, request, response, childRef, CaptureLevel.META),
					found,
					seenPages
				);
			}
		}
	}

	/**
	 * Make no instances.
	 */
	private NewsUtils() {
	}
}
