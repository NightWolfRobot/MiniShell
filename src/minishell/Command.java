/* 
 * Creation : 9 déc. 2015
 */
package minishell;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @date 9 déc. 2015
 * @author Anthony CHAFFOT
 * @author Jessica FAVIN
 */
public class Command extends Thread {

    private static int pid_global = 0;
    private int pid = 0;
    private String[] args;
    private boolean saveFile = false;
    private boolean valueSave = false;
    private String path= "";

    //**************************************************************************
    // CONSTRUCTOR
    //**************************************************************************
    public Command(String[] args) {
        pid = pid_global;
        pid_global++;
        this.args = args;
    }

    public Command(String[] args, boolean b, String path) {
        pid = pid_global;
        pid_global++;
        this.args = args;
        saveFile = true;
        valueSave = b;
        this.path = path;
    }

    //**************************************************************************
    // METHODS
    //**************************************************************************
    public void run() {
        Controller.addCmd(this);
        System.out.println("new thread -> PID :" + pid + "\n");
        if (saveFile) {
            try {
                FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/" + path, valueSave);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(Core.executeCommand(args));
                output.flush();
                output.close();
                //System.out.println("fichier créé");
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Error: impossible d'écrire dans le fichier à la suite");
            }
        } else {
            System.out.println("\t" + Core.executeCommand(args));
        }
        Controller.removeCmd(this);
    }

    @Override
    public String toString() {
        return "PID: " + pid + "   | " + args[0] + "\n";
    }

    //**************************************************************************
    // SETTERS / GETTERS
    //**************************************************************************

    public int getPid() {
        return pid;
    }

    public String getCommand() {
        return args[0];
    }

}
