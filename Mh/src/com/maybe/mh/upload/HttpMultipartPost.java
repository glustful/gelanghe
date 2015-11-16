package com.maybe.mh.upload;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.maybe.mh.MainTabActivity;
import com.maybe.mh.MyApplication;
import com.maybe.mh.upload.CustomMultipartEntity.ProgressListener;
import com.maybe.mh.util.ShowToast;

public class HttpMultipartPost extends AsyncTask<String, Integer, String> {

	private Context context;
	private String filePath;
	private ProgressDialog pd;
	private long totalSize;

	public HttpMultipartPost(Context context, String filePath) {
		this.context = context;
		this.filePath = filePath;
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("上传文件中...");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(String... params) {
		String serverResponse = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost("http://www.gelanghe.gov.cn/getFiles.php");

		try {

			CustomMultipartEntity multipartContent = new CustomMultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {
				@Override
				public void transferred(long num) {
					publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});

			File uploadFile = new File(filePath);

			multipartContent.addPart("file", new FileBody(uploadFile));

			totalSize = multipartContent.getContentLength();

			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return serverResponse;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("result: " + result);

		if (result.equals("-1")) {
			ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "上传失败");
		} else {
			ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "上传成功");
			MyApplication.getMyApplication().addFileName(result);

		}

		pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		System.out.println("cancle");
	}

}
