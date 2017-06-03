
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import javax.swing.event.MouseInputAdapter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jori
 */
public class EasyGame extends javax.swing.JFrame implements Runnable {

    /**
     * Creates new form EasyGame
     */
    public InputStreamReader isr;
    public InputStream is;
    public BufferedReader br;
    public OutputStream os;
    public OutputStreamWriter osw;
    public PrintWriter bw;
    public Socket socket;
    public boolean quit = false;
    public String name;
    public int numOfPlayers;
    public int stars;
    public String level;
    public int fileNum;
    public String players[] = new String[3];
    private JLabel b1[][] = new JLabel[4][4];//each button present part of photo
    private JButton b2[][] = new JButton[4][4];
    private int nums[] = new int[4];
    private int Piecenums[] = {0, 0, 0, 0};
    Thread th;
    boolean yourTurn = false;
    boolean begin = true;
    MouseListener ms;
    int from;
    int to;
    int i, j;
    boolean send = false;
    String[] winners;

    public EasyGame(String name, int numOfPlayers, int stars, String level, int fileNum, String players[], int nums[], String[] winners) {
        initComponents();
        getContentPane().setBackground(new Color(234, 240, 237));
        this.name = name;
        this.numOfPlayers = numOfPlayers;
        this.stars = stars;
        this.level = level;
        jLabel12.setText("Welcome " + name + " (" + stars + ") ");
        this.fileNum = fileNum;
        this.players = players;
        this.nums = nums;
        this.winners = winners;
        connectToServer();
        bw.println("anyOrder:" + level);
        bw.flush();
        setPlayers();
        makePuzzels();
        setTurn();
        setFivePlayers();
        th = new Thread(this);
        th.start();
    }

    public void setFivePlayers() {
        highFivePlayer.setModel(new javax.swing.AbstractListModel<String>() {

            public int getSize() {
                return winners.length;
            }

            public String getElementAt(int i) {
                if(i == 0){
                    return "topFive";
                }
                return winners[i];
            }
        });
    }

    public void setPlayers() {
        //function to put player name in the label
        player1.setForeground(Color.GREEN);
        player2.setForeground(Color.GREEN);
        player3.setForeground(Color.GREEN);
        player1.setText(players[0]);
        player2.setText(players[1]);
        player3.setText(players[2]);
    }

    public void setTurn() {
        //check if he is the player that must start the game
        bw.println("turn:" + name);
        bw.flush();
    }

