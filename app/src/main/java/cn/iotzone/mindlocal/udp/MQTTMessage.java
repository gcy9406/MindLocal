package cn.iotzone.mindlocal.udp;

public class MQTTMessage {
    private String topic;
    private String mesg;

    public MQTTMessage() {
    }

    public MQTTMessage(String topic, String mesg) {
        this.topic = topic;
        this.mesg = mesg;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    @Override
    public String toString() {
        return "MQTTMessage{" +
                "topic='" + topic + '\'' +
                ", mesg='" + mesg + '\'' +
                '}';
    }
}
