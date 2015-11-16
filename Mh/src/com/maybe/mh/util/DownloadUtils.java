package com.maybe.mh.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class DownloadUtils {

	public static final int BUFFER_SIZE = 1024;

	public static void download(String srcUrl, String filePath) throws IOException {

		long startPos = 0; // 开始下载位置

		// 转换空格
		srcUrl = srcUrl.replace(" ", "%20");

		byte[] buf = new byte[BUFFER_SIZE];
		int size = -1;

		isFinished(filePath);

		if (isFinished(filePath)) {
			return;
		}

		BufferedInputStream bis = null;
		RandomAccessFile raf = null;
		HttpURLConnection httpUrl = null;

		try {
			// 建立链接
			URL url = new URL(srcUrl);
			httpUrl = (HttpURLConnection) url.openConnection();

			// 根据URL获取文件路径
			File fileName = new File(filePath);
			startPos = fileName.length();

			if (fileName.exists()) {
				// 判断是否需要续传

				if (isContinue(srcUrl, filePath)) {
					// 获取获取文件已下载部分长度

					startPos = fileName.length();
					// 加入续传头信息

					httpUrl.setRequestProperty("RANGE", "bytes=" + startPos + "-");

					// System.out.println("isContinue");
				}
			}

			// 连接指定的资源

			httpUrl.connect();

			// 获取网络输入流

			bis = new BufferedInputStream(httpUrl.getInputStream());
			// 建立文件
			raf = new RandomAccessFile(fileName, "rw");
			raf.seek(startPos);

			// 保存文件
			while ((size = bis.read(buf)) != -1) {
				raf.write(buf, 0, size);
			}

		} finally {
			if (null != bis) {
				bis.close();
			}
			if (null != raf) {
				raf.close();
			}
			if (null != httpUrl) {
				httpUrl.disconnect();
			}
		}
	}

	private static boolean isFinished(String fileName) throws IOException {

		File file = new File(fileName);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}

	}

	private static boolean isContinue(String srcUrl, String fileName) throws IOException {

		URL url = new URL(srcUrl);
		File file = new File(fileName);

		HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
		if (file.exists()) {
			if ((file.lastModified() / 1000 >= httpUrl.getLastModified() / 1000)) {
				httpUrl.disconnect();
				return true;
			} else {
				file.delete();
			}
		}

		httpUrl.disconnect();
		return false;
	}

}
