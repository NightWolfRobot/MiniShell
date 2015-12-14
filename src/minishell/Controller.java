package minishell;

import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author Anthony
 * @author Jessica
 */
public class Controller{
    private static ArrayList<Command> list_thread = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public static synchronized void addCmd(Command cmd) {
        list_thread.add(cmd);
    }

    public static synchronized void removeCmd(Command cmd) {
        list_thread.remove(cmd);
    }

    public static void print_threads() {
        if(list_thread.isEmpty()){
            System.out.println("\tError: EmptyList\n");
        }
        else{
            for (Command cmd : list_thread) {
                System.out.println("\t"+cmd.toString());
            }
        }
    }
    
    public void launch(String[] args) {
        while(true){
            System.out.print(">> MiniShell$: ");
            String str = sc.nextLine();
            String[] tokenized_str = str.split(" ");
            
            if(tokenized_str[tokenized_str.length-1].equals("&")){
                String[] str2 = new String[tokenized_str.length-1];
                int cmpt =0;
                for(String token: tokenized_str){
                    str2[cmpt] = tokenized_str[cmpt];
                    cmpt++;
                    if(cmpt == tokenized_str.length-1){
                        break;
                    }
                }
                Command cmd = new Command(str2);
                cmd.start();
            }
            else{
                System.out.println("\t"+Core.executeCommand(tokenized_str));
            }
        }
    }

}