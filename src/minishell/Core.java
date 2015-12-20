package minishell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author Jessica Favin
 * @author Anthony CHAFFOT
 */
public class Core {

    public static String executeCommand(String[] args) {

        if (args.length == 0) {
            return "\n";
        } else if (args[0].equals("exit")) {
            return executeCommandExit(args);
        } else if (args[0].equals("pwd")) {
            return executeCommandPwd(args);
        } else if (args[0].equals("ls")) {
            return executeCommandLs(args);
        } else if (args[0].equals("cd")) {
            return executeCommandCd(args);
        } else if (args[0].equals("date")) {
            return executeCommandDate(args);
        } else if (args[0].equals("cmpt")) {
            return executeCommandCompteJusqua(args);
        } else if (args[0].equals("ps")) {
            return executeCommandPid(args);
        } else if (args[0].equals("kill")) {
            return executeCommandKill(args);
        } else if (args[0].equals("find")) {
            return executeCommandFind(args);
        } else if (args[0].equals("grep")) {
            return executeCommandGrep(args);
        } else if (args[0].equals("sleep")) {
            return executeCommandSleep(args);
        } else if (args[0].equals("sed")) {
            return executeCommandSed(args);
        } else {
            return stringError("Commande introuvable\n");
        }
    }

    public static String executeCommandPid(String[] args) {
        Controller.print_threads();
        return "";
    }

    public static String executeCommandExit(String[] args) {
        if (args.length == 1) {
            System.exit(0);
        }
        //Netbeans considère qu'un return != 0 est une erreur
        /*
         if(args.length==2){
         try{
         int val = Integer.parseInt(args[1]);
         System.exit(val);
         } catch (NumberFormatException e) {
         System.out.println("exit prends aucun ou un entier en argument");
         }
         }
         */
        return stringError("Exit prends aucun ou un entier en argument\n");
    }

    public static String executeCommandGrep(String[] args) {
        String str = new String();

        Pattern p;
        if (args.length == 1) {
            Scanner sc = new Scanner(System.in);
            try {
                //!!!!!!!!!!!Tourne à l'infini malgré Ctrl+D!!!!!!!!!!!!!!!!!!
                while (sc.hasNextLine()) {
                    System.out.println(sc.nextLine());
                }
            } finally {
                sc.close();
            }
            return "";
        }

        args[1] = args[1].replaceAll("'", "");
        int cmpt = 0;
        try {
            p = Pattern.compile(args[1]);
        } catch (Exception e) {
            return stringError("Expression régulière incorrecte");
        }
        if (args.length < 3) {
            return stringError("Nombre d'arguments incorrect");
        }
        for (int i = 2; i <= args.length - 1; i++) {
            //Should replace / with IOUtils.DIR_SEPERATOR
            File file = new File(System.getProperty("user.dir") + "/" + args[i]);
            if (file.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                    String sCurrentLine;
                    while ((sCurrentLine = br.readLine()) != null) {
                        cmpt++;
                        if (p.matcher(sCurrentLine).find()) {
                            str += "" + cmpt + ": " + sCurrentLine + "\n\t";
                            //System.out.println("\tSucceed Line :" + cmpt + " -> " + sCurrentLine);
                        }
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Erroreeeeeee");
                }
            } else {
                return stringError(args[i] + " n'est pas un fichier");
            }
        }
        return str;
    }
    
    public static String executeCommandSed(String[] args){
        if (args.length<2 || args.length>3) {
            return stringError("sed <format> [<fichier>]");
        }
        
        String str = new String();
        String[] tab = args[1].split(args[1].charAt(1)+"");
        Pattern p;
        tab[1] = tab[1].replaceAll("'", "");
        try {
            p = Pattern.compile(tab[1]);
        } catch (Exception e) {
            return stringError("Expression régulière incorrecte");
        }
        
        if (args.length == 2) {
            Scanner sc = new Scanner(System.in);
            try {
                //how do I stop the loop?
                while (sc.hasNextLine()) {
                    if(tab.length==4 && tab[3].equals("g")){
                        System.out.println(sc.nextLine().replaceAll(tab[1],tab[2]));
                    } else {
                        System.out.println(sc.nextLine().replaceFirst(tab[1],tab[2]));
                    }
                }
            } finally {
                sc.close();
            }
            return "";
        }

        File file = new File(System.getProperty("user.dir") + "/" + args[2]);
        if (file.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                String sCurrentLine;
                String total = "";
                while ((sCurrentLine = br.readLine()) != null) {
                    total+=sCurrentLine+"\n";
                }
                if(tab.length==4 && tab[3].equals("g")){
                    total = total.replaceAll(tab[1],tab[2]);
                } else {
                    total = total.replaceFirst(tab[1],tab[2]);
                }
                FileWriter fw = new FileWriter(file.getAbsolutePath());
                fw.write(total);
                fw.close();
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Erroreeeeeee");
            }
        } else {
            return stringError(args[2] + " n'est pas un fichier");
        }
        
