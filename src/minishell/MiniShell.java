
package minishell;

/**
 *
 * @author Anthony
 */
public class MiniShell {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.launch(args);
    }
    
}
