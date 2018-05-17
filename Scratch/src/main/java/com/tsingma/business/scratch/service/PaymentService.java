package com.tsingma.business.scratch.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.tsingma.business.coupon.dao.ActCouponDao;
import com.tsingma.business.coupon.model.ActCoupon;
import com.tsingma.business.member.dao.MemCouponDao;
import com.tsingma.business.member.dao.MemScratchDao;
import com.tsingma.business.member.model.MemCoupon;
import com.tsingma.business.member.model.MemScratch;
import com.tsingma.business.scratch.dao.ActivityDao;
import com.tsingma.business.scratch.dao.PaymentDao;
import com.tsingma.business.scratch.dao.ScratchDao;
import com.tsingma.business.scratch.dao.ScratchTextDao;
import com.tsingma.business.scratch.model.Activity;
import com.tsingma.business.scratch.model.Payment;
import com.tsingma.business.scratch.model.Scratch;
import com.tsingma.business.scratch.model.ScratchText;
import com.tsingma.common.exception.TsingmaException;
import com.tsingma.common.util.MyRandom;
import com.tsingma.common.util.UUIDUtil;
import com.tsingma.core.util.Utils;

@Service("paymentService")
@Transactional
public class PaymentService {

	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private ActCouponDao actCouponDao;
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private MemCouponDao memCouponDao;
	@Autowired
	private MemScratchDao memScratchDao;
	@Autowired
	private ScratchDao scratchDao;
	@Autowired
	private ScratchTextDao scratchTextDao;
	
	/**
	 * 根据订单号查询唯一付款信息
	 * @param outTradeNo
	 * @return
	 * @throws Exception
	 */
	public Payment getByOurTradeNo(String outTradeNo) throws Exception {
		return paymentDao.loadByOutTradeNo(outTradeNo);
	}
	
	/**
	 * 根据请求参数创建付款信息
	 * @param request
	 * @throws Exception
	 */
	public void insertByOrderRequest(WxPayUnifiedOrderRequest request, String appid) throws Exception {
		if(!Utils.isEmpty(request)){
			Payment payment = new Payment();
			payment.setOutTradeNo(request.getOutTradeNo());
			payment.setAttach(Integer.parseInt(request.getAttach()));
			payment.setProductName(request.getBody());
			payment.setClientIp(request.getSpbillCreateIp());
			payment.setPrice(request.getTotalFee());
			payment.setOpenid(request.getOpenid());
			payment.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			this.insert(payment);
			//增加活动参与人数
			activityDao.addNumber(appid, 1);
		}
	}
	
	/**
	 * 根据返回结果更新付款信息
	 * @param result
	 * @throws Exception
	 */
	public void updateByWxPayOrderNotifyResult(WxPayOrderNotifyResult result) throws Exception {
		if(!Utils.isEmpty(result) && "SUCCESS".equals(result.getReturnCode())){
			Payment payment = paymentDao.loadByOutTradeNo(result.getOutTradeNo());
			if(!Utils.isEmpty(payment)){
				payment.setAppid(result.getAppid());
				payment.setAttach(Integer.parseInt(result.getAttach()));
				payment.setBankType(result.getBankType());
				payment.setErrorCode(result.getErrCode());
				payment.setErrorMsg(result.getErrCodeDes());
				payment.setFeeType(result.getFeeType());
				payment.setIsSubscribe(result.getIsSubscribe());
				payment.setMchId(result.getMchId());
				payment.setNotifyTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				payment.setResultCode(result.getResultCode());
				payment.setTimeEnd(result.getTimeEnd());
				payment.setTotalFee(result.getTotalFee());
				payment.setTradeType(result.getTradeType());
				payment.setTransactionId(result.getTransactionId());
				this.update(payment);
				this.grantActCoupon(payment);
				this.grantActScratch(payment);
			} else {
				payment = new Payment();
				payment.setOutTradeNo(result.getOutTradeNo());
				payment.setPrice(result.getTotalFee());
				payment.setOpenid(result.getOpenid());
				//如CreatedTime 和 NotifyTime 相同则为创建的支付记录
				payment.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				
				payment.setAppid(result.getAppid());
				payment.setAttach(Integer.parseInt(result.getAttach()));
				payment.setBankType(result.getBankType());
				payment.setErrorCode(result.getErrCode());
				payment.setErrorMsg(result.getErrCodeDes());
				payment.setFeeType(result.getFeeType());
				payment.setIsSubscribe(result.getIsSubscribe());
				payment.setMchId(result.getMchId());
				payment.setNotifyTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				payment.setResultCode(result.getResultCode());
				payment.setTimeEnd(result.getTimeEnd());
				payment.setTotalFee(result.getTotalFee());
				payment.setTradeType(result.getTradeType());
				payment.setTransactionId(result.getTransactionId());
				this.insert(payment);
			}
		}
	}
	
