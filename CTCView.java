/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CTC;

/**
 *
 * @author AM
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.*;

public class CTCView extends JFrame
{
    private CTCControl control = new CTCControl();
    private CTCModel model = new CTCModel();
    private boolean isOpen = true;
    private int clockRate = 60;
    private boolean demoMode = false;
    private MainPanel mainPanel = new MainPanel();
    private DispatcherPanel dispatcherPanel = new DispatcherPanel();
    private OperatorPanel operatorPanel = new OperatorPanel();
    private TrainPanel trainPanel = new TrainPanel();
    private TrackPanel trackPanel = new TrackPanel();
    private MetricsPanel metricsPanel = new MetricsPanel();
    private SplashPanel splashPanel = new SplashPanel();
    private String dispatcherID;
    
    public CTCView()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        intialize();
        setLayout(new BorderLayout());
        this.add(splashPanel);
        this.setSize(800,600);
        this.setVisible(true);
    }
    
    private void intialize()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileClose = new JMenuItem("Close");
        JMenu viewMenu = new JMenu("View");
        JMenuItem viewMain = new JMenuItem("Main");
        JMenuItem viewDispatcher = new JMenuItem("Dispatcher");
        JMenuItem viewOperator = new JMenuItem("Operator");
        JMenuItem viewTrain = new JMenuItem("Add/Modify Train");
        JMenuItem viewTrack = new JMenuItem("View/Modify Track");
        JMenuItem viewMetrics = new JMenuItem("Metrics");
        JMenu runMenu = new JMenu("Run");
        JMenuItem runDemo = new JMenuItem("Run Demo");
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(runMenu);
        fileMenu.add(fileClose);
        viewMenu.add(viewMain);
        viewMenu.add(viewDispatcher);
        viewMenu.add(viewOperator);
        viewMenu.add(viewTrain);
        viewMenu.add(viewTrack);
        viewMenu.add(viewMetrics);
        runMenu.add(runDemo);
        setJMenuBar(menuBar);
        
        fileClose.addActionListener(new MenuActionClose());
        viewMain.addActionListener(new MenuOpenScreen(mainPanel));
        viewDispatcher.addActionListener(new MenuDialogLogin(this));
        viewOperator.addActionListener(new MenuOpenScreen(operatorPanel));
        viewTrain.addActionListener(new MenuOpenScreen(trainPanel));
        viewTrack.addActionListener(new MenuOpenScreen(trackPanel));
        viewMetrics.addActionListener(new MenuOpenScreen(metricsPanel));
    }
    
    private class MenuActionClose implements ActionListener 
    {
        private MenuActionClose() {}
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            isOpen = false;
            dispose();
        }

    }
    
    private class MenuDialogLogin implements ActionListener
    {
        private JFrame frame;
        
        private MenuDialogLogin(JFrame f) 
        {
            this.frame = f;
        }
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            LogInDialog login = new LogInDialog(frame);
            login.setVisible(true);
            if(login.isSucceeded())
            {
                dispatcherID = login.getVerifiedUsername();
                dispatcherPanel.initialize();
                changePanel(dispatcherPanel);
            }
            else
            {
                changePanel(mainPanel);
            }
                        
        }
    }
    
    private class MenuOpenScreen implements ActionListener 
    {
        private JPanel panel;
        
        private MenuOpenScreen(JPanel pnl) 
        {
            this.panel = pnl;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            changePanel(panel);
        }

    }
    
    private void changePanel(JPanel panel) 
    {
        getContentPane().removeAll();
        getContentPane().add(panel);
        //getContentPane().doLayout();
        getContentPane().validate();
        update(getGraphics());
    }
    
    private class MainPanel extends JPanel
    {
        private MapPanel map = new MapPanel(model);
        private JLabel clockLabel = new JLabel("Clock Rate (1 hr = )");
        private final String clockRates [] = {"15 minutes", "20 minutes", "30 minutes", "40 minutes", "45 minutes", "60 minutes", "90 minutes", "120 minutes"};
        private JComboBox clockCombo= new JComboBox(clockRates);
        

        MainPanel()
        {
            this.setLayout(new GridBagLayout());
            initialize();
        }

        private void initialize()
        {
            addComponent(this, map, 0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(this, clockLabel, 0, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, clockCombo, 1, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);
            clockCombo.addActionListener(clockComboListener);
            clockCombo.setSelectedItem("60 minutes");
        }
        
        
        ActionListener clockComboListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                JComboBox cb = (JComboBox)event.getSource();
                int index = (int)cb.getSelectedIndex();
                switch(index)
                {
                    case 0:
                        clockRate = 15;
                        break;
                    case 1:
                        clockRate = 20;
                        break;
                    case 2:
                        clockRate = 30;
                        break;
                    case 3:
                        clockRate = 40;
                        break;
                    case 4:
                        clockRate = 45;
                        break;
                    case 5:
                        clockRate = 60;
                        break;
                    case 6:
                        clockRate = 90;
                        break;
                    default:
                        clockRate = 120;       
                }
            }
        };
        

        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.WHITE);
            g.fillRect(0,0, width, height);
        }
    }
    
    private class DispatcherPanel extends JPanel
    {
        private MapPanel map = new MapPanel(model);
        private JLabel dispatcherIDLabel;
        private JLabel trainID = new JLabel("Train ID");
        private JComboBox trains = new JComboBox();
        private JLabel setpointLabel = new JLabel("Setpoint");
        private JTextField setpoint = new JTextField();
        private JLabel authorityLabel = new JLabel("Authority");
        private JTextField authority = new JTextField();
        private JButton send = new JButton("Send");

        DispatcherPanel()
        {
            this.setLayout(new GridBagLayout());
            initialize();
        }

        private void initialize()
        {
            dispatcherIDLabel = new JLabel(dispatcherID);
System.out.println(dispatcherID);
            setpoint.setColumns(10);
            authority.setColumns(10);
            send.addActionListener(buttonClick);
            addComponent(this, map, 0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(this, dispatcherIDLabel, 0, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, trainID, 0, 2, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, trains, 1, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, setpointLabel, 0, 3, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, setpoint, 1, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);      
            addComponent(this, authorityLabel, 0, 4, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, authority, 1, 4, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, send, 2, 4, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        }

        ActionListener buttonClick = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                sendCommands();
            }
        };

        public void sendCommands()
        {
            control.setCommands(Integer.parseInt(setpoint.getText()), Integer.parseInt(authority.getText()));
        }
        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.BLUE);
            g.fillRect(0,0, width, height);
        }
    }
    private class OperatorPanel extends JPanel
    {
        
        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.GREEN);
            g.fillRect(0,0, width, height);
        }
    }
    private class TrainPanel extends JPanel
    {
        
        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.MAGENTA);
            g.fillRect(0,0, width, height);
        }
    }
    private class TrackPanel extends JPanel
    {
        
        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.ORANGE);
            g.fillRect(0,0, width, height);
        }
    }
    private class MetricsPanel extends JPanel
    {
        
        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.YELLOW);
            g.fillRect(0,0, width, height);
        }
    }
    private class SplashPanel extends JPanel
    {
        
        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.WHITE);
            g.fillRect(0,0, width, height);
        }
    }
    
    
    
    private void addComponent(Container c, Component comp, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill)
    {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, c.getInsets(), 0, 0);
        c.add(comp, gbc);
    }
    
    public int getClockRate()
    {
        return clockRate;
    }
    
    public boolean getDemo()
    {
        return demoMode;
    }

    public boolean getIsOpen()
    {
        return isOpen;
    }
}