    public void makePuzzels() {
        //function to put photo in the jpanel
        //split panels int 4 pices to put image pice on it
        ms = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (yourTurn == true) {
                    if ((e.getSource() instanceof JButton)) {
                        int x = e.getLocationOnScreen().x;
                        int y = e.getLocationOnScreen().y;
                        for (int i = 0; i < 2; i++) {
                            for (int j = 0; j < 2; j++) {
                                int x1 = b2[j][i].getLocationOnScreen().x;
                                int y1 = b2[j][i].getLocationOnScreen().y;
                                int width = b2[j][i].getPreferredSize().width;
                                int height = b2[j][i].getPreferredSize().height;
                                //    System.out.println(x1 + "  " + width + "   " + x);
                                //  System.out.println(y1 + "  " + height + "   " + y);

                                if (x >= x1 && x <= x1 + width && y >= y1 && y <= y1 + height) {
                                    //if (x1 == x && y1 == y) {
                                    if (i == 0) {
                                        from = j;
                                    } else {
                                        from = j + (2 * i);
                                    }
                                    send = false;
                                    break;
                                }
                            }
                        }
                        JComponent jc = (JComponent) e.getSource();
                        TransferHandler th = jc.getTransferHandler();
                        th.exportAsDrag(jc, e, TransferHandler.COPY);
                    }
                } else if (begin) {
                    JOptionPane.showMessageDialog(null, "please Wait it is not your turn", "wait", JOptionPane.CANCEL_OPTION);
                    begin = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (yourTurn) {
                    if ((e.getSource() instanceof EasyGame)) {
                        javax.swing.JFrame jc = (javax.swing.JFrame) e.getSource();
                        int x = e.getLocationOnScreen().x;
                        int y = e.getLocationOnScreen().y;
                        for (int i = 0; i < 2; i++) {
                            for (int j = 0; j < 2; j++) {
                                int x1 = b1[j][i].getLocationOnScreen().x;
                                int y1 = b1[j][i].getLocationOnScreen().y;
                                int width = b1[j][i].getPreferredSize().width;
                                int height = b1[j][i].getPreferredSize().height;
                                //   System.out.println(x1 + "  " + width + "   " + x);
                                //   System.out.println(y1 + "  " + height + "   " + y);

                                if (x >= x1 && x <= x1 + width && y >= y1 && y <= y1 + height) {
                                    if (i == 0) {
                                        to = j;
                                    } else {
                                        to = j + (2 * i);
                                    }
                                    if (!send) {

                                        System.out.println("move" + to + from);
                                        //swap the index
                                        Piecenums[to] = nums[from];
                                        bw.println("move:" + from + ":" + to + ":" + name + ":" + level);
                                        bw.flush();
                                        send = true;
                                    }
                                    break;
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        };
        jPanel1.setLayout(new GridLayout(2, 2, 1, 1));
        jPanel2.setLayout(new GridLayout(2, 2, 1, 1));
        int k = 0; //varible to get index of nums array;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                try {

                    //set a new button
                    b2[j][i] = new JButton();
                    //set button size
                    b2[j][i].setPreferredSize(new Dimension(100, 100));
                    int num = nums[k];
                    k++;
                    //read the image as afile
                    Image img = ImageIO.read(new File("Project/p_" + fileNum + "_4_/p_" + fileNum + "_4_" + num + ".png"));
                    //resize the image to be fill in the buttom
                    Image resizedImage = img.getScaledInstance(100, 100, 0);
                    //set button background
                    ImageIcon icn = new ImageIcon(resizedImage);
                    b2[j][i].setIcon(icn);

                    b2[j][i].addMouseListener(ms);
                    //get the item that was clicked 

                    b2[j][i].setTransferHandler(new TransferHandler("icon"));

                    jPanel1.add(b2[j][i]);

                } catch (IOException ex) {
                    Logger.getLogger(EasyGame.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                b1[j][i] = new JLabel();
                b1[j][i].setText(j + "  " + i);
                //set button size
                b1[j][i].setPreferredSize(new Dimension(100, 100));
                b1[j][i].setTransferHandler(new TransferHandler("icon"));
                //   b1[j][i].addMouseListener(ms);
                //add button to the panel
                jPanel2.add(b1[j][i]);

            }
        }
        addMouseListener(ms);

    }

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        player1 = new javax.swing.JLabel();
        player3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        player2 = new javax.swing.JLabel();
        turn = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        highFivePlayer = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(580, 415));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Capture3.PNG"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setText("Welcome");

        player1.setForeground(new java.awt.Color(244, 75, 98));
        player1.setText("Player2");

        player3.setForeground(new java.awt.Color(244, 75, 98));
        player3.setText("Player1");

        jLabel6.setFont(new java.awt.Font("Hannotate SC", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(53, 172, 149));
        jLabel6.setText("pieces");

        jLabel7.setFont(new java.awt.Font("Hannotate SC", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(53, 172, 149));
        jLabel7.setText("Puzzel");

        player2.setForeground(new java.awt.Color(244, 75, 98));
        player2.setText("Player2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 241, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 207, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 207, Short.MAX_VALUE)
        );

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png"))); // NOI18N
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });

        highFivePlayer.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(highFivePlayer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(turn, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(20, 20, 20)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(121, 121, 121))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
            .addGroup(layout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(player1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(player2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(player3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel16)
                .addGap(82, 82, 82)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(211, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(turn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel12)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel6)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player1)
                    .addComponent(player2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(3, 3, 3))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        JOptionPane.showMessageDialog(null, "are you sure you want to exit", "Error", JOptionPane.ERROR_MESSAGE);
        bw.println("Quit:" + name);
        bw.flush();
        quit = true;
        System.exit(0);
    }//GEN-LAST:event_jLabel16MouseClicked

    @Override
    public void run() {
        while (!quit) {
            try {
                String message = br.readLine();
                if (message != null) {
                    if (message.contains("offline")) {
                        this.numOfPlayers = Integer.parseInt(message.split(":")[2]);
                        String na = message.split(":")[1];
                        if (numOfPlayers == 2) {
                            String name1 = player1.getText().toString();
                            String name2 = player2.getText().toString();
                            String name3 = player3.getText().toString();

                            if (name1.contains(na)) {
                                //change the text of the label so appear to other player that this player is offline
                                player1.setForeground(Color.red);
                                player1.setText(na + " offline");
                            }
                            if (name2.contains(na)) {
                                player2.setForeground(Color.red);
                                player2.setText(na + " offline");
                            }
                            if (name3.contains(na)) {
                                player3.setForeground(Color.red);
                                player3.setText(na + " offline");
                            }
                        } else if (numOfPlayers == 1) {
                            // if the player play alone
                            String name1 = player1.getText().toString();
                            String name2 = player2.getText().toString();
                            String name3 = player3.getText().toString();

                            if (name1.contains(na)) {
                                //change the text of the label so appear to other player that this player is offline
                                player1.setForeground(Color.red);
                                player1.setText(na + " offline");
                            }
                            if (name2.contains(na)) {
                                player2.setForeground(Color.red);
                                player2.setText(na + " offline");
                            }
                            if (name3.contains(na)) {
                                player3.setForeground(Color.red);
                                player3.setText(na + " offline");
                            }
                            //exit the game and go to the login screen
                            JOptionPane.showMessageDialog(null, "no one play with you \n try later", "close", JOptionPane.WARNING_MESSAGE);
                            this.setVisible(false);
                            quit = true;
                            socket.close();
                            new Frame1().setVisible(true);
                        }
                    }
                    if (message.contains("yourTurn")) {
                        String na = message.split(":")[1];
                        if (na.equals(name)) {
                            turn.setIcon(new ImageIcon("green.png"));
                            yourTurn = true;
                        } else {
                            turn.setIcon(new ImageIcon("red.png"));
                            yourTurn = false;
                            begin = true;
                        }
                    }
                    if (message.equals("notYourTurn")) {
                        turn.setIcon(new ImageIcon("red.png"));
                        yourTurn = false;
                    }
                    if (message.contains("won")) {
                        String na = message.split(":")[1];
                        if (na.equals(name)) {
                            this.stars += 1;
                        }
                        new Frame6(name, na, players, (this.stars), "Easy").setVisible(true);

                        EasyGame.this.setVisible(false);

                    }
                    if (message.contains("move")) {
                        String msg[] = message.split(":");
                        int from = Integer.parseInt(msg[1]);
                        int to = Integer.parseInt(msg[2]);
                        String na = msg[3];
                        if (na.equals(name)) {
                            if (to <= 1) {
                                i = 0;
                                j = to;
                            } else {
                                i = 1;
                                j = to - 2;
                            }
                            b1[j][i].setIcon(null);

                        }

                    }
                    if (message.contains("addstar")) {
                        String msg[] = message.split(":");
                        String na = msg[1];
                        int scor = Integer.parseInt(msg[2]);
                        int from = Integer.parseInt(msg[3]);
                        int to = Integer.parseInt(msg[4]);
                        int i;
                        int j;
                        int fromi = 0;
                        int fromj = 0;
                        if (!na.equals(name)) {

                            if (to <= 1) {
                                i = 0;
                                j = to;
                            } else {
                                i = 1;
                                j = to - 2;
                            }
                            if (from <= 1) {
                                fromi = 0;
                                fromj = from;
                            } else {
                                fromi = 1;
                                fromj = from - 2;
                            }
                            int num = nums[from];
                            Image img = ImageIO.read(new File("Project/p_" + fileNum + "_4_/p_" + fileNum + "_4_" + num + ".png"));
                            //resize the image to be fill in the buttom
                            Image resizedImage = img.getScaledInstance(100, 100, 0);
                            //set button background
                            ImageIcon icn = new ImageIcon(resizedImage);
                            b1[j][i].setIcon(icn);
                            b1[j][i].setOpaque(true);

                        }
                        if (from <= 1) {
                            fromi = 0;
                            fromj = from;
                        } else {
                            fromi = 1;
                            fromj = from - 2;
                        }
                        b2[fromj][fromi].setIcon(null);
                        String name1 = player1.getText().toString();
                        String name2 = player2.getText().toString();
                        String name3 = player3.getText().toString();

                        if (name1.contains(na)) {
                            player1.setText(na + " " + scor);
                        } else if (name2.contains(na)) {
                            player2.setText(na + " " + scor);

                        } else if (name3.contains(na)) {
                            player3.setText(na + " " + scor);

                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(EasyGame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> highFivePlayer;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel player1;
    private javax.swing.JLabel player2;
    private javax.swing.JLabel player3;
    private javax.swing.JLabel turn;
    // End of variables declaration//GEN-END:variables
}
