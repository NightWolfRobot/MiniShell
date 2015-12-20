package minishell;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author Anthony
 * @author Jessica
 */
public class Controller {

    private static ArrayList<Command> list_thread = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public static synchronized void addCmd(Command cmd) {
        list_thread.add(cmd);
    }

    public static synchronized void removeCmd(Command cmd) {
        list_thread.remove(cmd);
    }

    public static void print_threads() {
        if (list_thread.isEmpty()) {
            System.out.print("\tOnly the main thread is running\n");
        } else {
            for (Command cmd : list_thread) {
                System.out.println("\t" + cmd.toString());
            }
        }
    }

    public static void interrupt_thread(int pid) {
        if (list_thread.isEmpty()) {
            System.out.println("\tError: EmptyList\n");
        } else {
            for (Command cmd : list_thread) {
                cmd.interrupt();
            }
        }
    }

    public void launch(String[] args) {
        while (true) {
            System.out.print(">> MiniShell$: ");
            String str = sc.nextLine();

            // On sépare les différentes actions à enchainer ";"
            String[] actions_str = str.split(";");
            for (int i = 0; i < actions_str.length; i++) {
                actions_str[i] = actions_str[i].trim();

                Pattern p1, p2;
                p1 = Pattern.compile(">");
                p2 = Pattern.compile(">>");

                if (p1.matcher(actions_str[i]).find() || p2.matcher(actions_str[i]).find()) {
                    boolean b = false;
                    String[] redirect;
                    if(p2.matcher(actions_str[i]).find()){
                        b= true;
                    }
                    else {
                        b= false;
                    }
                    
                    if(!b){
                        redirect = actions_str[i].split(">");
                    }
                    else{
                        redirect = actions_str[i].split(">>");
                    }
                    
                    redirect[0] = redirect[0].trim();
                    redirect[1] = redirect[1].trim();

                    String[] tokenized_str = redirect[0].split(" ");
                    String[] token_str_path = redirect[1].split(" ");
                    
                    if (token_str_path.length>1 && token_str_path[1].equals("&")) {
                        String[] str2 = new String[tokenized_str.length - 1];
                        int cmpt = 0;
                        for (String token : tokenized_str) {
                            str2[cmpt] = tokenized_str[cmpt];
                            cmpt++;
                            if (cmpt == tokenized_str.length - 1) {
                                break;
                            }
                        }
                        Command cmd = new Command(tokenized_str, b, token_str_path[0]);
                        cmd.start();
                    } else {
                        try {
                            FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/" + redirect[1], b);
                            BufferedWriter output = new BufferedWriter(fw);
                            output.write(Core.executeCommand(tokenized_str));
                            output.flush();
                            output.close();
                            //System.out.println("fichier créé");
                        } catch (Exception e) {
                            e.printStackTrace();
                            //System.out.println("Error: impossible d'écrire dans le fichier à la suite");
                        }
                    }

                } else {
                    // On tokenize la commande à executer pour envoyer un tableau d'args
                    String[] tokenized_str = actions_str[i].split(" ");
                    if (tokenized_str[tokenized_str.length - 1].equals("&")) {
                        String[] str2 = new String[tokenized_str.length - 1];
                        int cmpt = 0;
                        for (String token : tokenized_str) {
                            str2[cmpt] = tokenized_str[cmpt];
                            cmpt++;
                            if (cmpt == tokenized_str.length - 1) {
                                break;
                            }
                        }
                        Command cmd = new Command(str2);
                        cmd.start();
                    } else {
                        System.out.println("\t" + Core.executeCommand(tokenized_str));
                    }
                }

            }

        }
    }

}
