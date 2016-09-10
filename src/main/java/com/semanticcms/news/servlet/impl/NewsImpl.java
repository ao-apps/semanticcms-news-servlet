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
package com.semanticcms.news.servlet.impl;

import static com.aoindustries.encoding.TextInXhtmlAttributeEncoder.encodeTextInXhtmlAttribute;
import com.aoindustries.lang.NotImplementedException;
import com.semanticcms.core.model.Element;
import com.semanticcms.core.model.Node;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.model.PageRef;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.CapturePage;
import com.semanticcms.core.servlet.CurrentNode;
import com.semanticcms.core.servlet.CurrentPage;
import com.semanticcms.core.servlet.PageIndex;
import com.semanticcms.core.servlet.PageRefResolver;
import com.semanticcms.core.servlet.impl.LinkImpl;
import com.semanticcms.news.model.News;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

final public class NewsImpl {

	public static void writeNewsImpl(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Appendable out,
		News news
	) throws ServletException, IOException {
		// Get the current capture state
		final CaptureLevel captureLevel = CaptureLevel.getCaptureLevel(request);
		if(captureLevel.compareTo(CaptureLevel.META) >= 0) {
			final Node currentNode = CurrentNode.getCurrentNode(request);
			final Page currentPage = CurrentPage.getCurrentPage(request);
			if(currentPage == null) throw new ServletException("news must be nested within a page");

			final String element = news.getElement();

			// Find the target page
			PageRef currentPageRef = currentPage.getPageRef();
			PageRef targetPageRef;
			if(news.getBook() == null) {
				if(news.getTargetPage() == null) {
					targetPageRef = currentPageRef;
				} else {
					targetPageRef = PageRefResolver.getPageRef(
						servletContext,
						request,
						null,
						news.getTargetPage()
					);
				}
			} else {
				if(news.getTargetPage() == null) throw new ServletException("page required when book provided.");
				targetPageRef = PageRefResolver.getPageRef(
					servletContext,
					request,
					news.getBook(),
					news.getTargetPage()
				);
			}
			// Set book and targetPage always, since news is used from views on other pages
			news.setBook(targetPageRef.getBookName());
			news.setTargetPage(targetPageRef.getPath());
			// Add page links if linking to another page
			if(
				currentNode != null
				&& !targetPageRef.equals(currentPageRef)
			) {
				currentNode.addPageLink(targetPageRef);
			}
			// The target page will be null when in a missing book
			Page targetPage;
			if(targetPageRef.getBook()==null) {
				targetPage = null;
			} else if(
				// Short-cut for element already added above within current page
				targetPageRef.equals(currentPageRef)
				&& (
					element==null
					|| currentPage.getElementsById().containsKey(element)
				)
			) {
				targetPage = currentPage;
			} else {
				// Capture required, even if capturing self
				// TODO: This would cause unbound recursion and stack overflow at this time, there may be a complicate workaround when needed, such as not running this element on the recursive capture
				if(targetPageRef.equals(currentPageRef)) throw new NotImplementedException("Forward reference to element in same page not supported yet");
				targetPage = CapturePage.capturePage(
					servletContext,
					request,
					response,
					targetPageRef,
					element==null ? CaptureLevel.PAGE : CaptureLevel.META
				);
			}
			// Find the optional target element, may remain null when in missing book
			Element targetElement;
			if(element == null) {
				if(news.getBook() == null && news.getPage() == null) {
					if(currentNode instanceof Element) {
						// Default to current element
						targetElement = (Element)currentNode;
					} else {
						// No current element
						targetElement = null;
					}
				} else {
					// No element since book and/or page provided
					targetElement = null;
				}
			} else {
				// Find the element
				if(targetPage != null) {
					targetElement = targetPage.getElementsById().get(element);
					if(targetElement == null) throw new ServletException("Element not found in target page: " + element);
					if(targetPage.getGeneratedIds().contains(element)) throw new ServletException("Not allowed to link to a generated element id, set an explicit id on the target element: " + element);
				} else {
					targetElement = null;
				}
			}
			if(news.getTitle() == null) {
				String title;
				if(element != null) {
					if(targetElement == null) {
						// Element in missing book
						title = LinkImpl.getBrokenPath(targetPageRef, element);
					} else {
						title = targetElement.getLabel();
					}
				} else {
					if(targetPage == null) {
						// Page in missing book
						title = LinkImpl.getBrokenPath(targetPageRef);
					} else {
						title = targetPage.getTitle();
					}
				}
				news.setTitle(title);
			}
			if(captureLevel == CaptureLevel.BODY) {
				// Write an empty div so links to this news ID work
				String refId = PageIndex.getRefIdInPage(servletContext, request, currentPage, news.getId());
				out.append("<div class=\"semanticcms-news-anchor\" id=\"");
				encodeTextInXhtmlAttribute(refId, out);
				out.append("\" />");
				// TODO: Should we show the news entry here when no news view is active?
				// TODO: Hide from tree views, or leave but link to "news" view when news view is active?
			}
		}
	}

	/**
	 * Make no instances.
	 */
	private NewsImpl() {
	}
}
