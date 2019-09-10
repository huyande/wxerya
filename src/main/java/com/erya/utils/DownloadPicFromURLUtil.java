package com.erya.utils;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class DownloadPicFromURLUtil {
	public static void main(String[] args) {
		String url ="https://mmbiz.qpic.cn/mmbiz_png/TAoksPVlXMJugTRrUemBjyXEUHiaEhqCOH2CBQhwjH2nhvrnHcYNve3hpQW6yx3j7OyQm6DqBR9Su5NNGSBialxw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1";
		String path = "F:\\wxImage\\"+ UUID.randomUUID().toString()+".png";
		//downloadPicture(url, path);
		downloadFromUrl(url, path);
	}

	/**
	 * 更具链接下载图片
	 *
	 * @param urlList
	 * @param path
	 */
	/*public static String downloadPicture(String urlList, String path) {
		URL url = null;
		String realPath = path;
		try {
			if(!FileUtil.isExist(realPath)){
				FileUtil.createFile(realPath);
			}
			url = new URL(urlList);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());

			FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());

			dataInputStream.close();
			fileOutputStream.close();
			output.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return realPath;
	}*/

	public static String downloadFromUrl(String url,String fileName) {

		try {
			if(!FileUtil.isExist(fileName)){
				FileUtil.createFile(fileName);
			}
			URL httpurl = new URL(url);
			File f = new File(fileName);
			FileUtils.copyURLToFile(httpurl, f);
		} catch (Exception e) {
			e.printStackTrace();
			return "Fault!";
		}
		return "Successful!";
	}

}
