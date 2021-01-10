import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

/**TO DO
 * change 2d buttons array to a normal array
 * add attributes to NodeButton class so we can perform a* on it
 * add astar algorithm
 */

public class Pathfinding {
    final int gridSize = 3;
    final int frameSize = 700;

    final Color defColor = Color.BLACK;
    final Color leftColor = Color.WHITE;
    final Color rightColor = Color.RED;

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

        search.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                constuctAdjacenyMatrix(buttons);
                findPOI(buttons);
                outputPOI();
                //outputAdjacencyMatrix();
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

        menu.add(search);
        menu.add(clear);
        menu.add(fill);

        //iterator used for the nodeID 
        int iterator = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                //adding buttons and setting the background for each button
                buttons[i][j] = new NodeButton(iterator);
                buttons[i][j].setBackground(Color.BLACK);
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
                if(buttons[i][j].getBackground() == Color.BLACK){
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
                            if(buttons[i + colChecks[k]][j].getBackground() == Color.BLACK){
                                edges.add(buttons[i + colChecks[k]][j].getNodeID());
                            }
                        }
                        if(rowChecks[k] != 0){
                            if(buttons[i][j + rowChecks[k]].getBackground() == Color.BLACK){
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
    
    public static void main(String[] args) {
        Pathfinding program = new Pathfinding();
    }

}