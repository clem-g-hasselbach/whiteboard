package drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


enum DrawType {scribble, oval, filledOval, rectangle, filledRectangle, 
	arrow, straightLine, textType, imageType, eraseType};

public class DrawingPanel extends JPanel{

	DrawType drawType = DrawType.scribble;
	//char rowsChar ='1';
	Color color = Color.black;
	boolean eraseMode = false;
	Color backGroundColor = Color.white;
	ArrayList<Shape> storage = new ArrayList<Shape>();
	Shape inProgress = null;
	Point currentMouseLocation = new Point(0,0);
	//boolean inTextMode = false;
	int fontSize = 20;
	int lineWidth = 2;
	Dimension previousDimension = new Dimension();
	JTextField strokeWidth, fontSizeField;
	JTextArea textToDisplay;
	JTextField myRows, myCols, imageScaleField;

	Image image_to_display = null;
	double imageScale = 1.0;
	int imageWidth;
	int imageHeight;
	File lastFileOpened=null;
	
	Image offScreenImage=null;
	
/*
	public boolean isFocusable()
	{
		return true;
	}
	*/
	public void setCommand(String cmd)
	{
		if (cmd.equals(COMMANDS.removeLast.toString()))
		{
			if (storage.size() > 0)
				storage.remove(storage.size()-1);
		}
		else if (cmd.equals(COMMANDS.empty.toString()))
		{
			storage = new ArrayList<Shape>();
			inProgress = null;
		}
		else if (cmd.equals(COMMANDS.openImage.toString()))
		{
			setupImage();
		}
		
		repaint();
	}
	public void setupImage()
	{
		JFileChooser fc = (lastFileOpened==null?new JFileChooser()  :  new JFileChooser(lastFileOpened));
		int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	lastFileOpened = fc.getSelectedFile();
            try
            {
				Image img = ImageIO.read(lastFileOpened);
				image_to_display=img;
				imageWidth=img.getWidth(null);
				imageHeight = img.getHeight(null);
            }
            catch (IOException e)
            {
            	System.out.println("Image File IO error: "+e);
            }
        }
	}
	public void setDrawType(DrawType dt)
	{
		drawType = dt;
	}

	public void setColor(Color c)
	{
		eraseMode= false;
		color = c;
	}
	void setEraseMode()
	{
		setColor(backGroundColor);
		eraseMode = true;
	}


	public void init(JTextField strokeWidth, JTextField fontSizeField, JTextArea textToDisplay, 
			JTextField myRows, JTextField myCols, JTextField imageScaleField)
	{

		// Our JFrame is a Component.  We want to register it to
		// fire MouseEvents according to the MouseListener interface
		// The class which will handle this interface in the JFrame itself.
		// This implies our JFrame must implement the MouseListener interface.
		this.strokeWidth = strokeWidth;
		this.fontSizeField = fontSizeField;
		this.textToDisplay = textToDisplay;
		this.myRows = myRows;
		this.myCols = myCols;
		this.imageScaleField = imageScaleField;

		MyMouse mm = new MyMouse();
		addMouseListener(mm);
		// MouseMotionListener interface gives us the Drag capability

		addMouseMotionListener(mm);

	
	}

	public void paint(Graphics gScreen)
	{
		// find out how big our drawing area is
		Dimension d = getSize();
		if (!d.equals(previousDimension))
		{
			previousDimension = d;
			offScreenImage = null;
		}
		Graphics g_basic;
		if (offScreenImage == null)
		{
			offScreenImage = createImage(getBounds().width, getBounds().height);			
		}
		g_basic = offScreenImage.getGraphics();
		Graphics2D g = (Graphics2D)g_basic.create();

		g.setColor(backGroundColor);
		g.fillRect(0,0, d.width, d.height);
		
		

		for (int i=0; i < storage.size(); i++)
		{
			Shape s = storage.get(i);
			s.draw(g);
		}
		if (inProgress != null)
			if (drawType == DrawType.textType)
			{
				TextShape t = (TextShape)inProgress;
				t.draw(g);
			}
			else
				inProgress.draw(g);
		else
		{	
			if (drawType == DrawType.textType)
			{
				TextShape ts = new TextShape(currentMouseLocation, color, fontSize, "");
				ts.drawCursor(g);
			}
			else
			{
				g.setColor(color);
				int size =20;
				Point p = currentMouseLocation;
				// Draw a little cross
				DrawTypeTools.drawLine(g, lineWidth, p.x-size, p.y, p.x+size, p.y);
				DrawTypeTools.drawLine(g, lineWidth, p.x, p.y-size, p.x, p.y+size);		
			}
		}
		gScreen.drawImage(offScreenImage, 0,0, this);
	}    
	

	private int getIntFromStr(String s, int defaultValue)
	{
		int value = defaultValue;
		try
		{
			value = Integer.parseInt(s.trim());
		}
		catch (NumberFormatException e)
		{
			// too bad.
		}
		return value;
	}

	private double getDoubleFromStr(String s, double defaultValue)
	{
		double value = defaultValue;
		try
		{
			value = Double.parseDouble(s.trim());
		}
		catch (NumberFormatException e)
		{
			// too bad.
		}
		return value;
	}






	class MyMouse extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{    
			currentMouseLocation = e.getPoint();
			Point p = new Point(currentMouseLocation);
			lineWidth = getIntFromStr(strokeWidth.getText(), 1);
			switch (drawType)
			{
			case rectangle:
				inProgress = new Rectangle(p, color, false, lineWidth);
				((Rectangle)inProgress).setRowsCols(getIntFromStr(myRows.getText(),1),
						getIntFromStr(myCols.getText(),1));
				break;
			case filledRectangle:
				inProgress = new Rectangle(p, color, true, lineWidth);
				break;
			case oval:
				inProgress = new Oval(p, color, false, lineWidth);
				break;
			case filledOval:
				inProgress = new Oval(p, color, true, lineWidth);
				break;
			case scribble:
				inProgress = new Scribble(p, color, lineWidth);
				break;  
			case straightLine:
				inProgress = new StraightLine(p, color, lineWidth);
				break;   
			case arrow:
				inProgress = new Arrow(p, color, lineWidth);
				break;      
			case textType:
				fontSize = getIntFromStr(fontSizeField.getText(), 8);
				inProgress = new TextShape(p, color, fontSize, textToDisplay.getText());				
				break;
			case imageType:
				if (image_to_display != null)
				{
					inProgress = new ImageShape(p, image_to_display, 
						getDoubleFromStr(imageScaleField.getText(), 1),
						imageWidth, imageHeight);
				}
				break;
			}    
			repaint();
		}

		public void mouseReleased(MouseEvent e)
		{    
			currentMouseLocation = e.getPoint();
			if (inProgress == null) return;			

			Point p = new Point(currentMouseLocation);
			inProgress.subsequentPoint(p);
			storage.add(inProgress);
			inProgress=null;

			repaint();
		}

		public void mouseDragged(MouseEvent e)
		{
			currentMouseLocation = e.getPoint();
			if (inProgress == null) return;
			Point p = new Point(currentMouseLocation);
			inProgress.subsequentPoint(p);    
			repaint();
		}
		public void mouseMoved(MouseEvent e)
		{
			currentMouseLocation = e.getPoint();	
			repaint();
		}


	}
	
	
}