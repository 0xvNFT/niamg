package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TronScanResSystemStauts implements Serializable {
	public DataBase database;
	public Sync sync;
	public NetWork network;
	public Full full;
	public Solidity solidity;

	public static class DataBase implements Serializable {
		public Long block;
		public Long confirmedBlock;
	}

	public static class Sync implements Serializable {
		public Double progress;
	}

	public static class NetWork implements Serializable {
		public String type;
	}

	public static class Full implements Serializable {
		public Long block;
	}

	public static class Solidity implements Serializable {
		public Long block;
	}

	public DataBase getDatabase() {
		return database;
	}

	public void setDatabase(DataBase database) {
		this.database = database;
	}

	public Sync getSync() {
		return sync;
	}

	public void setSync(Sync sync) {
		this.sync = sync;
	}

	public NetWork getNetwork() {
		return network;
	}

	public void setNetwork(NetWork network) {
		this.network = network;
	}

	public Full getFull() {
		return full;
	}

	public void setFull(Full full) {
		this.full = full;
	}

	public Solidity getSolidity() {
		return solidity;
	}

	public void setSolidity(Solidity solidity) {
		this.solidity = solidity;
	}

	@Override
	public String toString() {
		return "TronScanResSystemStauts [database=" + database + ", sync=" + sync + ", network=" + network + ", full=" + full + ", solidity=" + solidity + "]";
	}
}
