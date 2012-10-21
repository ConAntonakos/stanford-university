
/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */
 
import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
 
public class NameSurferGraph extends GCanvas
implements NameSurferConstants, ComponentListener {
 
    /* Implementation of the ComponentListener interface */
    public void componentHidden(ComponentEvent e) { }
    public void componentMoved(ComponentEvent e) { }
    public void componentResized(ComponentEvent e) { update(); }
    public void componentShown(ComponentEvent e) { }
 
    /** Instance variables **/
    private ArrayList <NameSurferEntry> entriesArray = new ArrayList<NameSurferEntry>();
 
    /**
     * Creates a new NameSurferGraph object that displays the data.
     */
    public NameSurferGraph() {
        addComponentListener(this);
        drawGraph();
         
    }
 
    /**
     * Clears the list of name surfer entries stored inside this class.
     */
    public void clear() {
        entriesArray.clear();
    }
 
    /* Method: addEntry(entry) */
    /**
     * Adds a new NameSurferEntry to the list of entries on the display.
     * Note that this method does not actually draw the graph, but
     * simply stores the entry; the graph is drawn by calling update.
     */
    public void addEntry(NameSurferEntry entry) {
        entriesArray.add(entry);
    }
 
 
 
    /**
     * Updates the display image by deleting all the graphical objects
     * from the canvas and then reassembling the display according to
     * the list of entries. Your application must call update after
     * calling either clear or addEntry; update is also called whenever
     * the size of the canvas changes.
     */
    public void update() {
        removeAll();
        drawGraph();
        if(entriesArray.size() >= 0) {
            for(int i = 0; i < entriesArray.size(); i++) {
                NameSurferEntry entries = entriesArray.get(i); 
                drawEntry(entries, i);
            }
        }
    }
 
    private void drawGraph() {
        drawVerticalLines();
        drawHorizontalLines();
        drawDecadeLabels(); 
    }
 
    //Draws the vertical lines in the graph
    private void drawVerticalLines() {
        for(int i = 0; i < NDECADES; i++) {
            double startY = 0;
            double maxY = getHeight();
            double x = i * (getWidth()/NDECADES);
            GLine line = new GLine(x, startY, x, maxY);
            add(line);
        }
    }
 
    //Draws the horizontal lines in the graph
    private void drawHorizontalLines() {
        double startX = 0;
        double maxX = getWidth();
        double yLineBottom = getHeight() - GRAPH_MARGIN_SIZE;
        GLine lineBottom = new GLine(startX, yLineBottom, maxX, yLineBottom);
        add(lineBottom);
        double yLineTop = GRAPH_MARGIN_SIZE;
        GLine lineTop = new GLine(startX, yLineTop, maxX, yLineTop);
        add(lineTop);
    }
 
    //Draws the decade labels
    private void drawDecadeLabels() {
        for(int i = 0; i < NDECADES; i++) {
            int decade = START_DECADE;
            decade += 10*i;
            String label = Integer.toString(decade);
            double y = getHeight() - GRAPH_MARGIN_SIZE/5;
            double x = 3 + i * (getWidth()/NDECADES);
            GLabel decadeDisplay = new GLabel(label, x, y);
            add(decadeDisplay);
        }
    }
 
    //Draws the graph line with the name and rank number labels
    private void drawEntry(NameSurferEntry entry, int entryNumber) {
        //Draws the graph line
        for(int i = 0; i < NDECADES - 1; i++) {
        	//Get the one ranking, followed by the subsequent
            int ranking1 = entry.getRank(i);
            int ranking2 = entry.getRank(i+1);
            
            //Get their respective x and y coordinates on the vertical lines in order to draw the endpoints of the line
            double x1 = i * (getWidth()/NDECADES);
            double x2 = (i+1) * (getWidth()/NDECADES);
            double y1 = 0;
            double y2 = 0;
            
            //Consider the margin when the rankings are greater than 0 and when they are not
            if(ranking1 != 0 && ranking2 != 0) {
                y1 = GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * ranking1/MAX_RANK;
                y2 = GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * ranking2/MAX_RANK;
            } else if(ranking1 == 0 && ranking2 == 0) {
                y1 = getHeight() - GRAPH_MARGIN_SIZE;
                y2 = getHeight() - GRAPH_MARGIN_SIZE;
            } else if (ranking1 == 0){
                y1 = getHeight() - GRAPH_MARGIN_SIZE;
                y2 = GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * ranking2/MAX_RANK;
            } else if(ranking2 == 0) {
                y1 = GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * ranking1/MAX_RANK;
                y2 = getHeight() - GRAPH_MARGIN_SIZE;
            }
            GLine line = new GLine(x1, y1, x2, y2);
            //Change the colors for distinction between entry names and respective rankings
            if(entryNumber%4 == 1) {
                line.setColor(Color.RED);
            } else if(entryNumber%4 == 2) {
                line.setColor(Color.BLUE);
            } else if(entryNumber%4 == 3) {
                line.setColor(Color.MAGENTA);
            }
            add(line);
        }
        //Adds in the labels with the entry name and respective rankings per decade
        for(int i = 0; i < NDECADES; i++) {
            String name = entry.getName();
            int rank = entry.getRank(i);
            String rankString = Integer.toString(rank);
            String label = name + " " + rankString;
            double x = i * (getWidth()/NDECADES) + 5;
            double y = 0;
            if(rank != 0) {
                y = GRAPH_MARGIN_SIZE + (getHeight() - GRAPH_MARGIN_SIZE*2) * rank/MAX_RANK - 5;
            } else {
                label = name + " *";
                y = getHeight() - GRAPH_MARGIN_SIZE - 5;
            }
            GLabel nameLabel = new GLabel(label, x, y);
            if(entryNumber%4 == 1) {
                nameLabel.setColor(Color.RED);
            } else if(entryNumber%4 == 2) {
                nameLabel.setColor(Color.BLUE);
            } else if(entryNumber%4 == 3) {
                nameLabel.setColor(Color.MAGENTA);
            }
            add(nameLabel);
        }
    }
}
    