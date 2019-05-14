package Socket;

import FUNC.FILE_GUI_FUN;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    boolean status=false;
    boolean disconnet=false;
    public boolean isStatus() {
        return status;
    }

    private FILE_GUI_FUN file_gui_fun;
    public void over()
    {
        status=false;
        disconnet=true;
    }
    public void init(FILE_GUI_FUN file_gui_fun,String s)
    {
        this.file_gui_fun=file_gui_fun;
        file_gui_fun.List(s);
    }
    public void main()
    {
        try {
            status=true;
            System.out.println("等待连接");
            ServerSocket serverSocket=new ServerSocket(5000);
            Socket socket=null;
            while(!disconnet)
            {
//                Runnable my= new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("匿名类");
//                    }
//                };
//                my.run();
                socket=serverSocket.accept();
                new ServerThread(socket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
