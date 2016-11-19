package com.lenovo.push.marketing.lestat.flume.vo;

import com.lenovo.push.marketing.lestat.flume.util.ProjectUtil;


public class SdacApiVo{
	
	private String c;
	private String mothod="";
	private String times="";
	private String imei="";
	private String imei2="";
	private String version;
	private String networkMode="";
	private String v="";
	private String imsi="";
	private String imsi2="";
	private String s="";
	private String l="";
	private String p="";
	private String ip="";
	private String reg_time="";
	private String system_id="";
	private String cell_id="";
	private String latitude="";
	private String longitude="";
	private String province="";
	private String city="";
	private String district="";
	private String coTime="";
	private String f="";
	private String isRetry="";
	private String _countryCode;//新增字段;
	public String getIsRetry() {
		return isRetry;
	}
	public void setIsRetry(String isRetry) {
		this.isRetry = isRetry;
	}
	public String getImei2() {
		return imei2;
	}
	
	public void setImei2(String imei2) {
		this.imei2 = imei2;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getMothod() {
		return mothod;
	}
	public void setMothod(String mothod) {
		this.mothod = mothod;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getNetworkMode() {
		return networkMode;
	}
	public void setNetworkMode(String networkMode) {
		this.networkMode = networkMode;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getImsi2() {
		return imsi2;
	}
	public void setImsi2(String imsi2) {
		this.imsi2 = imsi2;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getReg_time() {
		return reg_time;
	}
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}
	public String getSystem_id() {
		return system_id;
	}
	public void setSystem_id(String system_id) {
		this.system_id = system_id;
	}
	public String getCell_id() {
		return cell_id;
	}
	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCoTime() {
		return coTime;
	}
	public void setCoTime(String coTime) {
		this.coTime = coTime;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public String get_countryCode() {
		return _countryCode;
	}
	public void set_countryCode(String _countryCode) {
		this._countryCode = _countryCode;
	}
	
	
	public SdacApiVo deserializeSdac(String kafkaData){
		String[] str = kafkaData.split(ProjectUtil.SEPARATOR);
		
		SdacApiVo vo = null;
		if(str.length > 0){
			vo = new SdacApiVo();
			vo.setReg_time(str[1].equals(ProjectUtil.NULL_VALUE)?"":str[1]);
			vo.setMothod(str[2].equals(ProjectUtil.NULL_VALUE)?"":str[2]);
			vo.setTimes(str[3].equals(ProjectUtil.NULL_VALUE)?"":str[3]);
			vo.setVersion(str[4].equals(ProjectUtil.NULL_VALUE)?"":str[4]);
			vo.setNetworkMode(str[5].equals(ProjectUtil.NULL_VALUE)?"":str[5]);
			vo.setV(str[6].equals(ProjectUtil.NULL_VALUE)?"":str[6]);
			vo.setImsi(str[7].equals(ProjectUtil.NULL_VALUE)?"":str[7]);
			vo.setImei(str[8].equals(ProjectUtil.NULL_VALUE)?"":str[8]);
			vo.setS(str[9].equals(ProjectUtil.NULL_VALUE)?"":str[9]);
			vo.setL(str[10].equals(ProjectUtil.NULL_VALUE)?"":str[10]);
			vo.setP(str[11].equals(ProjectUtil.NULL_VALUE)?"":str[11]);
			vo.setImsi2(str[12].equals(ProjectUtil.NULL_VALUE)?"":str[12]);
			vo.setC(str[13].equals(ProjectUtil.NULL_VALUE)?"":str[13]);
			vo.setIp(str[14].equals(ProjectUtil.NULL_VALUE)?"":str[14]);
			vo.setF(str[15].equals(ProjectUtil.NULL_VALUE)?"":str[15]);
			vo.setImei2(str[16].equals(ProjectUtil.NULL_VALUE)?"":str[16]);
			vo.setIsRetry(str[17].equals(ProjectUtil.NULL_VALUE)?"":str[17]);
			vo.setSystem_id(str[18].equals(ProjectUtil.NULL_VALUE)?"":str[18]);
			vo.setCell_id(str[19].equals(ProjectUtil.NULL_VALUE)?"":str[19]);
			vo.setLatitude(str[20].equals(ProjectUtil.NULL_VALUE)?"":str[20]);
			vo.setLongitude(str[21].equals(ProjectUtil.NULL_VALUE)?"":str[21]);
			vo.setProvince(str[22].equals(ProjectUtil.NULL_VALUE)?"":str[22]);
			vo.setCity(str[23].equals(ProjectUtil.NULL_VALUE)?"":str[23]);
			vo.setDistrict(str[24].equals(ProjectUtil.NULL_VALUE)?"":str[24]);
			if(kafkaData.endsWith(ProjectUtil.SEPARATOR)){
				vo.setCoTime("");
			}else{
				vo.setCoTime(str[25].equals(ProjectUtil.NULL_VALUE)?"":str[25]);
			}
		}
		return vo;
	}
}
