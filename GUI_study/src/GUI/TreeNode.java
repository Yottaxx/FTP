package GUI;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNode extends DefaultMutableTreeNode {
    String path;
    String name;
    public TreeNode(String name)
    {
        super(name);
        this.name=name;
    }
    public TreeNode (String name,String path)
    {
        super(name);
        this.path=path;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_File_Path() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
