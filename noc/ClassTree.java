package noc;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

public class ClassTree {
    private Vector<Node> nodes;
    private Vector<Node> counter;
    private Node root;

    public ClassTree(){
        root = new Node(new ClassInfo());
        nodes = new Vector<Node>();
        counter = new Vector<Node>();
    }

    public void addClass(ClassInfo c){
        Node n = new Node(c);
        nodes.add(n);
    }

    public void magic(){
        while(nodes.size() > 0){
            Node n = nodes.remove(0);
            String[] s = n.getElement().getKnownSuperclass();
            if(s.length <= 0)
                root.addChildren(n);
            for(int i = 0; i < s.length; i++)
                traversalAdd(n, s[i]);
            counter.add(n);
        }
    }

    public Vector<String> allCounted(){
        Vector<String> out = new Vector<String>();
        out.add("class,noc\n");
        Iterator<Node> it = counter.iterator();
        while(it.hasNext()){
            String s = "";
            Node n = it.next();
            s = n.getElement().getName() + "," + numberOfChildren(n) + "\n";
            out.add(s);
        }
        return out;
    }

    public int numberOfChildren(Node n){
        int output = 0;
        if(n.getChildren() == null)
            return output;
        Vector<Node> ch = n.getChildren();
        output += ch.size();
        Iterator<Node> it = ch.iterator();
        while(it.hasNext()){
            output += numberOfChildren(it.next());
        }
        return output;
    }

    //c = the superclass of toInsert
    private void traversalAdd(Node toInsert,String currentSearchedClass) {
        if (currentSearchedClass.equals((new ClassInfo().getName()))){
            this.root.addChildren(toInsert);
            return;
        }
        Stack<Node> nodeStack = new Stack<>();
        nodeStack.push(root);
        
        while(!nodeStack.empty()) {
            Node currentNode = nodeStack.pop();

            if(currentNode != root && currentNode.getElement().getName().equals(currentSearchedClass)){
                currentNode.addChildren(toInsert);
                return;
            }

            Vector<Node> ch = currentNode.getChildren();
            Iterator<Node> it = ch.iterator();
            while(it.hasNext()){
                nodeStack.push(it.next());
            }
        
        }

        //there is no superclass in the tree, so...

        Iterator<Node> it = nodes.iterator();
        while(it.hasNext()){
            Stack<Node> stack = new Stack<Node>();
            stack.push(it.next());
            Node n = null;
            while(!stack.isEmpty()){
                n = stack.pop();
                Iterator<Node> ite = n.getChildren().iterator();
                while(ite.hasNext()){
                    stack.push(ite.next());
                }
                ClassInfo cc = n.getElement();

                if(cc.getName().equals(currentSearchedClass)){
                    n.addChildren(toInsert);
                    return;
                }
            }
        }

        //if there is no superclass in the vector too we do this:

        root.addChildren(toInsert);
    }
}
