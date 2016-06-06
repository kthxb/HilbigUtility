package com.ash.test;

import java.util.List;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.ThetaStarGridFinder;

public class PathfinderTest {
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		GridCell[][] cells = new GridCell[5][5];
		/*int y = 0;
		int x = 0;
		for(GridCell[] row : cells){
			for(GridCell c : row){
				System.out.println("Init " + x + " " + y);
				c = new GridCell(x,y,true);
				x++;
			}
			y++;
			x=0;
		}*/
		
		for(int y=0; y<5;y++)
			for(int x=0; x<5; x++)
				cells[x][y] = new GridCell();
		
		
		NavigationGrid<GridCell> navGrid = new NavigationGrid(cells, true);
		AStarGridFinder<GridCell> finder = new AStarGridFinder(GridCell.class);
		List<GridCell> pathToEnd = finder.findPath(0, 0, 1, 1, navGrid);
		System.out.println(pathToEnd);
	}

}
