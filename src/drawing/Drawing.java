package drawing;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


enum COMMANDS {removeLast, empty, openImage};


public class Drawing extends JFrame
implements ActionListener
{
	DrawingPanel drawingPanel = new DrawingPanel();
	ButtonGroup drawingTypeGroup = new ButtonGroup();
	String[] dtNames ={"Arrow", "Line", "Rectangle", "Oval", "Scribble", "Text", "Image", "Erase"};
	JRadioButton[] dtButtons = new JRadioButton[dtNames.length];
	JCheckBox filledBox = new JCheckBox("Filled");
	Color[] colors = {Color.black, Color.red, Color.cyan, Color.blue, 
			Color.green, Color.darkGray, Color.magenta, Color.orange, Color.pink, Color.yellow};
	String[] colorText={"black", "red", "cyan", "blue", 
			"green", "darkGray", "magenta", "orange", "pink", "yellow"};
	ButtonGroup colorGroup = new ButtonGroup();
	JRadioButton[] colorButtons = new JRadioButton[colors.length];
	JTextArea myText = new JTextArea(2,100);
	JTextField myRows = new JTextField(3);
	JTextField myCols = new JTextField(3);
	JTextField strokeWidth = new JTextField(3);
	JTextField fontSizeField = new JTextField(3);
	JTextField imageScaleSize = new JTextField(5);
	
	
	COMMANDS[] commands = COMMANDS.values();
	JButton[] commandButtons = new JButton[commands.length];
	
	//Image startingImage=null;
/*	
	public boolean isFocusable()
	{
		return true;
	}
*/
	
	
	public void init()
	{
		Color backdrop = Color.lightGray;
		setLayout (new BorderLayout());
		add(drawingPanel, BorderLayout.CENTER);
		drawingPanel.init(strokeWidth, fontSizeField, myText, myRows, myCols,imageScaleSize);
		
		JPanel jpn1 = new JPanel();
		jpn1.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpn1.setBackground(backdrop);
		strokeWidth.setText("1");
		jpn1.add(strokeWidth);
		
		for (int i = 0; i < dtNames.length; i++)
		{
			JRadioButton b = new JRadioButton(dtNames[i], false);
			if ("Scribble".equals(dtNames[i]))
				b.setSelected(true);
			dtButtons[i]= b;
			drawingTypeGroup.add(b);
			jpn1.add(b);
			b.addActionListener(this);
		}
		filledBox.setVisible(false);
		jpn1.add(filledBox);
		filledBox.addActionListener(this);
		JPanel jpn = new JPanel();
		
		
		jpn.setLayout(new GridLayout(2,1));
		jpn.add(jpn1);
		JPanel jpn2 = new JPanel();
		jpn2.setLayout(new FlowLayout(FlowLayout.LEFT));
		fontSizeField.setText("10");
		jpn2.add(fontSizeField);
		jpn2.setBackground(backdrop);
		Font myFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
		myText.setFont(myFont);
		jpn2.add(myText);
		jpn.add(jpn2);
	
		
		add(jpn, BorderLayout.NORTH);
		
		
		JPanel jpw = new JPanel();
		jpw.setLayout(new GridLayout(0,1));
		jpw.setBackground(backdrop);
	
		for (int i=0; i < commands.length; i++)
		{
			JButton btn = new JButton(commands[i].toString());
			
			jpw.add(btn);
			btn.addActionListener(this);
			
		}
		jpw.add(new JLabel("Image Scale"));
		jpw.add(imageScaleSize);
		imageScaleSize.setText("1.0");
		
	
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new GridLayout(0,1));
		for (int i=0; i < colors.length; i++)
		{
			JRadioButton b = new JRadioButton(colorText[i], false);
			if (colors[i].equals(Color.black))
				b.setSelected(true);
			//b.setBackground(colors[i]);
			b.setForeground(colors[i]);
			colorButtons[i]= b;
			colorGroup.add(b);
			cPanel.add(b);
			b.addActionListener(this);
		}
		JScrollPane scrollPane = new JScrollPane(cPanel);
		jpw.add(new JLabel("colors"));
		jpw.add(scrollPane );
		
		jpw.add(new JLabel("rows/cols"));
		JPanel rcPanel = new JPanel();
		rcPanel.setLayout(new FlowLayout());
		rcPanel.add(myRows);
		rcPanel.add(myCols);
		myRows.setText("1");
		myCols.setText("1");
		jpw.add(rcPanel);
		
		// Adding some blank labels at the end to effectively move the 
		// previously ones up .... Otherwise they get evenly spaced out. 
		for (int i=0; i < 2; i++)
		{
			jpw.add(new JLabel(""));
		
		}
		add(jpw, BorderLayout.WEST);
		
		

		
	}
	public void actionPerformed (ActionEvent evt)
	{
		String action = evt.getActionCommand();
		//scribble, oval, filledOval, rectangle, filledRectangle, 
	//	arrow, straightLine, textType
		for (int i=0; i < colors.length; i++)
		{
			if (colorButtons[i].isSelected())
				drawingPanel.setColor(colors[i]);
		}
		if ("Arrow".equals(action))
		{
			drawingPanel.setDrawType(DrawType.arrow);
			filledBox.setVisible(false);
		}
		else if ("Line".equals(action))
		{
			drawingPanel.setDrawType(DrawType.straightLine);
			filledBox.setVisible(false);
		}
		else if ("Rectangle".equals(action))
		{
			filledBox.setVisible(true);
			if (filledBox.isSelected())
				drawingPanel.setDrawType(DrawType.filledRectangle);
			else
				drawingPanel.setDrawType(DrawType.rectangle);
		}
		else if ("Oval".equals(action))
		{
			filledBox.setVisible(true);
			if (filledBox.isSelected())
				drawingPanel.setDrawType(DrawType.filledOval);
			else
			drawingPanel.setDrawType(DrawType.oval);
		}
		else if ("Scribble".equals(action))
		{
			filledBox.setVisible(false);
			drawingPanel.setDrawType(DrawType.scribble);
		}
		else if ("Text".equals(action))
		{
			filledBox.setVisible(false);
			drawingPanel.setDrawType(DrawType.textType);
		}
		else if ("Image".equals(action))
		{
			filledBox.setVisible(false);
			drawingPanel.setDrawType(DrawType.imageType);
		}
		else 
		{
			for (int i=0; i < commands.length; i++)
			{
				if (action.equals(commands[i].toString()))
				{
					drawingPanel.setCommand(action);
				}
			}
			
		}
		
		
		if (filledBox.isSelected())
		{
			if (drawingPanel.drawType == DrawType.rectangle)
				drawingPanel.setDrawType(DrawType.filledRectangle);

			if (drawingPanel.drawType == DrawType.oval)
				drawingPanel.setDrawType(DrawType.filledOval);				
		}
		else
		{
			if (drawingPanel.drawType == DrawType.filledRectangle)
				drawingPanel.setDrawType(DrawType.rectangle);

			if (drawingPanel.drawType == DrawType.filledOval)
				drawingPanel.setDrawType(DrawType.oval);	
		}
		if (action.equals("Erase"))
		{
			drawingPanel.setDrawType(DrawType.filledRectangle);
			filledBox.setVisible(false);
			drawingPanel.setColor(Color.white);
		}
	}

	
	Drawing()
	{
		super("White Board");
		setSize(1000,800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		
		init();
		setVisible(true);
	}
	

	public static void main(String[] args) { 
		Drawing drw = new Drawing();
	} // end of main
}