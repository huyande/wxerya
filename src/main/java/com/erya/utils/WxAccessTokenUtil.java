package com.erya.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WxAccessTokenUtil {

	public static String URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	
	public static String getAccessToken(String appid,String secret) {
		 String replacedUrl = URL
                 .replace("APPID", appid)
                 .replace("SECRET", secret);
		 String access_token = "";
		 try {
			String boby = HttpClientUtil.get(replacedUrl);
			ObjectMapper mapper = new ObjectMapper();
			Map<String,String> data = mapper.readValue(boby, Map.class);
			access_token=data.get("access_token");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return access_token;
	}
	
	/**
	 * 判断access_token 是否失效 如果失效 则重新生成 没有失效 则返回
	 */
	public static void iSaccessTokenTimeOut(Map<String,Date> accessMap,String appid,String secret) {
		String accessToken ="";
		if(accessMap.size()==0) {
			accessToken = getAccessToken(appid,secret);
			accessMap.put(accessToken, new Date());
			System.err.println("新建");
		}else {
			System.err.println("取map");
			int deltaTime=0; //时间差
			for(Map.Entry<String, Date> entry: accessMap.entrySet()) {
				Date oldTime = entry.getValue();
				deltaTime = DateUtil.getSecondsBetween(oldTime, new Date());
				System.err.println(deltaTime);
				if(deltaTime>7200) {
					//如果时间差大于 7200 秒  重新生成一个 access token  并且先清除map 集合 在添加
					accessMap.clear();
					accessToken = getAccessToken(appid,secret);
					accessMap.put(accessToken, new Date());
					break;
				}
			}
		}
	}

	/**
	* @Description 本地生成token文件 用户保存token
	* @Author  hyd
	* @Date   2019/8/22 0022 上午 10:01
	* @Param
	* @Return
	* @Exception
	*/
	public static String getAccessToken(String filePath,String appid,String secret ){
		String accessToken ="";
		try {
			//先判断是否存在该文件
			if(FileUtil.isExist(filePath)){
				File file = new File(filePath);
				//判断文件是否有值
				if(file.length()!=0){
					//有值则进行读取 并将读取出来的值进行有效时间的判断
					Map<String,Date> accessMap = (Map<String,Date>)WxFileUtils.readWxUserByFile(filePath);
					int deltaTime=0; //时间差
					for(Map.Entry<String, Date> entry: accessMap.entrySet()) {
						Date oldTime = entry.getValue();
						deltaTime = DateUtil.getSecondsBetween(oldTime, new Date());
						System.err.println(deltaTime);
						if(deltaTime>7200) {
							//如果时间差大于 7200 秒  重新生成一个 access token  并且先清除map 集合 在添加
							accessToken = getAccessToken(appid,secret);
							//先清除集合
							accessMap.clear();
							accessMap.put(accessToken, new Date());
							//保存集合 到文件中
							FileOutputStream out;

							out = new FileOutputStream(file);
							ObjectOutputStream objOut = new ObjectOutputStream(out);
							objOut.writeObject(accessMap);
							objOut.flush();
							objOut.close();
							break;
						}else{//如果有效
							accessToken = entry.getKey();
							break;
						}
					}
				}
			}else{ //创建该文件 将生成的token 存到改文件中
				//创建文件
				FileUtil.createFile(filePath);
				File file = new File(filePath);
				Map<String,Date> accessMap = new HashMap<>();
				accessToken = getAccessToken(appid,secret);
				accessMap.put(accessToken, new Date());
				//保存集合 到文件中
				FileOutputStream out;
				out = new FileOutputStream(file);
				ObjectOutputStream objOut = new ObjectOutputStream(out);
				objOut.writeObject(accessMap);
				objOut.flush();
				objOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}




	
	public static void main(String[] args) {
		/*
		 * Date convertStrToDate; try { convertStrToDate =
		 * DateUtil.convertStrToDate("2019-6-22 13:49:47"); int secondsBetween =
		 * DateUtil.getSecondsBetween(convertStrToDate,new Date());
		 * System.out.println(new Date()); System.out.println(convertStrToDate);
		 * System.out.println(secondsBetween); } catch (ParseException e) {
		 * e.printStackTrace(); }
		 */
		
		
		String accessToken = getAccessToken("wx541622ec4dee0592","9d11a5cddf9121244ff614095f2dc24b");
		System.out.println(accessToken);
		
	}
}
