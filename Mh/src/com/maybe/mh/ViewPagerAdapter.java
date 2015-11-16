package com.maybe.mh;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

class ViewPagerAdapter extends PagerAdapter {
	private List<ImageView> mImageViewList;
	public ViewPagerAdapter(List<ImageView> imageViewList) {
		super();
		this.mImageViewList = imageViewList;
	}
	/**
	 * 该方法将返回所包含的 Item总个数。为了实现一种循环滚动的效果，返回了基本整型的最大值，这样就会创建很多的Item,
	 * 其实这并非是真正的无限循环。
	 */
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}
         /**
	 * 判断出去的view是否等于进来的view 如果为true直接复用
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	/**
	 * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来，就是position，
	 * 因为mImageViewList只有五条数据，而position将会取到很大的值，
	 * 所以使用取余数的方法来获取每一条数据项。
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mImageViewList.get(position % mImageViewList.size()));
	}
	/**
	 * 创建一个view，
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mImageViewList.get(position % mImageViewList.size()));
		return mImageViewList.get(position % mImageViewList.size());
	}
}