	/**
	 * 发放优惠券
	 * @param payment
	 * @throws Exception
	 */
	public void grantActCoupon(Payment payment) throws Exception {
		if(!Utils.isEmpty(payment)){
			Activity act = activityDao.loadEnableActivity(payment.getAppid()); 
			if(!Utils.isEmpty(act)){
				List<ActCoupon> actList = actCouponDao.loadListByActId(act.getId());
				if(!Utils.isEmpty(actList)){
					int[] ids = new int[actList.size()];
					int[] prob = new int[actList.size()];
					Map<Integer, ActCoupon> actMap = new HashMap<Integer, ActCoupon>();
					for(int i = 0; i < actList.size(); i++){
						ActCoupon actCoupon = actList.get(i);
						ids[i] = actCoupon.getId();
						prob[i] = actCoupon.getProbability();
						actMap.put(actCoupon.getId(), actCoupon);
					}
					if(payment.getAttach() == 1){
						int actCouponId = MyRandom.probabilityRandom(ids, prob);
						MemCoupon memCoupon = new MemCoupon();
						memCoupon.setActcouponId(actCouponId);
						memCoupon.setCode(UUIDUtil.get12UUID());
						memCoupon.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
						memCoupon.setEndDate(actMap.get(actCouponId).getEndDate());
						memCoupon.setOpenid(payment.getOpenid());
						memCoupon.setStatus(0);
						memCouponDao.save(memCoupon);
					} else if(payment.getAttach() > 1) {
						int[] actIds = MyRandom.probabilityRandom(ids, prob, 10);
						MemCoupon memCoupon = null;
						String now = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
						for(int i = 0; i < actIds.length; i++){
							memCoupon = new MemCoupon();
							memCoupon.setActcouponId(actIds[i]);
							memCoupon.setCode(UUIDUtil.get12UUID());
							memCoupon.setCreatedTime(now);
							memCoupon.setEndDate(actMap.get(actIds[i]).getEndDate());
							memCoupon.setOpenid(payment.getOpenid());
							memCoupon.setStatus(0);
							memCouponDao.save(memCoupon);
						}
					} else
						throw new TsingmaException(payment.getOutTradeNo() + " 未知数量！");
				} else
					throw new TsingmaException(act.getId() + " 活动优惠券为空！");
			} else
				throw new TsingmaException(payment.getOutTradeNo() + " 活动不存在！");
		}
	}
	
	/**
	 * 发放刮奖
	 * @param payment
	 * @throws Exception
	 */
	public void grantActScratch(Payment payment) throws Exception {
		if(!Utils.isEmpty(payment)){
			Activity act = activityDao.loadEnableActivity(payment.getAppid()); 
			if(!Utils.isEmpty(act)){
				List<Scratch> scratchList = scratchDao.loadListByAppid(payment.getAppid());
				ScratchText text = scratchTextDao.loadByActId(act.getId());
				if(!Utils.isEmpty(scratchList)){
					int[] ids = new int[scratchList.size()];
					int[] prob = new int[scratchList.size()];
					Map<Integer, Scratch> srcMap = new HashMap<Integer, Scratch>();
					for(int i = 0; i < scratchList.size(); i++){
						Scratch scratch = scratchList.get(i);
						ids[i] = scratch.getId();
						prob[i] = scratch.getProbability();
						srcMap.put(scratch.getId(), scratch);
					}
					if(payment.getAttach() == 1){
						int scratchId = MyRandom.probabilityRandom(ids, prob);
						MemScratch memScratch = new MemScratch();
						memScratch.setActId(act.getId());
						memScratch.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
						if(scratchId != 0) {
							memScratch.setName(srcMap.get(scratchId).getName());
							memScratch.setPrice(srcMap.get(scratchId).getPrice());
						} else {
							memScratch.setName(Utils.isEmpty(text)?"谢谢参与":text.getName());
							memScratch.setPrice(0.00);
						}
						memScratch.setTradeNo("S" + new SimpleDateFormat("yyyyddMMssmmHHSSS").format(new Date()) + MyRandom.generateRandomNumber(4));
						memScratch.setIsscratch(0);
						memScratch.setOpenid(payment.getOpenid());
						memScratch.setStatus(0);
						memScratchDao.save(memScratch);
					} else if(payment.getAttach() > 1) {
						int[] srcIds = MyRandom.probabilityRandom(ids, prob, 11);
						MemScratch memScratch = null;
						for(int i = 0; i < srcIds.length; i++){
							memScratch = new MemScratch();
							memScratch.setActId(act.getId());
							memScratch.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
							if(srcIds[i] != 0) {
								memScratch.setName(srcMap.get(srcIds[i]).getName());
								memScratch.setPrice(srcMap.get(srcIds[i]).getPrice());
							} else {
								memScratch.setName(Utils.isEmpty(text)?"谢谢参与":text.getName());
								memScratch.setPrice(0.00);
							}
							memScratch.setTradeNo("S" + new SimpleDateFormat("yyyyddMMssmmHHSSS").format(new Date()) + MyRandom.generateRandomNumber(4));
							memScratch.setIsscratch(0);
							memScratch.setOpenid(payment.getOpenid());
							memScratch.setStatus(0);
							memScratchDao.save(memScratch);
						}
					} else
						throw new TsingmaException(payment.getOutTradeNo() + " 未知数量！");
				} else
					throw new TsingmaException(payment.getAppid() + " 刮奖设置为空！");
			} else
				throw new TsingmaException(payment.getOutTradeNo() + " 活动不存在！");
		}
	}
	
	public void insert(Payment payament) throws Exception {
		paymentDao.save(payament);
	}
	
	public void update(Payment payament) throws Exception {
		paymentDao.update(payament);
	}
	
	public void merge(Payment payament) throws Exception {
		paymentDao.merge(payament);
	}
	
	public void delete(Payment payament) throws Exception {
		paymentDao.deleteObject(payament);
	}
}
