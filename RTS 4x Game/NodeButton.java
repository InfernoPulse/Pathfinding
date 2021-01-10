import javax.swing.JButton;

public class NodeButton extends JButton{
    
    private int nodeID;

    public NodeButton(int nodeID){
        this.nodeID = nodeID;
    }

    public int getNodeID(){
        return this.nodeID;
    }
}
