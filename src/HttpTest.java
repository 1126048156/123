

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
		//1.����httpclientʵ��
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		//2.����get����ʵ��
		
		String serverAddr = "http://v.juhe.cn/weather/index";
		
		Scanner scan=new Scanner(System.in);
		//String city= scan.nextLine();
		String city = "�人";
		city=URLEncoder.encode(city,"utf-8");
		System.out.println(city);
		
		String params="cityname="+city +"&dtype=xml&key=79f100c74c4252475e0a3b85c406646e";
	
		HttpGet httpget = new HttpGet(serverAddr+"?"+params);
		
		//3.ִ�з����������Ӧ
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		
		//4.����õ���httpʵ��
		String responseStr=EntityUtils.toString(entity);
		System.out.println(responseStr);//��String��ʽ��ȡ����
		
		
		//����GSON��ʵ��json�ַ����Ľ���
		//��1�����json����{},������תΪJsonObject
		JsonObject obj= new JsonParser().parse(responseStr).
				getAsJsonObject();  
		//(2)ȡ�����е��ֶΣ��������ͽ��з���
		String code=obj.get("resultcode").getAsString();
		if(code.equals("200")){  //���سɹ�
			JsonObject result= obj.get("result").getAsJsonObject();
			JsonObject today= result.get("future").getAsJsonObject();
			String c=today.get("city").getAsString();
			String date_y=today.get("date_y").getAsString();
			System.out.println(c+date_y+"�������£�");
			String temperature=today.get("temperature").getAsString();
			System.out.println("�¶� :"+ temperature);
			
			
			
		}
		else{
			String reason = obj.get("reason").getAsString();
			System.out.println("��ȡʧ�ܣ�ԭ��"+reason);
		}
		
		
		
		//����dom4j��ʵ��xml�ַ����Ľ���
		//(1)����string����xml��document����
	/*	Document document = DocumentHelper.parseText(responseStr);
		//(2)���Ȼ�ȡroot�ڵ�
		Element root= document.getRootElement();
		//(3)ʹ��element������ȡĳ�ڵ��ֵ
		String code = root.element("resultcode").getText();
		if(code.equals("200")){
			Element today=root.element("result").element("today");
			String c = today.element("city").getText();
			String date_y= today.element("date_y").getText();
			System.out.println(c+date_y+"�������£�");
			String temperature=today.element("temperature").getText();
			System.out.println("�¶� :"+ temperature);
		}
		else
		{
			String reason = root.element("reason").getText();
			System.out.println("��ȡʧ�ܣ�ԭ��"+reason);
		}*/
		
		
		
		

		
		//5. �ͷ�������Դ������ִ�еģ�
		httpclient.close();


		

		

		

	}

}
