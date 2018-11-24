package com.dcdzsoft.partner.zhili.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.poi.hssf.record.formula.ErrPtg;

import com.dcdzsoft.EduException;
import com.dcdzsoft.client.web.PMWebClientAdapter;
import com.dcdzsoft.config.ApplicationConfig;
import com.dcdzsoft.config.ErrorMsgConfig;
import com.dcdzsoft.constant.ErrorCode;
import com.dcdzsoft.dto.business.InParamPMPostmanAdd;
import com.dcdzsoft.dto.business.InParamPMPostmanDel;
import com.dcdzsoft.dto.business.InParamPMPostmanMod;
import com.dcdzsoft.dto.business.InParamPMPostmanModPwd;
import com.dcdzsoft.dto.business.InParamPMPostmanQry;
import com.dcdzsoft.partner.zhili.util.SignRankUtil;
import com.dcdzsoft.sda.db.RowSetUtils;

/**
 * <p>Title: 智能自助包裹柜系统</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2014</p>
 *
 * <p>Company: 杭州东城电子有限公司</p>
 *
 * @author 
 * @version 1.0
 */

@WebServlet(name = "/postmanmsg", urlPatterns = { "/postmanmsg" })
public class PostmanMsgServlet extends HttpServlet{
	/**
	 * 公司快递员信息：qry：查询，add：增加 mod：修改：del 删除（必填）
	 */
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json;charset=utf-8";
	private static final String CHARSET = "utf-8";
	private static Log log = org.apache.commons.logging.LogFactory.getLog(PostmanMsgServlet.class);
	private static ApplicationConfig apCfg = ApplicationConfig.getInstance();
	
