/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.rsrch.funding_agency_approval;

import com.sits.general.Common;

public class FundingAgencyApprovalModel extends Common {

	private String location_code;
	private String ddo_id;
	
	private String PS_MID;
	private String fa_id;
	
	private String approved_amount;
	private String alloted_amount;
	
	private String alloted_date;
	private String remaining_amount;
	
	private String is_upload_file;
	private String remarks;
	
	private String CREATED_BY;
	private String CREATED_MACHINE;
	private String CREATED_DATE;
	
	private String LocationName;
	private String DdoName;
	private String faaId;
	private String funding;
	private String uploadedFile;
	private String all_date;
	
	//New fields added oct 2023
	private String sanction_orderno;
	private String sanction_orderdate;
	private String isfundallocated;
	private String amount_diff;
	private String fin_yr;
	
	//New fields added March 2024
	private String is_opening_blnc;
	private String opening_blnc;
	
	//New fields added 14May 2024
	private String received_date;
	
	private String fagencyName;
	public String getFagencyName() {
		return fagencyName;
	}
	public void setFagencyName(String fagencyName) {
		this.fagencyName = fagencyName;
	}
	public String getProjTitle() {
		return projTitle;
	}
	public void setProjTitle(String projTitle) {
		this.projTitle = projTitle;
	}
	private String projTitle;
	
	public String getLocation_code() {
		return location_code;
	}
	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}
	public String getDdo_id() {
		return ddo_id;
	}
	public void setDdo_id(String ddo_id) {
		this.ddo_id = ddo_id;
	}
	public String getPS_MID() {
		return PS_MID;
	}
	public void setPS_MID(String pS_MID) {
		PS_MID = pS_MID;
	}
	public String getFa_id() {
		return fa_id;
	}
	public void setFa_id(String fa_id) {
		this.fa_id = fa_id;
	}
	public String getApproved_amount() {
		return approved_amount;
	}
	public void setApproved_amount(String approved_amount) {
		this.approved_amount = approved_amount;
	}
	public String getAlloted_amount() {
		return alloted_amount;
	}
	public void setAlloted_amount(String alloted_amount) {
		this.alloted_amount = alloted_amount;
	}
	public String getAlloted_date() {
		return alloted_date;
	}
	public void setAlloted_date(String alloted_date) {
		this.alloted_date = alloted_date;
	}
	public String getRemaining_amount() {
		return remaining_amount;
	}
	public void setRemaining_amount(String remaining_amount) {
		this.remaining_amount = remaining_amount;
	}
	public String getIs_upload_file() {
		return is_upload_file;
	}
	public void setIs_upload_file(String is_upload_file) {
		this.is_upload_file = is_upload_file;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	public String getCREATED_MACHINE() {
		return CREATED_MACHINE;
	}
	public void setCREATED_MACHINE(String cREATED_MACHINE) {
		CREATED_MACHINE = cREATED_MACHINE;
	}
	public String getCREATED_DATE() {
		return CREATED_DATE;
	}
	public void setCREATED_DATE(String cREATED_DATE) {
		CREATED_DATE = cREATED_DATE;
	}
	public String getLocationName() {
		return LocationName;
	}
	public void setLocationName(String locationName) {
		LocationName = locationName;
	}
	public String getDdoName() {
		return DdoName;
	}
	public void setDdoName(String ddoName) {
		DdoName = ddoName;
	}
	public String getFaaId() {
		return faaId;
	}
	public void setFaaId(String faaId) {
		this.faaId = faaId;
	}
	public String getFunding() {
		return funding;
	}
	public void setFunding(String funding) {
		this.funding = funding;
	}
	public String getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(String uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	public String getSanction_orderno() {
		return sanction_orderno;
	}
	public void setSanction_orderno(String sanction_orderno) {
		this.sanction_orderno = sanction_orderno;
	}
	public String getSanction_orderdate() {
		return sanction_orderdate;
	}
	public void setSanction_orderdate(String sanction_orderdate) {
		this.sanction_orderdate = sanction_orderdate;
	}
	public String getAll_date() {
		return all_date;
	}
	public void setAll_date(String all_date) {
		this.all_date = all_date;
	}
	public String getIsfundallocated() {
		return isfundallocated;
	}
	public void setIsfundallocated(String isfundallocated) {
		this.isfundallocated = isfundallocated;
	}
	public String getAmount_diff() {
		return amount_diff;
	}
	public void setAmount_diff(String amount_diff) {
		this.amount_diff = amount_diff;
	}
	public String getFin_yr() {
		return fin_yr;
	}
	public void setFin_yr(String fin_yr) {
		this.fin_yr = fin_yr;
	}
	public String getIs_opening_blnc() {
		return is_opening_blnc;
	}
	public void setIs_opening_blnc(String is_opening_blnc) {
		this.is_opening_blnc = is_opening_blnc;
	}
	public String getOpening_blnc() {
		return opening_blnc;
	}
	public void setOpening_blnc(String opening_blnc) {
		this.opening_blnc = opening_blnc;
	}
	public String getReceived_date() {
		return received_date;
	}
	public void setReceived_date(String received_date) {
		this.received_date = received_date;
	}
	
}