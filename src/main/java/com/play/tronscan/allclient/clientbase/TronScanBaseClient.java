package com.play.tronscan.allclient.clientbase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.play.tronscan.allclient.alltool.HttpClientForTron;
import com.play.tronscan.allclient.clientbase.res.TronScanResAccount;
import com.play.tronscan.allclient.clientbase.res.TronScanResBlock;
import com.play.tronscan.allclient.clientbase.res.TronScanResBlockList;
import com.play.tronscan.allclient.clientbase.res.TronScanResSystemStauts;
import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20TransferList;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransactionList;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransferList;
import com.play.web.utils.http.Response;

/**
 * tronscan client
 */
public class TronScanBaseClient {
	/** log */
	static Logger log = LoggerFactory.getLogger(TronScanBaseClient.class);
	/** 主网域名 */
	// private static final String domain = "https://apilist.tronscan.org";
	private static final String domain = "https://apilist.tronscanapi.com";

	/**
	 * 接口编号：#1
	 * </p>
	 * Desc: List data synchronization state
	 */
	public static TronScanResSystemStauts sysStatus() {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/system/status");
		Response response = HttpClientForTron.newClient().curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResSystemStauts.class);
	}

	/**
	 * 接口编号：#2
	 * </p>
	 * Desc: Get the lastest block
	 */
	public static TronScanResBlock latestBlock() {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/block/latest");
		Response response = HttpClientForTron.newClient().curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResBlock.class);
	}

	/**
	 * 接口编号：#4
	 * </p>
	 * Desc: Get a single account's detail
	 */
	public static TronScanResAccount account(String address) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/account");
		Response response = HttpClientForTron.newClient().addParameter("address", address).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResAccount.class);
	}

	/**
	 * 接口编号：#5
	 * </p>
	 * Desc: List the blocks in the blockchain(only display the latest 10,000 data records in the query time range)
	 * </p>
	 * https://apilist.tronscan.org/api/block?sort=-number&limit=20&count=true&start=20&start_timestamp=1551772110000&end_timestamp=1551772172616
	 * </p>
	 * 该接口有严重问题，请不要使用 【start_timestamp, end_timestamp】两个过滤条件
	 * </p>
	 * 完全无法理解。。。。。
	 */
	public static TronScanResBlockList block(String sort, Integer limit, Integer start, String count, Long start_timestamp, Long end_timestamp) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/block");
		Response response = HttpClientForTron.newClient().addParameter("sort", sort).addParameter("limit", limit).addParameter("start", start).addParameter("count", count).addParameter("start_timestamp", start_timestamp).addParameter("end_timestamp", end_timestamp).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResBlockList.class);
	}

	/**
	 * 接口编号：#7
	 * </p>
	 * Desc: Get a single block's detail
	 */
	public static TronScanResBlockList block(Long number) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/block");
		Response response = HttpClientForTron.newClient().addParameter("number", number).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResBlockList.class);
	}

	/**
	 * 接口编号：#8 #9
	 * </p>
	 * Desc: List the transactions in the blockchain(only display the latest 10,000 data records in the query time range)
	 * </p>
	 * 这里 【start_timestamp】、【end_timestamp】都是【秒有效】【毫秒无效】
	 * </p>
	 * 比如对于【start_timestamp=1666285806101, end_timestamp=1666285806102】
	 * </p>
	 * 返回结果会包含【1666285806000】的数据
	 * </p>
	 * 对于【start_timestamp=1666285806104, end_timestamp=1666285806101】
	 * </p>
	 * 即使【start_timestamp > end_timestamp】仍然会返回【1666285806000】的数据
	 * </p>
	 * https://apilist.tronscan.org/api/transaction?sort=-timestamp&count=true&limit=20&start=0&address=TMuA6YqfCeX8EhbfYEg5y7S4DqzSJireY9 &start_timestamp=1666285806104&end_timestamp=1666285806101
	 * 
	 * </p>
	 * 建议使用【block】来查询（虽然接口上并未写出可以使用该参数，但是实际上是可以的）请看以下链接：
	 * </p>
	 * https://apilist.tronscan.org/api/transaction?sort=-timestamp&count=true&limit=20&start=0&address=TMuA6YqfCeX8EhbfYEg5y7S4DqzSJireY9&block=51984459
	 */
	public static TronScanResTransactionList transaction(String sort, Integer limit, Integer start, String count, Long start_timestamp, Long end_timestamp, String address, Long block) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/transaction");
		Response response = HttpClientForTron.newClient().addParameter("sort", sort).addParameter("limit", limit).addParameter("start", start).addParameter("count", count).addParameter("start_timestamp", start_timestamp).addParameter("end_timestamp", end_timestamp).addParameter("address", address).addParameter("block", block).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResTransactionList.class);
	}

	/**
	 * 接口编号：#10
	 * </p>
	 * Desc: List the transactions related to an smart contract(only display the latest 10,000 data records in the query time range)
	 * </p>
	 * 这里 【start_timestamp】、【end_timestamp】都是【秒有效】【毫秒无效】
	 * </p>
	 * 比如对于【start_timestamp=1560506454103, end_timestamp=1560506454001】
	 * </p>
	 * 返回结果会包含【1560506454000】的数据
	 * </p>
	 * 对于【start_timestamp=1560506454003, end_timestamp=1560506454001】
	 * </p>
	 * 即使【start_timestamp > end_timestamp】仍然会返回【1560506454000】的数据
	 * </p>
	 * https://apilist.tronscan.org/api/contracts/transaction?sort=-timestamp&count=true&limit=20&start=0&contract=TGfbkJww3x5cb9u4ekLtZ9hXvJo48nUSi4 &start_timestamp=1560506454003&end_timestamp=1560506454001&contract=TGfbkJww3x5cb9u4ekLtZ9hXvJo48nUSi4
	 */
	public static TronScanResTransactionList contractTransaction(String sort, Integer limit, Integer start, String count, Long start_timestamp, Long end_timestamp, String contract) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/contracts/transaction");
		Response response = HttpClientForTron.newClient().addParameter("sort", sort).addParameter("limit", limit).addParameter("start", start).addParameter("count", count).addParameter("start_timestamp", start_timestamp).addParameter("end_timestamp", end_timestamp).addParameter("contract", contract).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResTransactionList.class);
	}

	/**
	 * 接口编号：#11
	 * </p>
	 * Desc: List a transaction detail
	 */
	public static TronScanResTransaction transactionInfo(String hash) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/transaction-info");
		Response response = HttpClientForTron.newClient().addParameter("hash", hash).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResTransaction.class);
	}

	/**
	 * 接口编号：#12 #13
	 * </p>
	 * Desc: List the transfers in the blockchain(only display the latest 10,000 data records in the query time range)
	 * </p>
	 * 这里 【start_timestamp】、【end_timestamp】都是【秒有效】【毫秒无效】
	 * </p>
	 * 比如对于【start_timestamp=1548057645300, end_timestamp=1548057645501】
	 * </p>
	 * 返回结果会包含【1548057645000】的数据
	 * </p>
	 * 对于【start_timestamp=1548057645300, end_timestamp=1548057645001】
	 * </p>
	 * 即使【start_timestamp > end_timestamp】仍然会返回【1666285806000】的数据
	 * </p>
	 * https://apilist.tronscan.org/api/transfer?sort=-timestamp&count=true&limit=20&start=0 &start_timestamp=1548057645300&end_timestamp=1548057645001
	 */
	public static TronScanResTransferList transfer(String sort, Integer limit, Integer start, String count, Long start_timestamp, Long end_timestamp, String token, String address) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/transfer");
		Response response = HttpClientForTron.newClient().addParameter("sort", sort).addParameter("limit", limit).addParameter("start", start).addParameter("count", count).addParameter("start_timestamp", start_timestamp).addParameter("end_timestamp", end_timestamp).addParameter("token", token).addParameter("address", address).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResTransferList.class);
	}

	/**
	 * 接口编号：#42
	 * </p>
	 * Desc: List the transfers related to a specified TRC20 token(only display the latest 10,000 data records in the query time range)
	 * </p>
	 * https://apilist.tronscan.org/api/token_trc20/transfers?limit=20&start=0&contract_address=TCN77KWWyUyi2A4Cu7vrh5dnmRyvUuME1E&start_timestamp=1529856000000&end_timestamp=1552550375474
	 * </p>
	 * 请注意指定了【block】以后【contract_address】将不再起作用
	 * </p>
	 * 在指定了【contract_address】时，【start_timestamp】和【end_timestamp】只能指定一个，如果两个都指定返回结果将不再稳定（很难理解。。。。）
	 * </p>
	 * 建议使用以下接口：
	 * </p>
	 * https://apilist.tronscan.org/api/token_trc20/transfers?limit=50&start=0&contract_address=TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t&end_timestamp=1686485667000
	 */
	public static TronScanResTRC20TransferList trc20Transfers(Integer limit, Integer start, String contract_address, Long start_timestamp, Long end_timestamp, Long block) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/token_trc20/transfers");
		Response response = HttpClientForTron.newClient().addParameter("limit", limit).addParameter("start", start).addParameter("contract_address", contract_address).addParameter("start_timestamp", start_timestamp).addParameter("end_timestamp", end_timestamp).addParameter("block", block).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResTRC20TransferList.class);
	}

	/**
	 * 接口编号：#42
	 * </p>
	 * Desc: List the transfers related to a specified TRC20 token(only display the latest 10,000 data records in the query time range)
	 * </p>
	 * https://apilist.tronscan.org/api/token_trc20/transfers?limit=50&start=0&filterTokenValue=0&relatedAddress=TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t&start_timestamp=1686482667000&end_timestamp=1686485667000
	 */
	public static TronScanResTRC20TransferList trc20TransfersRelatedAddress(Integer limit, Integer start, String filterTokenValue, String relatedAddress, Long start_timestamp, Long end_timestamp, Long block) {
		StringBuilder builder = new StringBuilder().append(domain).append("/api/token_trc20/transfers");
		Response response = HttpClientForTron.newClient().addParameter("limit", limit).addParameter("start", start).addParameter("filterTokenValue", filterTokenValue).addParameter("relatedAddress", relatedAddress).addParameter("start_timestamp", start_timestamp).addParameter("end_timestamp", end_timestamp).addParameter("block", block).curl(builder.toString());
		return JSON.toJavaObject(HttpClientForTron.getJSONObject(response), TronScanResTRC20TransferList.class);
	}
}
