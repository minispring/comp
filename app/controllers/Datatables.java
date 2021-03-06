package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.mvc.*;
import utils.Format;
import views.html.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class Datatables extends Controller {

    public static Result options() {
        String id = request().getQueryString("id");
        String title = request().getQueryString("title");
        String model = request().getQueryString("model");
        String start_time = request().getQueryString("start_time");
        String end_time = request().getQueryString("end_time");
        ObjectNode options = Json.newObject();
        if("cfg_subsystem".equals(model))
            options = getSubsystemCfg(id, title, start_time, end_time);
        else if("cfg_report".equals(model))
            options = getReportCfg(id, title, start_time, end_time);
        else if("prf_subsystem".equals(model))
            options = getSubsystemPrf(id, title, start_time, end_time);
        else if("prf_raidgroup".equals(model))
            options = getRaidGroupPrf(id, title, start_time, end_time);
        else if("prf_fcport".equals(model))
            options = getFCPortPrf(id, title, start_time, end_time);
        else if("cfg_fcport".equals(model))
            options = getFCPortCfg(id);
        else if("cfg_raidgroup".equals(model))
            options = getRaidGroupCfg(id);
        else if("cfg_volume".equals(model))
            options = getVolumeCfg(id);
        else if("cfg_disk".equals(model))
            options = getDiskCfg(id);
        else if("cfg_hostgroup".equals(model))
            options = getHostGroupCfg(id);
        else if("cfg_business".equals(model))
            options = getBusinessCfg(id);
        else if("cfg_port".equals(model))
            options = getPortCfg(id);
        else if("alarm".equals(model))
            options = getAlarm(id, title, start_time, end_time);
        else if("guest_os".equals(model))
            options = getVMGuest(id);
        return ok(options);
    }

    private static ObjectNode getSubsystemCfg(String id, String title, String start_time, String end_time) {
        String[] kpiColumns = {"名称","已使用容量","空闲容量","总容量","IP地址","序列号","厂商","端口数量","卷数量"};
        List<TResStorageSubsystem> subsystems = TResStorageSubsystem.findAll();
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for (TResStorageSubsystem subsystem : subsystems){
            ArrayNode obj = rows.addArray();
            obj.add(subsystem.NAME);
            obj.add(Format.parserCapacity(subsystem.ALLOCATED_CAPACITY));
            obj.add(Format.parserCapacity(subsystem.ASSIGNED_CAPACITY-subsystem.ALLOCATED_CAPACITY));
            obj.add(Format.parserCapacity(subsystem.ASSIGNED_CAPACITY));
            obj.add(subsystem.IP_ADDRESS==null?"no data":subsystem.IP_ADDRESS);
            obj.add(subsystem.SERIAL_NUMBER);
            obj.add(TResVendor.findById(subsystem.VENDOR_ID).NAME);
            obj.add(TResPort.findBySubsystemId(subsystem.ID).size());
            obj.add(TResStorageVolume.findBySubsystemId(subsystem.ID).size());
        }
        return options;
    }

    private static ObjectNode getReportCfg(String id, String title, String start_time, String end_time) {
        String[] kpiColumns = {"名称","设备类型","类型","上一次运行时间","生成次数"};
        List<TResReport> reports = TResReport.findAll();
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        Random random = new Random();
        for (String colname : kpiColumns)
            cols.add(colname);
        for (TResReport report : reports){
            ArrayNode obj = rows.addArray();
            obj.add("<a href='#'>"+report.REPORT_NAME+"</a>");
            obj.add(report.DEVICE_TYPE);
            obj.add("临时报表");
            obj.add(Format.parseString(report.CREATE_TIME,"yyyy-MM-dd HH:mm:ss"));
            obj.add(random.nextInt(10));
        }
        return options;
    }

    private static ObjectNode getSubsystemPrf(String id, String title, String start_time, String end_time) {
        long testStartTime = 1400480760000L;
        long interval = 60000;
        int count = 50;
        String[] testSubsystems = {"USPV.29846", "USPV.29416", "VSP.90873"};
        String[] kpiColumns = {"IOPS","Transfer(mb)","Response Time(ms)","Cache Hits(%)"};
        Random random = new Random();

        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        cols.add("名称");
        cols.add("时间");
        for (String colname : kpiColumns)
            cols.add(colname);

        for (int i = 0; i < count; i++){
            ArrayNode obj = rows.addArray();
            obj.add(testSubsystems[random.nextInt(2)]);
            obj.add(Format.parseDateString(testStartTime + random.nextInt(50)*interval,"yyyy-MM-dd HH:mm:ss"));
            for (String colname : kpiColumns)
                obj.add(random.nextInt(130));
        }
        return options;
    }

    private static ObjectNode getFCPortPrf(String id, String title, String start_time, String end_time) {
        String[] kpiColumns = {"IOPS","Transfer(mb)"};
        List<TResPort> ports = TResPort.findBySubsystemId(id);
        Random random = new Random();
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        cols.add("名称");
        for (String colname : kpiColumns)
            cols.add(colname);
        for (int i=0;i < ports.size();i++) {
            ArrayNode obj = rows.addArray();
            obj.add(ports.get(i).ELEMENT_NAME);
            for (String colname : kpiColumns)
                obj.add(random.nextInt(130));
        }
        return options;
    }

    private static ObjectNode getFCPortCfg(String id) {
        String[] kpiColumns = {"名称","WWN","类型","端口号","Speed"};
        List<TResPort> ports = TResPort.findBySubsystemId(id);
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for(TResPort port : ports){
            ArrayNode obj = rows.addArray();
            obj.add(port.ELEMENT_NAME);
            obj.add(Format.splitWWN(port.NAME));
            obj.add(port.USAGE_RESTRICTION == null ? "FICON": "Fibre");
            obj.add(port.PORT_NUMBER == null ? "no data" : port.PORT_NUMBER);
            obj.add(port.PORT_SPEED == null ? "no data" : Format.parserSpeed(port.PORT_SPEED));
        }
        return options;
    }

    private static ObjectNode getVMGuest(String id) {
        String[] kpiColumns = {"主机名","IP地址","操作系统","状态","内存大小", "内存使用率", "CPU数量", "CPU使用率"};
        List<TResHost> hosts = TResHost.find("PARENT_ID='"+id+"'");
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for(TResHost host : hosts){
            ArrayNode obj = rows.addArray();
            obj.add(host.ELEMENT_NAME);
            obj.add(host.IP_ADDRESS==null?"":host.IP_ADDRESS);
            obj.add(host.OS_DESCRIPTION);
            obj.add(host.STATUS.equals("poweredOn")?"<a href=\"#\" class=\"badge badge-success\">运行中</a>":"<a href=\"#\" class=\"badge badge-warning\">已关机</a>");
            obj.add(host.MEMEORY_SIZE);
            obj.add(host.MEMEORY_USAGE);
            obj.add(host.CPU_CORES);
            obj.add(host.CPU_USAGE);
        }
        return options;
    }

    private static ObjectNode getPortCfg(String id) {
        String[] kpiColumns = {"名称","WWN","端口号","类型","Speed"};
        List<TResPort> ports = TResPort.findBySubsystemId(id);
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for(TResPort port : ports){
            ArrayNode obj = rows.addArray();
            obj.add(port.ELEMENT_NAME);
            obj.add(Format.splitWWN(port.NAME));
            obj.add(port.PORT_NUMBER == null ? "no data" : port.PORT_NUMBER);
            obj.add("Fibre");
            obj.add(port.PORT_SPEED == null ? "no data" : Format.parserSpeed(port.PORT_SPEED));
        }
        return options;
    }

    private static ObjectNode getRaidGroupCfg(String id) {
        String[] kpiColumns = {"名称","Raid Level","容量","类型","卷数量"};
        List<TResRaidGroup> raidgroups = TResRaidGroup.findBySubsystemId(id);
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for(TResRaidGroup raidgroup : raidgroups){
            ArrayNode obj = rows.addArray();
            obj.add(raidgroup.NAME);
            obj.add(raidgroup.RAID_LEVEL);
            obj.add(Format.parserCapacity(raidgroup.DDM_CAP));
            obj.add("FC");
            obj.add(TResRaidGroup2Vol.countByRaidGroupId(raidgroup.ID));
        }
        return options;
    }

    private static ObjectNode getVolumeCfg(String id) {
        String[] kpiColumns = {"名称","Raid Level","容量","类型","Raid组","Num of Blocks"};
        List<TResStorageVolume> volumes = TResStorageVolume.findBySubsystemId(id);
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for(TResStorageVolume volume : volumes){
            ArrayNode obj = rows.addArray();
            obj.add(volume.NAME);
            obj.add(volume.RAID_LEVEL == null ? "no data":volume.RAID_LEVEL);
            obj.add(Format.parserCapacity(volume.CAPACITY));
            obj.add("FC");
            TResRaidGroup2Vol rd2v = null;
            obj.add(rd2v == null ? "no data" : TResRaidGroup.findById(rd2v.RAIDGROUP_ID).NAME);
            obj.add(volume.NUMBER_BLOCKS == null?"no data":volume.NUMBER_BLOCKS+"");
        }
        return options;
    }

    private static ObjectNode getDiskCfg(String id) {
        String[] kpiColumns = {"名称","Raid Level","容量","类型","厂商","序列号","Slot"};
        List<TResDisk> disks = TResDisk.findBySubsystemId(id);
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for(TResDisk disk : disks){
            ArrayNode obj = rows.addArray();
            obj.add(disk.NAME);
            obj.add(disk.RAID_LEVEL == null ? "no data":disk.RAID_LEVEL);
            obj.add(Format.parserCapacity(disk.CAPACITY));
            obj.add("FC");
            obj.add(TResVendor.findById(disk.VENDOR_ID).NAME);
            obj.add(disk.SERIAL_NUMBER);
            obj.add(disk.SLOT);
        }
        return options;
    }

    private static ObjectNode getBusinessCfg(String id) {
        String[] kpiColumns = {"名称","描述","Host Group","容量","卷数量"};
        List<TResApplication> apps = TResApplication.findAll();
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for(TResApplication app : apps){
            ArrayNode obj = rows.addArray();
            obj.add(app.NAME);
            obj.add(app.DESCRIPTION);
            String hostgroups = "";
            HashMap<String,String> hostgroupMap = new HashMap<String,String>();
            List<TResApplication2Lun> app2lunList = TResApplication2Lun.findByApplicationId(app.ID);
            for(TResApplication2Lun app2lun : app2lunList){
                if(hostgroupMap.containsKey(app2lun.HOSTGROUP))
                    continue;
                else
                    hostgroupMap.put(app2lun.HOSTGROUP,app2lun.HOSTGROUP);
                hostgroups += (app2lun.HOSTGROUP+"<br>");
            }
            obj.add(hostgroups);
            obj.add(Format.parserCapacity(app.CAPACITY));
            obj.add(app.N_VOL);
        }
        return options;
    }

    private static ObjectNode getHostGroupCfg(String id) {
        String[] kpiColumns = {"Raid Level","RaidGroup","卷","容量","前端口","HBA WWN","Host Group","应用名称"};
        List<TResLunMapping> lunmappingList = TResLunMapping.findBySubsystemId(id);
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        for (String colname : kpiColumns)
            cols.add(colname);
        for(TResLunMapping lunmapping : lunmappingList){
            TResStorageVolume volume = TResStorageVolume.findById(lunmapping.VOLUME_ID);
            TResRaidGroup2Vol rd2vol = TResRaidGroup2Vol.findByVolumeId(volume.ID);
            String rd = "no data";
            String rd_level = (volume.RAID_LEVEL == null ? "no data" : volume.RAID_LEVEL);
            String fcport = "*";
            if(rd2vol != null){
                TResRaidGroup raidgroup = TResRaidGroup.findById(rd2vol.RAIDGROUP_ID);
                if(raidgroup != null) {
                    rd = raidgroup.NAME;
                    rd_level = raidgroup.RAID_LEVEL;
                }
            }
            if(lunmapping.FCPORT_ID != null){
                TResPort port = TResPort.findById(lunmapping.FCPORT_ID);
                if(port != null)
                    fcport = port.ELEMENT_NAME;
            }
            ArrayNode obj = rows.addArray();
            obj.add(rd_level);
            obj.add(rd);
            obj.add(volume.ELEMENT_NAME);
            obj.add(Format.parserCapacity(volume.CAPACITY));
            obj.add(fcport);
            obj.add(Format.splitWWN(lunmapping.HBA_WWN));
            obj.add(lunmapping.HOST_NAME);
            List<TResApplication2Lun> app = TResApplication2Lun.findBySubsystemId(id,lunmapping.HOST_NAME);
            if(app.isEmpty())
                obj.add("");
            else
                obj.add(app.get(0).APPLICATION_NAME);
        }
        return options;
    }

    private static ObjectNode getRaidGroupPrf(String id, String title, String start_time, String end_time) {
        String[] kpiColumns = {"IOPS","Transfer(mb)","Response Time(ms)","Cache Hits(%)"};
        List<TResRaidGroup> raidgroupList = TResRaidGroup.findBySubsystemId(id);
        Random random = new Random();
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        cols.add("名称");
        cols.add("容量");
        cols.add("Raid Level");
        for (String colname : kpiColumns)
            cols.add(colname);
        for (int i=0;i < raidgroupList.size();i++) {
            TResRaidGroup raidgroup = raidgroupList.get(i);
            ArrayNode obj = rows.addArray();
            obj.add(raidgroup.NAME);
            obj.add(Format.parserCapacity(raidgroup.DDM_CAP));
            obj.add(raidgroup.RAID_LEVEL);
            for (String colname : kpiColumns)
                obj.add(random.nextInt(130));
        }
        return options;
    }

    private static ObjectNode getAlarm(String id, String title, String start_time, String end_time) {
        String[] severity = {"Clear","Information","Warning","Minor","Major","Critical","Fatal"};
        String[] colors = {"btn-light-green","btn-light-green","btn-light-green","btn-info","btn-warning","btn-danger","btn-dark-gray"};
        List<TAlarm> alarms = TAlarm.findAll();
        ObjectNode options = Json.newObject();
        ArrayNode cols = options.putArray("cols");
        ArrayNode rows = options.putArray("rows");
        cols.add("设备");
        cols.add("级别");
        cols.add("描述");
        cols.add("分类");
        cols.add("开始时间");
        for(int i = 0; i < alarms.size(); i++){
            TAlarm alarm = alarms.get(i);
            ArrayNode obj = rows.addArray();
            obj.add(alarm.DEVICE_NAME);
            obj.add("<a class='btn btn-sm "+colors[alarm.ALARM_SEVERITY]+" btn-labeled'>"+severity[alarm.ALARM_SEVERITY]+"</a>");
            obj.add(alarm.ORIGINAL_MESSAGE);
            obj.add(alarm.DEVICE_TYPE);
            obj.add(Format.parseString(alarm.LAST_OCCURENCE,"yyyy-MM-dd HH:mm:ss"));
        }
        return options;
    }

}
