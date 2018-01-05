

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpTest {

	public static void main(String[] args) throws Exception{
		//1.创建httpclient实例
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		//2.构建get方法实例
		
		String serverAddr = "http://v.juhe.cn/weather/index";
		
		Scanner scan=new Scanner(System.in);
		//String city= scan.nextLine();
		String city = "武汉";
		city=URLEncoder.encode(city,"utf-8");
		System.out.println(city);
		
		String params="cityname="+city +"&dtype=xml&key=79f100c74c4252475e0a3b85c406646e";
	
		HttpGet httpget = new HttpGet(serverAddr+"?"+params);
		
		//3.执行方法，获得响应
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		
		//4.处理得到的http实体
		String responseStr=EntityUtils.toString(entity);
		System.out.println(responseStr);//以String形式获取内容
		
		
		//利用GSON包实现json字符串的解析
		//（1）如果json串是{},则首先转为JsonObject
		JsonObject obj= new JsonParser().parse(responseStr).
				getAsJsonObject();  
		//(2)取对象中的字段，根据类型进行返回
		String code=obj.get("resultcode").getAsString();
		if(code.equals("200")){  //返回成功
			JsonObject result= obj.get("result").getAsJsonObject();
			JsonObject today= result.get("future").getAsJsonObject();
			String c=today.get("city").getAsString();
			String date_y=today.get("date_y").getAsString();
			System.out.println(c+date_y+"天气如下：");
			String temperature=today.get("temperature").getAsString();
			System.out.println("温度 :"+ temperature);
			
			
			
		}
		else{
			String reason = obj.get("reason").getAsString();
			System.out.println("获取失败，原因："+reason);
		}
		
		
		
		//利用dom4j包实现xml字符串的解析
		//(1)根据string生成xml的document对象
	/*	Document document = DocumentHelper.parseText(responseStr);
		//(2)首先获取root节点
		Element root= document.getRootElement();
		//(3)使用element方法获取某节点的值
		String code = root.element("resultcode").getText();
		if(code.equals("200")){
			Element today=root.element("result").element("today");
			String c = today.element("city").getText();
			String date_y= today.element("date_y").getText();
			System.out.println(c+date_y+"天气如下：");
			String temperature=today.element("temperature").getText();
			System.out.println("温度 :"+ temperature);
		}
		else
		{
			String reason = root.element("reason").getText();
			System.out.println("获取失败，原因："+reason);
		}*/
		
		
		
		

		
		//5. 释放连接资源（必须执行的）
		httpclient.close();


		

		

		

	}

}
