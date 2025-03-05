package com.hawaii.epc;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityHeadersFilter {} /*implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self'");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
    /*
    this goes on WEB-INF/web.xml
  <!-- Restrict HTTP Methods -->
  <security-constraint>
    <web-resource-collection>
      <web-resource-name>Restricted Methods</web-resource-name>
      <url-pattern>/*</url-pattern>
      <http-method>TRACE</http-method>
      <http-method>OPTIONS</http-method>
    </web-resource-collection>
    <auth-constraint />
  </security-constraint>

  <!-- Enforce HTTPS -->
  <security-constraint>
    <web-resource-collection>
      <web-resource-name>Secure Content</web-resource-name>
      <url-pattern>/*</url-pattern>
    </web-resource-collection>
    <user-data-constraint>
      <transport-guarantee>CONFIDENTIAL</transport-guarantee>
    </user-data-constraint>
  </security-constraint>

  <!-- Security Headers Filter -->
  <filter>
    <filter-name>SecurityHeadersFilter</filter-name>
    <filter-class>com.hawaii.epc.SecurityHeadersFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>SecurityHeadersFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

  <!-- Secure Cookie Configuration -->
  <cookie-config>
    <http-only>true</http-only>
    <secure>true</secure>
  </cookie-config>
  }
     */


