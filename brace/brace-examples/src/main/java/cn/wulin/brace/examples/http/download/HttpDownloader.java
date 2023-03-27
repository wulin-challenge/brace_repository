package cn.wulin.brace.examples.http.download;

import java.io.*;
import java.net.*;

public class HttpDownloader {
	private static final int BUFFER_SIZE = 4096;

	public static void main(String[] args) {
		// 下载地址和本地保存路径
		String url = "http://www.notescloud.top/rssdld/dldfl/downloader.zip";
		String filepath = "d:/wulin_java_resources_repository.zip";

		try {
			// 创建本地文件
			File file = new File(filepath);
			RandomAccessFile raf = new RandomAccessFile(file, "rw");

			// 发起HTTP请求
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);

			// 检查响应状态码
			if (conn instanceof HttpURLConnection) {
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				if (httpConn.getResponseCode() != 200) {
					System.out.println("Unexpected status code " + httpConn.getResponseCode());
					return;
				}
			}

			// 获取文件大小和下载位置
			long fileSize = conn.getContentLengthLong();
			long downloadedSize = 0;

			// 检查本地文件是否已存在
			if (file.exists()) {
				downloadedSize = file.length();
				if (downloadedSize >= fileSize) {
					System.out.println("File already downloaded");
					return;
				}

				// 设置HTTP请求头，支持断点续传
				HttpURLConnection httpConn = (HttpURLConnection) conn;
//				httpConn.setRequestProperty("Range", "bytes=" + downloadedSize + "-");
//				httpConn.setRequestProperty("Connection", "close");
				raf.seek(downloadedSize);
			}

			// 下载文件
			InputStream is = conn.getInputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				raf.write(buffer, 0, len);
				downloadedSize += len;
			}

			// 关闭输入输出流
			raf.close();
			is.close();

			System.out.println("Download completed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
