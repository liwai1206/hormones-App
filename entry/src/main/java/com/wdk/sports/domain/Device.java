package com.wdk.sports.domain;

import java.io.Serializable;

public class Device implements Serializable {

    private String product_id; // 产品ID
    private String product_type; // 产品类型
    private String device_id; // 设备id
    private String device_type; // 设备类型
    private String software_version; // 软件版本
    private String hardware_version; // 硬件版本
    private String batch_production; // 生产批次

    public Device(String product_id, String product_type, String device_id, String device_type, String software_version, String hardware_version, String batch_production) {
        this.product_id = product_id;
        this.product_type = product_type;
        this.device_id = device_id;
        this.device_type = device_type;
        this.software_version = software_version;
        this.hardware_version = hardware_version;
        this.batch_production = batch_production;
    }

    public Device() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getSoftware_version() {
        return software_version;
    }

    public void setSoftware_version(String software_version) {
        this.software_version = software_version;
    }

    public String getHardware_version() {
        return hardware_version;
    }

    public void setHardware_version(String hardware_version) {
        this.hardware_version = hardware_version;
    }

    public String getBatch_production() {
        return batch_production;
    }

    public void setBatch_production(String batch_production) {
        this.batch_production = batch_production;
    }

    @Override
    public String toString() {
        return "Device{" +
                "product_id='" + product_id + '\'' +
                ", product_type='" + product_type + '\'' +
                ", device_id='" + device_id + '\'' +
                ", device_type='" + device_type + '\'' +
                ", software_version='" + software_version + '\'' +
                ", hardware_version='" + hardware_version + '\'' +
                ", batch_production='" + batch_production + '\'' +
                '}';
    }
}
