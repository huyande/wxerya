package com.erya.utils;

import com.erya.bean.bo.WxUserSearch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author hyd
 * @Description : 用于读写微信用户信息的 文件工具
 * @Date 2019/8/16 0016
 **/
public class WxFileUtils {
	/**
	 * @Description 记录用户查题次数 是否点击了链接  用户唯一的标识
	 * @Author hyd
	 * @Date 2019/8/16 0016 下午 5:06
	 * @Param
	 * @Return
	 * @Exception
	 */
	public static void wirteWxUserToFile(Map<String, String> msgXmlMap,boolean isclick,String filePath) {
		try {
			//创建文件
			FileUtil.createFile(filePath);
			List<WxUserSearch> wxUserSearches = null;
			//读取之前的文件 ，将其他的对象读取出来 获取一个集合  在给这个集合添加值 然后在进保存
			//先判断文件是否为空 如果为空则跳过 读取文件 直接进行保存文件
			File file = new File(filePath);
			if(file.length()!=0){
				wxUserSearches= (List<WxUserSearch>) readWxUserByFile(filePath);
			}
			//创建对象
			WxUserSearch wxUserSearch = new WxUserSearch();
			wxUserSearch.setOpenid(msgXmlMap.get("FromUserName"));
			wxUserSearch.setSearchCount(1);
			wxUserSearch.setClickLink(false);

			boolean writeFlag = true;
			if(wxUserSearches!=null){
				//找到是否有该openid 的对象 如果有更新值 没有则新增值
				boolean flag = false;
				for(WxUserSearch wxUser :wxUserSearches){
					if(wxUser.getOpenid().equals(msgXmlMap.get("FromUserName"))){
						flag=true;
						break;
					}
				}
				if(flag){
					for(WxUserSearch wxUser :wxUserSearches){
						if(wxUser.getOpenid().equals(msgXmlMap.get("FromUserName"))){
							wxUser.setSearchCount(wxUser.getSearchCount()+1);
							if(wxUser.getClickLink()){
								writeFlag=false;
								break;
							}
							wxUser.setClickLink(isclick);
							break;
						}
					}
				}else{ //没有找到
					wxUserSearches.add(wxUserSearch);
				}
			}else{
				wxUserSearches = new ArrayList<>();
				wxUserSearches.add(wxUserSearch);
			}

			if(writeFlag){
				FileOutputStream out;

				out = new FileOutputStream(file);
				ObjectOutputStream objOut = new ObjectOutputStream(out);
				objOut.writeObject(wxUserSearches);
				objOut.flush();
				objOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description 记录用户查题次数 是否点击了链接  用户唯一的标识
	 * @Author hyd
	 * @Date 2019/8/16 0016 下午 5:06
	 * @Param
	 * @Return
	 * @Exception
	 */
	public static void wirteWxUserToFile(String openid,boolean isclick,String filePath) {
		try {
			//创建文件
			FileUtil.createFile(filePath);
			List<WxUserSearch> wxUserSearches = null;
			//读取之前的文件 ，将其他的对象读取出来 获取一个集合  在给这个集合添加值 然后在进保存
			//先判断文件是否为空 如果为空则跳过 读取文件 直接进行保存文件
			File file = new File(filePath);
			if(file.length()!=0){
				wxUserSearches= (List<WxUserSearch>) readWxUserByFile(filePath);
			}
			//创建对象
			WxUserSearch wxUserSearch = new WxUserSearch();
			wxUserSearch.setOpenid(openid);
			wxUserSearch.setSearchCount(1);
			wxUserSearch.setClickLink(false);

			boolean writeFlag = true;
			if(wxUserSearches!=null){
				//找到是否有该openid 的对象 如果有更新值 没有则新增值
				boolean flag = false;
				for(WxUserSearch wxUser :wxUserSearches){
					if(wxUser.getOpenid().equals(openid)){
						flag=true;
						break;
					}
				}
				if(flag){
					for(WxUserSearch wxUser :wxUserSearches){
						if(wxUser.getOpenid().equals(openid)){
							wxUser.setSearchCount(wxUser.getSearchCount()+1);
							if(wxUser.getClickLink()){
								writeFlag=false;
								break;
							}
							wxUser.setClickLink(isclick);
							break;
						}
					}
				}else{ //没有找到
					wxUserSearches.add(wxUserSearch);
				}
			}else{
				wxUserSearches = new ArrayList<>();
				wxUserSearches.add(wxUserSearch);
			}

			if(writeFlag){
				FileOutputStream out;

				out = new FileOutputStream(file);
				ObjectOutputStream objOut = new ObjectOutputStream(out);
				objOut.writeObject(wxUserSearches);
				objOut.flush();
				objOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description 读取文件
	 * @Author hyd
	 * @Date 2019/8/16 0016 下午 5:42
	 * @Param
	 * @Return
	 * @Exception
	 */
	public static Object readWxUserByFile(String filePath) {
		Object temp = null;
		File file = new File(filePath);
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			objIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return temp;
	}

}
