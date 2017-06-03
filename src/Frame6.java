
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
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Frame6 extends javax.swing.JFrame {

    private Object OptionPane;
    public InputStreamReader isr;
    public InputStream is;
    public BufferedReader br;
    public OutputStream os;
    public OutputStreamWriter osw;
    public PrintWriter bw;
    public Socket socket;
    String name;
    String[] players;
    int score;
    String level;
    String[] winners = new String[5];

    /**
     * Creates new form Frame6
     */
    public Frame6(String name, String player1, String[] players, int score, String level) {
        initComponents();
        getContentPane().setBackground(new Color(234, 240, 237));
        this.connectToServer();
        this.name = name;
        this.players = players;
        this.score = score;
        this.level = level;
        username();
        setNames(player1, players);
        getWinners();

    }

    public void getWinners() {
        try {
            bw.println("Winners");
            bw.flush();
            String message = br.readLine();
            if (message.contains("Winner")) {
                String[] winne = message.split(":");
                winners[0] = winne[1];
                winners[1] = winne[2];
                winners[2] = winne[3];
                winners[3] = winne[4];
                winners[4] = winne[5];

            }
        } catch (IOException ex) {
            Logger.getLogger(Frame6.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setNames(String na, String[] players) {
        player1.setText(na + " winner");
        int j = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(na)) {
                i = j;
            }
        }
        if (j == 0) {
            player2.setText(players[1]);
            player3.setText(players[2]);

        }
        if (j == 1) {
            player2.setText(players[0]);
            player3.setText(players[2]);

        }
        if (j == 2) {
            player2.setText(players[0]);
            player3.setText(players[1]);

        }
        username();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        player3 = new javax.swing.JLabel();
        player2 = new javax.swing.JLabel();
        player1 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(580, 415));

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jButton1.setText("Play Again!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Capture3.PNG"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setText("Welcome");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(53, 172, 149));
        jLabel13.setText("Game's Result");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png"))); // NOI18N
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Brain.PNG"))); // NOI18N

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jButton2.setText("End Game!");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        player3.setFont(new java.awt.Font("Hannotate SC", 1, 18)); // NOI18N
        player3.setForeground(new java.awt.Color(244, 75, 98));
        player3.setText("jLabel5");

        player2.setFont(new java.awt.Font("Hannotate SC", 1, 18)); // NOI18N
        player2.setForeground(new java.awt.Color(244, 75, 98));
        player2.setText("jLabel5");

        player1.setFont(new java.awt.Font("Hannotate SC", 1, 18)); // NOI18N
        player1.setForeground(new java.awt.Color(244, 75, 98));
        player1.setText("jLabel5");

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stars.PNG"))); // NOI18N
        jLabel24.setText("jLabel2");

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stars.PNG"))); // NOI18N
        jLabel25.setText("jLabel2");

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Stars.PNG"))); // NOI18N
        jLabel26.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(player1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(player2)
                        .addGap(100, 100, 100)
                        .addComponent(player3)
                        .addGap(112, 112, 112))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(34, 34, 34))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(87, 87, 87))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(89, 89, 89)
                                        .addComponent(jLabel13)))))))
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player3)
                    .addComponent(player1)
                    .addComponent(player2)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Frame6.this.setVisible(false);
        new Frame3(name, players.length, score, level, winners).setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed
    public void connectToServer() {
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            socket = new Socket(address, 3333);
            initialReadAndWrite();

        } catch (UnknownHostException ex) {
            Logger.getLogger(Frame1.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Frame1.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Frame1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        Frame5_1 fm = new Frame5_1();
        fm.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        JOptionPane.showMessageDialog(null, "are you sure you want to exit", "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }//GEN-LAST:event_jLabel14MouseClicked
    public void username() {
        jLabel12.setText("Welcome " + name);
    }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel player1;
    private javax.swing.JLabel player2;
    private javax.swing.JLabel player3;
    // End of variables declaration//GEN-END:variables
}
