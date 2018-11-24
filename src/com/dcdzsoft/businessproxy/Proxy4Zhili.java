package com.dcdzsoft.businessproxy;

import com.dcdzsoft.EduException;
import com.dcdzsoft.dto.business.*;
import com.dcdzsoft.aop.BusiBeanAOPBaseClass;
import com.dcdzsoft.businessproxy.BusinessProxy;
import com.dcdzsoft.client.locker.businessproxy.ProxyDcdzsoft;
import com.dcdzsoft.constant.SysDict;

/**
 * 
 * <p>
 * Title: 智能自助包裹柜系统
 * </p>
 * 
 * <p>
 * Description: 暴露给设备端所有业务
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * 
 * <p>
 * Company: 杭州东城电子有限公司
 * </p>
 * 
 * @author zxy
 * @version 1.0
 */
public class Proxy4Zhili extends ProxyDcdzsoft {
    
    /**
     * 投递包裹
     * 
     * @param p1
     * @param TerminalNo
     * @return
     * @throws com.dcdzsoft.EduException
     */
    public OutParamPTDeliveryPackage doBusiness(InParamPTDeliveryPackage p1, String TerminalNo)
            throws com.dcdzsoft.EduException {
        BusiBeanAOPBaseClass aop = BusiBeanAOPBaseClass.getInstance();
        com.dcdzsoft.partner.zhili.business.pt.PTDeliveryPackage bean = (com.dcdzsoft.partner.zhili.business.pt.PTDeliveryPackage) aop
                .bind(com.dcdzsoft.partner.zhili.business.pt.PTDeliveryPackage.class);
        return bean.doBusiness(p1);
    }

    /**
     * 取回逾期包裹
     * 
     * @param p1
     * @param TerminalNo
     * @return
     * @throws com.dcdzsoft.EduException
     */
    public OutParamPTWithdrawExpiredPack doBusiness(InParamPTWithdrawExpiredPack p1, String TerminalNo)
            throws com.dcdzsoft.EduException {
        BusiBeanAOPBaseClass aop = BusiBeanAOPBaseClass.getInstance();
        com.dcdzsoft.partner.zhili.business.pt.PTWithdrawExpiredPack bean = (com.dcdzsoft.partner.zhili.business.pt.PTWithdrawExpiredPack) aop
                .bind(com.dcdzsoft.partner.zhili.business.pt.PTWithdrawExpiredPack.class);
        return bean.doBusiness(p1);
    }

    /**
     * 用户取件
     * 
     * @param p1
     * @param TerminalNo
     * @return
     * @throws com.dcdzsoft.EduException
     */
    public OutParamPTPickupPackage doBusiness(InParamPTPickupPackage p1, String TerminalNo)
            throws com.dcdzsoft.EduException {
        BusiBeanAOPBaseClass aop = BusiBeanAOPBaseClass.getInstance();
        com.dcdzsoft.partner.zhili.business.pt.PTPickupPackage bean = (com.dcdzsoft.partner.zhili.business.pt.PTPickupPackage) aop
                .bind(com.dcdzsoft.partner.zhili.business.pt.PTPickupPackage.class);
        return bean.doBusiness(p1);
    }

    /**
     * 管理员取件
     * 
     * @param p1
     * @param TerminalNo
     * @return
     * @throws com.dcdzsoft.EduException
     */
    public OutParamPTManagerPickupPack doBusiness(InParamPTManagerPickupPack p1, String TerminalNo)
            throws com.dcdzsoft.EduException {
        BusiBeanAOPBaseClass aop = BusiBeanAOPBaseClass.getInstance();
        com.dcdzsoft.partner.zhili.business.pt.PTManagerPickupPack bean = (com.dcdzsoft.partner.zhili.business.pt.PTManagerPickupPack) aop
                .bind(com.dcdzsoft.partner.zhili.business.pt.PTManagerPickupPack.class);
        return bean.doBusiness(p1);
    }

}
