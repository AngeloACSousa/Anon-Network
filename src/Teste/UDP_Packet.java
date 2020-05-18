package Teste;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UDP_Packet{
    private int response;
    private int fragment;

    private InetAddress address;
    private int port;

    private int client_id;

    private byte[] data;
    private int data_size;

    public UDP_Packet(DatagramPacket packet){
        this.address = packet.getAddress();
        this.port = packet.getPort();

        ByteBuffer wrapped = ByteBuffer.wrap(packet.getData());

        this.client_id = wrapped.getInt();
        this.response = wrapped.getInt();
        this.fragment = wrapped.getInt();
        this.data_size = wrapped.getInt();

        if(this.data_size > 0){
            this.data = new byte[this.data_size];

            wrapped.get(this.data);
        }
    }

    public UDP_Packet(boolean response, int fragment, InetAddress addr, int port, int client_id, byte[] data){
        if(response){
            this.response = 1;
        }
        else{
            this.response = 0;
        }
        this.fragment = fragment;

        this.address = addr;
        this.port = port;

        this.client_id = client_id;

        this.data = data;
        this.data_size = data.length;
    }

    public boolean isResponse(){
        if(this.response == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public int getClient_id(){
        return this.client_id;
    }

    public byte[] getData(){
        return this.data;
    }

    public int getFragment(){
        return this.fragment;
    }

    public DatagramPacket toDatagramPacket(){
        int packet_size = 4*4 + this.data_size;
        ByteBuffer b = ByteBuffer.allocate(packet_size);

        b.putInt(this.client_id);
        b.putInt(this.response);
        b.putInt(this.fragment);
        b.putInt(this.data_size);

        if(this.data_size > 0){
            b.put(this.data);
        }

        byte[] buff = b.array();

        return new DatagramPacket(buff, buff.length, address, port);
    }
}
