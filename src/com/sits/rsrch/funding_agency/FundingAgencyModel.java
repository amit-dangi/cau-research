/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.funding_agency;

import com.sits.general.Common;

public class FundingAgencyModel extends Common {
	private String faName;		
	private String faType;	
	private String faMobNo;
	private String faUrl;
	private String faAddr;
	private String faDetail;
	private String faId;
	private String fundedby;
	
	public String getFaName() {
		return faName;
	}
	public void setFaName(String faName) {
		this.faName = faName;
	}
	public String getFaType() {
		return faType;
	}
	public void setFaType(String faType) {
		this.faType = faType;
	}
	public String getFaMobNo() {
		return faMobNo;
	}
	public void setFaMobNo(String faMobNo) {
		this.faMobNo = faMobNo;
	}
	public String getFaUrl() {
		return faUrl;
	}
	public void setFaUrl(String faUrl) {
		this.faUrl = faUrl;
	}
	public String getFaAddr() {
		return faAddr;
	}
	public void setFaAddr(String faAddr) {
		this.faAddr = faAddr;
	}
	public String getFaDetail() {
		return faDetail;
	}
	public void setFaDetail(String faDetail) {
		this.faDetail = faDetail;
	}
	public String getFaId() {
		return faId;
	}
	public void setFaId(String faId) {
		this.faId = faId;
	}
	public String getFundedby() {
		return fundedby;
	}
	public void setFundedby(String fundedby) {
		this.fundedby = fundedby;
	}	
}