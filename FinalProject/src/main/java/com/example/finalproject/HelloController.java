package com.example.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class HelloController {
    @FXML
    private Button leftMoveButton;
    @FXML
    private Button upMoveButton;
    @FXML
    private Button rightMoveButton;
    @FXML
    private Button downMoveButton;
    @FXML
    private TextArea mainTextBox;
    @FXML
    private TextArea secondaryTextBox;
    @FXML
    private Button searchActionButton;
    @FXML
    private Button fightActionButton;
    @FXML
    private Button runActionButton;
    @FXML
    private Button sleepActionButton;
    @FXML
    private Button startGameButton;
    @FXML
    private Label playerStrength;
    @FXML
    private Label playerDexterity;
    @FXML
    private Label playerIntelligence;
    @FXML
    private Label playerGold;
    @FXML
    private Label playerHealth;
    @FXML
    private Label npcHealth;
    @FXML
    private Label npcStrength;
    @FXML
    private Label npcDexterity;
    @FXML
    private Label npcIntelligence;
    private Dungeon mainDungeon;
    private Player mainPlayer;
    private Integer[] npcStats;
    private String[] reloadStats;
    @FXML
    public void onMoveLeftButtonClicked(ActionEvent actionEvent) {
        playerMovement(-1,0, "left");
    }
    @FXML
    public void onMovedRightButtonClicked(ActionEvent actionEvent) {
        playerMovement(1,0, "right");
    }
    @FXML
    public void onMoveUpButtonClicked(ActionEvent actionEvent) {
        playerMovement(0,-1, "up");
    }
    @FXML
    public void onMoveDownButtonClicked(ActionEvent actionEvent) {
        playerMovement(0,1, "down");
    }
    @FXML
    public void onSearchButtonClicked(ActionEvent actionEvent) {
        mainTextBox.appendText("You search the room for anything unusual \n");
        checkForGold();
    }
    @FXML
    public void onFightButtonClicked(ActionEvent actionEvent) {
        mainTextBox.appendText("You attack the monster \n");
        playerAttackMonster();
    }
    @FXML
    public void onRunButtonClicked(ActionEvent actionEvent) {
        try {
            mainTextBox.appendText("You turn to run \n");
            mainDungeon.storeMonsterStats(npcStats);
            monsterEncountered(false);
            isRunAwaySuccessful();
            clearNPCStats();
            secondaryTextBox.clear();
            //move player to the next unblocked room to the right
            int xRoomCount = 1;
            while (true){
                if (!mainDungeon.isRoomBlocked(mainDungeon.getPlayerXCoordinate() + xRoomCount, mainDungeon.getPlayerYCoordinate())){
                    mainDungeon.setPlayerXCoordinate(mainDungeon.getPlayerXCoordinate() + xRoomCount);
                    secondaryTextBox.appendText(mainDungeon.printMazeToScreen());
                    break;
                } else {
                    xRoomCount++;
                }
            }
        } catch (IOException ex){
            System.out.println(ex);
        }
    }
    @FXML
    public void onSleepButtonClicked(ActionEvent actionEvent) {
        mainTextBox.appendText("You lay down and take a nap \n");
        mainPlayer.setHealth(20);
        mainTextBox.appendText("Your health has been restored! \n");
        setPlayerStats();
        sleepGeneratesMonster();
    }
    @FXML
    public void onStartButtonClicked(ActionEvent actionEvent) {
        mainTextBox.clear();
        mainDungeon = new Dungeon();
        mainPlayer = new Player();
        gameOver(false);
        secondaryTextBox.appendText(mainDungeon.printMazeToScreen());
        startGameButton.setDisable(true);
        setPlayerStats();
        clearNPCStats();
        gameOver(false);
        monsterEncountered(false);
    }
    public void setPlayerStats(){
        playerHealth.setText("Health: " + mainPlayer.getHealth());
        playerDexterity.setText("Dexterity: " + mainPlayer.getDexterity());
        playerIntelligence.setText("Intelligence: " + mainPlayer.getIntelligence());
        playerStrength.setText("Strength: " + mainPlayer.getStrength());
        playerGold.setText("Gold: " + mainPlayer.getGold());
    }
    public void generateNpcStats(){
        NonPlayerCharacter mainNPC = new NonPlayerCharacter();
        npcStats = new Integer[4];
        npcStats[0] = mainNPC.getHealth();
        npcStats[1] = mainNPC.getStrength();
        npcStats[2] = mainNPC.getDexterity();
        npcStats[3] = mainNPC.getIntelligence();
        setNPCStats();
    }
    public void setNPCStats(){
        npcHealth.setText("Health: " + npcStats[0]);
        npcStrength.setText("Strength: " + npcStats[1]);
        npcDexterity.setText("Dexterity: " + npcStats[2]);
        npcIntelligence.setText("Intelligence: " + npcStats[3]);

    }
    public void reloadNpcStats(){
        NonPlayerCharacter mainNPC = new NonPlayerCharacter(Integer.parseInt(reloadStats[0]), Integer.parseInt(reloadStats[1]), Integer.parseInt(reloadStats[2]), Integer.parseInt(reloadStats[3]));
        npcStats = new Integer[4];
        npcStats[0] = mainNPC.getHealth();
        npcStats[1] = mainNPC.getStrength();
        npcStats[2] = mainNPC.getDexterity();
        npcStats[3] = mainNPC.getIntelligence();
        setNPCStats();
    }
    public void clearNPCStats(){
        npcDexterity.setText("");
        npcHealth.setText("");
        npcIntelligence.setText("");
        npcStrength.setText("");
    }
    public void playerMovement(Integer xDirection, Integer yDirection, String direction){
        try {
            mainDungeon.generateBlockedRoom(mainDungeon.getPlayerXCoordinate() + xDirection, mainDungeon.getPlayerYCoordinate() + yDirection);
            if (!mainDungeon.isRoomBlocked(mainDungeon.getPlayerXCoordinate() + xDirection, mainDungeon.getPlayerYCoordinate() + yDirection)) {
                mainTextBox.appendText("You moved " + direction + "\n");
                secondaryTextBox.clear();
                mainDungeon.setPlayerXCoordinate(mainDungeon.getPlayerXCoordinate() + xDirection);
                mainDungeon.setPlayerYCoordinate(mainDungeon.getPlayerYCoordinate() + yDirection);
                secondaryTextBox.appendText(mainDungeon.printMazeToScreen());
                checkForMonster();
            } else {
                mainTextBox.appendText("That room is blocked, you cannot enter it. \n");
                secondaryTextBox.clear();
                secondaryTextBox.appendText(mainDungeon.printMazeToScreen());
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public void checkForMonster() {
        try {
            if (mainDungeon.isMonsterInRoom()){
                reloadStats = mainDungeon.getMonsterStats();
                monsterEncountered(true);
                reloadNpcStats();
                mainTextBox.appendText("A monster is still in this room! \n");
                mainTextBox.appendText("You must choose to either fight or flee the maze monster \n \n");
            } else if (mainDungeon.generateMonsterInRoom()){
                mainTextBox.appendText("A monster is in the room! \n");
                generateNpcStats();
                monsterEncountered(true);
                mainTextBox.appendText("You must choose to either fight or flee the maze monster \n \n");
            }
        } catch (FileNotFoundException ex){
            System.out.println(ex);
        }

    }
    public void monsterEncountered(Boolean buttonState){
        leftMoveButton.setDisable(buttonState);
        rightMoveButton.setDisable(buttonState);
        upMoveButton.setDisable(buttonState);
        downMoveButton.setDisable(buttonState);
        sleepActionButton.setDisable(buttonState);
        searchActionButton.setDisable(buttonState);
        runActionButton.setDisable(!buttonState);
        fightActionButton.setDisable(!buttonState);
    }
    public void playerAttackMonster(){
        Random random = new Random();
        int diceRoll = random.nextInt(1,21);
            if (diceRoll >= npcStats[2]){
                //Player Attacks
                npcStats[0] -= (mainPlayer.getStrength() / 3);
                mainTextBox.appendText("You hit the monster for " + (mainPlayer.getStrength() / 3) + " damage \n");
                npcHealth.setText("Health: " + npcStats[0]);
            } else {
                //Failed Attack
                mainTextBox.appendText("You did not role a high enough dexterity to hurt the monster \n");
            }
            //Check if monster is dead
            if (npcStats[0] <= 0){
                mainTextBox.appendText("You have defeated the monster! \n");
                clearNPCStats();
                monsterEncountered(false);
            } else {
                monsterAttackPlayer();
                if (!(mainPlayer.getHealth() <= 0)){
                    mainTextBox.appendText("The monster is still standing, you can either attack again or run away. \n \n");
                }
            }
    }
    public void monsterAttackPlayer(){
        Random random = new Random();
        int diceRoll = random.nextInt(1,21);
            if (diceRoll >= mainPlayer.getDexterity()){
                //Monster Attack
                mainTextBox.appendText("Now the monster attacks you \n");
                if ((npcStats[1] / 3) > 1){
                    mainPlayer.setHealth(mainPlayer.getHealth() - (npcStats[1] / 3));
                    mainTextBox.appendText("The monster hit you for " + (npcStats[1] / 3) + " damage \n");
                    playerHealth.setText("Health: " + mainPlayer.getHealth());
                } else {
                    mainPlayer.setHealth(mainPlayer.getHealth() - 1);
                    mainTextBox.appendText("The monster hit you for 1 damage \n");
                    playerHealth.setText("Health: " + mainPlayer.getHealth());
                }
            } else {
                mainTextBox.appendText("The monster missed his attack! \n");
            }
            //Check if player is dead
            if (mainPlayer.getHealth() <= 0){
                mainTextBox.clear();
                secondaryTextBox.clear();
                gameOver(true);
                mainTextBox.appendText("You have been defeated by the monster. Game over \n");
                startGameButton.setDisable(false);
            }
        }
    public void sleepGeneratesMonster(){
        Random random = new Random();
        int diceRoll = random.nextInt(1,7);
        if (diceRoll == 1){
            mainTextBox.appendText("During your slumber, a monster has wondered into the room! \n");
            generateNpcStats();
            monsterEncountered(true);
            mainTextBox.appendText("You must choose to either fight or flee the maze monster \n \n");
        }
    }
    public void isRunAwaySuccessful(){
        Random random = new Random();
        int diceRoll = random.nextInt(1,21);
        if (diceRoll < npcStats[3]){
            mainTextBox.appendText("The monster sees you running in fear and attacks! \n");
            monsterAttackPlayer();
        } else {
            mainTextBox.appendText("You have successfully evaded the monster in that room \n");
        }
    }
    public void checkForGold(){
        try {
            if (mainDungeon.isGoldInRoom() && mainDungeon.getGoldAmountInRoom() == 0){
                mainTextBox.appendText("You have already looted this room \n \n");
            } else if (mainPlayer.doIntelligenceCheck()){
                while (true) {
                    if (mainDungeon.isGoldInRoom() && mainDungeon.getGoldAmountInRoom() > 0){
                        mainTextBox.appendText("You've found " + mainDungeon.getGoldAmountInRoom() + " gold bars in the room! \n \n");
                        mainPlayer.setGold(mainDungeon.getGoldAmountInRoom() + mainPlayer.getGold());
                        setPlayerStats();
                        mainDungeon.lootGold();
                        break;
                    } else if (!mainDungeon.isGoldInRoom()){
                        mainDungeon.generateGoldInRoom(mainDungeon.getPlayerXCoordinate(), mainDungeon.getPlayerYCoordinate());
                    }
                }
            } else {
                mainTextBox.appendText("You do not see anything unusual in the room \n \n");
            }
        } catch (IOException ex){
            System.out.println(ex);
        }

    }
    public void gameOver(Boolean buttonState){
        leftMoveButton.setDisable(buttonState);
        rightMoveButton.setDisable(buttonState);
        upMoveButton.setDisable(buttonState);
        downMoveButton.setDisable(buttonState);
        sleepActionButton.setDisable(buttonState);
        searchActionButton.setDisable(buttonState);
        runActionButton.setDisable(buttonState);
        fightActionButton.setDisable(buttonState);
    }
}