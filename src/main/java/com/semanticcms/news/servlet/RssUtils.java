/*
 * semanticcms-news-servlet - SemanticCMS newsfeeds in a Servlet environment.
 * Copyright (C) 2016, 2017, 2021, 2022  AO Industries, Inc.
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

import com.aoapps.servlet.attribute.ScopeEE;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.model.PageRef;
import java.util.Arrays;
import javax.servlet.ServletContext;

/**
 * Utilities for working with RSS feeds.
 */
public final class RssUtils {

  /** Make no instances. */
  private RssUtils() {
    throw new AssertionError();
  }

  public static final String EXTENSION = ".rss";

  public static final String CONTENT_TYPE = "application/rss+xml";

  /**
   * Using classname to avoid introducing cyclic dependency.
   */
  private static final String RSS_SERVLET_CLASSNAME = "com.semanticcms.news.rss.RssServlet";

  private static final ScopeEE.Application.Attribute<Boolean> ISS_RSS_ENABLED_CACHE_KEY =
      ScopeEE.APPLICATION.attribute(RssUtils.class.getName() + ".isRssEnabled");

  /**
   * Checks if the RSS module is installed.  This is done by checking for the existence of the
   * servlet class.  This is cached in application scope to avoid throwing and catching ClassNotFoundException
   * repeatedly.
   */
  public static boolean isRssEnabled(ServletContext servletContext) {
    return ISS_RSS_ENABLED_CACHE_KEY.context(servletContext).computeIfAbsent(name -> {
      try {
        Class.forName(RSS_SERVLET_CLASSNAME);
        return true;
      } catch (ClassNotFoundException e) {
        return false;
      }
    });
  }

  /**
   * The resources in the order they will be checked, last one assumed if none specifically found as a resource.
   */
  private static final String[] RESOURCE_EXTENSIONS = {
      ".jspx",
      ".jsp",
      ""
  };

  public static String[] getResourceExtensions() {
    return Arrays.copyOf(RESOURCE_EXTENSIONS, RESOURCE_EXTENSIONS.length);
  }

  /**
   * The extensions that will not ever be included.
   */
  private static final String[] PROTECTED_EXTENSIONS = {
      ".inc.jspx",
      ".inc.jsp",
      ".jspf"
  };

  /**
   * Checks that the given resource should not be included under any circumstances.
   */
  public static boolean isProtectedExtension(String path) {
    for (String extension : PROTECTED_EXTENSIONS) {
      if (path.endsWith(extension)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the servletPath to the RSS feed for the give page ref.
   */
  public static String getRssServletPath(PageRef pageRef) {
    String servletPath = pageRef.getServletPath();
    for (String extension : RESOURCE_EXTENSIONS) {
      if (servletPath.endsWith(extension)) {
        servletPath = servletPath.substring(0, servletPath.length() - extension.length());
        break;
      }
    }
    return servletPath + EXTENSION;
  }

  /**
   * Gets the servletPath to the RSS feed for the give page.
   *
   * @see  RssUtils#getRssServletPath(com.semanticcms.core.model.PageRef)
   */
  public static String getRssServletPath(Page page) {
    return getRssServletPath(page.getPageRef());
  }
}
