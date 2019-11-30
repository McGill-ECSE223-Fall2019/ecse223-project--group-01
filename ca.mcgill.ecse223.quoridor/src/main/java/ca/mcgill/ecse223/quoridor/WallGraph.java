package ca.mcgill.ecse223.quoridor;

import java.util.Iterator;
import java.util.LinkedList;

public class WallGraph {

    private int V = 81;


    // array of linked lists
    private LinkedList<Integer> adj[];

    public WallGraph(){
        this.adj = new LinkedList[81];
        for(int i = 0; i<81 ;i++){
            this.adj[i] = new LinkedList<>();
        }
    }

    public void addEdge(int v, int w){
        this.adj[v].add(w);
    }

    public void addEdgeUndirected(int v, int w){
        this.adj[v].add(w);
        this.adj[w].add(v);
    }

    public void cutEdge(int v, int w){
        this.adj[v].removeFirstOccurrence(w);
        this.adj[w].removeFirstOccurrence(v);
    }


    public boolean reachesDest(int s, int dest_row,int dest_col)
    {
        int row,col = 0;
        // Mark all the vertices as not visited
        boolean visited[] = new boolean[V];

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark the current node as visited and enqueue it
        visited[s]=true;
        queue.add(s);
        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            s = queue.poll();
            col = s%9+1;
            row = s/9 +1;

            if(row==dest_row || col== dest_col){
                return true;
            }
            Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext())
            {
                int n = i.next();
                if (!visited[n])
                {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
        return false;
    }
}
