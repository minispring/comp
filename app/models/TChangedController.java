/**
 * File Name：TResController.java
 *
 * Version：
 * Date：2012-2-29
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
 * Class Name：TResController
 * Class Desc：
 * Author：tigaly
 * Create Date：2012-2-29 下午3:48:38
 * Last Modified By：tigaly
 * Last Modified：2012-2-29 下午3:48:38
 * Remarks：
 * @version 
 * 
 */

@Entity
@Table(name="T_CHANGED_CONTROLLER",uniqueConstraints={@UniqueConstraint(columnNames={"ELEMENT_ID","CHANGED_TIME"})})
public class TChangedController extends ChangedModel {
	public Short INSTANCE_NUMBER;
	public Short BUS_NUMBER;
	public String DRIVER_NAME;
	public String DRIVER_DESCRIPTION;
	public Short TARGET;
	public String TYPE;
	public String LOOPS;
	public String ADAPTER_PAIR;
	public String ARRAY;
	public String WWN;
	public String SERIAL_NUMBER;
	public String FIRMWARE_VERSION;
	public String PHYSICAL_PACKAGE_ID;
	public Short USAGE_RESTRICTION;
}
