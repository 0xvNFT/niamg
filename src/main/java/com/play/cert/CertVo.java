package com.play.cert;

public class CertVo {
	private Integer id;
	private String name;
	private String fileName;
	private String keyFile;
	private String csrFile;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getKeyFile() {
		return keyFile;
	}

	public void setKeyFile(String keyFile) {
		this.keyFile = keyFile;
	}

	public String getCsrFile() {
		return csrFile;
	}

	public void setCsrFile(String csrFile) {
		this.csrFile = csrFile;
	}

}
