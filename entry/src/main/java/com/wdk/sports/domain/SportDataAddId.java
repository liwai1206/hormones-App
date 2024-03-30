package com.wdk.sports.domain;

import java.io.Serializable;

public class SportDataAddId implements Serializable {

    private int id;
    private int uid;

    private String device_id;//设备ID
    private String device_type ;//设备型号
    private String mode;//模式
    private int counts;//次数
    private int frequency;//频率
    private int interrupt_count;//中断次数
    private float average_speed;//平均速度
    private int average_range;//平均幅度
    private int rotation_counts ;//最大连转个数
    private int average_heart;
    private float calories;//卡路里
    private String time;//训练时间（yyyy-MM-dd HH:mm:ss）
    private int duration;//训练时长（秒
    private String ext_msg ;//拓展字段
    private String created;

    @Override
    public String toString() {
        return "SportDataAddId{" +
                "id=" + id +
                ", uid=" + uid +
                ", device_id='" + device_id + '\'' +
                ", device_type='" + device_type + '\'' +
                ", mode='" + mode + '\'' +
                ", counts=" + counts +
                ", frequency=" + frequency +
                ", interrupt_count=" + interrupt_count +
                ", average_speed=" + average_speed +
                ", average_range=" + average_range +
                ", rotation_counts=" + rotation_counts +
                ", average_heart=" + average_heart +
                ", calories=" + calories +
                ", time='" + time + '\'' +
                ", duration=" + duration +
                ", ext_msg='" + ext_msg + '\'' +
                ", created='" + created + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getInterrupt_count() {
        return interrupt_count;
    }

    public void setInterrupt_count(int interrupt_count) {
        this.interrupt_count = interrupt_count;
    }

    public float getAverage_speed() {
        return average_speed;
    }

    public void setAverage_speed(float average_speed) {
        this.average_speed = average_speed;
    }

    public int getAverage_range() {
        return average_range;
    }

    public void setAverage_range(int average_range) {
        this.average_range = average_range;
    }

    public int getRotation_counts() {
        return rotation_counts;
    }

    public void setRotation_counts(int rotation_counts) {
        this.rotation_counts = rotation_counts;
    }

    public int getAverage_heart() {
        return average_heart;
    }

    public void setAverage_heart(int average_heart) {
        this.average_heart = average_heart;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getExt_msg() {
        return ext_msg;
    }

    public void setExt_msg(String ext_msg) {
        this.ext_msg = ext_msg;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public SportDataAddId() {
    }

    public SportDataAddId(int id, int uid, String device_id, String device_type, String mode, int counts, int frequency, int interrupt_count, float average_speed, int average_range, int rotation_counts, int average_heart, float calories, String time, int duration, String ext_msg, String created) {
        this.id = id;
        this.uid = uid;
        this.device_id = device_id;
        this.device_type = device_type;
        this.mode = mode;
        this.counts = counts;
        this.frequency = frequency;
        this.interrupt_count = interrupt_count;
        this.average_speed = average_speed;
        this.average_range = average_range;
        this.rotation_counts = rotation_counts;
        this.average_heart = average_heart;
        this.calories = calories;
        this.time = time;
        this.duration = duration;
        this.ext_msg = ext_msg;
        this.created = created;
    }
}
