/* 
 * Creation : 9 déc. 2015
 */
package minishell;


/**
 * @date 9 déc. 2015
 * @author Anthony CHAFFOT
 * @author Jessica FAVIN
 */
public class Command extends Thread{
    private static int pid_global = 0;
    private int pid = 0;
    private String[] args;
    
    //**************************************************************************
    // CONSTRUCTOR
    //**************************************************************************

    public Command(String[] args) {
        pid = pid_global;
        pid_global ++;
        this.args = args;
    }

    //**************************************************************************
    // METHODS
    //**************************************************************************
    public void run() {
        Controller.addCmd(this);
        System.out.println("new thread -> PID :"+ pid+"\n");
        System.out.println("\t"+Core.executeCommand(args));
        Controller.removeCmd(this);
    }
    
    @Override
    public String toString(){
        return "PID: "+pid+"   | "+ args[0]+"\n";
    }
    //**************************************************************************
    // SETTERS / GETTERS
    //**************************************************************************
    public int getPid(){
        return pid;
    }

    public String getCommand(){
        return args[0];
    }

}
