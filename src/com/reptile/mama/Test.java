package com.reptile.mama;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class Test {

	public static void main(String[] args) throws Exception {
		String urlString = "//pics.mama.cn/attachment/mamacn/images/201712/20171228/163101_12641.jpg";
		URL url = new URL("http:" + urlString);
		System.out.println(url.getPath());
		String path = url.getPath().replace("/attachment/mamacn", "mom");
		File desk = new File("/Users/admin/Desktop");
		File imgFile = FileUtils.getFile(desk, path);
		System.out.println(imgFile.getAbsolutePath());
		FileUtils.write(imgFile, "asdf");
	}
}
