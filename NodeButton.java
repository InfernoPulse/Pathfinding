import javax.swing.JButton;

public class NodeButton extends JButton{
    
    private int nodeID;
    //boolean array ij, buttons[i][j]
    private int i;
    private int j;
    
    private int pathValue;
    private int heurPathValue;
    private NodeButton prevNode;

    public NodeButton(int nodeID, int i, int j){
        this.nodeID = nodeID;
        this.i = i;
        this.j = j;
        this.pathValue = -1;
        this.heurPathValue = -1;
    }

    public int getNodeID(){
        return this.nodeID;
    }

    public int getI(){
        return this.i;
    }

    public int getJ(){
        return this.j;
    }

    public int getPathValue(){
        return this.pathValue;
    }

    public int getHeurPathValue(){
        return this.heurPathValue;
    }

    public NodeButton getPrevNode(){
        return this.prevNode;
    }

    public void setPathValue(int pathValue){
        this.pathValue = pathValue;
    }

    public void setHeurPathValue(int heurPathValue){
        this.heurPathValue = heurPathValue;
    }

    public void setPrevNode(NodeButton prevNode){
        this.prevNode = prevNode;
    }

    public void reset(){
        this.prevNode = null;
        this.pathValue = -1;
        this.heurPathValue = -1;
    }
}
