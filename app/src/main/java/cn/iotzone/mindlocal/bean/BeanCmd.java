package cn.iotzone.mindlocal.bean;

public class BeanCmd {

    /**
     * cmd : setr
     * output : 11111111
     * input : 00000000
     * sn : 002ffd008af2d150
     */

    private String cmd;
    private String output;
    private String input;
    private String sn;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "BeanCmd{" +
                "cmd='" + cmd + '\'' +
                ", output='" + output + '\'' +
                ", input='" + input + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }
}
