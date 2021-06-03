package noc;
import java.util.Vector;

public class Node {
    private ClassInfo element;
    private Vector<Node> children;

    public Node(ClassInfo e){
        element = e;
        children = new Vector<Node>();
    }

    public void setElement(ClassInfo e){
        element = e;
    }

    public void addChildren(Node c){
        children.add(c);
    }

    public ClassInfo getElement(){
        return element;
    }

    public Vector<Node> getChildren(){
        return children;
    }
}
