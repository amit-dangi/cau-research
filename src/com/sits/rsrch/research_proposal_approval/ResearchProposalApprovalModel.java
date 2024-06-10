package com.sits.rsrch.research_proposal_approval;

public class ResearchProposalApprovalModel {

	private String ppId;
	private String title_pp;
	private String ps_date;
	private String app_rej_date;
	private String reason;
	private String id;
	private String psId;
	private String status;
	private String appRejDt;
	private String IsAppReq;
	
	public String getPpId() {
		return ppId;
	}
	public void setPpId(String ppId) {
		this.ppId = ppId;
	}
	public String getTitle_pp() {
		return title_pp;
	}
	public void setTitle_pp(String title_pp) {
		this.title_pp = title_pp;
	}
	public String getPs_date() {
		return ps_date;
	}
	public void setPs_date(String ps_date) {
		this.ps_date = ps_date;
	}
	public String getApp_rej_date() {
		return app_rej_date;
	}
	public void setApp_rej_date(String app_rej_date) {
		this.app_rej_date = app_rej_date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPsId() {
		return psId;
	}
	public void setPsId(String psId) {
		this.psId = psId;
	}
	public String getAppRejDt() {
		return appRejDt;
	}
	public void setAppRejDt(String appRejDt) {
		this.appRejDt = appRejDt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsAppReq() {
		return IsAppReq;
	}
	public void setIsAppReq(String isAppReq) {
		IsAppReq = isAppReq;
	}
}