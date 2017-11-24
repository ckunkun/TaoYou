package com.ad.taoyou.common.helper;

/**
 * Created by 施行 on 2017/6/1.
 */

public class RoleInfo {
    private String roleId;//角色唯一标识
    private String roleName;//角色名
    private String roleLevel;//角色的等级
    private String zoneId;//角色所在区域唯一标识
    private String zoneName;//角色所在区域名称
    private String fightPower;//角色战斗力
    private String chargeMoney;//该区充值金额

    public String getFightPower() {
        return fightPower;
    }

    public void setFightPower(String fightPower) {
        this.fightPower = fightPower;
    }

    public String getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(String chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    @Override
    public String toString() {
        return "RoleInfo{" +
                "roleName='" + roleName + '\'' +
                ", roleLevel='" + roleLevel + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", zoneName='" + zoneName + '\'' +
                '}';
    }
}
