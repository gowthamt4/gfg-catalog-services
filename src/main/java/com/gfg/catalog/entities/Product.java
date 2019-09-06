package com.gfg.catalog.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.gfg.catalog.utils.Link;

/**
 * Model for the Entity Product
 */

@Entity
@Table(name = "products")
public class Product extends AbstractEntity{
	
	@Column(name = "title")
	@NotNull
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "brand", nullable = false)
	@NotNull
	private String brand;
	
	@Column(name = "price", nullable = false)
	private double price;
	
	@Column(name = "color", nullable = false)
	private String color;

	@Transient
	private List<Link> links;
	/**
	 * @return the link
	 */
	public List<Link> getLink() {
		return links;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(List<Link> links) {
		this.links = links;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
	public void addLink(String url, String rel, String method) {
		Link link = new Link(url, rel, method);
		links.add(link);
	}
	@Override
	public String toString() {
		return "Product [title=" + title + ", description=" + description + ", brand=" + brand + ", price=" + price
				+ ", color=" + color + ", created=" + created + ", lastModified=" + lastModified + "]";
	}
}
