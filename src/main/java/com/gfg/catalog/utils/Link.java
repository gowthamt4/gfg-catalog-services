package com.gfg.catalog.utils;

public class Link {
	private String href;
	private String rel;
	private String method;
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	public Link(String href, String rel, String method) {
		super();
		this.href = href;
		this.rel = rel;
		this.method = method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return href;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String href) {
		this.href = href;
	}
	/**
	 * @return the rel
	 */
	public String getRel() {
		return rel;
	}
	/**
	 * @param rel the rel to set
	 */
	public void setRel(String rel) {
		this.rel = rel;
	}
	
}
