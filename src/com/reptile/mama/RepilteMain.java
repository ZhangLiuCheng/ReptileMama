package com.reptile.mama;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RepilteMain {

	public static void main(String[] args) {
		String url = "http://www.mama.cn/z/t674/";
		try {
//			Document document = Jsoup.connect(url)
//					.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
//					.timeout(10000).get();
//			File file = FileUtils.getFile("mama.html");
//			System.out.println(file.getAbsolutePath());
//			FileUtils.write(file, document.toString());
			
			File file = FileUtils.getFile("mama.html");
			String html = FileUtils.readFileToString(file, "UTF-8");
			Document document = Jsoup.parse(html);
			parserList(document, "1", "备孕");
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	// 解析列表
	private static void parserList(Document document, String id, String name) throws Exception {
		Elements elements = document.getElementsByClass("g-left").get(0).getElementsByClass("list-mod");
		for (Element element : elements) {
			String modId = element.attr("id");
			String modTitle = element.getElementsByClass("mod-title").select("h4").get(0).text();
//			System.out.println(modId + " " + modTitle);
			
			Elements itemList = element.getElementsByClass("mod-ctn").get(0).getElementsByClass("cate-itemList");
			for (Element itemElem : itemList) {
				Element itemE = itemElem.select("h5 a").get(0);
				String itemHref = itemE.attr("href");
				String itemId = getIdByHref(itemHref);
				String itemName = itemE.text();
//				System.out.println(itemId + "  " + itemName);
				
				Elements cateList = itemElem.getElementsByClass("common-list").get(0).select("li");
				for (Element cateElem : cateList) {
					Element cateE = cateElem.select("a").get(0);
					String cateHref = cateE.attr("href");
					String cateId = getIdByHref(cateHref);
					String cateName = cateE.text();
//					System.out.println(cateId + "  " + cateName);
					parserDetail(cateHref);
					return;
				}
			}
		}
	}
	
	// 解析详情
	private static void parserDetail(String detailUrl) throws Exception {
//		Document document = Jsoup.connect(detailUrl)
//		.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
//		.timeout(10000).get();
		
		File file = FileUtils.getFile("detail.html");
		String html = FileUtils.readFileToString(file, "UTF-8");
		Document document = Jsoup.parse(html);
		Element mainElem = document.getElementsByClass("detail-main").get(0);
		String title = mainElem.getElementsByClass("detail-title").select("h1").text();
		String summary = mainElem.getElementsByClass("detail-summary").text();
		String imgPath = mainElem.getElementsByClass("detail-mainImg").select("img").attr("src");
		imgPath = downloadImage(imgPath);

//		System.out.println(title);
//		System.out.println(summary);
//		System.out.println(img);
//		System.out.println(imgPath);
		
		Elements modElems = mainElem.getElementsByClass("detail-mod");
		for (int i = 0; i <modElems.size() - 3; i ++) {
			Element modElem = modElems.get(i);
			
			String modId = getIdByHref(modElem.getElementsByClass("mod-title").select("a").attr("href"));
			String modTitle = modElem.getElementsByClass("mod-title").select("a").text();
			String modBrief = modElem.getElementsByClass("mod-ctn").html();

			System.out.println(modId + modTitle);
		}
		
		// 相关百科
		Element baikeElem = modElems.get(modElems.size() - 3);
		Elements baikeElems = baikeElem.getElementsByClass("extend-ctn").select("li a");
		for (Element baiElem : baikeElems) {
			String baiKeId = getIdByHref(baiElem.attr("href"));
			String baiKeName = baiElem.text();
//			System.out.println(baiKeId + "  ---  " + baiKeName);
		}
		
		// 相关知识
		Element klElem = modElems.get(modElems.size() - 2);
		Elements klElems = klElem.getElementsByClass("extend-ctn").select("li a");
		for (Element baiElem : klElems) {
			String klId = getIdByHref(baiElem.attr("href"));
			String klName = baiElem.text();
			System.out.println(klId + "  ---  " + klName);
		}
	}
	
	private static String getIdByHref(String href) {
		String[] paths = href.split("/");
		String id = paths[paths.length - 1];
		return id;
	}
	
	private static String downloadImage(String imgPath) throws Exception {
		URL url = new URL("http:" + imgPath);
		String path = url.getPath().replace("/attachment/mamacn", "mom");
		File desk = new File("/Users/admin/Desktop");
		File imgFile = FileUtils.getFile(desk, path);
		if (!imgFile.exists()) {
			File parentFile = new File(imgFile.getParent());
			if (!parentFile.exists()) parentFile.mkdirs();
			imgFile.createNewFile();
			URLConnection con = url.openConnection();  
			con.setConnectTimeout(5*1000);  
			InputStream is = con.getInputStream();  
			byte[] bs = new byte[1024];  
			int len;  
			OutputStream os = new FileOutputStream(imgFile);  
			while ((len = is.read(bs)) != -1) {  
				os.write(bs, 0, len);  
			}  
			os.close();  
			is.close();
		}
		return path;
	}
}
