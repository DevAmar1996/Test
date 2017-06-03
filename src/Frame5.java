
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Frame5 extends javax.swing.JFrame implements Runnable {

    /**
     * Creates new form Frame5
     */
    public InputStreamReader isr;
    public InputStream is;
    public BufferedReader br;
    public OutputStream os;
    public OutputStreamWriter osw;
    public PrintWriter bw;
    public Socket socket;
    public boolean quit = false;
    String name;
    int numOfPlayers;
    int stars;
    String level;
    Thread th;
    String winners[];
    public void connectToServer() {
        try {
            InetAddress address = InetAddress.getByName(ServerConnection.serverIP);
            socket = new Socket(address, 3333);
            initialReadAndWrite();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Frame1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Frame1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialReadAndWrite() {
        try {
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new PrintWriter(osw);
        } catch (IOException ex) {
            Logger.getLogger(Frame1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Frame5(String name, int numOfPlayers, int stars, String level) {
        initComponents();
        getContentPane().setBackground(new Color(234, 240, 237));
        connectToServer();
        th = new Thread(this);
        th.start(); // read all message that come from server
        getHighFivePlayer();
        jLabel14.setText("Welcome " + name + " (" + stars + ") ");
        this.name = name;
        this.numOfPlayers = numOfPlayers;
        this.stars = stars;
        this.level = level;
    }

    public void getHighFivePlayer() {
        //get high five players score
        bw.println("Winners");
        bw.flush();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        firstPlayer = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        fivthPlayer = new javax.swing.JLabel();
        fourthPlayer = new javax.swing.JLabel();
        thirdPlayer = new javax.swing.JLabel();
        secondPlayer = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(603, 415));
        setSize(new java.awt.Dimension(580, 415));

        firstPlayer.setFont(new java.awt.Font("Hannotate SC", 1, 18)); // NOI18N
        firstPlayer.setForeground(new java.awt.Color(244, 75, 98));
        firstPlayer.setText("jLabel5");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Capture3.PNG"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel14.setText("Welcome");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(53, 172, 149));
        jLabel4.setText("Top 5 Players");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stars.PNG"))); // NOI18N
        jLabel7.setText("jLabel2");

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setText("Let's GO!");
        jButton1.setSize(new java.awt.Dimension(110, 48));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        fivthPlayer.setFont(new java.awt.Font("Hannotate SC", 1, 18)); // NOI18N
        fivthPlayer.setForeground(new java.awt.Color(244, 75, 98));
        fivthPlayer.setText("jLabel5");

        fourthPlayer.setFont(new java.awt.Font("Hannotate SC", 1, 18)); // NOI18N
        fourthPlayer.setForeground(new java.awt.Color(244, 75, 98));
        fourthPlayer.setText("jLabel5");

        thirdPlayer.setFont(new java.awt.Font("Hannotate SC", 1, 18)); // NOI18N
        thirdPlayer.setForeground(new java.awt.Color(244, 75, 98));
        thirdPlayer.setText("jLabel5");

        secondPlayer.setFont(new java.awt.Font("Hannotate SC", 1, 18)); // NOI18N
        secondPlayer.setForeground(new java.awt.Color(244, 75, 98));
        secondPlayer.setText("jLabel5");

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stars.PNG"))); // NOI18N
        jLabel20.setText("jLabel2");

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stars.PNG"))); // NOI18N
        jLabel21.setText("jLabel2");

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stars.PNG"))); // NOI18N
        jLabel22.setText("jLabel2");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stars.PNG"))); // NOI18N
        jLabel23.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(firstPlayer)
                            .addGap(25, 25, 25))
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(fourthPlayer))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(secondPlayer)
                                        .addGap(53, 53, 53)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(thirdPlayer))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(73, 73, 73)
                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(fivthPlayer)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(217, 217, 217)))
                        .addGap(69, 69, 69))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(60, 60, 60))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(40, 40, 40)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstPlayer)
                    .addComponent(jLabel7)
                    .addComponent(secondPlayer)
                    .addComponent(thirdPlayer)
                    .addComponent(jLabel21)
                    .addComponent(jLabel23))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fourthPlayer)
                    .addComponent(jLabel20)
                    .addComponent(jLabel22)
                    .addComponent(fivthPlayer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            quit = true;
            th.stop();
            socket.close();
            this.setVisible(false);
            new Frame3(name, numOfPlayers, stars, level,winners).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(Frame5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel firstPlayer;
    private javax.swing.JLabel fivthPlayer;
    private javax.swing.JLabel fourthPlayer;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel secondPlayer;
    private javax.swing.JLabel thirdPlayer;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (!quit) {
            try {
                //read message from server
                String message = br.readLine();
                if (message != null) {
                    if (message.contains("offline")) {
                        this.numOfPlayers = Integer.parseInt(message.split(":")[2]);
                    }
                    if (message.contains("Winner")) {
                        winners = message.split(":");
                        firstPlayer.setText(winners[1]);
                        secondPlayer.setText(winners[2]);
                        thirdPlayer.setText(winners[3]);
                        fourthPlayer.setText(winners[4]);
                        fivthPlayer.setText(winners[5]);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(Frame1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}