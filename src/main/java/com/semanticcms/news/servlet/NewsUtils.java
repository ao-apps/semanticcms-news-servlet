/*
 * semanticcms-news-servlet - SemanticCMS newsfeeds in a Servlet environment.
 * Copyright (C) 2016, 2017, 2019, 2020, 2021, 2022, 2025, 2026  AO Industries, Inc.
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

import com.semanticcms.core.model.Element;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.CapturePage;
import com.semanticcms.news.model.News;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utilities for working with news.
 */
public final class NewsUtils {

  /** Make no instances. */
  private NewsUtils() {
    throw new AssertionError();
  }

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
    CapturePage.traversePagesAnyOrder(
        servletContext,
        request,
        response,
        page,
        CaptureLevel.META,
        p -> {
          for (Element element : p.getElements()) {
            if (element instanceof News) {
              found.add((News) element);
            }
          }
          return null;
        },
        Page::getChildRefs,
        childPage -> childPage.getBook() != null
    );
    Collections.sort(found);
    return Collections.unmodifiableList(found);
  }
}
