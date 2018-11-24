package com.dcdzsoft.sms.impl;

import java.io.IOException;
import java.util.Properties;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.dcdzsoft.EduException;
import com.dcdzsoft.config.ApplicationConfig;

import com.dcdzsoft.sms.SMSInfo;
import com.dcdzsoft.sms.SMSManager;


public class TestSMS {
    private static String physicalPath = "D:\\MyProject\\java\\DBSManagerBZ\\dbs\\";
    private static ApplicationConfig apCfg = ApplicationConfig.getInstance();
    public static void init(){
        
        apCfg.setPhysicalPath(physicalPath);
        try {
            //apCfg.load("D:\\MyProject\\java\\DBSManagerBZ\\dbs\\WEB-INF\\appconfig.xml");
            apCfg.load("dbs/WEB-INF/appconfig.xml");
            Properties p = new Properties();
            p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, physicalPath+"WEB-INF/vm/");
            p.setProperty(Velocity.INPUT_ENCODING, "utf-8");
            p.setProperty(Velocity.OUTPUT_ENCODING, "utf-8");
            Velocity.init(p);
            
            
            
            apCfg.setSmsCharset("UTF-8");
            SMSManager.getInstance().loadTemplate();
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws EduException {
        // TODO Auto-generated method stub
        init();
        String result = "";

		SMSInfo smsInfo = new SMSInfo();
//        smsInfo.MsgType = 1;//1-投递 2-逾期 3-催领 4; //取件 5;   //重发投递  7; //注册验证码
//        smsInfo.CustomerMobile = "15957135105";//56974767643";//"4165674636";
//        //smsInfo.MsgContent     = "您的快递"+RandUtils.generateNumber(6)+"已送达杭海路1221号东城电子园区研发楼3楼自提柜,请凭提货密码"+RandUtils.generateNumber(6)+"取件。投递员电话:123456";
//        smsInfo.MsgContent     = "Su pedido numero 123456 se dejó en Lockers número dcdz, su clave es 123456 y fue enviada a su celular número 15957135105.";
//        smsInfo.OpenBoxKey     = RandUtils.generateNumber(4);
//        smsInfo.TerminalName   = "东城电子";
//        smsInfo.PostmanID      = "123456";
//        smsInfo.PostmanMobile  = "123456";
//        smsInfo.BoxNo          = "1";
//        smsInfo.msgTel         = "86468639";
//        smsInfo.DynamicCode    = "7180";//"7085";
//        smsInfo.Location       = "elocker";
//        smsInfo.PackageID      = "1701"+RandUtils.generateNumber(8);
//        MsgProxyDcdzsoft m = new MsgProxyDcdzsoft();
//        result = m.sendMessage(smsInfo);
        //烽火 
        //apCfg.setSendShortMsg("MsgProxyFenghuo213");
        //apCfg.setGatewayUser("mobilis");
        //apCfg.setGatewayPwd("mobilis");
        //ISMSProxy smsSender = new MsgProxyFenghuo213();
        //SMSManager.getInstance().sentDeliverySMS(smsInfo);
        //阿里大于
        //apCfg.setGatewayUser("23592026");
        //apCfg.setGatewayPwd("cadd7f03b4468fefdf21a1fcf4b8f464");
        //ISMSProxy smsSender = new MsgProxyTaiHeAli();
        //亿美软通
        //apCfg.setGatewayUser("0SDK-EBB-6699-RDRNQ");
        //apCfg.setGatewayPwd("761570");
        //ISMSProxy smsSender = new MsgProxyYiMeiRuanTong();
        
        //上海希奥
        //apCfg.setGatewayUser("506991");
        //apCfg.setGatewayPwd("sioo01");
        //ISMSProxy smsSender = new MsgProxySioo();
        
        //君诚科技
        //apCfg.setGatewayUser("dcdz");
        //apCfg.setGatewayPwd("gspgwxzv");
        //ISMSProxy smsSender = new MsgProxyJunChengKJ();
        
        //助通科技
        //apCfg.setGatewayUser("dongcheng888");
        //apCfg.setGatewayPwd("53rVQ9By");
        //ISMSProxy smsSender = new MsgProxyZhuTongKj();
        
        //互亿科技
        //apCfg.setGatewayUser("cf_joyfreedom");
        //apCfg.setGatewayPwd("dcdz_918");
        //ISMSProxy smsSender = new MsgProxyIhuyi();
        
        //
        //smsInfo.BoxNo = "1";
        //smsInfo.Location = "杭海路1221号东城电子园区研发楼3楼自提柜";
        //smsInfo.MsgType  = SMSInfo.MSG_TYPE_DELIVERY;
        //smsInfo.PostmanMobile = "123456";
        //apCfg.setGatewayUser("green");
        //apCfg.setGatewayPwd("green");
        //ISMSProxy smsSender = new MsgProxyTaiHe();
        //result = smsSender.sendMessage(smsInfo);
		
        //土耳其的需求
//      smsInfo.numbers = "905XX";                   			 //手机号
//      smsInfo.message = "您的快递已送达";						 //消息文本
//      smsInfo.origin = "杭海路1221号东城电子园区研发楼3楼自提柜";//地址
//      smsInfo.sd = "201707251801";                        	 //开始时间
//      smsInfo.ed = "201707251902";                        	 //发货时间至少差开始时间1小时
		apCfg.setGatewayUser("demo.neo");
        apCfg.setGatewayPwd("M4A7K1L2");
		smsInfo.CustomerMobile = "905384422300";  // 15957135105"1000333328987;               //手机号
		smsInfo.MsgContent = "Postaniz NEO 7-24 POSTa teslim edildi.Paketinizi teslim almak icin cep no son 4 hanesini ve Sifre:123456 girin.İhtiyac halinde Barkod no:201712271949.";//[TEST]Your cpackage has been delivered, please check";	//消息文本
		smsInfo.Location = "China's Hangzhou City Xiasha Hanghai Road, No. 1221 ";//地址 
		smsInfo.MsgType = SMSInfo.MSG_TYPE_DELIVERYTOPOSTMAN;
		smsInfo.PostmanMobile = "1235412";
	    MsgProxyZhiLi sender = new MsgProxyZhiLi();
	    result = sender.sendMessage(smsInfo);
        
        System.out.println("result="+result);
    }

}
