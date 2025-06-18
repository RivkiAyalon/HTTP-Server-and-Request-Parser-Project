package test;

import java.util.ArrayList;
import java.util.List;



public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;
    
    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.msg = null;
    }
    
	public Message getMsg() {
		return msg;
	}
	public void setMsg(Message msg) {
		this.msg = msg;
	}
	public List<Node> getEdges() {
		return edges;
	}
	public void setEdges(List<Node> edges) {
		this.edges = edges;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void addEdge(Node node) {
        if (!edges.contains(node)) {
            edges.add(node);
        }
    }

	// בדיקת קיום מעגלים
    public boolean hasCycles() {
        return hasCyclesHelper(new ArrayList<>());
    }

    // מתודת עזר רקורסיבית
    private boolean hasCyclesHelper(List<Node> visited) {
        if (visited.contains(this)) {
            return true; // מעגל נמצא
        }

        visited.add(this);
        for (Node neighbor : edges) {
            if (neighbor.hasCyclesHelper(new ArrayList<>(visited))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Node(" + name + ")";
    }

}