package Socket;

import FUNC.MyFile;

import java.io.*;
import java.net.Socket;
public  class ServerThread extends Thread{
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private MyFile myFile;

    public ServerThread(Socket socket) throws IOException {
        super();
        System.out.println("接受连接");
        this.socket=socket;
        dataInputStream=new DataInputStream(socket.getInputStream());
        dataOutputStream=new DataOutputStream(socket.getOutputStream());
        myFile=new MyFile();
        start();
    }

    public  void message_over() throws IOException {
        dataOutputStream.writeUTF("END");
        dataOutputStream.flush();
    }

    public boolean is_END(String res) throws IOException {
        if(res.equals("END"))
        {
            dataOutputStream.writeUTF("COPY结束");
            dataOutputStream.flush();
            message_over();
            return true;
        }
        return false;
    }

    public String res_address() throws IOException {
        dataOutputStream.writeUTF("请输入地址");
        dataOutputStream.flush();
        message_over();
        System.out.println("已发送回复:");
        String str=dataInputStream.readUTF();
        return str;
    }

    public boolean list() throws IOException {
        System.out.println("接受到LIST请求");
        String str=res_address();
        if(!str.equals("END"))
            myFile.List(str);
        else
            System.out.println("list提前结束");
        System.out.println("list结束");
        dataOutputStream.writeUTF("list结束");
        dataOutputStream.flush();
        message_over();
        return true;
    }
    public boolean delete() throws IOException {
        System.out.println("接受到DELETE请求");
        String str=res_address();
        if(!str.equals("END"))
            myFile.Delete(str);
        else
            System.out.println("delete提前结束");
        System.out.println("delete结束");
        dataOutputStream.writeUTF("delete结束");
        dataOutputStream.flush();
        message_over();
        return true;
    }

    public boolean lock() throws IOException {
        System.out.println("接受到LOCK请求");
        String str=res_address();
        if(!str.equals("END"))
            myFile.Locked(str);
        else
            System.out.println("lock提前结束");
        System.out.println("lock结束");
        dataOutputStream.writeUTF("lock结束");
        dataOutputStream.flush();
        message_over();
        return true;
    }

    public boolean unlock() throws IOException {
        System.out.println("接受到UNLOCK请求");
        String str=res_address();
        if(!str.equals("END"))
            myFile.UnLocked(str);
        else
            System.out.println("unlock");
        System.out.println("unlock结束");
        dataOutputStream.writeUTF("unlock结束");
        dataOutputStream.flush();
        message_over();
        return true;
    }

    public boolean res_aim_address(StringBuffer res,StringBuffer aim) throws IOException {
        System.out.println("发送源地址请求");
        dataOutputStream.writeUTF("请输入源地址");
        dataOutputStream.flush();
        message_over();
        String res_temp=dataInputStream.readUTF();
        if(is_END(res_temp))
            return false;
        System.out.println("发送目标地址请求");
        dataOutputStream.writeUTF("请输入目标地址");
        dataOutputStream.flush();
        message_over();
        String aim_temp=dataInputStream.readUTF();
        if(is_END(aim_temp))
            return false;
        res.append(res_temp);
        aim.append(aim_temp);
        return true;
    }
    public boolean copy() throws IOException {
        System.out.println("接收到COPY请求");
        StringBuffer res=new StringBuffer();
        StringBuffer aim=new StringBuffer();
        if(res_aim_address(res,aim))
            myFile.Copy(res.toString(),aim.toString());
        System.out.println("copy结束");
        dataOutputStream.writeUTF("copy结束");
        dataOutputStream.flush();
        message_over();
        return  true;

    }
    public boolean cut_and_paste() throws IOException
    {
        System.out.println("接收到 CUT PASTE 请求");
        StringBuffer res=new StringBuffer();
        StringBuffer aim=new StringBuffer();
        if(res_aim_address(res,aim))
            myFile.Cut_and_Paste(res.toString(),aim.toString());
        System.out.println("cut paste 结束");
        dataOutputStream.writeUTF("cut paste结束");
        dataOutputStream.flush();
        message_over();
        return  true;
    }

    public boolean recieve() throws IOException {
        System.out.println("接收到SEND FILE请求");
        dataOutputStream.writeUTF("接收到SEND FILE请求");
        dataOutputStream.flush();
        message_over();
        String fileName=dataInputStream.readUTF();
        long fileLength=dataInputStream.readLong();
        File dir=new File("F:\\ftp_download");
        if(!dir.exists())
        {
            dir.mkdir();//java.io.File.mkdir()：只能创建一级目录，且父目录必须存在，否则无法成功创建一个目录。
            // java.io.File.mkdirs()：可以创建多级目录，父目录不一定存在。
        }
        File file=new File(dir.getAbsolutePath()+File.separatorChar+fileName);
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        dataOutputStream.writeUTF("开始接受文件");
        dataOutputStream.flush();
        byte []buf=new byte[(int)fileLength];
        int count=0;
        if ((count=dataInputStream.read(buf,0,buf.length))!=-1)
        {
            fileOutputStream.write(buf,0,count);
            fileOutputStream.flush();
        }
        fileOutputStream.close();
        dataOutputStream.writeUTF("send file结束");
        dataOutputStream.flush();
        message_over();
        return true;
    }

    @Override
    public void run()
    {
        try {
            String str;
            boolean NotEnd=true;
            while (NotEnd)
            {
                str=dataInputStream.readUTF();
                if(str.equals("LIST"))
                {
                    NotEnd=list();
                }
                else if(str.equals("COPY"))
                {
                    NotEnd=copy();
                }
                else if(str.equals("CUTPASTE"))
                {
                    NotEnd=cut_and_paste();
                }
                else if(str.equals("DELETE"))
                {
                    NotEnd=delete();
                }
                else if(str.equals("LOCK"))
                {
                    NotEnd=lock();
                }
                else if(str.equals("UNLOCK"))
                {
                    NotEnd=unlock();
                }
                else if(str.equals("SENDFILE"))
                {
                    NotEnd=recieve();
                }
                else if(str.equals("BYE"))
                {
                    NotEnd=false;
                    dataOutputStream.writeUTF("BYE");
                    dataOutputStream.flush();
                }
                else
                {
                    dataOutputStream.writeUTF("无该指令");
                    dataOutputStream.flush();
                    dataOutputStream.writeUTF("END");
                    dataOutputStream.flush();
                }
                System.out.println("-------------一次会话结束---------");
            }
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
