package com.ash.util.math;

import java.util.ArrayList;

public abstract class Node {
	
	public ArrayList<Node> connections;
	
	public void connect(Node n){
		if(connections == null) connections = new ArrayList<Node>();
		if(!connections.contains(n)) connections.add(n);
		if(!n.connections.contains(this)) n.connections.add(this);
	}

}
