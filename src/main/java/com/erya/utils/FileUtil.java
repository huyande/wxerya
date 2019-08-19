package com.erya.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作工具类
 * 
 * @author huyande
 */
public class FileUtil {

	private static final String SPLIT = ";";

	/**
	 * 删除文件夹或文件
	 *
	 * @param fileName
	 */
	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		deleteFile(file);
	}

	/**
	 * 删除文件夹或文件
	 *
	 * @param file
	 */
	public static void deleteFile(File file) {

		if (!file.exists())
			return;

		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}

		file.delete();
	}

	/**
	 * 判断本地文件或者目录是否存在
	 *
	 * @parma fullFileName 文件路径+文件名称 如:D:\\test\\test.jsp
	 * @return boolean
	 */
	public static boolean isExist(String fullFileName) {
		if ((null == fullFileName) || ("".equals(fullFileName))) {
			return false;
		}
		fullFileName = fullFileName.replace("//", "\\");
		File fl = new File(fullFileName);
		if (fl.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 创建本地目录
	 *
	 * @parma dirs 目录名称
	 * @return 是否成功
	 * @throws Exception
	 */
	public static boolean makeDir(String dirs) throws Exception {
		boolean result = false;
		File file = new File(dirs);
		// 创建目录
		result = file.mkdirs();
		return result;
	}

	/**
	 * 创建文本文件
	 *
	 * @parma fullFileName 文件路径+文件名称
	 * @parma txt 文件内容
	 * @return 是否成功
	 * @throws Exception
	 */
	public static void createFile(String fullFileName, String contents) throws Exception {

		if (!isExist(fullFileName)) {
			makeDir(fullFileName.substring(0, fullFileName.lastIndexOf("/")));
		}

		OutputStreamWriter outputStream = null;
		try {
			outputStream = new OutputStreamWriter(new FileOutputStream(fullFileName), "UTF-8");
			outputStream.write(null == contents ? "" : contents);
			outputStream.close();
		} finally {
			outputStream.close();
		}
	}
	/**
	 * 创建文本文件
	 *
	 * @parma fullFileName 文件路径+文件名称
	 * @parma txt 文件内容
	 * @return 是否成功
	 * @throws Exception
	 */
	public static boolean createFile(String destFileName) throws Exception {
		File file = new File(destFileName);
		if(file.exists()) {
			return false;
		}
		if (destFileName.endsWith(File.separator)) {
			return false;
		}
		//判断目标文件所在的目录是否存在
		if(!file.getParentFile().exists()) {
			//如果目标文件所在的目录不存在，则创建父目录
			if(!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		//创建目标文件
		try {
			if (file.createNewFile()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取文件为字节.
	 *
	 * @param filePath
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readByteArray(String filePath) throws Exception {
		FileInputStream in = null;
		ByteArrayOutputStream bout = null;
		byte[] orgData = null;
		try {
			in = new FileInputStream(new File(filePath));
			bout = new ByteArrayOutputStream();
			byte[] tmpbuf = new byte[1024];
			int count = 0;
			while ((count = in.read(tmpbuf)) != -1) {
				bout.write(tmpbuf, 0, count);
				tmpbuf = new byte[1024];
			}
			orgData = bout.toByteArray();
		} finally {
			if (null != bout) {
				bout.flush();
				bout.close();
			}
			if (null != in) {
				in.close();
			}
		}
		return orgData;
	}


	/**
	 * 下载文件
	 * 
	 * @param response
	 * @param inputStrem
	 *            输出的文件名称
	 * @throws IOException
	 */
	public static Boolean downloadFile(HttpServletResponse response, InputStream inputStrem,String suffix) {
		OutputStream output;
		try {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStrem);
			byte[] b = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(b);
			output = response.getOutputStream();
			response.setHeader("Content-disposition",
					"attachment; filename="+ DateUtil.getCurrentDate("yyyyMMdd")+ suffix);
			response.setContentType("application/msexcel");
			output.write(b);
			output.flush();
			output.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param response
	 * @param fileName
	 *            输出的文件名称
	 * @throws IOException
	 */
	public static Boolean downloadExcelFile(HttpServletResponse response, File file) {
		OutputStream output;
		if (file.exists()) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				byte[] b = new byte[bufferedInputStream.available()];
				bufferedInputStream.read(b);
				output = response.getOutputStream();
				response.setHeader("Content-disposition",
						"attachment; filename=" + DateUtil.getCurrentDate("yyyyMMddHHmmss")
								+ new String("模板文件".getBytes("UTF-8"), "ISO8859-1") + ".xls");
				response.setContentType("application/msexcel");
				output.write(b);
				output.flush();
				output.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * 上传文件
	 */
	public static String uploadFile(MultipartFile file, String path) {
		if (file.isEmpty()) { // 提示
			return "上传错误";
		}
		String fileName = file.getOriginalFilename();
		// 获取文件的后缀名称
		String fileNameExtention = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		// 生成真实的文件名称
		String realName = UUID.randomUUID().toString() + fileNameExtention;
		File dest = new File(path + "/" + realName);
		if (FileUtil.isExist(path)) {
			try {
				FileUtil.makeDir(path);
				file.transferTo(dest); // 保存文件
			} catch (Exception e) {
				e.printStackTrace();
				return "上传失败";
			}
		}
		return realName;
	}

	public static void textToFile(String strFilename, String strBuffer) {
		try {
			// 创建文件对象
			File fileText = new File(strFilename);
			// 向文件写入对象写入信息
			FileWriter fileWriter = new FileWriter(fileText);

			// 写文件
			fileWriter.write(strBuffer);
			// 关闭
			fileWriter.close();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件 生成字符串
	 * 
	 * @param buffer
	 * @param filePath
	 * @throws IOException
	 */
	public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
		InputStream is = new FileInputStream(filePath);
		String line; // 用来保存每行读取的内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		line = reader.readLine(); // 读取第一行
		while (line != null) { // 如果 line 为空说明读完了
			buffer.append(line); // 将读到的内容添加到 buffer 中
			buffer.append("\n"); // 添加换行符
			line = reader.readLine(); // 读取下一行
		}
		reader.close();
		is.close();
	}

}
