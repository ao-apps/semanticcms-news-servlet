/*
 * semanticcms-news-servlet - SemanticCMS newsfeeds in a Servlet environment.
 * Copyright (C) 2016, 2017, 2020, 2021, 2022, 2023, 2024, 2025  AO Industries, Inc.
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
 * along with semanticcms-news-servlet.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.semanticcms.news.servlet;

import com.aoapps.html.servlet.DocumentEE;
import com.aoapps.net.DomainName;
import com.aoapps.net.Path;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.model.Link;
import com.semanticcms.core.pages.CaptureLevel;
import com.semanticcms.core.pages.local.PageContext;
import com.semanticcms.core.renderer.html.PageIndex;
import com.semanticcms.core.servlet.Element;
import com.semanticcms.news.renderer.html.NewsHtmlRenderer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.Writer;
import java.time.ZonedDateTime;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.SkipPageException;

/**
 * SemanticCMS newsfeeds in a Servlet environment.
 */
public class News extends Element<com.semanticcms.news.model.News> {

  /**
   * Creates news.
   */
  public News(
      ServletContext servletContext,
      HttpServletRequest request,
      HttpServletResponse response,
      com.semanticcms.news.model.News element,
      ZonedDateTime pubDate
  ) {
    super(
        servletContext,
        request,
        response,
        element
    );
    element.setView(Link.DEFAULT_VIEW_NAME);
    element.setPubDate(pubDate);
  }

  /**
   * Creates news.
   */
  public News(
      ServletContext servletContext,
      HttpServletRequest request,
      HttpServletResponse response,
      ZonedDateTime pubDate
  ) {
    this(
        servletContext,
        request,
        response,
        new com.semanticcms.news.model.News(),
        pubDate
    );
  }

  /**
   * Creates news.
   */
  public News(
      ServletContext servletContext,
      HttpServletRequest request,
      HttpServletResponse response,
      com.semanticcms.news.model.News element,
      ZonedDateTime pubDate,
      String description
  ) {
    this(servletContext, request, response, element, pubDate);
    element.setDescription(description);
  }

  /**
   * Creates news.
   */
  public News(
      ServletContext servletContext,
      HttpServletRequest request,
      HttpServletResponse response,
      ZonedDateTime pubDate,
      String description
  ) {
    this(
        servletContext,
        request,
        response,
        new com.semanticcms.news.model.News(),
        pubDate
    );
    element.setDescription(description);
  }

  /**
   * Creates news in the current page context.
   *
   * @see  PageContext
   */
  public News(
      com.semanticcms.news.model.News element,
      ZonedDateTime pubDate
  ) {
    this(
        PageContext.getServletContext(),
        PageContext.getRequest(),
        PageContext.getResponse(),
        element,
        pubDate
    );
  }

  /**
   * Creates news in the current page context.
   *
   * @see  PageContext
   */
  public News(ZonedDateTime pubDate) {
    this(
        PageContext.getServletContext(),
        PageContext.getRequest(),
        PageContext.getResponse(),
        new com.semanticcms.news.model.News(),
        pubDate
    );
  }

  /**
   * Creates news in the current page context.
   *
   * @see  PageContext
   */
  @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
  public News(
      com.semanticcms.news.model.News element,
      ZonedDateTime pubDate,
      String description
  ) {
    this(element, pubDate);
    element.setDescription(description);
  }

  /**
   * Creates news in the current page context.
   *
   * @see  PageContext
   */
  @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
  public News(ZonedDateTime pubDate, String description) {
    this(pubDate);
    element.setDescription(description);
  }

  @Override
  public News id(String id) {
    super.id(id);
    return this;
  }

  /**
   * See {@link com.semanticcms.news.model.News#setDomain(com.aoapps.net.DomainName)}.
   *
   * @return {@code this}
   */
  public News domain(DomainName domain) {
    element.setDomain(domain);
    return this;
  }

  /**
   * See {@link com.semanticcms.news.model.News#setBook(com.aoapps.net.Path)}.
   *
   * @return {@code this}
   */
  public News book(Path book) {
    element.setBook(book);
    return this;
  }

  /**
   * See {@link com.semanticcms.news.model.News#setTargetPage(java.lang.String)}.
   *
   * @return {@code this}
   */
  public News page(String page) {
    element.setTargetPage(page);
    return this;
  }

  /**
   * See {@link com.semanticcms.news.model.News#setElement(java.lang.String)}.
   *
   * @return {@code this}
   */
  public News element(String element) {
    this.element.setElement(element);
    return this;
  }

  /**
   * See {@link com.semanticcms.news.model.News#setView(java.lang.String)}.
   *
   * @return {@code this}
   */
  public News view(String view) {
    element.setView(view);
    return this;
  }

  /**
   * See {@link com.semanticcms.news.model.News#setTitle(java.lang.String)}.
   *
   * @return {@code this}
   */
  public News title(String title) {
    element.setTitle(title);
    return this;
  }

  /**
   * See {@link com.semanticcms.news.model.News#setDescription(java.lang.String)}.
   *
   * @return {@code this}
   */
  public News description(String description) {
    element.setDescription(description);
    return this;
  }

  /**
   * See {@link com.semanticcms.news.model.News#setAllowRobots(java.lang.Boolean)}.
   *
   * @return {@code this}
   */
  public News allowRobots(Boolean allowRobots) {
    element.setAllowRobots(allowRobots);
    return this;
  }

  private PageIndex pageIndex;

  @Override
  protected void doBody(CaptureLevel captureLevel, Body<? super com.semanticcms.news.model.News> body) throws ServletException, IOException, SkipPageException {
    pageIndex = PageIndex.getCurrentPageIndex(request);
    super.doBody(captureLevel, body);
    NewsHtmlRenderer.doBodyImpl(
        servletContext,
        request,
        response,
        element
    );
  }

  @Override
  public void writeTo(Writer out, ElementContext context) throws IOException, ServletException {
    NewsHtmlRenderer.writeNewsImpl(
        request,
        new DocumentEE(servletContext, request, response, out),
        context,
        element,
        pageIndex
    );
  }
}
