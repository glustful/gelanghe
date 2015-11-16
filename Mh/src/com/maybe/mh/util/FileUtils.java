/**
 * 
 */
package com.maybe.mh.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.maybe.mh.DoWorkShowPageActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;

/**
 * @author xiehongdong
 * 
 *         2011-8-23
 */
public class FileUtils {
	private String SDPATH;

	/**
	 * 
	 */
	public FileUtils() {
		// TODO Auto-generated constructor stub
		// 获得当前外部存储设备的目录
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 */
	public File createSdFile(String fileName) {
		File file = new File(SDPATH + fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}

	/**
	 * 创建SD卡目录
	 * 
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {
		File file = new File(SDPATH + dirName);
		file.mkdir();

		return file;
	}

	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);

		return file.exists();

	}

	public File writeToSDFromInput(Context context, String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;

		try {
			createSDDir(path);
			file = createSdFile(path + fileName);
			output = new FileOutputStream(file);

			byte[] buffer = new byte[4 * 1024];
			int total = 0;
			while ((input.read(buffer)) != -1) {
				total = total + buffer.length;
				output.write(buffer);
				// 更新下载进度条
				((DoWorkShowPageActivity) context).sendMsg(1, total);
			}
			output.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 下载完成
		((DoWorkShowPageActivity) context).sendMsg(2, 0);
		return file;
	}

}
