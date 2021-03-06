/**
 * File Name：TResSwitch.java
 *
 * Version：
 * Date：2012-3-2
 * Copyright CloudWei Dev Team 2012 
 * All Rights Reserved.
 *
 */
package models;

import models.core.ChangedModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * Project Name：com.cldouwei.monitor.model
 * Class Name：TResSwitch
 * Class Desc：
 * Author：tigaly
 * Create Date：2012-3-2 上午12:04:40
 * Last Modified By：tigaly
 * Last Modified：2012-3-2 上午12:04:40
 * Remarks：
 * @version 
 * 
 */
@Entity
@Table(name="T_CHANGED_SWITCH",uniqueConstraints={@UniqueConstraint(columnNames={"ELEMENT_ID","CHANGED_TIME"})})
public class TChangedSwitch extends ChangedModel {
	public String PARENT_SWITCH_WWN;
	public String SERIAL_NUMBER;
	public String CONTACT;
	public String LOCATION;
	public String IP_ADDRESS;
	/**
	 *  1 - Physical Device<br>
	 *  2 - Virtual Device
	 */
	public Short IS_PHYSICAL;
	public String DOMAIN;
	public String MANAGEMENT_ID;
	public String MGMT_TEL_ADDR;
	public String MGMT_SNMP_ADDR;
	public String MGMT_URL_ADDR;
	public String VERSION;
	public String DEDICATED;
	public Short SWITCH_MODE;
	public String PHYSICAL_PACKAGE_ID;
}
