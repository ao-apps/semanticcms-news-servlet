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

import com.aoindustries.io.TempFileList;
import com.aoindustries.io.buffer.AutoTempFileWriter;
import com.aoindustries.io.buffer.BufferResult;
import com.aoindustries.io.buffer.BufferWriter;
import com.aoindustries.servlet.filter.TempFileContext;
import com.aoindustries.taglib.AutoEncodingBufferedTag;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.Element;
import com.semanticcms.core.servlet.PageContext;
import com.semanticcms.core.servlet.SemanticCMS;
import com.semanticcms.news.servlet.impl.NewsImpl;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.SkipPageException;
import org.joda.time.ReadableDateTime;

public class News extends Element<com.semanticcms.news.model.News> {

	public News(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		ReadableDateTime pubDate
	) {
		super(
			servletContext,
			request,
			response,
			new com.semanticcms.news.model.News()
		);
		element.setView(SemanticCMS.DEFAULT_VIEW_NAME);
		element.setPubDate(pubDate);
	}

	public News(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		ReadableDateTime pubDate,
		String description
	) {
		this(servletContext, request, response, pubDate);
		element.setDescription(description);
	}

	/**
	 * Creates news in the current page context.
	 *
	 * @see  PageContext
	 */
	public News(ReadableDateTime pubDate) {
		this(
			PageContext.getServletContext(),
			PageContext.getRequest(),
			PageContext.getResponse(),
			pubDate
		);
	}

	/**
	 * Creates news in the current page context.
	 *
	 * @see  PageContext
	 */
	public News(ReadableDateTime pubDate, String description) {
		this(pubDate);
		element.setDescription(description);
	}

	@Override
	public News id(String id) {
		super.id(id);
		return this;
	}

	public News book(String book) {
		element.setBook(book);
		return this;
	}

	public News page(String page) {
		element.setTargetPage(page);
		return this;
	}

	public News element(String element) {
		this.element.setElement(element);
		return this;
	}

	public News view(String view) {
		element.setView(view);
		return this;
	}

	public News title(String title) {
		element.setTitle(title);
		return this;
	}

	public News description(String description) {
		element.setDescription(description);
		return this;
	}

	private BufferResult writeMe;
	@Override
	protected void doBody(CaptureLevel captureLevel, Body<? super com.semanticcms.news.model.News> body) throws ServletException, IOException, SkipPageException {
		super.doBody(captureLevel, body);
		BufferWriter capturedOut;
		if(captureLevel == CaptureLevel.BODY) {
			// Enable temp files if temp file context active
			capturedOut = TempFileContext.wrapTempFileList(
				AutoEncodingBufferedTag.newBufferWriter(),
				request,
				// Java 1.8: AutoTempFileWriter::new
				new TempFileContext.Wrapper<BufferWriter>() {
					@Override
					public BufferWriter call(BufferWriter original, TempFileList tempFileList) {
						return new AutoTempFileWriter(original, tempFileList);
					}
				}
			);
		} else {
			capturedOut = null;
		}
		try {
			NewsImpl.writeNewsImpl(
				servletContext,
				request,
				response,
				capturedOut,
				element
			);
		} finally {
			if(capturedOut != null) capturedOut.close();
		}
		writeMe = capturedOut==null ? null : capturedOut.getResult();
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		if(writeMe != null) writeMe.writeTo(out);
	}
}
