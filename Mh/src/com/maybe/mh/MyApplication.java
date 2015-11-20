package com.maybe.mh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Application;
import android.preference.PreferenceManager;

import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.Category;
import com.maybe.mh.pojo.User;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tiandu.mh.R;

public class MyApplication extends Application {

	private static MyApplication myApplication;
	private static final String serverUrl = "http://www.gelanghe.gov.cn";
	
	public HashMap<String , Object> map = new HashMap<String, Object>();
	public static int lock = 0;
	public static boolean isFirst = true;
	public static Set<String> alias = new HashSet<String>();

	private String localPath;
	private boolean articleDetailDLOK;
	private boolean categoryDLOK;
	private List<String> fileNameList = new ArrayList<String>();
	private int imageHeight = 0;
	private String count="";
	private String updateCount = "";
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public int getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	private List<ArticleDetail> articleDetails = new ArrayList<ArticleDetail>();
	
	private List<Category> categories = new ArrayList<Category>();
	
	public List<ArticleDetail> getArticleDetails() {
		return articleDetails;
	}

	public void setArticleDetails(List<ArticleDetail> articleDetails) {
		this.articleDetails = articleDetails;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	private User loginUser = null;

	private boolean newApkDLOK;

	public boolean isNewApkDLOK() {
		return newApkDLOK;
	}

	public void setNewApkDLOK(boolean newApkDLOK) {
		this.newApkDLOK = newApkDLOK;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public boolean isArticleDetailDLOK() {
		return articleDetailDLOK;
	}

	public void setArticleDetailDLOK(boolean articleDetailDLOK) {
		this.articleDetailDLOK = articleDetailDLOK;
	}

	public boolean isCategoryDLOK() {
		return categoryDLOK;
	}

	public void setCategoryDLOK(boolean categoryDLOK) {
		this.categoryDLOK = categoryDLOK;
	}

	public List<String> getFileNameList() {
		return fileNameList;
	}

	public void addFileName(String fileName) {
		fileNameList.add(fileName);
	}

	public void resetFileNameList() {
		fileNameList = new ArrayList<String>();
	}

	private boolean downLoadOK;

	public boolean isDownLoadOK() {
		return downLoadOK;
	}

	public void setDownLoadOK(boolean downLoadOK) {
		this.downLoadOK = downLoadOK;
	}

	public static String getServerurl() {
		return serverUrl;
	}

	public static MyApplication getMyApplication() {
		return myApplication;
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		map.put("topAds", null);

		myApplication = this;
		
		
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).discCacheFileCount(300).defaultDisplayImageOptions(options).tasksProcessingOrder(QueueProcessingType.FIFO).build();

		ImageLoader.getInstance().init(config);
		
		

	}
	public void setUpdateCount(String jsonStr) {
		// TODO Auto-generated method stub
		updateCount = jsonStr;
	}
	
	public String getUpdateCount(){
		return this.updateCount;
	}

}
