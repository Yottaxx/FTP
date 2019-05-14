package FUNC;//文件管理系统
//        1、基本文件管理功能：
//        a.文件夹和文件的创建、删除；
//        b.当前文件夹下的内容罗列；
//        c.文件、文件夹的拷贝和粘贴（文件夹拷贝指深度拷贝，包括所有子目录和文件）；
//        d.指定文件的加密和解密。（加密解密流）

import GUI.FILE;
import GUI.TreeNode;

import java.io.*;
import java.util.Scanner;

public class FILE_GUI_FUN {

    TreeNode treeNode;
    public FILE_GUI_FUN(TreeNode treeNode)
    {
        this.treeNode=treeNode;
    }
    public  FILE_GUI_FUN(){};

    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public boolean List(String res_path)
    {
        try {
            return lists(res_path,this.treeNode);
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }

    private boolean lists(String res_path,TreeNode treeNode) throws IOException {
        File file=new File(res_path);
        if(!file.exists()) {
            System.out.println("文件不存在");
            return false;
        }
        else if(file.isFile())
        {
            //System.out.println(file.getPath());
            TreeNode temp=new TreeNode(file.getPath().substring(file.getPath().lastIndexOf(File.separator)+1),
                    file.getPath());
            treeNode.add(temp);
            return true;
        }
        else if(file.isDirectory())
        {

            TreeNode temp=new TreeNode(file.getPath().substring(file.getPath().lastIndexOf(File.separator)+1),
                    file.getPath());
            treeNode.add(temp);
           // System.out.println(file.getPath());
            return list_dir(res_path,"..",temp);
        }
        System.out.println("意外的错误，文件打开失败");
        return false;
    }

    private boolean list_dir(String res_path,String addition,TreeNode node) throws IOException {
        File file_res = new File(res_path);
        File files[] = file_res.listFiles();
        for (File file : files) {
            if(!file.isHidden()) {
                if (file.isFile()) {
                    {
                        //System.out.println(addition + file.getPath());
                        TreeNode temp =
                                new TreeNode(file.getPath().substring(file.getPath().lastIndexOf(File.separator) + 1),
                                        file.getPath());
                        node.add(temp);
                    }
                } else if (file.isDirectory()) {
                    TreeNode temp =
                            new TreeNode(file.getPath().substring(file.getPath().lastIndexOf(File.separator) + 1),
                                    file.getPath());
                    node.add(temp);
                    //System.out.println(addition + file.getPath());
                    list_dir(file.getPath(), addition + "..", temp);
                }
            }
        }
        return true;
    }




}
