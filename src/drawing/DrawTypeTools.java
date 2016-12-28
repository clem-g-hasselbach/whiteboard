package drawing;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class DrawTypeTools {
	
	public static void drawLine(Graphics2D g, int lineWidth, int x1, int y1, int x2, int y2)
	{
		BasicStroke bs = new BasicStroke(lineWidth,BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND);
		g.setStroke(bs);
		int xdelta = Math.abs(x1-x2);
		int ydelta = Math.abs(y1-y2);
		double threshold = 0.01;
		
		if (xdelta < ydelta)
		{
			double ratio= xdelta/(double)ydelta;
			if (ratio < threshold)
				x2=x1;
		}
		else
		{
			double ratio= ydelta/(double)xdelta;
			if (ratio < threshold)
				y2=y1;
		}
		g.drawLine(x1, y1, x2, y2);
		
		// Old way with the Graphics class
//		if (lineWidth == 1)
//		{
//			// optimization
//			g.drawLine(x1, y1, x2, y2);
//			return;
//		}
//		double dx = x2 - x1;
//		double dy = y2 - y1;
//		double angle = Math.atan2(dy, dx) + Math.toRadians(90);
//		double cos = Math.cos(angle);
//		double sin = Math.sin(angle);
//		
//		//System.out.println("drawLine lineWidth="+lineWidth +" cos="+cos + " sin="+sin);
//		
//		x1 = (int)(x1 - cos*lineWidth/2);
//		x2 = (int)(x2 - cos*lineWidth/2);
//		y1 = (int)(y1 + sin*lineWidth/2);
//		y2 = (int)(y2 + sin*lineWidth/2);
//		
//		for (int i=0; i < lineWidth; i++)
//		{
//			g.drawLine((int)(x1 + i * cos), 
//						(int)(y1 - i * sin),
//						(int)(x2 + i * cos), 
//						(int)(y2 - i * sin));
//		}
		
	}
	public static void drawRect(Graphics2D g, int lineWidth, int x, int y, int w, int h)
	{	

		BasicStroke bs = new BasicStroke(lineWidth);
		g.setStroke(bs);
		g.drawRect(x, y, w, h);
		
		// Old way with the Graphics class
		//System.out.println("drawRect lineWidth="+lineWidth);
//		for (int i=1; i <= lineWidth; i++ )
//		{
//			
//			g.drawRect(x, y, w, h);
//			x+=1;
//			y+=1;
//			w -=2;
//			h -=2;
//		}

	}
	public static void drawOval(Graphics2D g, int lineWidth, int x, int y, int w, int h)
	{
		BasicStroke bs = new BasicStroke(lineWidth);
		g.setStroke(bs);
		g.drawOval(x, y, w, h);
		
		// Old way with the Graphics class
//		for (int i=1; i <= lineWidth; i++ )
//		{
//			g.drawOval(x, y, w, h);
//			x+=1;
//			y+=1;
//			w -=2;
//			h -=2;
//		}
	}
}