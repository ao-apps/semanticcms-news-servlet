/*
 * semanticcms-news-servlet - SemanticCMS newsfeeds in a Servlet environment.
 * Copyright (C) 2021  AO Industries, Inc.
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
module com.semanticcms.news.servlet {
	exports com.semanticcms.news.servlet;
	// Direct
	requires com.aoapps.html.servlet; // <groupId>com.aoapps</groupId><artifactId>ao-fluent-html-servlet</artifactId>
	requires com.aoapps.net.types; // <groupId>com.aoapps</groupId><artifactId>ao-net-types</artifactId>
	requires com.aoapps.servlet.util; // <groupId>com.aoapps</groupId><artifactId>ao-servlet-util</artifactId>
	requires javax.servlet.api; // <groupId>javax.servlet</groupId><artifactId>javax.servlet-api</artifactId>
	requires javax.servlet.jsp.api; // <groupId>javax.servlet.jsp</groupId><artifactId>javax.servlet.jsp-api</artifactId>
	requires org.joda.time; // <groupId>joda-time</groupId><artifactId>joda-time</artifactId>
	requires com.semanticcms.core.controller; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-controller</artifactId>
	requires com.semanticcms.core.model; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-model</artifactId>
	requires com.semanticcms.core.pages; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-pages</artifactId>
	requires com.semanticcms.core.pages.local; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-pages-local</artifactId>
	requires com.semanticcms.core.renderer.html; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-renderer-html</artifactId>
	requires com.semanticcms.core.servlet; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-servlet</artifactId>
	requires com.semanticcms.news.model; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-news-model</artifactId>
	requires com.semanticcms.news.renderer.html; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-news-renderer-html</artifactId>
	// Transitive
	requires com.aoapps.lang; // <groupId>com.aoapps</groupId><artifactId>ao-lang</artifactId>
}
