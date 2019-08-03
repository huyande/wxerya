package com.erya.service.impl;

import com.erya.bean.po.TAdurl;
import com.erya.bean.po.TAnswer;
import com.erya.bean.po.TQuestion;
import com.erya.bean.po.TSubject;
import com.erya.bean.vo.AnswerSet;
import com.erya.dao.TAdurlMapper;
import com.erya.dao.TAnswerMapper;
import com.erya.dao.TQuestionMapper;
import com.erya.dao.TSubjectMapper;
import com.erya.service.SolrService;
import com.erya.service.WxMessageService;
import com.erya.utils.ReplyMsgUtils;
import com.erya.utils.WXmlUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class WxMessageServiceImpl implements WxMessageService{
	
	//存储access token
	private Map<String,Date> accessMap = new HashMap<String, Date>();
	//存储用户 上一次操作的代码
	private Map<String,String> handleLastMap = new HashMap<String, String>();
	
	private int flagShareOrBiaob =0; // 1 代表 表白  2 代表分享图片

	
	@Autowired
	private TQuestionMapper questionMapper;
	
	@Autowired
	private TSubjectMapper subjectMapper;
	
	@Autowired
	private TAnswerMapper answerMapper;

	@Autowired
	private TAdurlMapper tAdurlMapper;

	@Autowired
	private SolrService solrService;
	

	@Override
	public Map<String, String> receptionMsg(HttpServletRequest request) {
		Map<String, String> xmlToMap =null;
		try {
			xmlToMap = WXmlUtils.xmlToMap(request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return xmlToMap;
	}

	@Override
	public String sendDealMsg(Map<String, String> msgXmlMap,String WxUser,String appid,String secret,String wXfileImagePath) throws Exception{
		//System.out.println(msgXmlMap);
		//消息类型
		String MsgType = msgXmlMap.get("MsgType");
		String msg="";
		
		
		switch (MsgType) {
			case WXmlUtils.MESSAGE_TEXT:
				StringBuilder sb = new StringBuilder();
				//如果集合中 没有当前的用记录的  则进入 看用户回复的什么
				if(handleLastMap.containsKey(msgXmlMap.get("FromUserName"))) { //如果包含当前用户的数据 
					if(handleLastMap.get(msgXmlMap.get("FromUserName")).equals("3")) {//执行想对应的东西
						if(msgXmlMap.get("Content").equals("0")) {
							String menuShow = menuShow();
							sb.append("退出搜题  感谢使用").append("\n\n").append(menuShow);;
							msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
							handleLastMap.remove(msgXmlMap.get("FromUserName"));
						}else {
							log.info("输入问题："+msgXmlMap.get("Content")+"====openid:"+msgXmlMap.get("FromUserName"));
							//PageInfo<AnswerSet> answerSet = searchQuestByContentLike(1,3,msgXmlMap.get("Content").replaceAll("[\\pP‘’“”]", ""));
							PageInfo<AnswerSet> answerSet = solrService.search(msgXmlMap.get("Content"));
							List<AnswerSet> list = answerSet.getList();
							if(list.size()>0) {
								for(AnswerSet ans :list) {
									sb.append("【问题 】："+ans.getQuestion()).append("\n\n").append("【答案】："+ans.getAnswerStr()).append("\n\n");
									if(ans.getAnswers()!=null && ans.getAnswers().size()>0) {
										for(TAnswer tAns :ans.getAnswers()) {
											sb.append("☞"+tAns.getAnswer()).append("\n\n");
										}
									}
									
									sb.append("-/:rose《"+ans.getSubjectName()+"》/:rose").append("\n\n");
								}
								//查询广告数据
								TAdurl tAdurl = tAdurlMapper.selectByPrimaryKey(1);
								//收集答案不容易 帮我点一下广告吧！〔点击该链接 文章下方的广告〕回复【0】 退出搜题
								sb.append("<a href='"+tAdurl.getAdurl()+"'>"+tAdurl.getAdcontent()+"</a>").append("\n\n");
								sb.append("回复【0】 退出搜题").append("\n\n");
								//发送消息
								msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
							}else {
								//查询广告数据
								TAdurl tAdurl = tAdurlMapper.selectByPrimaryKey(2);
								sb.append("/::O 您输入的内容 “"+msgXmlMap.get("Content")+"” 未找到答案，请输入题目内容前部分，【点击下方链接，留言反馈你需要的课程信息】").append("\n\n");
								sb.append("<a href='"+tAdurl.getAdurl()+"'>"+tAdurl.getAdcontent()+"</a>").append("\n\n");
								msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
							}
						}
						
					}
				}else {
					if(msgXmlMap.get("Content").equals("1")){
						sb.append("----------/:rose---------").append("\n\n");
						sb.append("欢迎使用尔雅课答案查询功能").append("\n\n");
						sb.append("将问题回复到此页面 请等待一段时间").append("\n\n");
						sb.append("<a href='https://mp.weixin.qq.com/s/1VilyBA-F_zCAE4ZcwqCOw'>点击我了解如何使用</a>").append("\n\n");
						sb.append("【回复 0 退出 搜题】").append("\n\n");
						sb.append("----------/:rose---------").append("\n\n");
						msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
						handleLastMap.put(msgXmlMap.get("FromUserName"), "3");
					}else {
						//菜单
						String menuShow = menuShow();
						sb.append(menuShow);
						msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
					}
				}
				
				break;
			case WXmlUtils.MESSAGE_EVENT:
				StringBuilder sb_event = new StringBuilder();
				//订阅事件
				if(msgXmlMap.get("Event").equals(WXmlUtils.MESSAGE_SUBSCRIBE)) {
					String menuShow = menuShow();
					sb_event.append("欢迎来到中卫校区校园服务。既然关注了，请不要轻易的取消关注吆！").append("\n\n")
					.append("【本站主打栏目】\n\n").append(menuShow);
					msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb_event.toString());
				}
				break;
			default:
				String menuShow = menuShow();
				msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,menuShow);
				break;
		}
		return msg;
	}
	
	
	public PageInfo<AnswerSet> searchQuestByContentLike(Integer currentpage, Integer pagesize, String search) {
		int page = currentpage==null?1:currentpage;
		int pageSize = pagesize==null?10:pagesize;
		PageHelper.startPage(page,pageSize);
		//查到所有的问题列表
		List<TQuestion> questList = questionMapper.searchQuestByContentLike(search);
		List<AnswerSet> answerList = new ArrayList<>();
		for(TQuestion quest:questList){
			AnswerSet answerSet = new AnswerSet();
			TSubject subject = subjectMapper.selectByPrimaryKey(quest.getSubId());
			//查询答案集合 
			List<TAnswer> answersList = answerMapper.selectByQuestId(quest.getId());
			List<String> ansStrList = new ArrayList<>();
			StringBuilder anStrsb = new StringBuilder();
			//标识 是否是判断题还是选择题  false 判断题  true 选择题 
			boolean flag =false; 
			for (TAnswer tAnswer : answersList) {
				//如果答案的长度大于1  说明是选择题
				if(tAnswer.getAnswer().length()>1){
					String substring = tAnswer.getAnswer().substring(0, tAnswer.getAnswer().indexOf('.'));
					ansStrList.add(substring);
					flag=true;
				}else{ //其他则是判断题 判断题 无需截取字符串 
					ansStrList.add(tAnswer.getAnswer());
				}
			}
			Collections.sort(ansStrList);
			for (String str : ansStrList) {
				anStrsb.append(str+" ");
			}
			//设置问题
			//answerSet.setQuestion(quest.getContent());
			answerSet.setQuestId(quest.getId());
			answerSet.setQuestion(quest.getOriginalContent());
			answerSet.setSubjectName(subject.getSubName());
			if(flag){
				answerSet.setAnswers(answersList);
			}
			answerSet.setAnswerStr(anStrsb.toString());
			answerList.add(answerSet);
		}
		int count = questionMapper.searchQuestByContentLikeCount(search);
		
		PageInfo<AnswerSet> pageInfo = new PageInfo<>();
		pageInfo.setList(answerList);
		pageInfo.setTotal(count);
		return pageInfo;
	}
	
	/**
	 * 菜单
	 * @return
	 */
	public String menuShow() {
		 StringBuilder sb = new StringBuilder();
		 sb.append("请回复对应的数字 进入相对应的功能\n\n").append("-----/:rose菜单STRT/:rose-----").append("\n\n")
		.append("〖1〗 尔雅超星答案查询\n\n")
		.append("-------/:roseEND/:rose-------");
		 
		 return sb.toString();
	}

	/**
	 * 创建一个定时方法  用来清除 handleLastMap集合中的数据 0 0 3 * * ?
	 */
	@Scheduled(cron = "0 0 3 * * ?")
	public void clearHandleLastMap(){
		log.info("执行定时任务");
		handleLastMap.clear();
		log.info("集合清除成功");
	}
}
