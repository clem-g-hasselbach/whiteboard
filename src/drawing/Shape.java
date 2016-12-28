package drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

abstract class Shape
{
	Point start;
	int lineWidth;
	Color color;
	Shape (Point start, Color c, int lineWidth)
	{
		this.start = start;
		color =c;
		this.lineWidth = lineWidth;
	}
	abstract void draw(Graphics2D g);
	abstract void subsequentPoint(Point p);
}
class ImageShape extends Shape
{
	double scaling;
	int imageWidth, imageHeight;
	Image image_to_display;

	ImageShape(Point start, Image image_to_display, double scaling, int imageWidth, int imageHeight)
	{
		super(start,null, 0);
		this.image_to_display = image_to_display;
		this.scaling = scaling;
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
	}
	void draw(Graphics2D g)
	{
		int w = (int)(imageWidth * scaling);
		int h = (int)(imageHeight * scaling);
		
		g.drawImage(image_to_display, start.x, start.y, w, h, null);
	}
	void subsequentPoint(Point p)
	{
		
	}
}

class Rectangle extends Shape
{
	boolean filled=false;
	Point lastPoint;
	int columns = 1;
	int rows =1;
	Rectangle(Point start, Color c, boolean filled, int lineWidth)
	{
		super(start,c, lineWidth);
		lastPoint = start;
		this.filled = filled;
	}
	
	public void setRowsCols(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
	}
	void subsequentPoint(Point p)
	{
		lastPoint = p;
	}
	void draw(Graphics2D g)
	{
		g.setColor(color);
		int x = Math.min(start.x, lastPoint.x);
		int y = Math.min(start.y, lastPoint.y);
		int w = Math.abs(start.x - lastPoint.x);
		int h = Math.abs(start.y - lastPoint.y);
		if (filled)
			g.fillRect(x, y, w, h);
		else
		{
			DrawTypeTools.drawRect(g, lineWidth, x, y, w, h);
			if (rows > 1)
			{
				int delta = h/rows;
				int y2 = y+delta;
				for (int i=2; i <= rows; i++)
				{
					DrawTypeTools.drawLine(g, lineWidth, x, y2, x+w-1, y2);
					y2 += delta;
				}
			}
			if (columns > 1)
			{
				int delta = w/columns;
				int x2 = x+delta;
				for (int i=2; i <= columns; i++)
				{
					DrawTypeTools.drawLine(g, lineWidth, x2, y, x2, y + h-1);
					x2 += delta;
				}
			}
			
		}
	}
}

class Oval extends Shape
{
	boolean filled=false;
	Point lastPoint;
	Oval(Point start, Color c, boolean filled, int lineWidth)
	{
		super(start,c, lineWidth);
		lastPoint = start;
		this.filled = filled;
	}
	void subsequentPoint(Point p)
	{
		lastPoint = p;
	}
	void draw(Graphics2D g)
	{
		g.setColor(color);
		int x = Math.min(start.x, lastPoint.x);
		int y = Math.min(start.y, lastPoint.y);
		int w = Math.abs(start.x - lastPoint.x);
		int h = Math.abs(start.y - lastPoint.y);
		if (filled)
			g.fillOval(x, y, w, h);
		else
			DrawTypeTools.drawOval(g, lineWidth, x, y, w, h);
	}
}


class StraightLine extends Shape
{
	Point lastPoint;
	StraightLine(Point start, Color c, int lineWidth)
	{
		super(start,c, lineWidth);
		lastPoint = start;
	}
	void subsequentPoint(Point p)
	{
		lastPoint = p;
	}
	void draw(Graphics2D g)
	{
		g.setColor(color);
		DrawTypeTools.drawLine(g, lineWidth, start.x, start.y, lastPoint.x, lastPoint.y);
	}
}
class Arrow extends Shape
{
	Point lastPoint;
	Arrow(Point start, Color c, int lineWidth)
	{
		super(start,c, lineWidth);
		lastPoint = start;
	}
	void subsequentPoint(Point p)
	{
		lastPoint = p;
	}
	void draw(Graphics2D g)
	{
		g.setColor(color);
		DrawTypeTools.drawLine(g, lineWidth, start.x, start.y, lastPoint.x, lastPoint.y);
		
		double dx = lastPoint.x - start.x  ;
		double dy = lastPoint.y - start.y ;
		double angle = Math.atan2(dy, dx);
		double dAngle = Math.toRadians(45);
		double angle1 = angle + dAngle;
		double angle2 = angle - dAngle;
		double arrowHeadSize = 20;
		double x1 = start.x + arrowHeadSize * Math.cos(angle1);
		double y1 = start.y + arrowHeadSize * Math.sin(angle1);
		double x2 = start.x + arrowHeadSize * Math.cos(angle2);
		double y2 = start.y + arrowHeadSize * Math.sin(angle2);
		
		DrawTypeTools.drawLine(g, lineWidth, start.x, start.y, (int)x1,(int)y1);
		DrawTypeTools.drawLine(g, lineWidth, start.x, start.y, (int)x2,(int)y2);
	}
}



class Scribble extends Shape
{
	ArrayList<Point> points= new ArrayList<Point>();
	Scribble(Point start, Color c, int lineWidth)
	{
		super(start,c, lineWidth);
	}
	void subsequentPoint(Point p)
	{
		points.add(p);
	}
	void draw(Graphics2D g)
	{
		g.setColor(color);
		for (int i=1; i < points.size(); i++)
		{            
			Point first = points.get(i-1);
			Point next = points.get(i);
			DrawTypeTools.drawLine(g, lineWidth,first.x, first.y, next.x, next.y);
		}
	}
}
