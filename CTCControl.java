/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CTC;

/**
 *
 * @author AM
 */
public class CTCControl 
{
    private int authority;
    private int setpoint;
    
    CTCControl()
    {
        
    }
    
    public void setCommands(int sp, int auth)
    {
        setpoint = sp;
        authority = auth;
        System.out.println("Setpoint: " + setpoint + "   Authority:  " + authority);
    }
}
