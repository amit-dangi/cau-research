package com.sits.rsrch.research_activity.objective_achievement_details;

import com.sits.general.Common;

public class ObjectiveAchievementModel extends Common {
	private String oa_id;
	private String rsrch_proposal;
	private String from_date;
	private String to_date;	
	private String achievement_det;
	private String is_doc_uploaded;
	private String rsrch_proposal_id;
	private String attach_id;
	private String upl_doc;
	
	public String getUpl_doc() {
		return upl_doc;
	}
	public void setUpl_doc(String upl_doc) {
		this.upl_doc = upl_doc;
	}
	public String getAttach_id() {
		return attach_id;
	}
	public void setAttach_id(String attach_id) {
		this.attach_id = attach_id;
	}
	public String getRsrch_proposal_id() {
		return rsrch_proposal_id;
	}
	public void setRsrch_proposal_id(String rsrch_proposal_id) {
		this.rsrch_proposal_id = rsrch_proposal_id;
	}
	public String getIs_doc_uploaded() {
		return is_doc_uploaded;
	}
	public void setIs_doc_uploaded(String is_doc_uploaded) {
		this.is_doc_uploaded = is_doc_uploaded;
	}
	public String getOa_id() {
		return oa_id;
	}
	public void setOa_id(String oa_id) {
		this.oa_id = oa_id;
	}
	public String getRsrch_proposal() {
		return rsrch_proposal;
	}
	public void setRsrch_proposal(String rsrch_proposal) {
		this.rsrch_proposal = rsrch_proposal;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
	public String getAchievement_det() {
		return achievement_det;
	}
	public void setAchievement_det(String achievement_det) {
		this.achievement_det = achievement_det;
	}
	

}

