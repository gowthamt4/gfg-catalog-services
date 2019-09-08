package com.gfg.catalog.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HateoasFormation {

  public List<Link> formHateoas(HttpServletRequest request, int totPages, int page, int size) {
    List<Link> links = new ArrayList<Link>();
    links.add(generateHateoas(request, "CUR", totPages, page, size));
    links.add(generateHateoas(request, "NEXT", totPages, page, size));
    links.add(generateHateoas(request, "PREV", totPages, page, size));
    return links;
  }

  private Link generateHateoas(HttpServletRequest request, String linkType, int totPages, int page,
      int size) {
    Map<String, String[]> queryParams = request.getParameterMap();
    int curPageNo = page;
    if ((linkType.equals("CUR")) || (linkType.equals("NEXT") && curPageNo + 1 == totPages)
        || (linkType.equals("PREV") && curPageNo == 0)) {
      if (request.getQueryString() != null) {
        return new Link(request.getRequestURL().toString() + "?" + request.getQueryString(),
            linkType, request.getMethod());
      } else {
        return new Link(request.getRequestURL().toString() + "?page=" + curPageNo + "&size=" + size,
            linkType, request.getMethod());
      }

    } else {
      StringBuffer uri = request.getRequestURL();
      uri.append("?page=");
      if (linkType.equals("NEXT")) {
        uri.append(curPageNo + 1);
      } else {
        uri.append(curPageNo - 1);
      }
      uri.append("&");
      if (!queryParams.containsKey("size")) {
        uri.append("size=" + size);
        if (queryParams != null) {
          uri.append("&");
        }
      }
      int lastEntry = 0;
      for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {
        lastEntry++;
        if (entry.getKey().equals("page")) {
          continue;
        } else {
          uri.append(entry.getKey() + "=");
          uri.append(String.join(",", entry.getValue()));
        }
        if (lastEntry != queryParams.size()) {
          uri.append("&");
        }
      }
      return new Link(uri.toString(), linkType, request.getMethod());
    }
  }
}
