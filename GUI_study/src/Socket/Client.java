package Socket;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private boolean NotEnd = true;
    private Scanner scanner = new Scanner(System.in);
    private boolean status=false;

    public boolean isStatus() {
        return status;
    }

    public  Client() {
    }


    public boolean sendFile(String res_path, String outstr) throws IOException {
        dataOutputStream.writeUTF(outstr);
        dataOutputStream.flush();
        File file = new File(res_path);
        FileInputStream fileInputStream = new FileInputStream(file);
        if(!file.exists())
        {
            dataOutputStream.writeUTF("文件夹不存在");
            return false;
        }
        if (re_message()) {
            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.flush();
            dataOutputStream.writeLong(file.length());
            dataOutputStream.flush();
            System.out.println("文件开始传输");
            byte[] bytes = new byte[1024];
            int count = 0;
            while ((count = fileInputStream.read(bytes, 0, bytes.length)) != -1) {
                dataOutputStream.write(bytes, 0, count);
                dataOutputStream.flush();
            }
            System.out.println("文件传输成功");
        }
        fileInputStream.close();
        re_message();
        return true;
    }

    public boolean trans(String outstr) throws IOException {
        dataOutputStream.writeUTF(outstr);
        dataOutputStream.flush();
        return (re_message());
    }

    public boolean re_message() throws IOException {
        String instr;
        instr = dataInputStream.readUTF();
        if (!instr.equals("BYE")) {
            System.out.println("------------返回结果-------------");
            while (!instr.equals("END")) {
                System.out.println(instr);
                instr = dataInputStream.readUTF();
            }
            System.out.println("------------一次对话完成-------------");
            return true;
        } else
            return false;
    }

    public boolean connect(String ip,int port) {
        try {
            status=true;
            socket = new Socket(ip, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void disconnect() {
        status=false;
        System.out.println("断开连接");
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void send(String string) {
        try {
            if (!trans(string)) ;
            System.out.println("连接结束");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public  void main()
//    {
//        try {
//            socket=new Socket("localhost",5000);
//            dataInputStream=new DataInputStream(socket.getInputStream());
//            dataOutputStream=new DataOutputStream(socket.getOutputStream());
//            System.out.println("已经连接服务器localhost:5000");
//            String outstr,instr;
//
//            while(NotEnd)
//            {
//               // trans();
////                outstr=scanner.nextLine();
////                dataOutputStream.writeUTF(outstr);
////                dataOutputStream.flush();
////                instr=dataInputStream.readUTF();
////                if(!instr.equals("BYE"))
////                {
////                    System.out.println("------------返回结果-------------");
////                    while (!instr.equals("END"))
////                    {
////                        System.out.println(instr);
////                        instr=dataInputStream.readUTF();
////                    }
////                    System.out.println("------------一次对话完成-------------");
////
////                }
////                else
////                    NotEnd=false;
//            }
//            dataInputStream.close();
//            dataOutputStream.close();
//            socket.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
}