	public void init() throws ServletException{
		
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JSONObject respJson = new JSONObject();
		StringBuilder errMsg = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
        String errorMsg ="";
		req.setCharacterEncoding(CHARSET);
		resp.setContentType(CONTENT_TYPE);
		boolean isOk = true;
		//读取消息
		BufferedReader bufferReader = req.getReader();//获取头部参数信息
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = bufferReader.readLine()) != null) {
		    buffer.append(line);
		}		
		String postData = buffer.toString();
		if (apCfg.isLogRawMsg()){
            log.info(" =>request: " + postData.toString());
        }
		bufferReader.close();	
		//文档中选填字段必须用optString()来解析。
		// 解析		
		try{
			JSONObject reqStr = JSONObject.fromObject(postData).getJSONObject("param");
			String action = reqStr.getString("action");    //快递员信息操作编号：qry：查询，add：增加 mod：修改：del 删除（必填）
			if(!action.equals("qry")&&!action.equals("add")&&!action.equals("mod")&&!action.equals("del")){
				isOk = false;
				errMsg.append("action,");
			}
			map.put("action", action);
			String companyID = reqStr.getString("companyID");
			if(StringUtils.isEmpty(companyID)){
				isOk = false;
				errMsg.append("companyID,");
			}
			map.put("companyID", companyID);
			//验证sign
			String sign = reqStr.optString("sign");
			if (StringUtils.isEmpty(sign)) {
				isOk = false;
				errMsg.append("sign,");
			}
			String postmanID = reqStr.optString("postmanID");
			if(StringUtils.isNotEmpty(postmanID)){
				map.put("postmanID", postmanID);
			}					
			String postmanName = "";
			String password = "";
			String mobile = "";
			String email = "";
			String cardNo = "";
			if(action.equals("add")||action.equals("mod")){
					postmanName = reqStr.optString("postmanName");
					password = reqStr.optString("password");
					mobile = reqStr.optString("mobile");
					email = reqStr.optString("email");
					cardNo = reqStr.optString("cardNo");
					map.put("postmanName", postmanName);
					map.put("password",password);
					map.put("mobile", mobile);
					if(StringUtils.isNotEmpty(email)){
						map.put("email", email);
					}
					if(StringUtils.isNotEmpty(cardNo)){
						map.put("cardNo", cardNo);
					}					
			}			
			String compareSign =SignRankUtil.signRank(map,  apCfg.getApiKeyWs());
			if( !sign.equals(compareSign)){
				isOk = false;
				errMsg.append("sign,");
			}
	    	JSONArray data = new JSONArray();
	        if(isOk){
	        	try{
	        		//快递员信息查询
	        		if(action.equals("qry")){
	        			InParamPMPostmanQry inparam = new InParamPMPostmanQry();
			        	inparam.setPostmanID(postmanID);
			        	inparam.setCompanyID(companyID);
			            RowSet rset2 = PMWebClientAdapter.doBusiness(inparam);
				        while(RowSetUtils.rowsetNext(rset2)){
				        	JSONObject dataJson = new JSONObject();
				        	dataJson.put("postmanID", RowSetUtils.getStringValue(rset2, "PostmanID"));
				        	dataJson.put("postmanName", RowSetUtils.getStringValue(rset2, "PostmanName"));
				        	dataJson.put("companyName", RowSetUtils.getStringValue(rset2, "CompanyName"));
				        	dataJson.put("cardNo", RowSetUtils.getStringValue(rset2, "BindCardID"));
				        	dataJson.put("mobile", RowSetUtils.getStringValue(rset2, "Mobile"));	        				        			
				        	dataJson.put("email", RowSetUtils.getStringValue(rset2, "Email"));
				        	data.add(dataJson);
				        }
				        //快递员信息增加
	        		}else if(action.equals("add")){
	        			InParamPMPostmanAdd inparamAdd = new InParamPMPostmanAdd();
	        			inparamAdd.setCompanyID(companyID); 
			        	inparamAdd.setPostmanID(postmanID);
			        	inparamAdd.setPostmanName(postmanName);
			        	inparamAdd.setPassword(password); 
			        	inparamAdd.setMobile(mobile); 
			        	inparamAdd.setEmail(email); 
			        	inparamAdd.setBindCardID(cardNo); 		        	    
			        	PMWebClientAdapter.doBusiness(inparamAdd);
	        		}			 		        	
			        //快递员信息修改
	        		else if(action.equals("mod")){
			        	InParamPMPostmanMod inparamMod = new InParamPMPostmanMod();
			        	inparamMod.setPostmanID(postmanID);
			        	inparamMod.setPostmanName(postmanName);
			        	inparamMod.setMobile(mobile); 
			        	inparamMod.setEmail(email); 
			        	inparamMod.setBindCardID(cardNo); 
			        	if(StringUtils.isNotEmpty(password)){
			        		InParamPMPostmanModPwd inparamModPwd = new InParamPMPostmanModPwd();
			        		inparamModPwd.setPostmanID(postmanID);
			        		inparamModPwd.setPassword(password); 
			        		PMWebClientAdapter.doBusiness(inparamModPwd);
			        	};
			        	if(StringUtils.isEmpty(inparamMod.PostmanName)&&StringUtils.isEmpty(password)&&StringUtils.isEmpty(inparamMod.Mobile)&&StringUtils.isEmpty(inparamMod.Email)&&StringUtils.isEmpty(inparamMod.BindCardID))
			        	{
			        		throw new EduException(ErrorCode.ERR_MODPMPOSTMANFAIL);
			        	}//修改信息为空，修改失败	
			        	PMWebClientAdapter.doBusiness(inparamMod);
			        	}	
			        //快递员信息删除
	        		else {
			        	InParamPMPostmanDel inparamDel = new InParamPMPostmanDel();
			        	inparamDel.setPostmanID(postmanID);
			        	PMWebClientAdapter.doBusiness(inparamDel);        
			        }
				}catch(EduException e){
					isOk = false;
				    errMsg.append(e.getMessage());
				    errorMsg = ErrorMsgConfig.getLocalizedMessage(e.getMessage());//读取错误信息提示文件error_zh中错误代码对应的错误信息。
				    log.error(errMsg+"|"+errorMsg);
				}catch(Exception e){
					isOk = false;
					errMsg.append(e.getMessage());
					log.error(errMsg);
				}
				if(isOk){
					respJson.put("success", true);
					respJson.put("code", "0");
					respJson.put("data", (action.equals("qry")?data.toString():""));
				}else{
					respJson.put("success", false);
					respJson.put("code", errMsg.toString());
					respJson.put("data", "");
				}
	        }else{
				respJson.put("success", false);
				respJson.put("code", ErrorCode.ERR_PARMERR);
				respJson.put("data", "");
	        }
		}catch(JSONException e){
			respJson.put("success", false);
			respJson.put("code", ErrorCode.ERR_PARMERR);
			respJson.put("data", "");
		}
		System.out.println("respJson："+respJson);
		PrintWriter out = resp.getWriter();
        out.println(respJson);
        out.flush();
        out.close();
	}
}
