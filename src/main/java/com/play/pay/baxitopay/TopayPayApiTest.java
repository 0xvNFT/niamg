package com.play.pay.baxitopay;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.play.pay.baxitopay.params.TopayParamCollect;
import com.play.pay.baxitopay.params.TopayParamPay;
import com.play.pay.baxitopay.params.TopayParamQueryBalance;
import com.play.pay.baxitopay.params.TopayParamQueryOrder;
import com.play.pay.baxitopay.result.TopayResultBalance;
import com.play.pay.baxitopay.result.TopayResultOrder;

public class TopayPayApiTest {
	private final static Logger log = LoggerFactory.getLogger(TopayPayApiTest.class);
	private static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANKRBYg9PSbuWtx1/WbCN+fZmBPsoElDE5ppclAOT4bC/a3jTyk0NkwSUi43wggO2bfQxbZkKqwDZzbHf7K03dHu+R7cSHaJidzGP6BI3u3iVxtBGcpVbfHnGdKUxF4GA19ZMdg49AuLlY1EG5+kfqaz22rX6GcZXJF+X7z/GiIlAgMBAAECgYAK6G5T73Ayixhz4TzeTONehFwndBOdl/b1Ac9lLEicX4+KqYLZTJYffLd7I3JOvo4ywoLrfubjAE4hfVY1KL9w342dKZgo1jizBSs5Ee1Mb/y+CFQy+GxvZxS8amzeYHRAcyVHKjLcDeIbmUa/a/r32cARuSkk17MK6v+qcTWEwQJBAPQIDegsMyUH877Ig00QA5qIJPGmFSqCJw5WY7FCN+Gq2WweLj1egyabLDmkOIQ6N/PowxYLbG9/1QpfX7+599ECQQDc5MvxOIHzNW5uOVaoO1Hw2ljIr46Danxi+GfJ2WhSv3VOmJRhh6Oiklv1tDO4Z7wi9e/6WgAawJBDPurKzW4VAkAwtT7ZXaEjvg+Xv/v9MLoXaR8+oHDPGEVlZjbl1ZrDz08wdmGyXB//X6+XHcEuG1S5uXbe+o69Pp2uQVUiNqNRAkAWMClILqvm+SwBiGLq/SJq9hPK/M9s5epeqqMKRHtVLIQlA/CLp2tJbjgsgxBa6picTmXvmNC/3y3eg1hkTKRNAkEAyv/mPpWltLFd1pPMqGiQYTujOrjyvfDSj/VKefGKgMBygtplx8xIAWgOtMvjTOm6XcA3/RfVL1PBnmwNKFFikQ==";
	private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDhGd+slsmDBzq3StEk6zsWlnTsMtY3JaShT/HzCdoQz/Myl2dJb7OYbf9Gnc9vfGLQxUAtIR50yA5u401bP0jn1c6tjXuSa0Fl8KtpfbHwAQ05dga22WNPD6X0YFgP5xHQFD7DN2DnyorKuE6M7r7/d83Q9C8x8y5C/zO636ZDUQIDAQAB";
	private static final String queryUrl = "http://api-staging.br.toppay.cloud";

	public static void collectionTest() {
		TopayParamCollect param = new TopayParamCollect();
		param.setMerchant_no("M22071513511250");
		param.setOut_trade_no("CO1234564");
		param.setDescription("test des");
		param.setTitle("test title");
		param.setPay_amount("100.02");
		param.setNotify_url("http://www.baidu.com");
		TopayResultOrder result = new TopayPayApi().collection(publicKey, privateKey, queryUrl, param);
	//	log.error(JSON.toJSONString(result));
	}

	public static void payTest() {
		TopayParamPay param = new TopayParamPay();
		param.setMerchant_no("M23082209342195");
		param.setOut_trade_no("PY123a4566");
		param.setDescription("test pay");
		/** 用户名，必须英文、葡萄牙文 */
		param.setName("中文会错误");
		/** 传用户身份证，长度固定11位数字 */
//		param.setCpf("12345678901");
		param.setPix_type("CPF");
		param.setDict_key("12345678901");
		// 只有300块省着点花，最少10块钱一次。。
		param.setPay_amount("10.02");
		param.setNotify_url("http://www.baidu.com");
		TopayResultOrder result = new TopayPayApi().agentpay(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryOrderStatusTest() {
		TopayParamQueryOrder param = new TopayParamQueryOrder();
		param.setMerchant_no("M23082209342195");
		param.setOut_trade_no("PY1234565");
		TopayResultOrder result = new TopayPayApi().queryOrderStatus(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryBalance() {
		TopayParamQueryBalance param = new TopayParamQueryBalance();
		param.setMerchant_no("M23082209342195");
		param.setTimestamp("1633183946");
		TopayResultBalance result = new TopayPayApi().queryBalance(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) {
//		DamsonPayApiTest.queryBalance();
//		TopayPayApiTest.collectionTest();
//		TopayPayApiTest.payTest();
		TopayPayApiTest.collectionTest();
//		TopayPayApiTest.queryBalance();
//		BigDecimal mBigDecimal = new BigDecimal("12.");
//		BigDecimal result2 = mBigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
//		System.out.println(result2.toPlainString());
	}
}
