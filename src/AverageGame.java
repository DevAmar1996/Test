
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jori
 */
public class AverageGame extends javax.swing.JFrame implements Runnable {

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
    public int fileNum;
    public String players[] = new String[3];
    private JLabel b1[][] = new JLabel[6][6];//each button present part of photo
    private JButton b2[][] = new JButton[6][6];
    private int nums[] = new int[6];
    Thread th;
    boolean yourTurn = false;
    MouseListener ms;
    int from;
    int to;
    int i, j;
    boolean send = false;
    private int Piecenums[] = {0, 0, 0, 0, 0, 0};
    boolean begin = true;
    String[] winners;

    public AverageGame(String name, int numOfPlayers, int stars, String level, int fileNum, String players[], int nums[], String[] winners) {
        initComponents();
        getContentPane().setBackground(new Color(234, 240, 237));
        this.name = name;
        this.numOfPlayers = numOfPlayers;
        this.stars = stars;
        this.level = level;
        jLabel15.setText("Welcome " + name + " (" + stars + ") ");
        this.fileNum = fileNum;
        this.players = players;
        this.nums = nums;
        this.winners = winners;
        connectToServer();
        bw.println("anyOrder:" + level);
        bw.flush();
        setPlayers();
        makePuzzels();
        setFivePlayers();
        setTurn();
        th = new Thread(this);
        th.start();
    }

