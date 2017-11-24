package com.ad.taoyou.swk.login;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ThridAccess implements Serializable {

	@Expose
	private String access_token;
	@Expose
	private String expires_in;
	@Expose
	private String refresh_token;
	@Expose
	private String openid;
	@Expose
	private String scope;
	@Expose
	private String unionid;

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getAccess_token() {
		return this.access_token;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getExpires_in() {
		return this.expires_in;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getRefresh_token() {
		return this.refresh_token;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return this.openid;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getScope() {
		return this.scope;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getUnionid() {
		return this.unionid;
	}

	@Override
	public String toString() {
		return "ThridAccess [access_token=" + access_token + ", expires_in="
				+ expires_in + ", refresh_token=" + refresh_token + ", openid="
				+ openid + ", scope=" + scope + ", unionid=" + unionid + "]";
	}

}
