/*
 * semanticcms-news-servlet - SemanticCMS newsfeeds in a Servlet environment.
 * Copyright (C) 2016, 2017, 2019  AO Industries, Inc.
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

import com.semanticcms.core.controller.CapturePage;
import com.semanticcms.core.controller.SemanticCMS;
import com.semanticcms.core.model.ChildRef;
import com.semanticcms.core.model.Element;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.model.PageRef;
import com.semanticcms.core.pages.CaptureLevel;
import com.semanticcms.news.model.News;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilities for working with news.
 *
 * TODO: Move to different module?
 */
final public class NewsUtils {

	/**
	 * Gets all the new items in the given page and below, sorted by news natural order.
	 * 
	 * @see  com.semanticcms.news.model.News#compareTo(com.semanticcms.news.model.News)
	 */
	public static List<News> findAllNews(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Page page
	) throws ServletException, IOException {
		final List<News> found = new ArrayList<>();
		final SemanticCMS semanticCMS = SemanticCMS.getInstance(servletContext);
		CapturePage.traversePagesAnyOrder(
			servletContext,
			request,
			response,
			page,
			CaptureLevel.META,
			new CapturePage.PageHandler<Void>() {
				@Override
				public Void handlePage(Page page) throws ServletException, IOException {
					for(Element element : page.getElements()) {
						if(element instanceof News) {
							found.add((News)element);
						}
					}
					return null;
				}
			},
			new CapturePage.TraversalEdges() {
				@Override
				public Collection<ChildRef> getEdges(Page page) {
					return page.getChildRefs();
				}
			},
			new CapturePage.EdgeFilter() {
				@Override
				public boolean applyEdge(PageRef childPage) {
					return semanticCMS.getBook(childPage.getBookRef()).isAccessible();
				}
			}
		);
		Collections.sort(found);
		return Collections.unmodifiableList(found);
	}

	/**
	 * Make no instances.
	 */
	private NewsUtils() {
	}
}
