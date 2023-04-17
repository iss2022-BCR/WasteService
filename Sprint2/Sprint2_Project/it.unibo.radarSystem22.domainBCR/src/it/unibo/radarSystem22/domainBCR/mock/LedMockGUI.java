package it.unibo.radarSystem22.domainBCR.mock;

import java.awt.*;
import java.awt.event.*;

import it.unibo.radarSystem22.domainBCR.interfaces.ILed;
import it.unibo.radarSystem22.domainBCR.state.LedState;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public class LedMockGUI extends LedMockCLI {
	private final Color colorOff = Color.gray;
	private final Color colorOn = Color.green;

	private static int positionOffset = 0;

	private Frame frame;
	private Panel panel;
	private final Dimension sizeOn  = new Dimension(100,100);
	private final Dimension sizeOff = new Dimension(30,30);

	private Thread blinker;

	// Constructor
	public LedMockGUI(Frame frame)
	{
		super();
		//Colors.out("create LedMockGUI");
		this.frame = frame;
 		configure();
  	}

	public static Frame initFrame(int dx, int dy)
	{
		Frame frame         = new Frame();
		BorderLayout layout = new BorderLayout();
		frame.setSize(new Dimension(dx,dy));
		frame.setLayout(layout);
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		frame.setVisible(true);
		return frame;
	}

	protected void configure()
	{
		panel = new Panel();
		panel.setSize(sizeOff);
		panel.setBackground(colorOff);
		frame.add(BorderLayout.CENTER, panel);
		positionOffset = positionOffset + 50;
		frame.setLocation(positionOffset, positionOffset);
  	}

	public static ILed createLed()
	{
		return new LedMockGUI(initFrame(150,150));
	}

	public void destroyLedGui()
	{
		frame.dispose();
	}

	@Override // LedMock
	public void turnOn()
	{
		super.turnOn();
		panel.setSize(sizeOn);
		panel.setBackground(colorOn);
		panel.validate();
	}

	@Override // LedMock
	public void turnOff()
	{
		super.turnOff();
 		panel.setSize(sizeOff);
		panel.setBackground(colorOff);
		panel.validate();
	}

	@Override // LedMock
	public void blink()
	{
		if(getState().equals(LedState.BLINKING))
			return;

		super.blink();
		blinker = new Thread(new Runnable() {
			private boolean isOn = false;
			@Override
			public void run() {
				while(getState().equals(LedState.BLINKING))
				{
					isOn = !isOn;

					panel.setSize(isOn ? sizeOn : sizeOff);
					panel.setBackground(isOn ? colorOn : colorOff);

					panel.validate();
					panel.repaint();

					BasicUtils.delay(DomainSystemConfig.ledDelay);
				}
			}
		});

		blinker.start();
	}
}
