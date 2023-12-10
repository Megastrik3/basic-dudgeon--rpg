package com.example.finalproject;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Dungeon {
    private String[][] maze;
    private int playerYCoordinate;
    private int playerXCoordinate;
    private int readMonsterNumber = 0;
    private int monsterNumber = 0;
    private int roomNumber = 0;
    private int goldAmountInRoom = 0;
    private int goldRoomNumber = 0;
    private int readGoldRoomNumber = 0;
    public Dungeon() {
        //Define array size
        maze = new String[10][10];
        //set every space to "  "
        for (int i = 0; i < 10; i++){
            //suggested by intellij
            Arrays.fill(maze[i], "  ");
        }
        playerYCoordinate = (int)(Math.random() * (9));
        playerXCoordinate = (int)(Math.random() * (9));
        maze[playerYCoordinate][playerXCoordinate] = "X";

    }
    // Week9 TicTacToe.java https://github.com/EricCharnesky/CIS1500-Winter2023/blob/main/Week9-ArraysAndArrayLists/src/TicTacToe.java
    public String printMazeToScreen(){
        String mazeRow = "";
        for(String[] row : maze){
            mazeRow += Arrays.toString(row) + "\n";
        }
        return mazeRow;
    }
    public int getPlayerYCoordinate() {
        return playerYCoordinate;
    }
    public void setPlayerYCoordinate(int playerY) {
        if (playerY < 0) {
            playerY = 9;
        } else if (playerY > 9){
            playerY = 0;
        }
        maze[playerYCoordinate][playerXCoordinate] = "  ";
        maze[playerY][playerXCoordinate] = "X";
        this.playerYCoordinate = playerY;
    }
    public int getPlayerXCoordinate() {
        return playerXCoordinate;
    }
    public void setPlayerXCoordinate(int playerX) {
        if (playerX < 0) {
            playerX = 9;
        } else if (playerX > 9){
            playerX = 0;
        }
        maze[playerYCoordinate][playerXCoordinate] = "  ";
        maze[playerYCoordinate][playerX] = "X";
        this.playerXCoordinate = playerX;
    }
    public int getGoldAmountInRoom() {
        return goldAmountInRoom;
    }
    public void generateBlockedRoom(Integer xCoordinate, Integer yCoordinate) throws IOException {
        // Rolls a 30 sided dice. If the output is less than or equal to 5 then the room is generated as blocked.
        Random random = new Random();
        int diceRoll = random.nextInt(1,31);
        if (diceRoll <= 5){
            PrintWriter printWriter = new PrintWriter((new FileWriter("room" + roomNumber + ".txt")));
            roomNumber++;
            // Prevent array out of range error by setting invalid values to the correct one
            if (xCoordinate > 9){
                xCoordinate = 0;
            } else if (xCoordinate < 0){
                xCoordinate = 9;
            }
            if (yCoordinate > 9){
                yCoordinate = 0;
            } else if (yCoordinate < 0){
                yCoordinate = 9;
            }
            printWriter.println(xCoordinate + "|" + yCoordinate);
            printWriter.close();
        }
    }
    public boolean generateMonsterInRoom(){
        Random random = new Random();
        int diceRoll = random.nextInt(1,3);
        return diceRoll == 1;
    }
    public void generateGoldInRoom(Integer xCoordinate, Integer yCoordinate) throws IOException {
        Random random = new Random();
        int goldAmount = random.nextInt(1,6);
        PrintWriter printWriter = new PrintWriter((new FileWriter("gold" + goldRoomNumber + ".txt")));
        goldRoomNumber++;
        printWriter.println(xCoordinate + "|" + yCoordinate + "|" + goldAmount);
        printWriter.close();
    }
    public boolean isRoomBlocked(Integer xCoordinate, Integer yCoordinate) throws FileNotFoundException {
        int readRoomNumber = 0;
        // iterate through every room file, check if it exists, then check its coordinates if true
        while(readRoomNumber <= roomNumber){
            File fileToRead = new File("room" + readRoomNumber + ".txt");
            if (!fileToRead.exists()){
                readRoomNumber++;
                continue;
            }
            Scanner fileReader = new Scanner(fileToRead);
            String line = fileReader.nextLine();
            String[] roomBlocked = line.split("[|]");
            int roomXCoord = Integer.parseInt(roomBlocked[0]);
            int roomYCoord = Integer.parseInt(roomBlocked[1]);
            fileReader.close();
            readRoomNumber++;
            // Prevent array out of range error by setting invalid values to the correct one
            if (xCoordinate > 9){
                xCoordinate = 0;
            } else if (xCoordinate < 0){
                xCoordinate = 9;
            }
            if (yCoordinate > 9){
                yCoordinate = 0;
            } else if (yCoordinate < 0){
                yCoordinate = 9;
            }
            if (xCoordinate == roomXCoord && yCoordinate == roomYCoord){
                // mark room as blocked on the map
                maze[roomYCoord][roomXCoord] = "#";
                return true;
            }
        }
        return false;
    }
    public boolean isMonsterInRoom() throws FileNotFoundException {
        readMonsterNumber = 0;
        while(readMonsterNumber <= monsterNumber){
            File fileToRead = new File("monster" + readMonsterNumber + ".txt");
            if (!fileToRead.exists()){
                readMonsterNumber++;
                continue;
            }
            Scanner fileReader = new Scanner(fileToRead);
            String line = fileReader.nextLine();
            line = fileReader.nextLine();
            String[] monsterCoordinates = line.split(",");
            int monsterXCord = Integer.parseInt(monsterCoordinates[0]);
            int monsterYCord = Integer.parseInt(monsterCoordinates[1]);
            fileReader.close();
            readMonsterNumber++;
            if (playerXCoordinate == monsterXCord && playerYCoordinate == monsterYCord){
                return true;
            }
        }
        return false;
    }
    public boolean isGoldInRoom() throws IOException {
        readGoldRoomNumber = 0;
        while(readGoldRoomNumber <= goldRoomNumber){
            File fileToRead = new File("gold" + readGoldRoomNumber + ".txt");
            if (!fileToRead.exists()){
                readGoldRoomNumber++;
                continue;
            }
            Scanner fileReader = new Scanner(fileToRead);
            String line = fileReader.nextLine();
            String[] goldInRoom = line.split("[|]");
            int roomXCoord = Integer.parseInt(goldInRoom[0]);
            int roomYCoord = Integer.parseInt(goldInRoom[1]);
            int goldAmount = Integer.parseInt(goldInRoom[2]);
            fileReader.close();
            if (playerXCoordinate == roomXCoord && playerYCoordinate == roomYCoord){
                goldAmountInRoom = goldAmount;
                return true;
            }
            readGoldRoomNumber++;
        }
        return false;
    }
    public void storeMonsterStats(Integer[] monsterStats) throws IOException {
        PrintWriter printWriter = new PrintWriter((new FileWriter("monster" + monsterNumber + ".txt")));
        monsterNumber++;
            //                  Health,                 Strength,           Dexterity,              Intelligence
        printWriter.println(monsterStats[0] + "|" + monsterStats[1] + "|" + monsterStats[2] + "|" + monsterStats[3]);
        printWriter.println(playerXCoordinate + "," + playerYCoordinate);
        printWriter.close();

    }
    public String[] getMonsterStats() throws FileNotFoundException {
        readMonsterNumber = 0;
        while(readMonsterNumber <= monsterNumber){
            File fileToRead = new File("monster" + readMonsterNumber + ".txt");
            if (!fileToRead.exists()){
                readMonsterNumber++;
                continue;
            }
            Scanner fileReader = new Scanner(fileToRead);
                String line = fileReader.nextLine();
                String[] monsterStatistics = line.split("[|]");
                line = fileReader.nextLine();
                String[] monsterCoordinates = line.split(",");
                int monsterXCord = Integer.parseInt(monsterCoordinates[0]);
                int monsterYCord = Integer.parseInt(monsterCoordinates[1]);
                fileReader.close();
                readMonsterNumber++;
                if (playerXCoordinate == monsterXCord && playerYCoordinate == monsterYCord){
                    fileToRead.delete();
                    return monsterStatistics;
                }

        }
        return null;
    }
    public void lootGold() throws IOException {
        if (goldAmountInRoom > 0) {
            PrintWriter printWriter = new PrintWriter((new FileWriter("gold" + readGoldRoomNumber + ".txt")));
            printWriter.println(playerXCoordinate + "|" + playerYCoordinate + "|" + "0");
            printWriter.close();
        }
    }
}
