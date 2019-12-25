package cn.iotzone.mindlocal.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DBDevice {
    @Id(autoincrement = true)
    private Long id;
    private String deviceName;
    private String deviceHead;
    private String deviceIp;
    private boolean state;
    private String deviceOutput;
    private String deviceInput;
    @Generated(hash = 1876024223)
    public DBDevice(Long id, String deviceName, String deviceHead, String deviceIp,
            boolean state, String deviceOutput, String deviceInput) {
        this.id = id;
        this.deviceName = deviceName;
        this.deviceHead = deviceHead;
        this.deviceIp = deviceIp;
        this.state = state;
        this.deviceOutput = deviceOutput;
        this.deviceInput = deviceInput;
    }
    @Generated(hash = 340485021)
    public DBDevice() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeviceName() {
        return this.deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceHead() {
        return this.deviceHead;
    }
    public void setDeviceHead(String deviceHead) {
        this.deviceHead = deviceHead;
    }
    public String getDeviceIp() {
        return this.deviceIp;
    }
    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }
    public boolean getState() {
        return this.state;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    public String getDeviceOutput() {
        return this.deviceOutput;
    }
    public void setDeviceOutput(String deviceOutput) {
        this.deviceOutput = deviceOutput;
    }
    public String getDeviceInput() {
        return this.deviceInput;
    }
    public void setDeviceInput(String deviceInput) {
        this.deviceInput = deviceInput;
    }

    @Override
    public String toString() {
        return "DBDevice{" +
                "id=" + id +
                ", deviceName='" + deviceName + '\'' +
                ", deviceHead='" + deviceHead + '\'' +
                ", deviceIp='" + deviceIp + '\'' +
                ", state=" + state +
                ", deviceOutput='" + deviceOutput + '\'' +
                ", deviceInput='" + deviceInput + '\'' +
                '}';
    }
}
