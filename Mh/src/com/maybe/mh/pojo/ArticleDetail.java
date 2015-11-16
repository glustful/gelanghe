package com.maybe.mh.pojo;

public class ArticleDetail {

	private int article_id;
	private String title;
	private String time;
	private String shortcon;
	private String imageurl;
	private String videourl;
	private String price;
	private String contents;
	private String f_tiaojian;
	private String f_chengxu;
	private String f_shixian;
	private String f_shoufei;
	private String f_cailiao;
	private String category;
	private String alias;
	private int recommend;
	private String groups;
	private int timesort;
	

	public ArticleDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArticleDetail(int article_id, String title, String time, String shortcon, String imageurl, String videourl, String price, String contents, String f_tiaojian, String f_chengxu, String f_shixian, String f_shoufei, String f_cailiao, String category, String alias, int recommend, String groups, int timesort) {
		super();
		this.article_id = article_id;
		this.title = title;
		this.time = time;
		this.shortcon = shortcon;
		this.imageurl = imageurl;
		this.videourl = videourl;
		this.price = price;
		this.contents = contents;
		this.f_tiaojian = f_tiaojian;
		this.f_chengxu = f_chengxu;
		this.f_shixian = f_shixian;
		this.f_shoufei = f_shoufei;
		this.f_cailiao = f_cailiao;
		this.category = category;
		this.alias = alias;
		this.recommend = recommend;
		this.groups = groups;
		this.timesort = timesort;
	}

	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getShortcon() {
		return shortcon;
	}

	public void setShortcon(String shortcon) {
		this.shortcon = shortcon;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getVideourl() {
		return videourl;
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getF_tiaojian() {
		return f_tiaojian;
	}

	public void setF_tiaojian(String f_tiaojian) {
		this.f_tiaojian = f_tiaojian;
	}

	public String getF_chengxu() {
		return f_chengxu;
	}

	public void setF_chengxu(String f_chengxu) {
		this.f_chengxu = f_chengxu;
	}

	public String getF_shixian() {
		return f_shixian;
	}

	public void setF_shixian(String f_shixian) {
		this.f_shixian = f_shixian;
	}

	public String getF_shoufei() {
		return f_shoufei;
	}

	public void setF_shoufei(String f_shoufei) {
		this.f_shoufei = f_shoufei;
	}

	public String getF_cailiao() {
		return f_cailiao;
	}

	public void setF_cailiao(String f_cailiao) {
		this.f_cailiao = f_cailiao;
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

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public int getTimesort() {
		return timesort;
	}

	public void setTimesort(int timesort) {
		this.timesort = timesort;
	}

	@Override
	public String toString() {
		return "ArticleDetail [article_id=" + article_id + ", title=" + title + ", time=" + time + ", shortcon=" + shortcon + ", imageurl=" + imageurl + ", videourl=" + videourl + ", price=" + price + ", contents=" + contents + ", f_tiaojian=" + f_tiaojian + ", f_chengxu=" + f_chengxu + ", f_shixian=" + f_shixian + ", f_shoufei=" + f_shoufei + ", f_cailiao=" + f_cailiao + ", category=" + category + ", alias=" + alias + ", recommend=" + recommend + ", groups=" + groups + ", timesort=" + timesort + "]";
	}

}
