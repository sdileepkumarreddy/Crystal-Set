package edu.neu.csye6200.ca;

import java.awt.Polygon;
import java.util.function.BiConsumer;

/*
 * @author: Open source
 * Class Description: This class is used to create uniform hexagonal grid 
 */

public class HexagonCell extends Polygon {

	private static final long serialVersionUID = 1L;

	public HexagonCell() {
		
	}
	 private static final double H = Math.sqrt(3) / 2;

	    static class Hexagon {
	      final int row;
	      final int col;
	      final double sideLength;
	      public Hexagon(int r, int c, double a) {
	        this.row = r;
	        this.col = c;
	        this.sideLength = a;
	      }

	      double getCenterX() {
	        return 2 * H * sideLength * (col + (row % 2) * 0.5);
	      }

	      double getCenterY() {
	        return 3 * sideLength / 2  * row;
	      }

	      void foreachVertex(BiConsumer<Double, Double> f) {
	        double cx = getCenterX();
	        double cy = getCenterY();
	        f.accept(cx + 0, cy + sideLength);
	        f.accept(cx - H * sideLength, cy + 0.5 * sideLength);
	        f.accept(cx - H * sideLength, cy - 0.5 * sideLength);
	        f.accept(cx + 0, cy - sideLength);
	        f.accept(cx + H * sideLength, cy - 0.5 * sideLength);
	        f.accept(cx + H * sideLength, cy + 0.5 * sideLength);
	      }
	    }

}
