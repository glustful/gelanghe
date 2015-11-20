package com.maybe.mh;

import com.tiandu.mh.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ProductShowPageActivity extends MyActivity {

	private ImageView backIV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.product_show_page);

		backIV = (ImageView) super.findViewById(R.id.product_show_page_back_iv);

		backIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

}