        return "";
    }

    public static String executeCommandSleep(String[] args) {
        try {
            int n= Integer.parseInt(args[1]);
            Thread.sleep(n * 1000);
            return "";
        } catch (Exception ex) {
            System.err.println("Error dans le sleep");
            return "";
        }
    }

    public static String executeCommandFind(String[] args) {
        String str = new String();

        if (args.length != 4) {
            return stringError("Nombre d'arguments incorrects");
        } else {
            if (args[2].equals("-name") || args[2].equals("-iname")) {
                System.out.println(System.getProperty("user.dir") + "/" + args[1]);
                File dossier = new File(System.getProperty("user.dir") + "/" + args[1]);
                if (dossier.isDirectory()) {
                    String[] list = dossier.list();
                    args[3] = args[3].replaceAll("'", "");
                    //System.out.println("Pattern : "+args[3]);

                    for (String elem : list) {
                        try {
                            Pattern p;
                            if (args[2].equals("-iname")) {
                                p = Pattern.compile(args[3], Pattern.CASE_INSENSITIVE);
                            } else {
                                p = Pattern.compile(args[3]);
                            }
                            if (p.matcher(elem).matches()) {
                                str += elem + "\n\t";
                            }
                        } catch (Exception e) {
                            return stringError("L'expression régulière n'est pas correcte");
                        }

                    }
                } else {
                    return stringError("Le dossier n'existe pas");
                }

            } else {
                return stringError("Argument " + args[2] + " invalide");
            }
        }

        return str;
    }

    public static String stringError(String str) {
        return "Error: " + str;
    }

    public static String executeCommandCompteJusqua(String[] args) {
        int cmpt = 0;
        if (args.length == 1) {
            return stringError("compteJusqua prend un argument\n");
        } else if (args.length == 2 || args.length == 3) {
            int nb = Integer.parseInt(args[1]);
            //Integer cmpt = 1;
            int temp = 0;

            for (temp = 0; temp < nb; temp++) {
                try {
                    Thread.sleep(1000);
                    cmpt++;
                    System.out.println("\t" + cmpt);
                } catch (Exception ex) {
                    break;
                }
                //c.print(""+cmpt);
            }
        }
        return "";
    }

    public static String executeCommandDate(String[] args) {
        if (args.length == 2) {
            Pattern p = Pattern.compile("[+]([%][dHmMY][-/]*)*");
            if (!p.matcher(args[1]).matches()) {
                return stringError("Format incorrect");
            } else {
                SimpleDateFormat date = new SimpleDateFormat();
                Calendar cal = Calendar.getInstance();
                String pattern = args[1];
                pattern = pattern.replaceAll("%Y", "yyyy");
                pattern = pattern.replaceAll("%d", "dd");
                pattern = pattern.replaceAll("%m", "mm");
                pattern = pattern.replace("+", "");
                pattern = pattern.replaceAll("%M", "MM");
                //System.out.println(pattern);
                date.applyPattern(pattern);
                String str = date.format(cal.getTime());
                return str + "\n";
            }

        } else if (args.length == 1) {
            SimpleDateFormat date = new SimpleDateFormat();
            Calendar cal = Calendar.getInstance();
            date.applyPattern("yyyy-MM-dd");
            String str = date.format(cal.getTime());

            return str + "\n";
        } else {
            return stringError("Nombre d'arguments incorrect.\n");
        }
    }

    public static String executeCommandKill(String[] args) {
        if (args.length == 2) {
            int pid = Integer.parseInt(args[1]);
            //AJOUTER TRY CATCH
            Controller.interrupt_thread(pid);
            return "";
        } else {
            return stringError("Nombre d'arguments incorrect.\n");
        }
    }

    public static String executeCommandPwd(String[] args) {
        if (args.length != 1) {
            return stringError("pwd ne prends pas d'arguments\n");
        } else {
            return System.getProperty("user.dir") + '\n';
        }
    }

    public static String executeCommandLs(String[] args) {
        if (args.length != 1) {
            return stringError("ls ne prends pas d'arguments\n");
        } else {
            File dossier = new File(System.getProperty("user.dir"));
            String[] list = dossier.list();
            String res = "";
            for (String elem : list) {
                res += elem + "\n\t";
            }
            return res;
        }
    }

    public static String executeCommandCd(String[] args) {
        if (args.length != 2) {
            return stringError("Nombre d'args incorrect, essayer : cd <chemin_dossier>\n");
        } else {
            File dossier = new File(System.getProperty("user.dir") + "/" + args[1]);
            if (!dossier.isDirectory()) {
                String i = "";
                try {
                    i = dossier.getCanonicalPath();
                } catch (IOException en) {
                    //c.print_err("Donnez un dossier en argument.\n");
                    return stringError("Donnez un dossier en argument.\n");
                }
                System.out.println("Donnez un dossier en argument\n");
                //System.out.println(i + " " + dossier.isFile() + " " + dossier.isDirectory() + " " + dossier.isHidden());

                //return 1;
            } else {
                try {
                    System.setProperty("user.dir", dossier.getCanonicalPath());
                    return System.getProperty("user.dir") + '\n';
                } catch (IOException e) {
                    //c.print_err("Ouverture de fichier\n");
                    return stringError("Ouverture de fichier");
                }
            }
        }
        return "";
    }
}
