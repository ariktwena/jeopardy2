package jeopardy;

import jeopardy.classes.Board;
import jeopardy.classes.Question_board;
import jeopardy.tui.TUI;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JeopardyClient extends Thread implements Closeable {
    private final JeopardyServer server;
    private final Socket socket;
    private String name;
    private final ClientHandler handler;
    private final BlockingQueue<String> messageQueue;
    private volatile int score;
    private volatile boolean playerTurn;
    private final TUI tui;

//    private final Board board;

    public JeopardyClient(JeopardyServer server, Socket socket, String name) throws IOException {
        this.server = server;
        this.socket = socket;
        this.handler = new ClientHandler(
                socket.getInputStream(),
                new PrintWriter(socket.getOutputStream(), true));
        this.messageQueue = new LinkedBlockingQueue<>();
        this.name = name;
        this.score = 0;
        this.playerTurn = false;
        this.tui = new TUI(new Scanner(socket.getInputStream()), new PrintWriter(socket.getOutputStream()));

//        this.board = new Board();
    }

    public String getClientName() {
        return name;
    }


    @Override
    public void run() {
//        Thread t = new Thread(() -> {
//            try {
//                if(server.getNumberOfPlayer() <= 2){
//                    name = handler.getPlayerName();
//
//
//                    JeopardyClient superThis = this;
//                    JeopardyGame game = server.getActiveGame();
//                    game.play(superThis, new JeopardyGame.GameParticipant() {
//
//                        @Override
//                        public void notifyGameStart() {
//                            handler.printMessage("Who is the fastest on the buzzer");
//                        }
//
//                        @Override
//                        public String getAnswer() {
//                            return handler.waitForLine();
//                        }
//                        @Override
//                        public void toSlow() {
//                            handler.printMessage("Sorry... to slow");
//                        }
//
//                        @Override
//                        public void buzz() {
//                            handler.printMessage("You won the turn!");
//                        }
//
//                        @Override
//                        public JeopardyClient getClient() {
//                            return superThis;
//                        }
//
//                        @Override
//                        public void weHaveAllThePlayers() {
//                            handler.weHaveAllThePlayers();
//                            tui.loader();
//                            tui.welcomeMessage(getClientName());
//                            tui.loaderLong();
//                        }
//
//                        @Override
//                        public void WeAreWaitingForMorePlayers() {
//                            tui.waitingForOnMorePlayer(getClientName());
//                        }
//
//                        @Override
//                        public void youStartTheGameAndChooseCategory() {
//                            tui.youWonThetoes();
//                        }
//
//                        @Override
//                        public void theOtherPlayerISChoosingACategory() {
//                            tui.youDidntWinThetoes();
//                        }
//
//                        @Override
//                        public void drawBoard(ArrayList<Question_board> list) {
//                            String score1, score2, score3, score4, score5, score6;
//
//                            //We print the header of the board (The list has 30 spots. Every 5 spot is a now line of questions)
//                            tui.getBoardHeader();
//                            tui.getBoardCategoryLeftAlignFormat("A: " + list.get(0).getCategory().getCategoryName(),
//                                    "B: " +list.get(5).getCategory().getCategoryName(),
//                                    "C: " +list.get(10).getCategory().getCategoryName(),
//                                    "D: " +list.get(15).getCategory().getCategoryName(),
//                                    "E: " +list.get(20).getCategory().getCategoryName(),
//                                    "F: " +list.get(25).getCategory().getCategoryName());
//
//                            //We print 5 rows of point on the board
//                            for( int i = 0 ; i < 5 ; i++ ){
//                                tui.getBoardSeparator();
//                                score1 = list.get(0 + i).getAnswered() == null ? String.valueOf(list.get(0 + i).getScore()) : list.get(0 + i).getAnswered();
//                                score2 = list.get(5 + i).getAnswered() == null ? String.valueOf(list.get(5 + i).getScore()) : list.get(5 + i).getAnswered();
//                                score3 = list.get(10 + i).getAnswered() == null ? String.valueOf(list.get(10 + i).getScore()) : list.get(10 + i).getAnswered();
//                                score4 = list.get(15 + i).getAnswered() == null ? String.valueOf(list.get(15 + i).getScore()) : list.get(15 + i).getAnswered();
//                                score5 = list.get(20 + i).getAnswered() == null ? String.valueOf(list.get(20 + i).getScore()) : list.get(20 + i).getAnswered();
//                                score6 = list.get(25 + i).getAnswered() == null ? String.valueOf(list.get(25 + i).getScore()) : list.get(25 + i).getAnswered();
//
//
//                                tui.getBoardScoreLeftAlignFormatRow(score1, score2, score3, score4, score5, score6);
//                            }
//                            tui.getBoardFooter();
//                        }
//
//                        @Override
//                        public String playerCategoryChoice() {
//                            return tui.playerCategoryInput();
//                        }
//
//                        @Override
//                        public void getCategoryTitle(String categoryName) {
//                            tui.getCategoryTitle(categoryName);
//                        }
//
//                        @Override
//                        public void helpSwitch() {
//                            handler.loader();
//                            handler.getHelpGame();
//                        }
//
//                        @Override
//                        public void getScoreSwitch(String clientName, int score) {
//                            tui.getScore(clientName, score);
//                        }
//
//                        @Override
//                        public void getBoardSwitch(int numberOfAnswers) {
//                            tui.getBoardStatus(numberOfAnswers);
//                        }
//
//                        @Override
//                        public void exitSwitch(String clientName) throws IOException {
//                            handler.exitGame(clientName);
//                            socket.close();
//                        }
//
//                        @Override
//                        public void defaultSwitch() {
//                            tui.gameDefaultMessage();
//                        }
//
//                        @Override
//                        public void youAreTheWinner(String clientName) throws IOException {
//                            handler.exitGameWinner(clientName);
//                            socket.close();
//                        }
//
//                        @Override
//                        public void getHardBoardMessage() {
//                            tui.getHardBoardMessage();
//                        }
//
//                        @Override
//                        public void availableQuestionsInCategoryAndPoint(String choiseSpot, int score) {
//                            handler.availableQuestionsInCategoryAndPoint(choiseSpot, score);
//                        }
//
//                        @Override
//                        public void nonAvailableQuestionsInCategoryAndPoint(String choiseSpot) {
//                            handler.nonAvailableQuestionsInCategoryAndPoint(choiseSpot);
//
//                        }
//
//                        @Override
//                        public String playerQuestionInputChoice() {
//                            return handler.playerQuestionInputChoice().toLowerCase();
//                        }
//
//                        @Override
//                        public void loaderLong() {
//                            tui.loaderLong();
//                        }
//
//                        @Override
//                        public String playerQuestionInputAnswer() {
//                            return tui.playerQuestionInputAnswer();
//                        }
//
//                        @Override
//                        public void correctAnswer(String clientName, int score) {
//                            handler.correctAnswer(clientName, score);
//                        }
//
//                        @Override
//                        public void incorrectAnswer(String clientName, String answer) {
//                            handler.incorrectAnswer(clientName, answer);
//                        }
//
//                        @Override
//                        public void questionHasAlreadyBeenPlayed() {
//                            handler.questionHasAlreadyBeenPlayed();
//                        }
//
//                        @Override
//                        public void getQuestion(String question) {
//                            handler.getQuestion(question);
//                        }
//
//                        @Override
//                        public void youLostYourTurn() {
//                            handler.youLostYourTurn();
//                        }
//
//                        @Override
//                        public void itsYourTurn() {
//                            handler.itsYourTurn();
//                        }
//
//                    });
//
//                } else {
//                    handler.toManyPlayers();
//                    socket.close();
//                    server.removeClient(this);
//                }
//
//            } catch (IOException | InterruptedException | ParseException e) {
//                System.out.println(name + " exited with: " + e.getMessage());
//            } finally {
//                try { close(); } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        try {
            if(server.getNumberOfPlayer() <= 2){
                name = tui.getPlayerName();


                JeopardyClient superThis = this;
                JeopardyGame game = server.getActiveGame();
                game.play(superThis, new JeopardyGame.GameParticipant() {

                    @Override
                    public void notifyGameStart() {
                        handler.printMessage("Who is the fastest on the buzzer");
                    }

                    @Override
                    public String getAnswer() throws InterruptedException {
                        return tui.getAnswerReturnGame();
//                        return handler.waitForLine();
                    }
                    @Override
                    public void toSlow() {
                        handler.printMessage("Sorry... to slow");
                    }

                    @Override
                    public void buzz() {
                        handler.printMessage("You won the turn!");
                    }

                    @Override
                    public JeopardyClient getClient() {
                        return superThis;
                    }

                    @Override
                    public void weHaveAllThePlayers() {
                        tui.weHaveAllThePlayers();
                        tui.loader();
                        tui.welcomeMessage(getClientName());
                        tui.loaderLong();
                    }

                    @Override
                    public void WeAreWaitingForMorePlayers() {
                        tui.waitingForOnMorePlayer(getClientName());
                    }

                    @Override
                    public void youStartTheGameAndChooseCategory() {
                        tui.youWonThetoes();
                    }

                    @Override
                    public void theOtherPlayerISChoosingACategory() {
                        tui.youDidntWinThetoes();
                    }

                    @Override
                    public void drawBoard(ArrayList<Question_board> list) {
                        String score1, score2, score3, score4, score5, score6;

                        //We print the header of the board (The list has 30 spots. Every 5 spot is a now line of questions)
                        tui.getBoardHeader();
                        tui.getBoardCategoryLeftAlignFormat("A: " + list.get(0).getCategory().getCategoryName(),
                                "B: " +list.get(5).getCategory().getCategoryName(),
                                "C: " +list.get(10).getCategory().getCategoryName(),
                                "D: " +list.get(15).getCategory().getCategoryName(),
                                "E: " +list.get(20).getCategory().getCategoryName(),
                                "F: " +list.get(25).getCategory().getCategoryName());

                        //We print 5 rows of point on the board
                        for( int i = 0 ; i < 5 ; i++ ){
                            tui.getBoardSeparator();
                            score1 = list.get(0 + i).getAnswered() == null ? String.valueOf(list.get(0 + i).getScore()) : list.get(0 + i).getAnswered();
                            score2 = list.get(5 + i).getAnswered() == null ? String.valueOf(list.get(5 + i).getScore()) : list.get(5 + i).getAnswered();
                            score3 = list.get(10 + i).getAnswered() == null ? String.valueOf(list.get(10 + i).getScore()) : list.get(10 + i).getAnswered();
                            score4 = list.get(15 + i).getAnswered() == null ? String.valueOf(list.get(15 + i).getScore()) : list.get(15 + i).getAnswered();
                            score5 = list.get(20 + i).getAnswered() == null ? String.valueOf(list.get(20 + i).getScore()) : list.get(20 + i).getAnswered();
                            score6 = list.get(25 + i).getAnswered() == null ? String.valueOf(list.get(25 + i).getScore()) : list.get(25 + i).getAnswered();


                            tui.getBoardScoreLeftAlignFormatRow(score1, score2, score3, score4, score5, score6);
                        }
                        tui.getBoardFooter();
                    }

                    @Override
                    public String playerCategoryChoice() {
                        return tui.playerCategoryInput();
                    }

                    @Override
                    public void getCategoryTitle(String categoryName) {
                        tui.getCategoryTitle(categoryName);
                    }

                    @Override
                    public void helpSwitch() {
                        tui.loader();
                        tui.getHelpGame();
                    }

                    @Override
                    public void getScoreSwitch(String clientName, int score) {
                        tui.getScore(clientName, score);
                    }

                    @Override
                    public void getBoardSwitch(int numberOfAnswers) {
                        tui.getBoardStatus(numberOfAnswers);
                    }

                    @Override
                    public void exitSwitch(String clientName) throws IOException {
                        tui.exitGame(clientName);
                        socket.close();
                    }

                    @Override
                    public void defaultSwitch() {
                        tui.gameDefaultMessage();
                    }

                    @Override
                    public void youAreTheWinner(String clientName) throws IOException {
                        tui.exitGameWinner(clientName);
                        socket.close();
                    }

                    @Override
                    public void getHardBoardMessage() {
                        tui.getHardBoardMessage();
                    }

                    @Override
                    public void availableQuestionsInCategoryAndPoint(String choiseSpot, int score) {
                        tui.availableQuestionsInCategoryAndPoint(choiseSpot, score);
                    }

                    @Override
                    public void nonAvailableQuestionsInCategoryAndPoint(String choiseSpot) {
                        tui.nonAvailableQuestionsInCategoryAndPoint(choiseSpot);

                    }

                    @Override
                    public String playerQuestionInputChoice() {
                        return tui.playerQuestionInputChoice().toLowerCase();
                    }

                    @Override
                    public void loaderLong() {
                        tui.loaderLong();
                    }

                    @Override
                    public String playerQuestionInputAnswer() {
                        return tui.playerQuestionInputAnswer();
                    }

                    @Override
                    public void correctAnswer(String clientName, int score) {
                        tui.correctAnswer(clientName, score);
                    }

                    @Override
                    public void incorrectAnswer(String clientName, String answer) {
                        tui.incorrectAnswer(clientName, answer);
                    }

                    @Override
                    public void questionHasAlreadyBeenPlayed() {
                        tui.questionHasAlreadyBeenPlayed();
                    }

                    @Override
                    public void getQuestion(String question) {
                        tui.getQuestion(question);
                    }

                    @Override
                    public void youLostYourTurn() {
                        tui.youLostYourTurn();
                    }

                    @Override
                    public void itsYourTurn() {
                        tui.itsYourTurn();
                    }

                });

            } else {
                tui.toManyPlayers();
                socket.close();
                server.removeClient(this);
            }

        } catch (IOException | InterruptedException | ParseException e) {
            System.out.println(name + " exited with: " + e.getMessage());
        } finally {
            try { close(); } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeClient(JeopardyClient client){
        this.removeClient(client);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    @Override
    public String toString() {
        return "Player: " + name + " has score: " + score;
    }

    @Override
    public void close() throws IOException {
        server.removeClient(this);
        socket.close();
    }

    public void sendMessage(String s) {
        messageQueue.add(s);
    }

    public class ClientHandler {
        private final InputStream in;
        private final PrintWriter out;

        public ClientHandler(InputStream in, PrintWriter out) {
            this.in = in;
            this.out = out;
        };

        private void showPrompt() {
            out.print("> ");
            out.flush();
        }

        private String prompt() {
            showPrompt();
            return waitForLine();
        }

        private String fetchName() {
            out.println("What's your name, man?");
            return prompt();
        }

        private String waitForLine()  {
            return new Scanner(in).nextLine();
        }

        public boolean hasInput() throws IOException {
            in.readAllBytes();
            return in.available() > 0;
        }

        public void printMessage(String message) {
            out.println("");
            out.println(message);
            out.flush();
        }

//
//
//
//
//
//
//
//
//
//        //Get player name
//        public String getPlayerName() {
//            System.out.println("");
//            out.print("Enter your name > ");
//            out.flush();
//            String name = new Scanner(in).nextLine();
//            return name;
//        }
//
//        //Waiting for one more player
//        public void waitingForOnMorePlayer(String name){
//            out.println("");
//            out.println(name + " we are waiting for one more player :)");
//            out.flush();
//        }
//
//        //We have al the players
//        public void weHaveAllThePlayers(){
//            out.println("");
//            out.println("We have all the players, Lets play!!! :)");
//            out.flush();
//        }
//
//        //To many players
//        public void toManyPlayers(){
//            out.println("Sorry there are to many players. Come back later.... bye!");
//            out.flush();
//        }
//
//        //Get player name
//        public void welcomeMessage (String name) {
//            out.println("");
//            out.println("Welcome to the Quiz show " + name + "!!");
//            out.println("");
//            out.println("Here are the roles of the game: ");
//            out.println("You start by choosing a category A, B, C etc. Then you choose a question for points.");
//            out.println("If you answer correctly, you get the points.");
//            out.println("");
//            out.println("If you ever need help, then just write [help].");
//            out.flush();
//        }
//
//        //First round
//        public void youWonThetoes () {
//            out.println("");
//            out.println("You will begin the game. Choose a category");
//            out.flush();
//        }
//        //Fist round
//        public void youDidntWinThetoes () {
//            out.println("");
//            out.println("The other player will choose a category");
//            out.flush();
//        }
//
//        //Get input from player
//        public String playerInput(){
//            out.println("");
//            out.print("What do you want to do? > ");
//            out.flush();
//            String input = new Scanner(in).nextLine();
//            return input;
//        }
//
//        //Loading text
//        public void loader(){
//            try{
//                out.println("");
//                out.print("Loading");
//                out.flush();
//                for(int i = 0 ; i < 6 ; i ++){
//                    try{
//                        Thread.sleep(500);
//                        out.print(".");
//                        out.flush();
//                    } catch (InterruptedException e){
//                        throw new UnsupportedOperationException("You got an InterruptedException: " + e.getMessage());
//                    }
//                }
//                out.println("");
//                out.flush();
//                Thread.sleep(500);
//            } catch (InterruptedException e){
//                throw new UnsupportedOperationException("You got an InterruptedException: " + e.getMessage());
//            }
//        }
//
//        //Loading text
//        public void loaderLong(){
//            try{
//                out.println("");
//                out.print("Loading");
//                out.flush();
//                for(int i = 0 ; i < 6 ; i ++){
//                    try{
//                        Thread.sleep(500);
//                        out.print(".");
//                        out.flush();
//                    } catch (InterruptedException e){
//                        throw new UnsupportedOperationException("You got an InterruptedException: " + e.getMessage());
//                    }
//                }
//                out.println("");
//                out.flush();
//                Thread.sleep(1500);
//            } catch (InterruptedException i){
//                throw new UnsupportedOperationException("You got an InterruptedException: " + i.getMessage());
//            }
//        }
//
//
//        //Get help in the game
//        public void getHelpGame(){
//            out.println("");
//            out.println("You have to choose a category or question by typing: A, B, C, D, E or F.");
//            out.println("");
//            out.println("Here are the help commands:");
//            out.println("[Help]" + "\t" + "Get help and options");
//            out.println("[Score]" + "\t" + "See your current score");
//            out.println("[Exit]" + "\t" + "Exit the game");
//            out.flush();
//            loaderLong();
//        }
//
//        //Exit the game
//        public void exitGame(String name){
//            out.println("");
//            out.println("I have never met a successful person that was a quitter. Successful people never, ever, give up! " + name);
//            out.flush();
//        }
//
//        //Exit the game winner
//        public void exitGameWinner(String name){
//            out.println("");
//            out.println(name + " the other player has left the game. You are the winner!!!!");
//            out.flush();
//        }
//
//        //Get player score
//        public void getScore(String name, int score){
//            out.println("");
//            out.println(name + " you have " + score + " points.");
//            out.flush();
//        }
//
//        //Get player score
//        public void getBoardStatus(int boardStatus){
//            out.println("");
//            out.println("You have answered " + boardStatus + " questions. You still have " + (60-boardStatus) + " to go :)");
//            out.flush();
//        }
//
//        //Game default message
//        public void gameDefaultMessage(){
//            out.println("");
//            out.println("Wrong input....");
//            out.println("Do you need help? Then just write [help], or [exit] to end the game.");
//            out.flush();
//        }
//
//        //Choose category
//        public String playerCategoryInput(){
//            out.println("");
//            out.print("Choose a category > ");
//            out.flush();
//            String input = new Scanner(in).nextLine();
//            return input;
//        }
//
//
//        //Show chosen category
//        public void getCategoryTitle(String titel){
//            out.println("");
//            out.println("The chosen category is: " + titel );
//            out.flush();
//        }
//
//        //Available question and points
//        public void availableQuestionsInCategoryAndPoint(String option, int score){
//            out.println(option + ": " + score);
//            out.flush();
//        }
//
//        //Non available question and points
//        public void nonAvailableQuestionsInCategoryAndPoint(String option){
//            out.println(option + ": Question has already been answered.");
//            out.flush();
//        }
//
//        //Choose question choice
//        public String playerQuestionInputChoice(){
//            out.println("");
//            out.print("Choose a question > ");
//            out.flush();
//            String input = new Scanner(in).nextLine();
//            return input;
//        }
//
//        //Show the question is taken
//        public void questionHasAlreadyBeenPlayed(){
//            out.print("The question has already been played\n");
//            out.flush();
//        }
//
//        //Get the question
//        public void getQuestion(String question){
//            out.print(question);
//            out.flush();
//        }
//
//        //Choose question answer
//        public String playerQuestionInputAnswer(){
//            out.println("");
//            out.print("What is you answer? > ");
//            out.flush();
//            String input = new Scanner(in).nextLine();
//            return input;
//        }
//
//        //Correct answer
//        public void correctAnswer(String name, int score){
//            //Message to the player
//            out.print("Correct " + name + "!!! You have earned " + score + " points :)\n");
//            out.flush();
//        }
//
//        //Incorrect answer
//        public void incorrectAnswer(String name, String answer){
//            //Message to the player
//            out.print("Incorrect " + name + "! The right answer is: " + answer + "\n");
//            out.flush();
//        }
//
//        //Get help
//        public void getHardBoardMessage(){
//            out.println("");
//            out.println("You have reached the hard part of the game. You will now be tested on the hardest questions for maximum prizes :)");
//            out.flush();
//        }
//
//        //
//        public void youLostYourTurn(){
//            out.println("");
//            out.println("You lost your turn");
//            out.flush();
//        }
//
//        public void itsYourTurn(){
//            out.println("");
//            out.println("It's your turn now");
//            out.flush();
//        }
//
//        //Board header
//        public void getBoardHeader(){
//            out.println(board.getHeader());
//        }
//        //Board Category Left Align
//        public void getBoardCategoryLeftAlignFormat(String cat1, String cat2, String cat3, String cat4, String cat5, String cat6){
//            out.format(board.getCategoryLeftAlignFormat(), cat1, cat2, cat3, cat4, cat5, cat6);
//        }
//        //Board Separator
//        public void getBoardSeparator(){
//            out.println(board.getSeparator());
//        }
//        //Board Score Left
//        public void getBoardScoreLeftAlignFormatRow(String score1, String score2, String score3, String score4, String score5, String score6){
//            out.format(board.getScoreLeftAlignFormatRow(), score1, score2, score3, score4, score5, score6);
//        }
//        //Board Footer
//        public void getBoardFooter(){
//            out.println(board.getFooter());
//            out.flush();
//        }








    }
}