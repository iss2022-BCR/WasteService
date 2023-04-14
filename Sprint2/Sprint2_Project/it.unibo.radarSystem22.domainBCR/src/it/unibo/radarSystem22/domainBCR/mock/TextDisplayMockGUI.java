package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.concrete.TextDisplayConcreteLCD1602;
import it.unibo.radarSystem22.domainBCR.interfaces.ITextDisplay;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TextDisplayMockGUI extends TextDisplayMockCLI {
    private Frame frame;
    private TextField textField1, textField2;

    // Constructor
    public TextDisplayMockGUI(Frame frame)
    {
        super();
        //Colors.out("create TextDisplayMockGUI");
        this.frame = frame;
        configure();
    }

    protected void configure()
    {
        textField1 = new TextField();
        textField1.setEditable(false);
        textField1.setColumns(16);
        textField1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        textField2 = new TextField();
        textField2.setEditable(false);
        textField2.setColumns(16);
        textField2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        frame.add(BorderLayout.PAGE_START, textField1);
        frame.add(BorderLayout.PAGE_END, textField2);
    }

    public static Frame initFrame(int dx, int dy)
    {
        Frame frame         = new Frame();
        //Layout
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

    public static ITextDisplay createTextDisplay()
    {
        return new TextDisplayMockGUI(initFrame(200, 100));
    }

    public void destroyTextDisplayGui()
    {
        frame.dispose();
    }

    @Override
    public void setLine1(String text)
    {
        super.setLine1(text);
        textField1.setText(text);
        textField1.validate();
    }

    @Override
    public void setLine2(String text)
    {
        super.setLine2(text);
        textField2.setText(text);
        textField2.validate();
    }

    @Override
    public void setLines(String text1, String text2)
    {
        super.setLines(text1, text2);
        textField1.setText(super.getLine1());
        textField2.setText(super.getLine2());
        textField1.validate();
        textField2.validate();
    }

    @Override
    public void clear()
    {
        super.clear();
        textField1.setText("");
        textField2.setText("");
        textField1.validate();
        textField2.validate();
    }
}
