/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.research_activity.research_sub_head;

public class ResearchSubHeadModel {
	private String hName;
	private String hId;
	private String subHeadType;
	private String subHeadId;
	private String subHeadName;
	private String location;
	private String location_name;

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String gethName() {
		return hName;
	}

	public void sethName(String hName) {
		this.hName = hName;
	}

	public String getSubHeadType() {
		return subHeadType;
	}

	public void setSubHeadType(String subHeadType) {
		this.subHeadType = subHeadType;
	}

	public String getSubHeadId() {
		return subHeadId;
	}

	public void setSubHeadId(String subHeadId) {
		this.subHeadId = subHeadId;
	}

	public String getSubHeadName() {
		return subHeadName;
	}

	public void setSubHeadName(String subHeadName) {
		this.subHeadName = subHeadName;
	}

	public String gethId() {
		return hId;
	}

	public void sethId(String hId) {
		this.hId = hId;
	}
}