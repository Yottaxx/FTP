package GUI;

import FUNC.FILE_GUI_FUN;
import Socket.Client;
import Socket.Server;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.rotateLeft;

public class FILE {
    private JPanel panel1;
    private JTree tree1;
    private JTree tree2;
    private JButton 返回上一级Button1;
    private JButton 返回上一级Button;
    private JTextField textField1;
    private JTextField textField2;
    private JButton 本地上传Button;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JTextField ip;
    private JTextField port;
    private JButton 连接Button;
    private JButton 断开连接Button;
    private TreeNode top;
    private TreeNode top2;
    private TreeNode tempnode=new TreeNode("root");
    private TreeNode tempnode2=new TreeNode("root");
    private FILE_GUI_FUN file_gui_fun;
    private JPopupMenu menu;
    private Client client=new Client();
    private Server server=new Server();
    private String tree2_path;
    private String tree1_path;
    private String copy_path;
    private String paste_path;
    public FILE() {


        返回上一级Button.setEnabled(false);
        返回上一级Button1.setEnabled(false);
        tree2.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreeNode node = (TreeNode) tree2.getLastSelectedPathComponent();
                if(node!=null){
              textField1.setText(node.get_File_Path());
              if(node.getName().equals("root"))
                {
                    返回上一级Button.setEnabled(true);
                    TreeNode temp=(TreeNode) top2.getFirstChild();
                    String new_path=temp.get_File_Path().substring(0,
                            temp.get_File_Path().lastIndexOf(File.separator));
                   // System.out.println("old path"+new_path);
                    //System.out.println("new path"+new_path.substring(new_path.lastIndexOf(File.separator)+1));
                    if(new_path.charAt(new_path.length()-1)==':')
                        new_path=new_path+"\\";
                    tempnode=new TreeNode(new_path.substring(new_path.lastIndexOf(File.separator)+1),new_path);
                }else {
                  返回上一级Button.setEnabled(false);
                  tree2_path=node.get_File_Path();

                  TreeNode temp=(TreeNode) top2.getFirstChild();
                  String new_path=temp.get_File_Path();
                  tempnode=new TreeNode(new_path.substring(new_path.lastIndexOf(File.separator)+1),new_path);
              }
            }
            }
        });




        返回上一级Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });


        返回上一级Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint2();
            }
        });


        tree1.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreeNode node = (TreeNode) tree1.getLastSelectedPathComponent();
                if(node!=null){
                    textField2.setText(node.get_File_Path());
                    if(node.getName().equals("root"))
                    {
                        返回上一级Button1.setEnabled(true);
                        TreeNode temp=(TreeNode) top.getFirstChild();
                        String new_path=temp.get_File_Path().substring(0,
                                temp.get_File_Path().lastIndexOf(File.separator));
                        if(new_path.charAt(new_path.length()-1)==':')
                            new_path=new_path+"\\";
                        System.out.println("old path"+new_path);
                        System.out.println("new path"+new_path.substring(new_path.lastIndexOf(File.separator)+1));
                        tempnode2=new TreeNode(new_path.substring(new_path.lastIndexOf(File.separator)+1),new_path);
                    }
                    else {
                        返回上一级Button1.setEnabled(false);
                        tree1_path=node.get_File_Path();

                        TreeNode temp=(TreeNode) top.getFirstChild();
                        String new_path=temp.get_File_Path();
                        tempnode2=new TreeNode(new_path.substring(new_path.lastIndexOf(File.separator)+1),new_path);
                    }
                }
            }

        });

        tree2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showmenu(e);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                showmenu(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                showmenu(e);

            }
        });

        连接Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    if(!server.isStatus())
                        server.main();
                }).start();
                new Thread(() -> {
                    try {
                        if (!client.connect(ip.getText(), parseInt(port.getText()))) {
                            ;
                            String temp = "ip或port错误";
                            ip.setText(temp);
                            port.setText(temp);
                        }
                        else
                        {
                            file_gui_fun.setTreeNode(top2);
                            TreeNode temp=(TreeNode) top2.getFirstChild();
                            server.init(file_gui_fun,"F:\\");
                            String new_path="F:\\";
                            tempnode=new TreeNode("F","F:\\");
                            repaint();
                            tree1.setVisible(true);
                            tree2.setVisible(true);
                        }
                    }
                    catch (NumberFormatException e1)
                    {
                        ip.setText("格式错误");
                        port.setText("格式错误");
                    }
                }).start();
            }
        });
        断开连接Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(client.isStatus())
                    client.disconnect();
                tree2.setVisible(false);
                tree1.setVisible(false);
            }
        });
        本地上传Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.sendFile(tree1_path,"SENDFILE");
                    renovate_tree2();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
    }

    public void showmenu(MouseEvent e)
    {
        if(e.isPopupTrigger())
        {
                //panel1.add(menu);
                menu.show(e.getComponent(),e.getX(),e.getY());
        }
    }

    public void renovate_tree2()
    {
        TreeNode temp=(TreeNode) top2.getFirstChild();
        String new_path=temp.get_File_Path();
        tempnode=new TreeNode(new_path.substring(new_path.lastIndexOf(File.separator)+1),new_path);
        repaint();
    }
    public void repaint()
    {

        DefaultTreeModel treeModel= (DefaultTreeModel) tree2.getModel();
        // 下面一行，由DefaultTreeModel的getRoot()方法取得根节点.
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel
                .getRoot();

        // 下面一行删除所有子节点.
        rootNode.removeAllChildren();

        // 删除完后务必运行DefaultTreeModel的reload()操作，整个Tree的节点才会真正被删除.
        treeModel.reload();
        file_gui_fun.setTreeNode(top2);
        System.out.println("新路径"+tempnode.get_File_Path());
        file_gui_fun.List(tempnode.get_File_Path());
        TreeNode newNode = new TreeNode("root");
        treeModel.insertNodeInto(tempnode,newNode,tempnode.getChildCount());
//                top2=tempnode;
//
//                //treeModel.nodeStructureChanged(top2);
//                //treeModel.setRoot(top2);
//                //file_gui_fun.List(top2.get_File_Path());
//                treeModel.nodeStructureChanged(top2);
//                tree2.repaint();
      System.out.println("sdf:"+tempnode.get_File_Path()+"sdfsdf"+tempnode.getUserObject().toString());
    }
    public void repaint2()
    {

        System.out.println("新路径"+tempnode2.get_File_Path());
        DefaultTreeModel treeModel= (DefaultTreeModel) tree1.getModel();
        // 下面一行，由DefaultTreeModel的getRoot()方法取得根节点.
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel
                .getRoot();

        // 下面一行删除所有子节点.
        rootNode.removeAllChildren();
        file_gui_fun.setTreeNode(top);
        // 删除完后务必运行DefaultTreeModel的reload()操作，整个Tree的节点才会真正被删除.
        treeModel.reload();
        file_gui_fun.List(tempnode2.get_File_Path());
        TreeNode newNode = new TreeNode("root");
        treeModel.insertNodeInto(tempnode2,newNode,tempnode2.getChildCount());
       System.out.println("sdf:"+top2.get_File_Path()+"sdfsdf:"+top2.getUserObject().toString());
    }


    public static void main(String[] args) throws UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("远程文件管理系统中点击右键出现选择菜单");
        System.out.println("点击root时才能返回上一层");
        UIManager.setLookAndFeel(new NimbusLookAndFeel());//设置一个非常漂亮的外观
       // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        JFrame frame = new JFrame("远程文件管理系统——v1.1");
        frame.setContentPane(new FILE().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        top=new TreeNode("root");
        file_gui_fun=new FILE_GUI_FUN(top);
        file_gui_fun.List("G:\\");
        top2=new TreeNode("root");
        file_gui_fun.setTreeNode(top2);
       // server.init(file_gui_fun,"F:\\");
        file_gui_fun.List("F:\\");
        tree1=new JTree(top);
        tree2=new JTree(top2);
        tree1.setEditable(true);
        tree2.setEditable(true);
        tree1.setVisible(false);
        tree2.setVisible(false);
        scrollPane1=new JScrollPane(tree1);
        scrollPane2=new JScrollPane(tree2);
        Right_Mouse();

    }

    public void Right_Mouse(){
        menu=new JPopupMenu();
       JMenuItem delete,copy,cut,paste,lock,unlock;
       delete=new JMenuItem("删除");
       copy=new JMenuItem("拷贝");
       cut=new JMenuItem("剪切");
       paste=new JMenuItem("粘贴");
        lock=new JMenuItem("加密");
        unlock=new JMenuItem("解密");
        menu.add(delete);
        menu.add(copy);
        menu.add(cut);
        menu.add(paste);
        menu.add(lock);
        menu.add(unlock);
        paste.setEnabled(false);
       delete.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               client.send("DELETE");
               client.send(tree2_path);
               System.out.println("tree2:"+tree2_path);
               repaint();
               System.out.println("删除");
           }
       });
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy_path=tree2_path;
                System.out.println("复制");
                paste.setEnabled(true);
            }
        });
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!copy_path.equals(""))
                {
                client.send("CUTPASTE");
                client.send(copy_path);
                client.send(tree2_path);
                copy_path="";
                System.out.println("粘贴");
                repaint();
                paste.setEnabled(false);
                }
            }
        });
        lock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.send("LOCK");
                client.send(tree2_path);
                System.out.println("tree2:"+tree2_path);
                repaint();
                System.out.println("加密");
            }
        });
        unlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.send("UNLOCK");
                client.send(tree2_path);
                System.out.println("tree2:"+tree2_path);
                repaint();
                System.out.println("解密");
            }
        });
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy_path=tree2_path;
                System.out.println("剪切");
                paste.setEnabled(true);
            }
        });
    }


}
