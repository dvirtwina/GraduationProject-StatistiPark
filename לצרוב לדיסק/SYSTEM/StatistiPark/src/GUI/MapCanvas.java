package GUI;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class MapCanvas extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Color availableSpotClr = Color.GREEN;
	private final Color occupiedSpotClr = Color.RED;
	
	private int numOfParkingSpots;
	private ArrayList<String> occupiedParkingSpots;
	
	private int startXoffset;
	private int startYoffset;
	
	/**Creates a map of the parking lot. params startXoffset and startYoffset are irrelevant when drawing a parking lot of 50 parking spots.*/
	public MapCanvas(int numOfParkingSpots, ArrayList<String> occupiedParkingSpots, int startXoffset, int startYoffset) {
		this.numOfParkingSpots = numOfParkingSpots;
		this.occupiedParkingSpots = occupiedParkingSpots;
		this.startXoffset = startXoffset;
		this.startYoffset = startYoffset;
	}

	@Override
	public void paint(Graphics g) {
		if (numOfParkingSpots == 5) {
			int pStartX = 10;
			int pStartY = 10;
			int pParkingSpotWidth = 50;
			int pParkingSpotHeight = 100;
			
			for (int i = 0; i<numOfParkingSpots; i++) {
				if (occupiedParkingSpots.contains("ParkingSpot"+(i+1))) {
					g.setColor(occupiedSpotClr);
				}
				else {
					g.setColor(availableSpotClr);
				}
				g.fillRect(pStartX+i*pParkingSpotWidth, pStartY, pParkingSpotWidth, pParkingSpotHeight);
				g.setColor(Color.BLACK);
				g.drawRect(pStartX+i*pParkingSpotWidth, pStartY, pParkingSpotWidth, pParkingSpotHeight);
				g.drawString(""+(i+1), pStartX+(pParkingSpotWidth/2)+(i*pParkingSpotWidth), pStartY+(pParkingSpotHeight/4));
//				Image h = new ImageIcon("HandicappedParking.png").getImage();
//				g.drawImage(h, 20, 60, this);
			}
			
		} else if (numOfParkingSpots == 50) {
			int perimeterWidth = 400;
			int perimeterHeight = 350;
			int cornerEdge = 50;
			
			int PSheight_standing = 50;
			int PSwidth_standing = 25;
			
			int PSheight_lying = 25;
			int PSwidth_lying = 50;

			int gateWidth = 4 * PSwidth_standing;
			
			int numOfPSatTheSides = 10;
			int numOfPSatTheSouth = 12;
			int numOfPSatTheNorthLeft = 4;
			int numOfPSatTheNorthRight = 4;
			int numOfPSatTheMiddleLeft = 5;
			int numOfPSatTheMiddleRight = 5;
			
			int middleXoffset = startXoffset + cornerEdge + (perimeterWidth - 4*PSwidth_lying)/2;
			int middleYoffset = startYoffset + cornerEdge + (((perimeterHeight - 2*cornerEdge) - (numOfPSatTheMiddleLeft*PSheight_lying))/2);
			
			Graphics2D g2 = (Graphics2D) g;
			
			int legendXoffset = 5;
			int legendYoffset = 15;
			int legendSpacing = 5;
			
			// Legend:
			g.drawString("Legend:", legendXoffset, legendYoffset);
			
			g.setColor(availableSpotClr);
			g.fillRect(legendXoffset, legendYoffset + legendSpacing, PSwidth_lying, PSheight_lying);
			g.setColor(Color.BLACK);
			g.drawRect(legendXoffset, legendYoffset + legendSpacing, PSwidth_lying, PSheight_lying);
			g.drawString("Free", legendXoffset + PSwidth_lying + legendSpacing, legendYoffset + legendSpacing + PSheight_lying/2);
			
			g.setColor(occupiedSpotClr);
			g.fillRect(legendXoffset, PSheight_lying + legendSpacing + legendYoffset + legendSpacing, PSwidth_lying, PSheight_lying);
			g.setColor(Color.BLACK);
			g.drawRect(legendXoffset, PSheight_lying + legendSpacing + legendYoffset + legendSpacing, PSwidth_lying, PSheight_lying);
			g.drawString("Occupied", legendXoffset + PSwidth_lying + legendSpacing, PSheight_lying + legendSpacing + legendYoffset + legendSpacing + PSheight_lying/2);
			
			// PL perimeter:
			g.setColor(Color.BLACK);
			g.drawRect(startXoffset, startYoffset, perimeterWidth, perimeterHeight);
			
			// Draw corners:
			g.setColor(Color.BLACK); // N/W
			g.drawRect(startXoffset, startYoffset, cornerEdge, cornerEdge);
			
			g.setColor(Color.BLACK); // W/S
			g.drawRect(startXoffset, startYoffset + cornerEdge + (PSheight_lying*numOfPSatTheSides), cornerEdge, cornerEdge);
			
			g.setColor(Color.BLACK); // S/E
			g.drawRect(startXoffset + cornerEdge + (PSwidth_standing*numOfPSatTheSouth), startYoffset + cornerEdge + (PSheight_lying*numOfPSatTheSides), cornerEdge, cornerEdge);
			
			g.setColor(Color.BLACK); // E/N
			g.drawRect(startXoffset + cornerEdge + (PSwidth_standing*numOfPSatTheSouth), startYoffset, cornerEdge, cornerEdge);
			
			
			// Draw parking spots:
			int ps = 1;
			// West wing:
			for (int i = 0;i<numOfPSatTheSides; ps++, i++) {
				if (occupiedParkingSpots.contains("ParkingSpot"+ps)) {
					g.setColor(occupiedSpotClr);
				}
				else {
					g.setColor(availableSpotClr);
				}
				g.fillRect(startXoffset, startYoffset + cornerEdge + i*PSheight_lying, PSwidth_lying, PSheight_lying);
				g.setColor(Color.BLACK);
				g.drawRect(startXoffset, startYoffset + cornerEdge + i*PSheight_lying, PSwidth_lying, PSheight_lying);
				g.drawString(""+ps, startXoffset + (PSwidth_lying/2), startYoffset + cornerEdge + i*PSheight_lying + (PSheight_lying/2));
			}
			
			// East wing:
			for (int i = 0;i<numOfPSatTheSides; ps++, i++) {
				if (occupiedParkingSpots.contains("ParkingSpot"+ps)) {
					g.setColor(occupiedSpotClr);
				}
				else {
					g.setColor(availableSpotClr);
				}
				g.fillRect(startXoffset + cornerEdge + numOfPSatTheSouth*PSwidth_standing, startYoffset + cornerEdge + i*PSheight_lying, PSwidth_lying, PSheight_lying);
				g.setColor(Color.BLACK);
				g.drawRect(startXoffset + cornerEdge + numOfPSatTheSouth*PSwidth_standing, startYoffset + cornerEdge + i*PSheight_lying, PSwidth_lying, PSheight_lying);
				g.drawString(""+ps, startXoffset + cornerEdge + numOfPSatTheSouth*PSwidth_standing + (PSwidth_lying/2), startYoffset + cornerEdge + i*PSheight_lying + (PSheight_lying/2));
			}
			
			// South wing:
			for (int i = 0;i<numOfPSatTheSouth; ps++, i++) {
				if (occupiedParkingSpots.contains("ParkingSpot"+ps)) {
					g.setColor(occupiedSpotClr);
				}
				else {
					g.setColor(availableSpotClr);
				}
				g.fillRect(startXoffset + cornerEdge + i*PSwidth_standing, startYoffset + cornerEdge + numOfPSatTheSides*PSheight_lying, PSwidth_standing, PSheight_standing);
				g.setColor(Color.BLACK);
				g.drawRect(startXoffset + cornerEdge + i*PSwidth_standing, startYoffset + cornerEdge + numOfPSatTheSides*PSheight_lying, PSwidth_standing, PSheight_standing);
				g.drawString(""+ps, startXoffset + cornerEdge + ((i*PSwidth_standing) + (PSwidth_standing/2)), startYoffset + cornerEdge + numOfPSatTheSides*PSheight_lying + (PSheight_standing/2));
			}
			
			// North wing:
			for (int i = 0;i<numOfPSatTheNorthLeft; ps++, i++) { // North-Left 
				if (occupiedParkingSpots.contains("ParkingSpot"+ps)) {
					g.setColor(occupiedSpotClr);
				}
				else {
					g.setColor(availableSpotClr);
				}
				g.fillRect(startXoffset + cornerEdge + i*PSwidth_standing, startYoffset, PSwidth_standing, PSheight_standing);
				g.setColor(Color.BLACK);
				g.drawRect(startXoffset + cornerEdge + i*PSwidth_standing, startYoffset, PSwidth_standing, PSheight_standing);
				g.drawString(""+ps, startXoffset + cornerEdge + ((i*PSwidth_standing) + (PSwidth_standing/2)), startYoffset + (PSheight_standing/2));
			}
			g.setColor(Color.ORANGE); // Gate
            g2.setStroke(new BasicStroke(5));
            g2.draw(new Line2D.Float(startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing +3/*this addition is for fine tuning. it is not a part of the raw calculation*/, startYoffset, startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing + gateWidth -3/*this subtraction is for fine tuning. it is not a part of the raw calculation*/, startYoffset));
            g2.setStroke(new BasicStroke(1));
			for (int i = 0;i<numOfPSatTheNorthRight; ps++, i++) { // North-Right 
				if (occupiedParkingSpots.contains("ParkingSpot"+ps)) {
					g.setColor(occupiedSpotClr);
				}
				else {
					g.setColor(availableSpotClr);
				}
				g.fillRect(startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing + gateWidth + i*PSwidth_standing, startYoffset, PSwidth_standing, PSheight_standing);
				g.setColor(Color.BLACK);
				g.drawRect(startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing + gateWidth + i*PSwidth_standing, startYoffset, PSwidth_standing, PSheight_standing);
				g.drawString(""+ps, startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing + gateWidth + ((i*PSwidth_standing) + (PSwidth_standing/2)), startYoffset + (PSheight_standing/2));
			}
			
			// Middle:
			for (int i = 0; i<numOfPSatTheMiddleLeft; ps++, i++) { // Middle-Left
				if (occupiedParkingSpots.contains("ParkingSpot"+ps)) {
					g.setColor(occupiedSpotClr);
				}
				else {
					g.setColor(availableSpotClr);
				}
				g.fillRect(middleXoffset, middleYoffset + i*PSheight_lying, PSwidth_lying, PSheight_lying);
				g.setColor(Color.BLACK);
				g.drawRect(middleXoffset, middleYoffset + i*PSheight_lying, PSwidth_lying, PSheight_lying);
				g.drawString(""+ps, middleXoffset + (PSwidth_lying/2), middleYoffset + i*PSheight_lying + (PSheight_lying/2));
			}
			for (int i = 0; i<numOfPSatTheMiddleRight; ps++, i++) { // Middle-Right
				if (occupiedParkingSpots.contains("ParkingSpot"+ps)) {
					g.setColor(occupiedSpotClr);
				}
				else {
					g.setColor(availableSpotClr);
				}
				g.fillRect(middleXoffset + PSwidth_lying, middleYoffset + i*PSheight_lying, PSwidth_lying, PSheight_lying);
				g.setColor(Color.BLACK);
				g.drawRect(middleXoffset + PSwidth_lying, middleYoffset + i*PSheight_lying, PSwidth_lying, PSheight_lying);
				g.drawString(""+ps, middleXoffset + PSwidth_lying + (PSwidth_lying/2), middleYoffset + i*PSheight_lying + (PSheight_lying/2));
			}

			// Writings:
			g.drawString("Entrance/Exit", startXoffset + perimeterWidth/2 - 36, startYoffset-10);
			
			int writingsDistFromPerimeter = 40;
			
			g.setColor(Color.BLUE); // "N"
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
			g.drawString("N", startXoffset + perimeterWidth/2 - 10, startYoffset-writingsDistFromPerimeter);
			
			g.setColor(Color.BLUE); // "S"
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
			g.drawString("S", startXoffset + perimeterWidth/2 - 10, startYoffset + perimeterHeight + writingsDistFromPerimeter - 10);
			
			g.setColor(Color.BLUE); // "W"
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
			g.drawString("W", startXoffset - writingsDistFromPerimeter, startYoffset + perimeterHeight/2);

			g.setColor(Color.BLUE); // "E"
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
			g.drawString("E", startXoffset + perimeterWidth + 10, startYoffset + perimeterHeight/2);
			
			// Handicap zone:
			Image h = new ImageIcon("HandicappedParking.png").getImage(); // Icon size: 25*25
			g.drawImage(h, startXoffset + cornerEdge + ((numOfPSatTheNorthLeft*PSwidth_standing - 30)/2), startYoffset + cornerEdge + 5, this);
			
			g.setColor(Color.BLACK);
			g2.fill(createArrowShape(new Point(startXoffset + cornerEdge + ((numOfPSatTheNorthLeft*PSwidth_standing - 30)/2), startYoffset + cornerEdge+17), new Point(startXoffset + cornerEdge + ((numOfPSatTheNorthLeft*PSwidth_standing - 30)/2) - 25, startYoffset + cornerEdge+17)));
			g2.fill(createArrowShape(new Point(startXoffset + cornerEdge + ((numOfPSatTheNorthLeft*PSwidth_standing - 30)/2) + 26, startYoffset + cornerEdge+17), new Point(startXoffset + cornerEdge + ((numOfPSatTheNorthLeft*PSwidth_standing - 30)/2) + 25 + 25, startYoffset + cornerEdge+17)));
			
			// Direction arrows:
			g.setColor(Color.ORANGE); // N
			g2.fill(createArrowShape(new Point(startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing + PSwidth_standing, startYoffset + 10), 
										new Point(startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing + PSwidth_standing, startYoffset + 40)));
			g2.fill(createArrowShape(new Point(startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing + 3*PSwidth_standing, startYoffset + 40), 
										new Point(startXoffset + cornerEdge + numOfPSatTheNorthLeft*PSwidth_standing + 3*PSwidth_standing, startYoffset + 10)));
			
			g.setColor(Color.ORANGE); // W
			g2.fill(createArrowShape(new Point(startXoffset + cornerEdge + PSwidth_standing, startYoffset + (int)(3*PSheight_standing)), 
										new Point(startXoffset + cornerEdge + PSwidth_standing, startYoffset + (int)(3*PSheight_standing) + 30)));
			g2.fill(createArrowShape(new Point(startXoffset + cornerEdge + 3*PSwidth_standing, startYoffset + (int)(3*PSheight_standing) + 30), 
										new Point(startXoffset + cornerEdge + 3*PSwidth_standing, startYoffset + (int)(3*PSheight_standing))));
			
			g.setColor(Color.ORANGE); // E
			g2.fill(createArrowShape(new Point(startXoffset + cornerEdge + 8*PSwidth_standing + PSwidth_standing, startYoffset + (int)(3*PSheight_standing)), 
										new Point(startXoffset + cornerEdge + 8*PSwidth_standing + PSwidth_standing, startYoffset + (int)(3*PSheight_standing) + 30)));
			g2.fill(createArrowShape(new Point(startXoffset + cornerEdge + 8*PSwidth_standing + 3*PSwidth_standing, startYoffset + (int)(3*PSheight_standing) + 30), 
										new Point(startXoffset + cornerEdge + 8*PSwidth_standing + 3*PSwidth_standing, startYoffset + (int)(3*PSheight_standing))));
			
			g.setColor(Color.ORANGE); // S
			g2.fill(createArrowShape(new Point(startXoffset + perimeterWidth/2 + 15, middleYoffset + numOfPSatTheMiddleLeft*PSheight_lying + 15), 
										new Point(startXoffset + perimeterWidth/2 - 15, middleYoffset + numOfPSatTheMiddleLeft*PSheight_lying + 15)));
			g2.fill(createArrowShape(new Point(startXoffset + perimeterWidth/2 - 15, middleYoffset + numOfPSatTheMiddleLeft*PSheight_lying + PSheight_lying + 15), 
										new Point(startXoffset + perimeterWidth/2 + 15, middleYoffset + numOfPSatTheMiddleLeft*PSheight_lying + PSheight_lying + 15)));
		}
	}
	
	public static Shape createArrowShape(Point fromPt, Point toPt) {
	    Polygon arrowPolygon = new Polygon();
	    arrowPolygon.addPoint(-6,1);
	    arrowPolygon.addPoint(3,1);
	    arrowPolygon.addPoint(3,3);
	    arrowPolygon.addPoint(6,0);
	    arrowPolygon.addPoint(3,-3);
	    arrowPolygon.addPoint(3,-1);
	    arrowPolygon.addPoint(-6,-1);


	    Point midPoint = midpoint(fromPt, toPt);

	    double rotate = Math.atan2(toPt.y - fromPt.y, toPt.x - fromPt.x);

	    AffineTransform transform = new AffineTransform();
	    transform.translate(midPoint.x, midPoint.y);
	    double ptDistance = fromPt.distance(toPt);
	    double scale = ptDistance / 12.0; // 12 because it's the length of the arrow polygon.
	    transform.scale(scale, scale);
	    transform.rotate(rotate);

	    return transform.createTransformedShape(arrowPolygon);
	}

	private static Point midpoint(Point p1, Point p2) {
	    return new Point((int)((p1.x + p2.x)/2.0), 
	                     (int)((p1.y + p2.y)/2.0));
	}

}
