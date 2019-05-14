package FUNC;//文件管理系统
//        1、基本文件管理功能：
//        a.文件夹和文件的创建、删除；
//        b.当前文件夹下的内容罗列；
//        c.文件、文件夹的拷贝和粘贴（文件夹拷贝指深度拷贝，包括所有子目录和文件）；
//        d.指定文件的加密和解密。（加密解密流）

import java.io.*;
import java.util.Scanner;

public class FILE_FUN {

    public FILE_FUN()
    {
    }
    private int cut_flag=0;
    public boolean Copy(String res_path,String des_path)
    {
        cut_flag=0;
        try {
            return copy_file(res_path,des_path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Cut_and_Paste(String res_path,String des_path)
    {
        cut_flag=1;
        try {
            return copy_file(res_path,des_path);
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean Delete(String res_path)
    {
        try {
            return delete_all_kinds_of_files(res_path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean List(String res_path)
    {
        try {
            return lists(res_path);
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }

    private boolean lists(String res_path) throws IOException {
        File file=new File(res_path);
        if(!file.exists()) {
            System.out.println("文件不存在");
            return false;
        }
        else if(file.isFile())
        {
            System.out.println(file.getPath());
            return true;
        }
        else if(file.isDirectory())
        {
            System.out.println(file.getPath());
            return list_dir(res_path,"..");
        }
        System.out.println("意外的错误，文件打开失败");
        return false;
    }

    private boolean list_dir(String res_path,String addition) throws IOException {
        File file_res=new File(res_path);
        File files[]=file_res.listFiles();
        if(!file_res.exists())
        {
            System.out.println("文件夹不存在");
            return false;
        }
        for(File file:files) {
            if (file.isFile()) {
                System.out.println(addition + file.getPath());
            }else if (file.isDirectory()) {
                System.out.println(addition+file.getPath());
                list_dir(file.getPath(), addition + "..");
            }
        }
        return true;
    }
    private boolean delete_all_kinds_of_files(String res_path) throws IOException {
        File file=new File(res_path);
        if(!file.exists()) {
            System.out.println("文件不存在");
            return false;
        }
        else if(file.isFile())
        {
            System.out.println("开始删除文件");
            if(delete_file(res_path))
            {
                System.out.println("删除成功");
                return true;
            }
        }
        else if(file.isDirectory())
        {
            System.out.println("开始删除文件夹");
            if(delete_dir(res_path))
            {
                System.out.println("删除成功");
                return true;
            }
        }
        System.out.println("意外的错误，删除失败");
        return false;
    }



    private boolean copy_file(String res_path,String des_path) throws IOException {
        File file=new File(res_path);
        if(!file.exists()) {
            System.out.println("文件不存在");
            return false;
        }
        else if(file.isFile())
        {
            System.out.println("开始复制/剪切文件");
            if(copy_in_file(res_path, des_path))
            {
                System.out.println("复制/剪切成功");
                return true;
            }
        }
        else if(file.isDirectory())
        {
            System.out.println("文件夹开始复制/剪切");
            if(copy_in_directory(res_path,des_path))
            {
                System.out.println("复制/剪切成功");
                return true;
            }
        }
        System.out.println("意外的错误，复制/剪切失败");
        return false;
    }

    private boolean copy_in_file(String res_path,String des_dir) throws IOException {
        File file=new File(res_path);//源文件夹
        String file_name=res_path.substring(res_path.lastIndexOf(File.separator));
        String des_path=des_dir+file_name;//目标文件夹+源文件名字
        File file_des_dir=new File(des_dir);
        File file_des=new File(des_path);
        if(des_path.equals(res_path))
        {
            System.out.println("复制/剪切成功");
            return true;
        }
        else if(!file_des_dir.exists())
        {
            System.out.println("路径错误");
            return false;
        }
        else if(file.exists() && file_des.exists())
        {
            System.out.println("源文件已存在,复制失败");
            return false;
        }
        else
        {
            try {
                if(trans_file(res_path,des_path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            {
                System.out.println(res_path+" 复制成功到 "+des_path);
                if(cut_flag==1)
                    if(file.delete())
                        System.out.println(res_path+" 成功删除 ");
                return true;
            }
        }
    }

    private boolean trans_file(String res_path,String des_path) throws IOException {
        FileInputStream fileInputStream= null;
        try {
            fileInputStream = new FileInputStream(res_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner=new Scanner(fileInputStream);

        PrintWriter printWriter= null;
        try {
            printWriter = new PrintWriter(des_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scanner.hasNext())
        {
            String temp=scanner.nextLine();
            printWriter.println(temp);
        }
        scanner.close();
        fileInputStream.close();
        printWriter.close();
        return true;
    }
    private boolean copy_in_directory(String res_path,String des_dir) throws IOException {
        File file_res=new File(res_path);
        File file_des_dir =new File(des_dir);//目标文件夹
        if(!file_des_dir.exists())
        {
            System.out.println("目标文件夹不存在");
            return false;
        }

        String des_path=des_dir+res_path.substring(res_path.lastIndexOf(File.separator));
        File file_des=new File(des_path);//目标文件夹+源文件夹名字
        if(file_des.exists())
        {
            System.out.println("源路径已有同名文件夹");
            return false;
        }
        file_des.mkdirs();
        File files[]=file_res.listFiles();
        for(File file:files)
        {
            if(file.isFile())
                copy_in_file(file.getPath(),des_path);
            else if(file.isDirectory())
                copy_in_directory(file.getPath(),des_path);
        }
        if(cut_flag==1)
            if(file_res.delete())
                System.out.println(res_path+" 成功删除");
        System.out.println(res_path+" 复制成功到 "+des_path);
        return true;
    }

    private boolean delete_file(String res_path) throws IOException {
        File file=new File(res_path);
        if(!file.exists()) {
            System.out.println("文件不存在");
            return false;
        }
        if(file.delete())
            System.out.println(res_path+" 删除成功");
        return true;
    }

    private boolean delete_dir(String res_path) throws IOException {
        File file_res=new File(res_path);
        if(!file_res.exists())
        {
            System.out.println("文件夹不存在");
            return false;
        }

        File files[]=file_res.listFiles();
        for(File file:files) {
            if (file.isFile())
                delete_file(file.getPath());
            else if (file.isDirectory())
                delete_dir(file.getPath());
        }
        if(file_res.delete())
            System.out.println(res_path+" 成功删除");
        System.out.println(res_path+" 成功删除");
        return true;
    }


    private boolean lock(String res_path) throws IOException {
        File tempFile= File.createTempFile("temp",".txt");
        System.out.println(tempFile.getAbsolutePath());
        FileInputStream fin=new FileInputStream(res_path);
        FileOutputStream fout=new FileOutputStream(tempFile);
        System.out.println(res_path+"开始加密");
        // System.out.println("开始加密");
        System.out.println(res_path);
        System.out.println(tempFile.getAbsolutePath());
        key_ios(fout,fin);
//        int n=fin.available()/5;
//        byte buf[]=new byte[n];
//        int count=0;
//        while((count=fin.read(buf,0,n))!=-1)
//        {
//            for(int i=0;i<count;i++)
//                buf[i]=(byte)(buf[i]^key);
//            fout.write(buf,0,n);
//        }
        fin.close();
        fout.close();
        trans_byte(tempFile.getAbsolutePath(),res_path);
        tempFile.delete();
        System.out.println("完成加密");
        return true;
    }

    private void key_ios(FileOutputStream fileOutputStream,FileInputStream fileInputStream) throws IOException {
        int key=123;
        int n=fileInputStream.available()/5;
        byte []buf=new byte[n];
        int count=0;
        while((count=fileInputStream.read(buf,0,n))!=-1)
        {
            for(int i=0;i<count;i++)
                buf[i]=(byte)(buf[i]^key);
            fileOutputStream.write(buf,0,count);
        }
    }
    private void trans_byte(String res_path,String des_path) throws IOException {
        FileInputStream fileInputStream=new FileInputStream(res_path);
        FileOutputStream fileOutputStream=new FileOutputStream(des_path);
        BufferedInputStream bufferedInputStream=new BufferedInputStream(fileInputStream);
        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
        int n=fileInputStream.available()/5;
        byte buf[]=new byte[n];
        int count=0;
        while((count=bufferedInputStream.read(buf,0,n))!=-1)
        {
            bufferedOutputStream.write(buf,0,count);
        }
        bufferedInputStream.close();
        bufferedOutputStream.close();
        bufferedOutputStream.flush();
        fileInputStream.close();
        fileOutputStream.close();
        fileOutputStream.flush();

    }
    private boolean unlocked(String res_path) throws IOException {
        File tempfile=File.createTempFile("temp",".txt");
        FileInputStream fileInputStream=new FileInputStream(res_path);
        FileOutputStream fileOutputStream=new FileOutputStream(tempfile);
        System.out.println(res_path+"开始解密");
        key_ios(fileOutputStream,fileInputStream);
        trans_byte(tempfile.getAbsolutePath(),res_path);
        fileInputStream.close();
        fileOutputStream.close();
        System.out.println("完成解密");
        tempfile.delete();
        return true;
    }

    public boolean Locked(String res_path)
    {
        try {
            return lock(res_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean UnLocked(String res_path)
    {
        try {
            return unlocked(res_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
