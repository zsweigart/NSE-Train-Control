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
        private Insets insets = new Insets(0,0,0,0);

        MainPanel()
        {
            this.setLayout(new GridBagLayout());
            initialize();
        }

        private void initialize()
        {
            addComponent(this, map, 0, 0, 2, 1, 1.0, 1.0, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(this, clockLabel, 0, 1, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, clockCombo, 1, 1, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
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
        private Insets insets = new Insets(0,0,0,0);

        DispatcherPanel()
        {
            this.setLayout(new GridBagLayout());
            initialize();
        }

        private void initialize()
        {
            dispatcherIDLabel = new JLabel(dispatcherID);
            setpoint.setColumns(10);
            authority.setColumns(10);
            send.addActionListener(buttonClick);
            addComponent(this, map, 0, 0, 3, 1, 1.0, 1.0, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(this, dispatcherIDLabel, 0, 1, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, trainID, 0, 2, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, trains, 1, 2, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, setpointLabel, 0, 3, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, setpoint, 1, 3, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);      
            addComponent(this, authorityLabel, 0, 4, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, authority, 1, 4, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, send, 2, 4, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
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
            g.setColor(Color.WHITE);
            g.fillRect(0,0, width, height);
        }
    }
    private class OperatorPanel extends JPanel
    {
        private MapPanel map = new MapPanel(model);
        private JLabel trainID = new JLabel("Train ID");
        private JComboBox trains = new JComboBox();
        private JLabel speedLabel = new JLabel("Speed");
        private JTextField operatorSpeed = new JTextField();
        private JLabel brakeLabel = new JLabel ("Brake");
        private JCheckBox brake = new JCheckBox("");
        private JButton send = new JButton("Send");
        private Insets insets = new Insets(0,0,0,0);

        OperatorPanel()
        {
            this.setLayout(new GridBagLayout());
            initialize();
        }

        private void initialize()
        {
            operatorSpeed.setColumns(10);
            send.addActionListener(buttonClick);
            addComponent(this, map, 0, 0, 3, 1, 1.0, 1.0, insets, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(this, trainID, 0, 1, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, trains, 1, 1, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, speedLabel, 0, 2, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, operatorSpeed, 1, 2, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, brakeLabel, 0, 3, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, brake, 1, 3, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, send, 2, 3, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
        }

        ActionListener buttonClick = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                sendOperatorCommands();
            }
        };

        public void sendOperatorCommands()
        {
            control.setOperatorCommands(Integer.parseInt(operatorSpeed.getText()), brake.isSelected());
        }
        
        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.WHITE);
            g.fillRect(0,0, width, height);
        }
    }
    private class TrainPanel extends JPanel
    {
        private JLabel currentTrainsLabel = new JLabel("Current Trains");
        private JLabel trainYardSelectLabel = new JLabel("Add new train from train yard: ");
        private JLabel nextDestinationLabel = new JLabel("Next Destination");
        private JLabel heightLabel = new JLabel("Height");
        private JLabel widthLabel = new JLabel("Width");
        private JLabel numCarsLabel = new JLabel("Number of Cars");
        private JLabel lengthLabel = new JLabel("Length");
        private JLabel massLabel = new JLabel("Mass");
        private JLabel crewCountLabel = new JLabel("Crew Count");
        private JLabel passengerCountLabel = new JLabel("Passenger Count");
        private JLabel currentSpeedLabel = new JLabel("Current Speed");
        private JLabel currentAccelerationLabel = new JLabel("Current Acceleration");
        private JLabel messageLabel = new JLabel("Message Displayed");
        private JLabel lightLabel = new JLabel("Lights on");
        private JLabel doorLabel = new JLabel("Doors Open");
        private JLabel brakeFailLabel = new JLabel("Brake Failure");
        private JLabel engineFailLabel = new JLabel("Engine Failure");
        private JLabel signalPickupFailLabel = new JLabel("Signal Pickup Failure");
        private JLabel trainIDLabel = new JLabel("Train ID");
        private JComboBox currentTrains = new JComboBox();
        private JComboBox trainYard = new JComboBox();
        private JComboBox nextDestination = new JComboBox();
        private JTextField heightField = new JTextField();
        private JTextField widthField = new JTextField();
        private JTextField numCarsField = new JTextField();
        private JTextField lengthField = new JTextField();
        private JTextField massField = new JTextField();
        private JTextField crewCountField = new JTextField();
        private JTextField passengerCountField = new JTextField();
        private JTextField currentSpeedField = new JTextField();
        private JTextField currentAccelerationField = new JTextField();
        private JTextField messageField = new JTextField();
        private JTextField trainIDField = new JTextField();
        private JCheckBox lightCheck = new JCheckBox();
        private JCheckBox doorCheck = new JCheckBox();
        private JCheckBox brakeFailCheck = new JCheckBox();
        private JCheckBox engineFailCheck = new JCheckBox();
        private JCheckBox signalPickupFailCheck = new JCheckBox();
        private JButton emergencyBrakeButton = new JButton("Emergency Brake");
        private JButton addTrainButton = new JButton("Add Train");
        private JButton removeTrainButton = new JButton("Remove Train");
        private Insets insets = new Insets(5,20,0,20);
        
        TrainPanel()
        {
            this.setLayout(new GridBagLayout());
            initialize();
        }

        private void initialize()
        {
            heightField.setColumns(10);
            widthField.setColumns(10);
            numCarsField.setColumns(10);
            lengthField.setColumns(10);
            massField.setColumns(10);
            crewCountField.setColumns(10);
            passengerCountField.setColumns(10);
            currentSpeedField.setColumns(10);
            currentAccelerationField.setColumns(10);
            messageField.setColumns(10);
            trainIDField.setColumns(10);
            
            addComponent(this, currentTrainsLabel, 0, 0, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, currentTrains, 1, 0, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.BOTH);
            addComponent(this, trainYardSelectLabel, 2, 0, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, trainYard, 3, 0, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.BOTH);
            addComponent(this, trainIDLabel, 2, 1, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, trainIDField, 3, 1, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, nextDestinationLabel, 0, 2, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, nextDestination, 1, 2, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.BOTH);
            addComponent(this, heightLabel, 0, 3, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, heightField, 1, 3, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, widthLabel, 0, 4, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, widthField, 1, 4, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, numCarsLabel, 0, 5, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, numCarsField, 1, 5, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, lengthLabel, 0, 6, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, lengthField, 1, 6, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, massLabel, 0, 7, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, massField, 1, 7, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, crewCountLabel, 0, 8, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, crewCountField, 1, 8, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, passengerCountLabel, 0, 9, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, passengerCountField, 1, 9, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, currentSpeedLabel, 0, 10, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, currentSpeedField, 1, 10, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, currentAccelerationLabel, 0, 11, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, currentAccelerationField, 1, 11, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, messageLabel, 0, 12, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, messageField, 1, 12, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, lightLabel, 0, 13, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, lightCheck, 1, 13, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, doorLabel, 0, 14, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, doorCheck, 1, 14, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, brakeFailLabel, 0, 15, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, brakeFailCheck, 1, 15, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, engineFailLabel, 0, 16, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, engineFailCheck, 1, 16, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, signalPickupFailLabel, 0, 17, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, signalPickupFailCheck, 1, 17, 1, 1, 0, 0, insets, GridBagConstraints.WEST, GridBagConstraints.NONE);
            addComponent(this, emergencyBrakeButton, 1, 18, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, addTrainButton, 2, 18, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            addComponent(this, removeTrainButton, 3, 18, 1, 1, 0, 0, insets, GridBagConstraints.EAST, GridBagConstraints.NONE);
            
        }
        
        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.WHITE);
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
    
    
    
    private void addComponent(Container c, Component comp, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, Insets i, int anchor, int fill)
    {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, i, 0, 0);
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