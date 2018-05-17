package com.tsingma.business.member.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.Record;
import com.tsingma.common.dao.BaseDao;

@Component("recordDao")
public class RecordDao extends BaseDao<Record, Serializable> {

	public List<Record> loadListByOpenid(String openid) throws Exception {
		return super.listBySql2("SELECT * FROM ((SELECT scratch.`name` AS `name`, CONCAT('+',FORMAT(scratch.price,2)) AS price, "+
				"substring(scratch.scratchTime,1,16) AS time FROM `b_memscratch` AS `scratch` WHERE `scratch`.`openid` = ?0 AND `scratch`.`price` > 0) "+
				"UNION (SELECT payment.`productName` AS `name`, CONCAT('-',FORMAT(payment.price/100,2)) AS price,substring(payment.createdTime,1,16) AS time "+
				"FROM `b_payment` AS `payment` WHERE `payment`.`openid` = ?1 and `payment`.`resultCode`='SUCCESS')) AS record ORDER BY time DESC", openid, openid);
	}
	
}
