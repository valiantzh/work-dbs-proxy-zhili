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
import com.dcdzsoft.EduException;
import com.dcdzsoft.client.web.PMWebClientAdapter;
import com.dcdzsoft.client.web.QYWebClientAdapter;
import com.dcdzsoft.config.ApplicationConfig;
import com.dcdzsoft.config.ErrorMsgConfig;
import com.dcdzsoft.constant.ErrorCode;
import com.dcdzsoft.dto.business.InParamPMCompanyQry;
import com.dcdzsoft.dto.business.InParamQYStat4Terminal;
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
@WebServlet(name = "/boxusedstatus", urlPatterns = { "/boxusedstatus" })
public class BoxUsedStatusServlet extends HttpServlet {
	/**
	 * 获取箱体使用状态
	 */
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json;charset=utf-8";
	private static final String CHARSET = "utf-8";
	private static Log log = org.apache.commons.logging.LogFactory
			.getLog(PostmanMsgServlet.class);
	private static ApplicationConfig apCfg = ApplicationConfig.getInstance();

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JSONObject respJson = new JSONObject();
		StringBuilder errMsg = new StringBuilder();
		Map<String, String>map = new HashMap<String,String>();
		String errorMsg = "";
		req.setCharacterEncoding(CHARSET);
		resp.setContentType(CONTENT_TYPE);
		boolean isOk = true;
		// 读取消息
		BufferedReader bufferReader = req.getReader();// 获取头部参数信息
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = bufferReader.readLine()) != null) {
			buffer.append(line);
		}
		String postData = buffer.toString();
		if (apCfg.isLogRawMsg()) {
            log.info("/boxusedstatus =>request: " + postData);
        }
		bufferReader.close();
		// 解析
		try{
			JSONObject reqStr = JSONObject.fromObject(postData).getJSONObject("param");
			String action = reqStr.getString("action");   
			if(!action.equals("qry")){
				isOk = false;
				errMsg.append("action,");
			}
			map.put("action", action);
			String cabinetID = reqStr.optString("cabinetID");
			if(StringUtils.isNotEmpty(cabinetID)){
				map.put("cabinetID", cabinetID);
			}
			String companyID = reqStr.getString("companyID");
			if (StringUtils.isEmpty(companyID)) {
				isOk = false;
				errMsg.append("companyID,");
			}
			map.put("companyID", companyID);
			//sign
			String sign = reqStr.optString("sign");
			if (StringUtils.isEmpty(sign)) {
				isOk = false;
				errMsg.append("sign,");
			}
			String compareSign = SignRankUtil.signRank(map, apCfg.getApiKeyWs());	
			if( !sign.equals(compareSign)){
				isOk = false;
				errMsg.append("sign,");
			}		
			JSONArray data = new JSONArray();
			if (isOk) {
				try {
				    InParamPMCompanyQry p1 = new InParamPMCompanyQry();
				    p1.setCompanyID(companyID);
				    RowSet rset = PMWebClientAdapter.doBusiness(p1);
				    if(RowSetUtils.rowsetNext(rset)){
				        InParamQYStat4Terminal inparam = new InParamQYStat4Terminal();
	                    inparam.setTerminalNo(cabinetID);
	                    inparam.setDepartmentID( RowSetUtils.getStringValue(rset, "DepartmentID"));
	                    RowSet rset2 = QYWebClientAdapter.doBusiness(inparam);
	                    while (RowSetUtils.rowsetNext(rset2)) {
	                        JSONObject dataJson = new JSONObject();
	                        dataJson.put("cabinetID",RowSetUtils.getStringValue(rset2, "TerminalNo"));
	                        dataJson.put("cabinetName",RowSetUtils.getStringValue(rset2, "TerminalName"));
	                        dataJson.put("companyName",RowSetUtils.getStringValue(rset, "CompanyName"));
	                        dataJson.put("inboxNum",RowSetUtils.getStringValue(rset2, "InBoxNum"));
	                        dataJson.put("takeOutNum",RowSetUtils.getStringValue(rset2, "TakeOutNum"));
	                        dataJson.put("takeBackNum",RowSetUtils.getStringValue(rset2, "TakeBackNum"));
	                        dataJson.put("expiredNum",RowSetUtils.getStringValue(rset2, "ExpiredNum"));
	                        dataJson.put("managerOutNum",RowSetUtils.getStringValue(rset2, "ManagerOutNum"));	                
	                        data.add(dataJson);
	                    }
				    }else{
				        throw new EduException(ErrorCode.ERR_COMPANYNOTEXISTS);
				    }			
				} catch (EduException e) {
					isOk = false;
					errMsg.append(e.getMessage());
					errorMsg = ErrorMsgConfig.getLocalizedMessage(e.getMessage());// 读取错误信息提示文件error_zh中错误代码对应的错误信息。
					log.error(errMsg+"|"+errorMsg);
				} catch (Exception e) {
					isOk = false;
					errMsg.append(e.getMessage());
					log.error(errMsg);
				}
				if (isOk) {
					respJson.put("success", true);
					respJson.put("code", "0");
					respJson.put("data", (data.isEmpty()? "": data.toString() ));
				} else {
					respJson.put("success", false);
					respJson.put("code", ErrorCode.ERR_PARMERR);
					respJson.put("data", "");
				}
			} else {
				respJson.put("success", false);
				respJson.put("code", ErrorCode.ERR_PARMERR);
				respJson.put("data", "");
			}
		}catch(JSONException e){
			respJson.put("success", false);
			respJson.put("code", ErrorCode.ERR_PARMERR);
			respJson.put("data",  "");
		}
		System.out.println("respJson：" + respJson);
		PrintWriter out = resp.getWriter();
		out.println(respJson);
		out.flush();
		out.close();
	}
}
