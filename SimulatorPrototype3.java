/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatorprototype3;

import CTC.CTCView;
import java.lang.Math;

public class SimulatorPrototype3 
{
    private static CTCView view;
    private static int clockRate = 60;
    private static boolean demoMode = false;
    private static int demoEvent = 0;
    private static boolean isOpen;
    
    public static void main(String[] args) 
    {
        
        view = new CTCView();
        isOpen = true;
        
        while(isOpen)
        {
            int sleeptime = (int)(Math.ceil(clockRate/60.0));
            run();
            sleep(sleeptime);
        }
    }
    
    private static void run()
    {
        clockRate = view.getClockRate();
        demoMode = view.getDemo();
        isOpen = view.getIsOpen();
        
        if(demoMode)
        {
            switch(demoEvent)
            {
                case 0:
                    break;
                default:
                    break;
            }
            demoEvent++;
        }
        else
        {
            
            demoEvent = 0;
        }
    }
    
    public static void sleep(long milliseconds) 
    {
        Thread thread = new Thread();
        
        try 
        { 
            thread.sleep(milliseconds); 
        }
        catch (Exception e) 
        {
        }
    }
}
