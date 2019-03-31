package cn.iotzone.mindlocal.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPUtils {
    public static void sendUdp(final String ip, final String msg){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DatagramSocket socket = null;
                    try {
                        InetAddress address = InetAddress.getByName(ip);
                        int port = 9128;
                        byte[] dataSend = msg.getBytes();
                        DatagramPacket packetSend = new DatagramPacket(dataSend,dataSend.length,address,port);
                        socket = new DatagramSocket();
                        socket.setSoTimeout(1000);
                        socket.send(packetSend);

                        byte[] dataReceive = new byte[2048];
                        DatagramPacket packetReceive = new DatagramPacket(dataReceive,dataReceive.length);
                        socket.receive(packetReceive);
                        String receiveMsg = new String(dataReceive,0,packetReceive.getLength(),"utf-8");
                        if (receiveMsg != null){
                            RxBus.get().send(new MQTTMessage(ip,receiveMsg.replaceAll("\\s*", "")));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (socket != null) {
                            socket.close();
                        }
                    }
                }
            }).start();
    }
}