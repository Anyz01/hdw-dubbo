package com.hdw.test.entity;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 实时数据表
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-08
 */
@TableName("t_p_real_data")
public class RealData extends Model<RealData> {

    private static final long serialVersionUID = 1L;

    /**
     * 实时数据表主键
     */
    @Id
	private String id;
    /**
     * 企业ID与t_sys_unit表中code相等
     */
	@TableField("unit_id")
	private String unitId;
    /**
     * 设备编号
     */
	@TableField("device_id")
	private String deviceId;
    /**
     * 设备名称
     */
	@TableField("device_name")
	private String deviceName;
    /**
     * 安装位置
     */
	@TableField("device_loca")
	private String deviceLoca;
    /**
     * 状态
     */
	private String status;
    /**
     * 实时动态值
     */
	private BigDecimal realValue;
    /**
     * 计量单位
     */
	private String unit;
    /**
     * 量程上线
     */
	@TableField("max_range")
	private BigDecimal maxRange;
    /**
     * 量程下线
     */
	@TableField("min_range")
	private BigDecimal minRange;
    /**
     * 报警上限
     */
   
	@TableField("max_excep")
	private BigDecimal maxExcep;
    /**
     * 报警下限
     */
	@TableField("min_excep")
	private BigDecimal minExcep;
    /**
     * 正常范围（小于low1,大于low2为低三预警）
     */
   
	private BigDecimal low1;
    /**
     * 低二报警（大于或等于low3,小于或等于low2）
     */
   
	private BigDecimal low2;
    /**
     * 低一报警（小于low3）
     */
   
	private BigDecimal low3;
    /**
     * 正常范围（大于high1，小于high2为高三预警）
     */
   
	private BigDecimal high1;
    /**
     * 高二报警（大于或等于high1，小于或等于high2）
     */
   
	private BigDecimal high2;
    /**
     * 高三报警（大于high3）
     */
   
	private BigDecimal high3;
    /**
     * 是否关键传感器（1-是，0-否）
     */
   
	@TableField("is_key")
	private String isKey;
    /**
     * 报警级别
     */
   
	@TableField("alert_degree")
	private String alertDegree;
    /**
     * 报警类型
     */
   
	@TableField("device_type")
	private String deviceType;
    /**
     * 报警位置
     */
   
	@TableField("alarm_loca")
	private String alarmLoca;
    /**
     * 报警设备名称
     */
   
	@TableField("alarm_device_name")
	private String alarmDeviceName;
    /**
     * 报警时间
     */
   
	@TableField("alarm_time")
	private Date alarmTime;
    /**
     * 报警确认时间
     */
   
	@TableField("alarm_ack_time")
	private Date alarmAckTime;
    /**
     * 报警确认描述
     */
   
	@TableField("alarm_ack_des")
	private String alarmAckDes;
    /**
     * 报警确认状态
     */ 
	@TableField("alarm_ack_status")
	private String alarmAckStatus;
    /**
     * 实时数据时间
     */
	@TableField("update_time")
	private Date updateTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceLoca() {
		return deviceLoca;
	}

	public void setDeviceLoca(String deviceLoca) {
		this.deviceLoca = deviceLoca;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getRealValue() {
		return realValue;
	}

	public void setRealValue(BigDecimal realValue) {
		this.realValue = realValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(BigDecimal maxRange) {
		this.maxRange = maxRange;
	}

	public BigDecimal getMinRange() {
		return minRange;
	}

	public void setMinRange(BigDecimal minRange) {
		this.minRange = minRange;
	}

	public BigDecimal getMaxExcep() {
		return maxExcep;
	}

	public void setMaxExcep(BigDecimal maxExcep) {
		this.maxExcep = maxExcep;
	}

	public BigDecimal getMinExcep() {
		return minExcep;
	}

	public void setMinExcep(BigDecimal minExcep) {
		this.minExcep = minExcep;
	}

	public BigDecimal getLow1() {
		return low1;
	}

	public void setLow1(BigDecimal low1) {
		this.low1 = low1;
	}

	public BigDecimal getLow2() {
		return low2;
	}

	public void setLow2(BigDecimal low2) {
		this.low2 = low2;
	}

	public BigDecimal getLow3() {
		return low3;
	}

	public void setLow3(BigDecimal low3) {
		this.low3 = low3;
	}

	public BigDecimal getHigh1() {
		return high1;
	}

	public void setHigh1(BigDecimal high1) {
		this.high1 = high1;
	}

	public BigDecimal getHigh2() {
		return high2;
	}

	public void setHigh2(BigDecimal high2) {
		this.high2 = high2;
	}

	public BigDecimal getHigh3() {
		return high3;
	}

	public void setHigh3(BigDecimal high3) {
		this.high3 = high3;
	}

	public String getIsKey() {
		return isKey;
	}

	public void setIsKey(String isKey) {
		this.isKey = isKey;
	}

	public String getAlertDegree() {
		return alertDegree;
	}

	public void setAlertDegree(String alertDegree) {
		this.alertDegree = alertDegree;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAlarmLoca() {
		return alarmLoca;
	}

	public void setAlarmLoca(String alarmLoca) {
		this.alarmLoca = alarmLoca;
	}

	public String getAlarmDeviceName() {
		return alarmDeviceName;
	}

	public void setAlarmDeviceName(String alarmDeviceName) {
		this.alarmDeviceName = alarmDeviceName;
	}

	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

	public Date getAlarmAckTime() {
		return alarmAckTime;
	}

	public void setAlarmAckTime(Date alarmAckTime) {
		this.alarmAckTime = alarmAckTime;
	}

	public String getAlarmAckDes() {
		return alarmAckDes;
	}

	public void setAlarmAckDes(String alarmAckDes) {
		this.alarmAckDes = alarmAckDes;
	}

	public String getAlarmAckStatus() {
		return alarmAckStatus;
	}

	public void setAlarmAckStatus(String alarmAckStatus) {
		this.alarmAckStatus = alarmAckStatus;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
