package drawing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

class TextShape extends Shape
{
	Point lastPoint;
	FontMetrics fontMetrics;
	int fontWidth, fontHeight, fontMaxAscent;
	int fontSize;
	StringBuilder stringBuilder;
	TextShape(Point start, Color c, int fs, String text)
	{
		super(start,c, 1/* Not used for TextShape*/);
		lastPoint = new Point(start);
		fontSize = fs;
		stringBuilder = new StringBuilder(text);
	}
	
	void subsequentPoint(Point p)  // We don't want this called
	{
		//lastPoint = p;
	}
	void init(Graphics g)
	{
		Font myFont = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
		g.setFont(myFont);
		fontMetrics = g.getFontMetrics(myFont);
		fontHeight = fontMetrics.getHeight();
		fontWidth = fontMetrics.stringWidth("A");
		fontMaxAscent = fontMetrics.getMaxAscent();
		g.setColor(color);
	}
	void draw(Graphics2D g)
	{
		init(g);
		String s = stringBuilder.toString();
		// Need to walk through the new lines
		int index,  startIndex=0;
		lastPoint = new Point(start);
		do
		{
			index = s.indexOf('\n', startIndex);
			String line = (index >= 0?  
					s.substring(startIndex, index) : s.substring(startIndex));
			startIndex = index +1;
			g.drawString(line, lastPoint.x, lastPoint.y);
			if (index < 0)
				lastPoint.x = lastPoint.x + fontMetrics.stringWidth(line);
			else
				lastPoint.y += fontHeight;
		} while (index >= 0);
	}
	void drawCursor(Graphics g)
	{
		init(g);
		g.drawRect(lastPoint.x, lastPoint.y-fontMaxAscent, fontWidth, fontHeight);
	}
	
}