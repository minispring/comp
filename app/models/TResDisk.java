/**
 * File Name：TResDisk.java
 *
 * Version：
 * Date：2012-2-29
 * Copyright CloudWei Dev Team 2012 
 * All Rights Reserved.
 *
 */
package models;

import models.core.ResModel;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

/**
 * 
 * Project Name：com.cldouwei.monitor.model
 * Class Name：TResDisk
 * Class Desc：
 * Author：tigaly
 * Create Date：2012-2-29 上午11:32:13
 * Last Modified By：tigaly
 * Last Modified：2012-2-29 上午11:32:13
 * Remarks：
 * @version 
 * 
 */

@Entity
@Table(name="T_RES_DISK",uniqueConstraints={@UniqueConstraint(columnNames={"NAME","SUBSYSTEM_ID"})})
public class TResDisk extends ResModel {
	public String PHYSICAL_PACKAGE_ID;
	public String FIRMWARE_VERSION;
	public String SERIAL_NUMBER;
	public String SLOT;
	public String RAID_LEVEL;
	public Integer IS_HOTSWAP;
	public Long USED_CAPACITY;
	public Integer SPEED;
	public Long RAW_CAPACITY;
	public Long CAPACITY;
	public Integer BLOCK_SIZE;
	
	public String STATUS_KEY;


    public static Model.Finder<String, TResDisk> find = new Model.Finder<String, TResDisk>(
            String.class, TResDisk.class
    );

    public static List<TResDisk> findBySubsystemId(String subsystemId){
        return find.where().eq("SUBSYSTEM_ID",subsystemId).findList();
    }
}
