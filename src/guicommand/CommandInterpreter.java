/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guicommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author varman
 */
public class CommandInterpreter extends SwingWorker<String, String> {
    private final String    command;
    private final JButton   btnExecute;
    private final JButton   btnCancel;
    private final JTextArea txaCommandOutput;
    private final JLabel    lbStatus;
    
    public CommandInterpreter(String command, JButton btnExecute, 
                              JTextArea txaCommandOutput, JButton btnCancel,
                              JLabel lbStatus) {
        this.command          = command;
        this.btnExecute       = btnExecute;
        this.btnCancel        = btnCancel;
        this.txaCommandOutput = txaCommandOutput;
        this.lbStatus         = lbStatus;
    }

    @Override
    protected String doInBackground() throws Exception {
        String output = null;
        try {
            Process p = Runtime.getRuntime().exec(command);
            
            // normal output
            BufferedReader bufferReader = 
                new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            // error output
            BufferedReader errBufferReader = 
                new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            while((output = bufferReader.readLine()) != null) {
                publish(output);
            }    
            
            while((output = errBufferReader.readLine()) != null) {
                publish(output);
            }
        } catch (IOException e) {
            Logger.getLogger(CommandInterpreter.class.getName()).log(Level.SEVERE, null, e);
        }        
        return "successfully executed!!!";
    }
    
    @Override
    protected void process(List<String> publishedLines) {
      for(int i = 0; i < publishedLines.size(); i++) {
          txaCommandOutput.append(publishedLines.get(i) + "\n");
      }
    } 
    
    @Override
    protected void done() {
        btnExecute.setEnabled(true);
        btnCancel.setEnabled(false);
        String lastOutput = null;
        try {
            lastOutput = get();
        } catch(InterruptedException ex) {
            Logger.getLogger(CommandInterpreter.class.getName()).log(Level.SEVERE, null, ex);
            lbStatus.setText("interrupted before command is done.");
        } catch(ExecutionException ex) {
            Logger.getLogger(CommandInterpreter.class.getName()).log(Level.SEVERE, null, ex);
            lbStatus.setText("Unexpected error while executing the command.");
        } catch(CancellationException ex) {
            Logger.getLogger(CommandInterpreter.class.getName()).log(Level.SEVERE, null, ex);
            lbStatus.setText("cancelled by user.");
        }
        lbStatus.setText(lastOutput);
    }
    
}
