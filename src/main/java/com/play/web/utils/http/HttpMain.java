package com.play.web.utils.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.DateUtil;
import com.play.common.utils.security.RSAUtil;
import com.play.common.utils.security.Sha256Util;

public class HttpMain {
	private static String thirdPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDF6Qs7XvlZh0HvDq0X9E0GKmNU2rC8hWsdffYkZAeAJMdNxwTDq4iYE9ZDj3nYwNJkWwk6leHzmK/HLqx6Gg1d0YMaSsV9uoSqXSEiQvrruKuf9htB9hVAtL8x2lJaD4L93Nsx4QNO62GYQB32MoY1vA5yOxoOX29MDe6sFl1A5wIDAQAB";
	private static String thirdSignKey = "fqgqew12543sads/';PO)_=FL:FJOQP8qwy2f7g989r0-09]-pO:I}_{IPUYitq8o9p8w0eoilk;/NKJBUY&9809";
	private static String playPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOY4gQ1YUYr7NM2GflMetiua8zHMYudYzZijAwS7AbJxMQB5Vkte/7J9qUFsAd8t2zSFgYIMG2AHHQCeRdyMjsP/+Jk5/G2IeNR1JYTaYjEGq0IbynFlARp3hOTAD8ypa003x1TgK4B18egcYvhjPLiBbyNoAdiSRNMxYkkwmCNQIDAQAB";
	private static String playSignKey = "809qep2378y=2312123:fw39fpew8hi)_op1wq;L780NO'Q}pqowbirv5p4=8jfewff1;qfjep9240-]-O:j1879t}-01]I";
	
	private static String gamePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6sgIvpqTNkrIDEAX/vRStzb0tgZYc/QUw11TsMLP0bHjxHoUc7LwqZGJOAynlEdIMnf2ol6SquOyVl4gBr3k1eq6PTmbggw5g9JeW5Q7lOqqROdJyy6iXBriLA/URjhKPaQa0FOkRXscIedsw8TwGwwgK6RbOn/pEbXyuPg2IjwIDAQAB";
	private static String gameSignKey = "123:_))(U80y7*1fw9fpew8hi)_op1240-]-O78y=2312wq;L78f1;qfjep9t}-0]INO'Q}pqowbirv05p4=8jfewf";
	
	private static String sbPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4H8d/bsidd4vN6i60HQh1c7hgJu3Op4OTlTCUchaGJJs+Eu0BnSAvtTq1gDEGNHxkzqLDC+Dp6eKWURNvL3Fkc8RxD9rGW0r7Vk9xsSbmWb7C62QfI+5jKJ+2ouApqAwqUjtHEL4s9zVEUtVJYR8nbb2rHHPv+HozVGQDn0CZkwIDAQAB";
	private static String sbSignKey = "fkewqpfh&^)_)@#324219808q0w9hpuifp9[0qibfDP*({)Y9cvdnvkpih9*G@Q218ubflbi}_-2130fj9fhapgy12iuG*(*JFy";

	
	public static void main(String[] args) {
//		 doAddServer();
//		 doUpdateService();
//		 sendStationInfo();
//		doGetStationDailyReport();
//		 getStationInfo();
//		getPaymentsSportReport();
//		getGameStationInfo();
//		getGameLotReport();
		getGamePaymentsSportReport();
//		getShaBaStationDailyReport();
	}

	private static void getShaBaStationDailyReport() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("token", "8a4456219c65ad8d416912663576c28f");
					obj.put("date","2019-07-21");
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(obj.toJSONString(), sbPublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + sbSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://tech.newsport8.com/dataApi/getStationDailyReport.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void getGamePaymentsSportReport() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("token", "e30ab118-cf7b-4174-a354-7b739be4d082");
					obj.put("date","2019-06-30");
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(obj.toJSONString(), gamePublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + gameSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://127.0.0.1:8081/game/dataApi/getPaymentsAndSportReport.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void getGameLotReport() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("token", "e30ab118-cf7b-4174-a354-7b739be4d082");
					obj.put("date","2019-06-30");
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(obj.toJSONString(), gamePublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + gameSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://127.0.0.1:8081/game/dataApi/getLotReport.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void getGameStationInfo() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("token", "3b8801ac-1d65-4576-b237-be93a52fc82a");
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(obj.toJSONString(), gamePublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + gameSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://103.195.50.56/dataApi/getStationInfo.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void getPaymentsSportReport() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("token", "plt96b2ca99beb44612a0d1fdf56ba20316oqs0");
					obj.put("date","2019-07-18");
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(obj.toJSONString(), playPublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + playSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://127.0.0.1:8090/play/dataApi/getPaymentsAndSportReport.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void getStationInfo() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("token", "wx7022525fc1475408aad8670081f02db8dz3v3");
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(obj.toJSONString(), playPublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + playSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://66.203.145.52/dataApi/getStationInfo.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void doGetStationDailyReport() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("token", "lkhfl-d32onoq1-m3209d1u092-d3jijasodf");
					obj.put("date", DateUtil.toDate("2019-07-19"));
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(obj.toJSONString(), thirdPublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + thirdSignKey)));
					return pl;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.doRequest("http://realcenter.yb876.com/dataApi/getStationDailyReport.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void sendStationInfo() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					String data = "{\"token\":\"fewfewqfewqf21342\",\"stationIds\":[1,2,3]}";
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(data, thirdPublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + thirdSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://localhost:8085/realcenter/dataApi/stationInfo.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void doUpdateService() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					String data = "{\"serverName\":\"集群11\",\"status\":1,\"token\":\"fewfewqfewqf21342\",\"type\":1,\"url\":\"127.0.0.1:8090/play\",\"url2\":\"127.0.0.2:8090/play\",\"ips\":\"1231232132\"}";
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(data, thirdPublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + thirdSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://localhost:8085/realcenter/dataApi/updateService.do", HttpType.POST);
		System.out.println(ss);
	}

	private static void doAddServer() {
		String ss = new HttpClientProxy() {
			@Override
            public List<NameValuePair> getParameters() {
				try {
					String data = "{\"serverName\":\"集群1\",\"status\":2,\"token\":\"fewfewqfewqf21342\",\"type\":1,\"url\":\"127.0.0.1:8081/game\",\"url2\":\"127.0.0.1:8081/game\"}";
					List<NameValuePair> pl = new ArrayList<>();
					String data2 = RSAUtil.encrypt(data, thirdPublicKey);
					pl.add(new BasicNameValuePair("data", data2));
					pl.add(new BasicNameValuePair("sign", Sha256Util.digest(data2 + thirdSignKey)));
					return pl;
				} catch (Exception e) {

				}
				return null;
			}
		}.doRequest("http://localhost:8085/realcenter/dataApi/addService.do", HttpType.POST);
		System.out.println(ss);
	}
}
