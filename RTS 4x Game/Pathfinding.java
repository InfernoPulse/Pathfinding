import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

/**TO DO
 * problem with manhattan distance
 * possible problem with the adjacency matrix
 */

public class Pathfinding {
    final int gridSize = 3;
    final int frameSize = 700;

    final Color defColor = Color.BLACK;
    final Color leftColor = Color.WHITE;
    final Color rightColor = Color.RED;
    final Color pathColor = Color.GREEN;

    //true means there is an edge, false means no edge
    boolean[][] adjMatrix = new boolean[gridSize*gridSize][gridSize*gridSize];

    //points of interest that we would like to find the paths of
    LinkedList<Integer> poi = new LinkedList<Integer>();

    public Pathfinding(){
        
        JFrame frame = new JFrame("Pathfinding");
        JPanel grid = new JPanel();
        NodeButton[][] buttons = new NodeButton[gridSize][gridSize];

        JMenuBar menu = new JMenuBar();
        JMenuItem search = new JMenuItem("Search");
        JMenuItem clear = new JMenuItem("Clear");
        JMenuItem fill = new JMenuItem("Fill");
        JMenuItem debug = new JMenuItem("Debug");

        search.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                constuctAdjacenyMatrix(buttons);
                findPOI(buttons);
                // outputPOI();
                // outputAdjacencyMatrix();
                for (int i = 0; i < poi.size(); i++) {
                    NodeButton start = binarySearch(buttons, poi.get(i), gridSize, 0, gridSize, 0, true);
                    for (int j = i + 1; j < poi.size() - i; j++) {
                        NodeButton end = binarySearch(buttons, poi.get(j), gridSize, 0, gridSize, 0, true);
                        LinkedList<NodeButton> path = aStar(start, end, adjMatrix, buttons);
                        for (NodeButton node : path) {
                            if(node.getBackground() != rightColor){
                                node.setBackground(pathColor);
                            }
                        }
                    }
                }
            }
        });

        clear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for (NodeButton[] row : buttons) {
                    for (NodeButton col : row) {
                        col.setBackground(Color.BLACK);
                    }
                }
            }
        });

        fill.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for (NodeButton[] row : buttons) {
                    for (NodeButton col : row) {
                        col.setBackground(Color.WHITE);
                    }
                }
            }
        });

        debug.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int nodeToSearch = 0;
                NodeButton button = binarySearch(buttons, nodeToSearch, gridSize, 0, gridSize, 0, true);
                if(button == null){
                    System.err.println(false);
                }
                else{
                    System.err.println(true);
                }
            }
        });

        menu.add(search);
        menu.add(clear);
        menu.add(fill);
        menu.add(debug);

        //iterator used for the nodeID 
        int iterator = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                //adding buttons and setting the background for each button
                buttons[i][j] = new NodeButton(iterator, i, j);
                buttons[i][j].setBackground(defColor);
                //if you click on a button it will alternate between black and the colour for that mouse click
                buttons[i][j].addMouseListener(new MouseInputAdapter(){

                    public void mousePressed(MouseEvent e){
                        NodeButton button = (NodeButton) e.getSource();
                        //left click is white
                        if(e.getButton() == MouseEvent.BUTTON1){
                            if (button.getBackground() == leftColor) {
                                button.setBackground(defColor);
                            } else {
                                button.setBackground(leftColor);
                            }
                        }
                        //right click is red
                        else if(e.getButton() == MouseEvent.BUTTON3){
                            if (button.getBackground() == rightColor) {
                                button.setBackground(defColor);
                            } else {
                                button.setBackground(rightColor);
                            }
                        }
                    }
                });
                grid.add(buttons[i][j]);
                iterator++;
            }
        }

        frame.setSize(frameSize, frameSize);
        frame.add(menu, BorderLayout.NORTH);
        frame.add(grid);
        
        grid.setLayout(new GridLayout(gridSize, gridSize));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    public void constuctAdjacenyMatrix(NodeButton[][] buttons){
        adjMatrix = new boolean[gridSize*gridSize][gridSize*gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = i; j < gridSize; j++) {
                //if we are currently on a legal node
                if(buttons[i][j].getBackground() != leftColor){
                    //initialising variables to be used
                    int[] rowChecks = {-1, 1}; //check row positions j - 1, j + 1
                    int[] colChecks = {-1, 1}; //check grid positions i - 1, i + 1

                    LinkedList<Integer> edges = new LinkedList<Integer>(); //list of possible edges

                    int gridPos = buttons[i][j].getNodeID();

                    //finding grid positions we can look at
                    if (i == 0){
                        colChecks[0] = 0;
                    }
                    else if (i == gridSize - 1){
                        colChecks[1] = 0;
                    }

                    if (j == 0){
                        rowChecks[0] = 0;
                    }
                    else if (j == gridSize - 1){
                        rowChecks[1] = 0;
                    }

                    //finding legal edges
                    for (int k = 0; k < 2; k++) {
                        //checks if the colour next to the current grid position is black and if it is adds it to the list of edges
                        if(colChecks[k] != 0){
                            if(buttons[i + colChecks[k]][j].getBackground() != leftColor){
                                edges.add(buttons[i + colChecks[k]][j].getNodeID());
                            }
                        }
                        if(rowChecks[k] != 0){
                            if(buttons[i][j + rowChecks[k]].getBackground() != leftColor){
                                edges.add(buttons[i][j + rowChecks[k]].getNodeID());
                            }
                        }
                    }

                    //filling in the adjacency matrix
                    for (Integer edge : edges) {
                        adjMatrix[gridPos][edge] = true;
                        adjMatrix[edge][gridPos] = true;
                    }
                }
                
            }
        }
    }

    public void outputAdjacencyMatrix(){
         //outputting the adjacency matrix for debugging
         for (int k = 0; k < adjMatrix.length; k++) {
            for (int l = 0; l < adjMatrix.length; l++) {
                if(adjMatrix[k][l] == false){
                    System.err.print(0 + " ");
                }
                else{
                    System.err.print(1 + " ");
                }
            }
            System.err.println();
        }
        System.err.println();
    }

    //brute force the 2d array to find where the buttons are red
    public void findPOI(NodeButton[][] buttons){
        poi = new LinkedList<Integer>();
        for (NodeButton[] buttonArray : buttons) {
            for (NodeButton button : buttonArray) {
                if (button.getBackground() == rightColor){
                    poi.add(button.getNodeID());
                }
            }
        }
    }

    public void outputPOI(){
        for (Integer point : poi) {
            System.err.print(point + " ");
        }
        System.err.println("\n");
    }

    public NodeButton binarySearch(NodeButton[][] buttons, int nodeID, int outUpper, int outLower, int inUpper, int inLower, boolean inOuter){
        int outMid = (outUpper + outLower) / 2;
        if(inOuter){
            int buttonID = buttons[outMid][0].getNodeID();

            //if the current button is of the same unit as the nodeID
            if(Math.floorDiv(buttonID, gridSize) == Math.floorDiv(nodeID, gridSize)){
                inOuter =false;
            }
            //if the current button is of greater unit than nodeID
            else if(Math.floorDiv(buttonID, gridSize) > Math.floorDiv(nodeID, gridSize)){
                outUpper = outMid - 1;
            }
            //if the current button is of lower unit than nodeID
            else{
                outLower = outMid + 1;
            }
        }
        else{
            int inMid = (inUpper + inLower) / 2;
            NodeButton button = buttons[outMid][inMid];

            if(button.getNodeID() == nodeID){
                return button;
            }
            else if(button.getNodeID() > nodeID){
                inUpper = inMid - 1;
            }
            else{
                inLower = inMid + 1;
            }
        }

        return binarySearch(buttons, nodeID, outUpper, outLower, inUpper, inLower, inOuter);
    }

    public LinkedList<NodeButton> aStar(NodeButton start, NodeButton end, boolean[][] adjMatrix, NodeButton[][] buttons){
        //discovered nodes to check
        LinkedList<NodeButton> openQueue = new LinkedList<NodeButton>();
        openQueue.add(start);

        //cost to move to current node
        start.setPathValue(0);

        //total cost to move to a node
        start.setHeurPathValue(manhattan(start, end));

        while(openQueue.size() > 0){
            NodeButton currentNode = openQueue.removeFirst();

            if(currentNode.getNodeID() == end.getNodeID()){
                return reconstructPath(currentNode);
            }
            LinkedList<Integer> neighbours = new LinkedList<Integer>();
            for (int i = 0; i < adjMatrix.length; i++) {
                //if the current node is an edge then 
                if(adjMatrix[currentNode.getNodeID()][i]){
                    neighbours.add(i);
                }
            }
            for (Integer neighbour : neighbours) {
                NodeButton neighbourNode = binarySearch(buttons, neighbour, gridSize, 0, gridSize, 0, true);
                int neighbourPathVal = currentNode.getPathValue() + 1;

                if(neighbourNode.getPathValue() == -1 || neighbourPathVal < neighbourNode.getPathValue()){
                    neighbourNode.setPrevNode(currentNode);
                    neighbourNode.setPathValue(neighbourPathVal);
                    neighbourNode.setHeurPathValue(neighbourPathVal + manhattan(neighbourNode, end));
                    System.err.println(neighbourNode.getNodeID() + " " + neighbourNode.getPathValue());

                    if(openQueue.size() == 0){
                        openQueue.add(neighbourNode);
                    }
                    else if(!(openQueue.contains(neighbourNode))){
                        System.err.println();
                        for (int i = 0; i < openQueue.size(); i++) {
                            //if the neighbours heuristic value + path value is greater than the current node then insert the neighbour in front of it
                            if(neighbourNode.getHeurPathValue() < openQueue.get(i).getHeurPathValue()){
                                openQueue.add(i, neighbourNode);
                                break;
                            }
                        }
                    }
                }
            }
            System.err.println("////");
            System.err.println(currentNode.getNodeID());
            for (NodeButton node : openQueue) {
                System.err.println("\t" + node.getNodeID());
            }
            System.err.println("####");

        }
        return null;
    }

    public LinkedList<NodeButton> reconstructPath(NodeButton currentNode){
        LinkedList<NodeButton> totalPath = new LinkedList<NodeButton>();
        totalPath.add(currentNode);
        while(currentNode.getPrevNode() != null){
            currentNode = currentNode.getPrevNode();
            totalPath.add(currentNode);
        }
        return totalPath;
    }

    public int manhattan(NodeButton node, NodeButton end){
        return Math.abs(end.getI() - node.getI()) + Math.abs(end.getJ() - node.getJ());
    }
    
    public static void main(String[] args) {
        Pathfinding program = new Pathfinding();
    }

}