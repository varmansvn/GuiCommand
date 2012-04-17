/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guicommand;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author varman
 */
public class GuiCommand extends JFrame {
    private JLabel lbStatus;
    private JTextField txfCommandInput;
    private JTextArea txaCommandOutput;
    private JButton btnExecute;
    private JButton btnCancel;
    private CommandInterpreter cmdInterpreter;
    
    public GuiCommand() {        
        super("Command through GUI");
        initComponents();
        layoutPanel();
    }
    
    private void initComponents() {
        lbStatus          = new JLabel("Idle");
        txfCommandInput   = new JTextField(20);
        txaCommandOutput  = new JTextArea(500, 300);
        btnExecute        = new JButton("Execute");
        btnCancel         = new JButton("Cancel");
    }
    
    private void layoutPanel() {
        setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        northPanel.add(new JLabel("Command Input"));
        northPanel.add(txfCommandInput);
        
        btnExecute.addActionListener(
            new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command;
                command = txfCommandInput.getText();
                if(0 == command.length()){
                    return;
                }
                txaCommandOutput.setText("");
                lbStatus.setText("executing...");
                btnExecute.setEnabled(false);
                btnCancel.setEnabled(true);                
                cmdInterpreter = new CommandInterpreter(command, btnExecute, 
                                      txaCommandOutput, btnCancel,lbStatus);
                cmdInterpreter.execute();
            }
            }
        );
        northPanel.add(btnExecute);

        txaCommandOutput.setEditable(false);
        add(new JScrollPane(txaCommandOutput,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
       
        JPanel southPanel = new JPanel();
        btnCancel.setEnabled(false);
        btnCancel.addActionListener(
           new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // TO-DO
                // need to send CTRL + C to process in order to
                // to kill the process
                
                cmdInterpreter.cancel(true);
            }
        }
        );
        
        southPanel.add(btnCancel);
        southPanel.add(lbStatus);
        
        add(northPanel, BorderLayout.NORTH);
        add(txaCommandOutput, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);        
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(true);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GuiCommand guiCommandApp = new GuiCommand();
    }
}