    public void setFivePlayers() {
        highFivePlayer.setModel(new javax.swing.AbstractListModel<String>() {

            public int getSize() {
                return winners.length;
            }

            public String getElementAt(int i) {
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
        //split panels int 6 pices to put image pice on it
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
                            for (int j = 0; j < 3; j++) {
                                int x1 = b2[j][i].getLocationOnScreen().x;
                                int y1 = b2[j][i].getLocationOnScreen().y;
                                int width = b2[j][i].getPreferredSize().width;
                                int height = b2[j][i].getPreferredSize().height;
                                //  System.out.println(x1 + "  " + width + "   " + x);
                                //  System.out.println(y1 + "  " + height + "   " + y);

                                if (x >= x1 && x <= x1 + width && y >= y1 && y <= y1 + height) {
                                    //if (x1 == x && y1 == y) {
                                    if (i == 0) {
                                        from = j;
                                    } else {
                                        from = j + i + 2;
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
                    JOptionPane.showMessageDialog(null, "please Wait it is not your turn", "wiat", JOptionPane.CANCEL_OPTION);
                    begin = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (yourTurn) {
                    if ((e.getSource() instanceof AverageGame)) {
                        javax.swing.JFrame jc = (javax.swing.JFrame) e.getSource();
                        int x = e.getLocationOnScreen().x;
                        int y = e.getLocationOnScreen().y;
                        for (int i = 0; i < 2; i++) {
                            for (int j = 0; j < 3; j++) {
                                int x1 = b1[j][i].getLocationOnScreen().x;
                                int y1 = b1[j][i].getLocationOnScreen().y;
                                int width = b1[j][i].getPreferredSize().width;
                                int height = b1[j][i].getPreferredSize().height;
                                // System.out.println(x1 + "  " + width + "   " + x);
                                // System.out.println(y1 + "  " + height + "   " + y);

                                if (x >= x1 && x <= x1 + width && y >= y1 && y <= y1 + height) {
                                    if (i == 0) {
                                        to = j;
                                    } else {
                                        to = j + i + 2;
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
        jPanel1.setLayout(new GridLayout(2, 3, 1, 1));
        jPanel2.setLayout(new GridLayout(2, 3, 1, 1));
        int k = 0; //varible to get index of nums array;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                try {

                    //set a new button
                    b2[j][i] = new JButton();
                    //set button size
                    b2[j][i].setPreferredSize(new Dimension(100, 100));
                    int num = nums[k];
                    k++;
                    //read the image as afile
                    Image img = ImageIO.read(new File("Project/p_" + fileNum + "_6_/p_" + fileNum + "_6_" + num + ".png"));
                    //resize the image to be fill in the buttom
                    Image resizedImage = img.getScaledInstance(100, 100, 0);
                    //set button background
                    ImageIcon icn = new ImageIcon(resizedImage);
                    b2[j][i].setIcon(icn);

                    b2[j][i].addMouseListener(ms);
                    //get the item that was clicked 

                    b2[j][i].setTransferHandler(new TransferHandler("icon"));


                    /*DragListener drag = new DragListener(b2[j][i]);
                    b2[j][i].addMouseListener(drag);
                    b2[j][i].addMouseMotionListener(drag);*/
                    //add button to the panel
                    jPanel2.add(b2[j][i]);

                } catch (IOException ex) {
                    Logger.getLogger(EasyGame.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                b1[j][i] = new JLabel();
                b1[j][i].setText(j + "  " + i);
                //set button size
                b1[j][i].setPreferredSize(new Dimension(100, 100));
                b1[j][i].setTransferHandler(new TransferHandler("icon"));
                //   b1[j][i].addMouseListener(ms);
                //addMouseListener(ms);
                //add button to the panel
                jPanel1.add(b1[j][i]);

            }
        }
        addMouseListener(ms);

    }

    public void connectToServer() {
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
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

        jFrame1 = new javax.swing.JFrame();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        player1 = new javax.swing.JLabel();
        player2 = new javax.swing.JLabel();
        player3 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        turn = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        highFivePlayer = new javax.swing.JList<>();

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jFrame1.setSize(new java.awt.Dimension(580, 415));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png"))); // NOI18N
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Capture3.PNG"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setText("Welcome");

        jLabel1.setForeground(new java.awt.Color(244, 75, 98));
        jLabel1.setText("Player2");

        jLabel2.setForeground(new java.awt.Color(244, 75, 98));
        jLabel2.setText("Player1");

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("DONE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Hannotate SC", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(53, 172, 149));
        jLabel6.setText("pieces");

        jLabel7.setFont(new java.awt.Font("Hannotate SC", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(53, 172, 149));
        jLabel7.setText("Puzzel");

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel11)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(112, 112, 112)))
                        .addComponent(jLabel14)
                        .addContainerGap())
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1Layout.createSequentialGroup()
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addGap(15, 15, 15))))
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)))
                .addGap(3, 3, 3))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Capture3.PNG"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel15.setText("Welcome");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png"))); // NOI18N
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Hannotate SC", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(53, 172, 149));
        jLabel8.setText("Puzzel");

        player1.setForeground(new java.awt.Color(244, 75, 98));
        player1.setText("Player1");

        player2.setForeground(new java.awt.Color(244, 75, 98));
        player2.setText("Player1");

        player3.setForeground(new java.awt.Color(244, 75, 98));
        player3.setText("Player1");

        jLabel20.setFont(new java.awt.Font("Hannotate SC", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(53, 172, 149));
        jLabel20.setText("pieces");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 341, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

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
                .addGap(114, 114, 114)
                .addComponent(player1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(player3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(player2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(turn, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(258, 258, 258)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel16)
                        .addGap(42, 42, 42))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(turn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel20))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player2)
                    .addComponent(player3)
                    .addComponent(player1))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        JOptionPane.showMessageDialog(null, "are you sure you want to exit", "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        JOptionPane.showMessageDialog(null, "are you sure you want to exit", "Error", JOptionPane.ERROR_MESSAGE);
        bw.println("Quit:" + name);
        bw.flush();
        quit = true;
        System.exit(0);
    }//GEN-LAST:event_jLabel16MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> highFivePlayer;
    private javax.swing.JButton jButton1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel player1;
    private javax.swing.JLabel player2;
    private javax.swing.JLabel player3;
    private javax.swing.JLabel turn;
    // End of variables declaration//GEN-END:variables

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
                    if (message.contains("won")) {
                        String na = message.split(":")[1];
                        AverageGame.this.setVisible(false);
                        if (na.equals(name)) {
                            new Frame6(name, na, players, (this.stars + 1), "Average").setVisible(true);
                        } else {
                            new Frame6(name, na, players, (this.stars), "Average").setVisible(true);

                        }
                    }

                    if (message.contains("move")) {
                        String msg[] = message.split(":");
                        int from = Integer.parseInt(msg[1]);
                        int to = Integer.parseInt(msg[2]);
                        String na = msg[3];
                        int i;
                        int j;
                        if (na.equals(name)) {
                            if (to <= 2) {
                                i = 0;
                                j = to;
                            } else {
                                i = 1;
                                j = to - 3;
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

                            if (to <= 2) {
                                i = 0;
                                j = to;
                            } else {
                                i = 1;
                                j = to - 3;
                            }
                            if (from <= 2) {
                                fromi = 0;
                                fromj = from;
                            } else {
                                fromi = 1;
                                fromj = from - 3;
                            }
                            int num = nums[from];
                            Image img = ImageIO.read(new File("Project/p_" + fileNum + "_6_/p_" + fileNum + "_6_" + num + ".png"));
                            //resize the image to be fill in the buttom
                            Image resizedImage = img.getScaledInstance(100, 100, 0);
                            //set button background
                            ImageIcon icn = new ImageIcon(resizedImage);
                            b1[j][i].setIcon(icn);
                            b1[j][i].setOpaque(true);

                        }
                        if (from <= 2) {
                            fromi = 0;
                            fromj = from;
                        } else {
                            fromi = 1;
                            fromj = from - 3;
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
}
