package com.sits.rsrch.research_activity.fin_year_wise_utilization_certificate;

public class UtilizationCertificateModel {

	private String resPrpsId;
	private String uc_certname;
	private String auc_certname;
	private String fin_year;
	private String uc_id;
	
	public String getFin_year() {
		return fin_year;
	}
	public void setFin_year(String fin_year) {
		this.fin_year = fin_year;
	}
	public String getResPrpsId() {
		return resPrpsId;
	}
	public void setResPrpsId(String resPrpsId) {
		this.resPrpsId = resPrpsId;
	}
	public String getUc_certname() {
		return uc_certname;
	}
	public void setUc_certname(String uc_certname) {
		this.uc_certname = uc_certname;
	}
	public String getAuc_certname() {
		return auc_certname;
	}
	public void setAuc_certname(String auc_certname) {
		this.auc_certname = auc_certname;
	}
	public String getUc_id() {
		return uc_id;
	}
	public void setUc_id(String uc_id) {
		this.uc_id = uc_id;
	}
}