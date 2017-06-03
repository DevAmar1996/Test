
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class ServerConnection implements Runnable {

    public static int num_players = 0;
    public static String game_level = "";
    public String[] winner_names;
    public int[] winner_scores;
    public String[] best_winner_names = new String[5];
    public int[] best_winner_score = new int[5];
    public static Boolean flag_win = false;
    public static String win_name = "";
    public int win_stars = 0;
    public int win_score = 0;
    public int win_order = 0;
    public int current_track = 1;
    public static String[] answer_names;
    public static int[] answer_stars;
    public static int[] answer_order;
    public static int[] answer_scores;
    public static int score1, score2;
    public static int stars1, stars2;
    public static String name1, name2;
    public static int count_answer = 0;
    public ArrayList<Socket> sockets = new ArrayList<>();
    public Socket sock = null;
    public boolean found;
    public int stars = 0;
    public ArrayList<String> players = new ArrayList<String>();
    public ArrayList<Integer> easyOrder = new ArrayList<>();//for puzzles of level easy
    public ArrayList<Integer> avgOrder = new ArrayList<Integer>();//for puzzles of level average
    public ArrayList<Integer> advOrder = new ArrayList<Integer>();//for puzzles of level advance
    public ArrayList<Integer> easyPiece = new ArrayList<>();//for piece of level easy
    public ArrayList<Integer> avgPiece = new ArrayList<Integer>();//for puzzles of level average
    public ArrayList<Integer> advPiece = new ArrayList<Integer>();//for puzzles of level advance
    public ArrayList<Integer> easySolution = new ArrayList<Integer>();
    public ArrayList<Integer> avgSolution = new ArrayList<Integer>();
    public ArrayList<Integer> advSolution = new ArrayList<Integer>();
    public ArrayList<Integer> scores = new ArrayList<Integer>();
    public Random r;
    public boolean added = false;
    int numOfwaiting = 0;
    public static String serverIP = "127.0.0.1";
    int turn = 0;
    boolean first = true;
    int indexOfFirstWaiting;

    public ServerConnection() {
        //the correct order of the puzzle easy level
        fillEasySolution();
        //the correct order of the puzzle Average level
        fillAverageSolution();
        //the correct order of the puzzle Adavnced level
        fillAdvSolution();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(3333);
            System.out.println(" server is ready");// can also use static final PORT_NUM , when defined
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }
        ///// read the results file 
        readResults();
        ///// /////////////////////////////
        while (true) {
            try {
                sock = serverSocket.accept();
                sockets.add(sock); //add all client to array to send message to all client
                //begin client thread each client has one thread
                Thread st = new Thread(this);
                st.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String args[]) {
        new ServerConnection();
    }

    public void readResults() {
        BufferedReader readTxt = null;
        try {
            int n = 0;
            int y = 0;
            String line;
            readTxt = new BufferedReader(new FileReader("PlayerAndStars.txt"));
            while (readTxt.ready()) {
                line = readTxt.readLine();
                y += 1;
            }
            //// set the length of array to the number of file lines 
            winner_names = new String[y];
            winner_scores = new int[y];
            readTxt = new BufferedReader(new FileReader("PlayerAndStars.txt"));
            while (readTxt.ready()) {
                String[] splits = readTxt.readLine().split(",");
                winner_names[n] = splits[0];
                winner_scores[n] = Integer.parseInt(splits[1].trim());
                n += 1;
            }
            /////// rearrange the arrays from the smallest score to the highest score 
            for (int i = 0; i < winner_names.length - 1; i++) {
                for (int j = i + 1; j < winner_names.length; j++) {
                    if (winner_scores[i] < winner_scores[j]) {
                        int temp = winner_scores[i];
                        winner_scores[i] = winner_scores[j];
                        winner_scores[j] = temp;
                        String temp_name = winner_names[i];
                        winner_names[i] = winner_names[j];
                        winner_names[j] = temp_name;
                    }
                }

            }
            // take the first 5 scores into Most arrays to display the most 5 scores 
            for (int i = 0; i < 5; i++) {
                best_winner_names[i] = winner_names[i];
                best_winner_score[i] = winner_scores[i];
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                readTxt.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean checkPlayers(String name) {
        try {
            BufferedReader readTxt = new BufferedReader(new FileReader("PlayerAndStars.txt"));
            while (readTxt.ready()) {
                String[] splits = readTxt.readLine().split(",");
                //check if user exist
                if (splits[0].equals(name)) {
                    return true;
                }

            }
            //user not found in the file
            return false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    public void addNewPlayer(String name) {
        try {
            FileWriter fw = new FileWriter("PlayerAndStars.txt", true);
            PrintWriter writer = new PrintWriter(fw);
            writer.print("\n" + name + "," + 0);
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    @Override
    public void run() {
        try {
            InputStream is = sock.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = sock.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            PrintWriter bw = new PrintWriter(osw);
            while (true) {
                String data = br.readLine();
                if (data != null) {
                    //check if he first player to allow him choose the level
                    if (data.contains("first")) {
                        if (first == true) {
                            bw.println("yesFirst");
                            first = false;
                        } else {
                            bw.println("notFirst");
                        }
                        bw.flush();
                    }
                    //new player connect to the server
                    if (data.contains("Player: ")) {
                        String name = data.split(",")[0].substring(8);
                        //take the game level value if he is the first player
                        if (!data.split(",")[1].equals("no")) {
                            game_level = data.split(",")[1];
                        }
                        //increase the num of player each time new player connect
                        ServerConnection.num_players += 1;
                        //get the score of the player
                        for (int i = 0; i < winner_names.length; i++) {
                            if (data.equalsIgnoreCase(winner_names[i])) {
                                stars = winner_scores[i];
                            }
                        }
                        //add the score of the player to score array
                        scores.add(stars);

                        if (!checkPlayers(name)) {
                            addNewPlayer(name);
                        }
                        //add name of player to display all the name in the play frame
                        players.add(name);
                        //send response to the client(player)
                        bw.println(stars + "," + name + "," + ServerConnection.num_players + "," + game_level + "," + "connected ");
                        bw.flush();
                    }
                    //if one client leave the game
                    if (data.contains("Quit")) {
                        //one of the player quit
                        String name = data.split(":")[1];
                        //decrease the num of player by one
                        num_players--;
                        if (this.numOfwaiting != 0) {
                            //if he in game frame
                            numOfwaiting--;

                        }
                        //remove player from the list and remove his scores
                        int index = players.indexOf(name);
                        players.remove(name);
                        scores.remove(index);
                        if (turn < players.size()) {
                            if (turn == index) {
                                this.sentTurn(players.get(turn), turn, 0, 0, false, false);
                            }
                        } else {
                            turn = 0;
                            this.sentTurn(players.get(turn), turn, 0, 0, false, false);
                        }
                        //tell all the client that there one player leave the game
                        for (Socket s : sockets) {
                            is = s.getInputStream();
                            isr = new InputStreamReader(is);
                            br = new BufferedReader(isr);
                            os = s.getOutputStream();
                            osw = new OutputStreamWriter(os);
                            bw = new PrintWriter(osw);
                            bw.println("offline:" + name + ":" + num_players);
                            bw.flush();
                        }
                    }
                    //send to the client the top Five player
                    if (data.contains("Winners")) {
                        bw.print("Winner ");
                        for (int w = 0; w < 5; w++) {
                            bw.print(":" + best_winner_names[w]);
                        }
                        //send five high player to the clients
                        bw.println();
                        bw.flush();
                    }
                    //tell the player if he has to wait any more
                    if (data.contains("waiting")) {
                        //increase the num of waiting if new player wait
                        numOfwaiting++;
                        //choose randomly the folder that server will take the photo from it
                        r = new Random();
                        int i = r.nextInt(3) + 1;
                        //if num of waiting player is 3 then he can go to the game
                        if (numOfwaiting == 3) {
                            //tell all client to go to game
                            for (Socket s : sockets) {
                                if (!s.isClosed()) {
                                    is = s.getInputStream();
                                    isr = new InputStreamReader(is);
                                    br = new BufferedReader(isr);
                                    os = s.getOutputStream();
                                    osw = new OutputStreamWriter(os);
                                    bw = new PrintWriter(osw);

                                    bw.print("go:" + i);
                                    for (int j = 0; j < num_players; j++) {//send name player to the client
                                        bw.print(":" + players.get(j));
                                    }
                                    int k = 0;
                                    String level = data.split(":")[1];
                                    //choose the folder depends on the level
                                    if (level.equals("Easy")) {
                                        if (!added) {
                                            //add the puzzels photo to array and send it to the client(player)
                                            for (int j = 0; j < 4; j++) { // get four photo for easy level
                                                int num = r.nextInt(4) + 1;//choose photo randomly to put it as background of the button
                                                //check this photo is not backround of other button
                                                while (easyOrder.contains(num)) {
                                                    num = r.nextInt(4) + 1;
                                                }
                                                easyOrder.add(num);
                                            }
                                            added = true;
                                        }
                                        for (int n = 0; n < 4; n++) { // send image order to players
                                            bw.print(":" + easyOrder.get(n));
                                        }
                                        bw.println();
                                        bw.flush();

                                    } else if (level.equals("Average")) {
                                        if (!added) {
                                            for (int j = 0; j < 6; j++) { // get 6 photo for Average level
                                                int num = r.nextInt(6) + 1;//choose photo randomly to put it as background of the button
                                                //check this photo is not backround of other button
                                                while (avgOrder.contains(num)) {
                                                    num = r.nextInt(6) + 1;
                                                }
                                                avgOrder.add(num);
                                            }
                                            added = true;
                                        }
                                        for (int n = 0; n < 6; n++) { // send image order to players
                                            bw.print(":" + avgOrder.get(n));
                                        }
                                        bw.println();
                                        bw.flush();
                                    } else {
                                        if (!added) {
                                            for (int j = 0; j < 8; j++) { // get 6 photo for Average level
                                                int num = r.nextInt(8) + 1;//choose photo randomly to put it as background of the button
                                                //check this photo is not backround of other button
                                                while (advOrder.contains(num)) {
                                                    num = r.nextInt(8) + 1;
                                                }
                                                advOrder.add(num);
                                            }
                                            added = true;
                                        }
                                        for (int n = 0; n < 8; n++) { // send image order to players
                                            bw.print(":" + advOrder.get(n));
                                        }
                                        bw.println();
                                        bw.flush();
                                    }

                                }

                            }
                            //if there is one player  tell him to be wait for another player
                        } else if (numOfwaiting == 1) {
                            indexOfFirstWaiting = sockets.indexOf(sock);
                            bw.print("wait:" + numOfwaiting);
                            for (int j = 0; j < num_players; j++) {
                                //send players name  to the client
                                bw.print(":" + players.get(j));
                            }
                            bw.println();
                            bw.flush();
                            //if there is two player tell them to be wait for another player
                        } else if (numOfwaiting == 2) {
                            //for the second player
                            bw.print("wait:" + numOfwaiting);
                            for (int j = 0; j < num_players; j++) {
                                //send players name  to the client
                                bw.print(":" + players.get(j));
                            }
                            bw.println();
                            bw.flush();

                            //for the first player
                            InputStream is2 = sockets.get(indexOfFirstWaiting).getInputStream();
                            InputStreamReader isr2 = new InputStreamReader(is2);
                            BufferedReader br2 = new BufferedReader(isr2);
                            OutputStream os2 = sockets.get(indexOfFirstWaiting).getOutputStream();
                            OutputStreamWriter osw2 = new OutputStreamWriter(os2);
                            PrintWriter bw2 = new PrintWriter(osw2);
                            bw2.print("wait:" + numOfwaiting);
                            for (int j = 0; j < num_players; j++) {
                                //send players name  to the client
                                bw2.print(":" + players.get(j));
                            }
                            bw2.println();
                            bw2.flush();
                        }
                    }
                    //check who must begin the game, the first player connect, can begin the game
                    if (data.contains("turn")) {
                        String name = data.split(":")[1];
                        sentTurn(name, turn, 0, 0, false, false);
                    }
                    //if one player move puzzle to other place
                    if (data.contains("move")) {
                        boolean star = false;
                        boolean won = false;
                        numOfwaiting = 0;
                        String msg[] = data.split(":");
                        int from = Integer.parseInt(msg[1]);
                        int to = Integer.parseInt(msg[2]);
                        String name = msg[3];
                        String level = msg[4];
                        if (level.equals("Easy")) {
                            if (easyOrder.get(from) == to + 1) {
                                int temp = easyOrder.get(from);
                                easyPiece.set(to, temp);
                                scores.set(turn, (scores.get(turn) + 1));
                                star = true;
                                //check if the puzzle become sorted, then the game off
                                if (CompareWithSolution(easyPiece)) {
                                    int index = getMax();
                                    won = true;
                                    
                                    //add star to the player who has the highest score
                                    this.UpdateRule(players.get(index), 0);
                                    //send to all the client that the game terminate
                                    for (Socket s : sockets) {
                                        InputStream is2 = s.getInputStream();
                                        InputStreamReader isr2 = new InputStreamReader(is2);
                                        BufferedReader br2 = new BufferedReader(isr2);
                                        OutputStream os2 = s.getOutputStream();
                                        OutputStreamWriter osw2 = new OutputStreamWriter(os2);
                                        PrintWriter bw2 = new PrintWriter(osw2);
                                        bw2.println("won:" + players.get(index));
                                        bw2.flush();
                                    }
                                }
                            } else {
                                int temp = easyOrder.get(from);
                                easyPiece.set(to, temp);
                            }
                        } else if (level.equals("Average")) {
                            if (avgOrder.get(from) == to + 1) {
                                int temp = avgOrder.get(from);
                                avgPiece.set(to, temp);
                                scores.set(turn, (scores.get(turn) + 1));
                                star = true;
                                if (CompareWithSolution(avgPiece)) {
                                    int index = getMax();
                                    won = true;
                                    this.UpdateRule(players.get(index), 0);
                                    for (Socket s : sockets) {
                                        InputStream is2 = s.getInputStream();
                                        InputStreamReader isr2 = new InputStreamReader(is2);
                                        BufferedReader br2 = new BufferedReader(isr2);
                                        OutputStream os2 = s.getOutputStream();
                                        OutputStreamWriter osw2 = new OutputStreamWriter(os2);
                                        PrintWriter bw2 = new PrintWriter(osw2);
                                        bw2.println("won:" + players.get(index));
                                        bw2.flush();
                                    }
                                }
                            } else {
                                int temp = avgOrder.get(from);
                                avgPiece.set(to, temp);
                            }
                        } else if (advOrder.get(from) == to + 1) {
                            int temp = advOrder.get(from);
                            advPiece.set(to, temp);
                            scores.set(turn, (scores.get(turn) + 1));
                            star = true;
                            if (CompareWithSolution(advPiece)) {
                                int index = getMax();
                                won = true;
                                this.UpdateRule(players.get(index), 0);
                                for (Socket s : sockets) {
                                    InputStream is2 = s.getInputStream();
                                    InputStreamReader isr2 = new InputStreamReader(is2);
                                    BufferedReader br2 = new BufferedReader(isr2);
                                    OutputStream os2 = s.getOutputStream();
                                    OutputStreamWriter osw2 = new OutputStreamWriter(os2);
                                    PrintWriter bw2 = new PrintWriter(osw2);
                                    bw2.println("won:" + players.get(index));
                                    bw2.flush();
                                }
                            }

                        } else {
                            int temp = advPiece.get(from);
                            advPiece.set(to, temp);
                        }
                        if (turn == players.size() - 1) {
                            turn = -1;
                        }
                        turn++;
                        if (!won) {
                            sentTurn(name, turn, from, to, true, star);
                            star = false;
                        }

                    }

                }
            }

        } catch (IOException e) {
            System.out.println("IO data error");
        }
    }

    public int getMax() {
        //get the player who has highest score
        int max = 0;
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i) > scores.get(max)) {
                max = i;
            }
        }
        return max;
    }

    public ArrayList<Users> UpdateRule(String name, int score) {
        ArrayList<Users> alluser = new ArrayList<>();
        try {
            BufferedReader readTxt = new BufferedReader(new FileReader("PlayerAndStars.txt"));
            String line;
            while (readTxt.ready()) {
                line = readTxt.readLine();
                if (line.split(",")[0].equals(name)) {
                    alluser.add(new Users(line.split(",")[0], (Integer.valueOf(line.split(",")[1]) + 1)));
                } else {
                    
                    try{
                    alluser.add(new Users(line.split(",")[0], Integer.valueOf(line.split(",")[1])));
                    }catch(Exception ex){
                        
                    }
                }
            }
            Collections.sort(alluser);
            UpdateFile(alluser);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alluser;
    }

    public void UpdateFile(ArrayList<Users> users) {
        try {
            FileWriter fw = new FileWriter("PlayerAndStars.txt", false);
            PrintWriter writer = new PrintWriter(fw);
            for (int i = 0; i < users.size(); i++) {
                writer.println(users.get(i).getName() + "," + users.get(i).getScore());
            }
            writer.close();

        } catch (IOException e) {
            // do something
        }
    }

    public boolean checkTurn(String name, int index) {
        if (players.get(index).equals(name)) {
            return true;
        } else {
            return false;
        }
    }

    public void fillEasySolution() {
        easyPiece.clear();
        easySolution.clear();
        easySolution.add(1);
        easySolution.add(2);
        easySolution.add(3);
        easySolution.add(4);
        easyPiece.add(0);
        easyPiece.add(0);
        easyPiece.add(0);
        easyPiece.add(0);

    }

    public void fillAverageSolution() {
        avgSolution.clear();
        avgPiece.clear();
        avgSolution.add(1);
        avgSolution.add(2);
        avgSolution.add(3);
        avgSolution.add(4);
        avgSolution.add(5);
        avgSolution.add(6);
        avgPiece.add(0);
        avgPiece.add(0);
        avgPiece.add(0);
        avgPiece.add(0);
        avgPiece.add(0);
        avgPiece.add(0);

    }

    public void fillAdvSolution() {
        advSolution.clear();
        advPiece.clear();
        advSolution.add(1);
        advSolution.add(2);
        advSolution.add(3);
        advSolution.add(4);
        advSolution.add(5);
        advSolution.add(6);
        advSolution.add(7);
        advSolution.add(8);
        advPiece.add(0);
        advPiece.add(0);
        advPiece.add(0);
        advPiece.add(0);
        advPiece.add(0);
        advPiece.add(0);
        advPiece.add(0);
        advPiece.add(0);

    }

    public boolean CompareWithSolution(ArrayList<Integer> levelOrder) {
        if (levelOrder.size() == 4) {
            System.out.println("hi");
            if (levelOrder.equals(easySolution)) {
                numOfwaiting = 0;
                turn = 0;
                this.fillEasySolution();
                return true;
            }
            return false;
        } else if (levelOrder.size() == 6) {
            if (levelOrder.equals(avgSolution)) {
                numOfwaiting = 0;
                turn = 0;
                this.fillAverageSolution();
                return true;
            }
            return false;
        } else {
            if (levelOrder.equals(advSolution)) {
                numOfwaiting = 0;
                turn = 0;
                this.fillAdvSolution();
                return true;
            }
            return false;
        }
    }

    public void sentTurn(String name, int turn, int from, int to, boolean write, boolean star) throws IOException {
        if (checkTurn(name, turn) == false) {
            for (Socket s : sockets) {
                InputStream is2 = s.getInputStream();
                InputStreamReader isr2 = new InputStreamReader(is2);
                BufferedReader br2 = new BufferedReader(isr2);
                OutputStream os2 = s.getOutputStream();
                OutputStreamWriter osw2 = new OutputStreamWriter(os2);
                PrintWriter bw2 = new PrintWriter(osw2);
                if (star == true) {
                    if (turn == 0) {
                        bw2.println("addstar:" + name + ":" + (scores.get(scores.size() - 1)) + ":" + from + ":" + to);
                    } else {
                        bw2.println("addstar:" + name + ":" + (scores.get(turn - 1)) + ":" + from + ":" + to);

                    }
                    bw2.flush();

                } else if (write) {
                    bw2.println("move:" + from + ":" + to + ":" + name);
                    bw2.flush();

                }
                bw2.println("yourTurn:" + players.get(turn));
                bw2.flush();

            }
        } else {
            for (Socket s : sockets) {
                InputStream is2 = s.getInputStream();
                InputStreamReader isr2 = new InputStreamReader(is2);
                BufferedReader br2 = new BufferedReader(isr2);
                OutputStream os2 = s.getOutputStream();
                OutputStreamWriter osw2 = new OutputStreamWriter(os2);
                PrintWriter bw2 = new PrintWriter(osw2);

                if (star == true) {
                    if (turn == 0) {
                        bw2.println("addstar:" + name + ":" + (scores.get(scores.size() - 1)) + ":" + from + ":" + to);
                    } else {
                        bw2.println("addstar:" + name + ":" + (scores.get(turn - 1)) + ":" + from + ":" + to);

                    }
                    bw2.flush();
                } else if (write) {
                    bw2.println("move:" + from + ":" + to + ":" + name);
                    bw2.flush();

                }
                bw2.println("yourTurn:" + name);
                bw2.flush();
            }
        }
    }
}
