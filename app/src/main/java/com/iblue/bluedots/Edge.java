package com.iblue.bluedots;

public class Edge {
	int v1;	// id of vertex 1
	int v2;	// id of vertex 2
	int id;
	boolean intersection;
	public Edge(int v1, int v2, int id){
		this.v1 = v1;
		this.v2 = v2;
		this.id = id;
		intersection = false;
	}
}
