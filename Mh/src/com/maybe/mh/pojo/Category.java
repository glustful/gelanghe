package com.maybe.mh.pojo;

public class Category {

	private int category_id;
	private int parent_id;
	private String category;
	private String alias;
	private int order_num;

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getCategory_id() {
		return category_id;
	}

	public Category(int category_id, int parent_id, String category, String alias, int order_num) {
		super();
		this.category_id = category_id;
		this.parent_id = parent_id;
		this.category = category;
		this.alias = alias;
		this.order_num = order_num;
	}



	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}


	@Override
	public String toString() {
		return "Category [category_id=" + category_id + ", parent_id=" + parent_id + ", category=" + category + ", alias=" + alias + ", order_num=" + order_num + "]";
	}
	

}
