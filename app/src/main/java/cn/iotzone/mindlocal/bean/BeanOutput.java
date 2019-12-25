package cn.iotzone.mindlocal.bean;

import java.io.Serializable;

public class BeanOutput implements Serializable {
    private String name;
    private String head;
    private int channel;
    private boolean state;
    private int mode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BeanOutput{" +
                "name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", channel=" + channel +
                ", state=" + state +
                ", mode=" + mode +
                '}';
    }
}
