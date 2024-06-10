package com.sits.rsrch.reports.research_proposal_report;

public class ResearchProposalReportModel {

	private String tproj;
	private String empId;
	private String empName;
	private String fund_agn;
	private String fa_Name;
	private String submitted_date;
	private String type;
	private String status;
	
	public String getTproj() {
		return tproj;
	}
	public void setTproj(String tproj) {
		this.tproj = tproj;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getFund_agn() {
		return fund_agn;
	}
	public void setFund_agn(String fund_agn) {
		this.fund_agn = fund_agn;
	}
	public String getFa_Name() {
		return fa_Name;
	}
	public void setFa_Name(String fa_Name) {
		this.fa_Name = fa_Name;
	}
	public String getSubmitted_date() {
		return submitted_date;
	}
	public void setSubmitted_date(String submitted_date) {
		this.submitted_date = submitted_date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
}