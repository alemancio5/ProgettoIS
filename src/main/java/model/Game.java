package model;
import network.message.serverSide.InfoGameMessage;
import observer.Observable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * class that defines a game
 * @author Alessandro Mancini
 */
public class Game extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = -8506633854746922798L;
    private int num;
    private static final int MAX_PLAYERS = 4;
    private List<Player> players;
    private List<PersonalGoalCardType> personalgoalcards;
    private List<CommonGoalCard> commongoalcards;
    private List<CommonGoalCardScore> commongoalcardscores;
    private Board board;
    private Stack<Tile> bag;
    private Map<String, Integer> playerScore;


    public Game() {
        this.players = new ArrayList<>();
        this.commongoalcards = new ArrayList<>();
        this.commongoalcardscores = new ArrayList<>();
        this.personalgoalcards = new ArrayList<>();
        this.playerScore = new HashMap<>();
        this.initCommongoalcards();
        this.initBag();
    }

    /**
     * initialization of the game
     * @param numMax number of players chosen by the first player joined in the game
     * @author Alessandro Mancini
     */
    public void initGame(int numMax) {
        if(numMax >= 2 && numMax <= MAX_PLAYERS) {
            this.num = numMax;
        }
        this.initBoard();
        this.board.fillBoard(this.bag);
        this.initCommongoalcardscores();
    }

    /**
     * number of current players added in the game in a generic time
     * @return number of current players
     */
    public int getCurrentNum() {
        return players.size();
    }

    /**
     * getter of the max number of players chosen by the first player
     * @author Alessandro Mancini
     */
    public int getNum() {
        return num;
    }

    /**
     * setter of the max number of players chosen by the first player
     * @param num max number of players
     * @author Alessandro Mancini
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * check if a nickname is already use or not in the game
     * @param nickname selected by the client
     * @return true if the nickname is already used, false otherwise
     * @author Chiara Nguyen Ba
     */
    public boolean isNicknameTaken(String nickname) {
        return players.stream()
                .anyMatch(p -> nickname.equals(p.getNickname()));
    }

    /**
     * adds a new player to the game
     * @param nickname of the player to add to the game
     * @param isReconnected true if the player is reconnected, false otherwise
     * @author Alessandro Mancini
     */
    public void addPlayer(String nickname, boolean isReconnected) {
        if (!isReconnected) {
            Player player = new Player(nickname);
            players.add(player);
            initPersonalgoalcard(player);
            playerScore.put(nickname, 0);
        }
        notifyObserver(new InfoGameMessage(players, num));
    }


    /**
     * returns a player given his nickname
     * @param nickname of player to be found
     * @return the player of given nickname, null otherwise
     * @author Chiara Nguyen Ba
     */
    public Player getPlayerByNickname(String nickname) {
        return players.stream()
                .filter(player -> nickname.equals(player.getNickname()))
                .findFirst()
                .orElse(null);
    }

    /**
     * return a list of player nicknames that are already in the game
     * @return a list with all nicknames in the game
     * @author Alessandro Mancini
     */
    public List<String> getAllPlayers() {
        List<String> playersNicknames = new ArrayList<>();
        for (Player p : players) {
            playersNicknames.add(p.getNickname());
        }
        return playersNicknames;
    }

    /**
     * notify the disconnection of a player to the connected players
     */
    public void disconnectionOfPlayer() {
        List<Player> playersWithoutDisconnected = players.stream()
                .filter(player -> !player.isDisconnected())
                .collect(Collectors.toList());
        notifyObserver(new InfoGameMessage(playersWithoutDisconnected, num));
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * initializer of personalgoalcard of the player
     * @param player to be set the personal goal card
     * @author Alessandro Mancini
     */
    private void initPersonalgoalcard(Player player) {
        PersonalGoalCardType type;
        Random rand = new Random();
        do {
            type = PersonalGoalCardType.values()[rand.nextInt(PersonalGoalCardType.values().length)];
        } while (personalgoalcards.contains(type));
        personalgoalcards.add(type);
        player.setPersonalGoalCard(new PersonalGoalCard(type));
    }

    public List<PersonalGoalCardType> getPersonalgoalcards() {
        return personalgoalcards;
    }

    /**
     * initializer of commongoalcards with 2 random cards
     * @author Alessandro Mancini
     */
    private void initCommongoalcards() {
        Random rand = new Random();

        int id1 = rand.nextInt(12);
        CommonGoalCard c1 = typeCommongoalcards(id1);
        this.commongoalcards.add(c1);

        int id2 = rand.nextInt(12);
        while (id1 == id2) {
            id2 = rand.nextInt(12);
        }
        CommonGoalCard c2 = typeCommongoalcards(id2);
        this.commongoalcards.add(c2);
    }

    /**
     * help the initializer of commongoalcards, returning the correct type associated to the id
     * @param id random int for the switch
     * @author Alessandro Mancini
     */
    private CommonGoalCard typeCommongoalcards(int id) {
        return switch (id) {
            case 0 -> new CommonSixGroups();
            case 1 -> new CommonFourCorners();
            case 2 -> new CommonFourGroups();
            case 3 -> new CommonTwoSquares();
            case 4 -> new CommonMaxThreeColumns();
            case 5 -> new CommonEightSameType();
            case 6 -> new CommonMaxThreeRows();
            case 7 -> new CommonDifferentColumns();
            case 8 -> new CommonDifferentRows();
            case 9 -> new CommonXSameType();
            case 10 -> new CommonSameDiagonal();
            case 11 -> new CommonIncreasingHeight();
            default -> null;
        };
    }

    /**
     * initializer of commongoalcards
     * @author Alessandro Mancini
     */
    public List<CommonGoalCard> getCommongoalcards() {
        return commongoalcards;
    }

    /**
     * initializer of commongoalcardscores
     * @author Alessandro Mancini
     */
    private void initCommongoalcardscores() {
        this.commongoalcardscores.add(new CommonGoalCardScore(this.num));
        this.commongoalcardscores.add(new CommonGoalCardScore(this.num));
    }

    public List<CommonGoalCardScore> getCommongoalcardscores() {
        return commongoalcardscores;
    }

    /**
     * initializer of board
     * @author Alessandro Mancini
     */
    private void initBoard() {
        this.board = new Board(this.num);
    }

    public Board getBoard() {
        return board;
    }

    /**
     * initializer of the bag
     * @author Alessandro Mancini
     */
    private void initBag() {
        this.bag = new Stack<>();
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.CAT, 1);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.CAT, 2);
            this.bag.add(t);
        }
        for (int i = 0; i < 8; i++) {
            Tile t = new Tile(TileType.CAT, 3);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.PLANT, 1);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.PLANT, 2);
            this.bag.add(t);
        }
        for (int i = 0; i < 8; i++) {
            Tile t = new Tile(TileType.PLANT, 3);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.GAME, 1);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.GAME, 2);
            this.bag.add(t);
        }
        for (int i = 0; i < 8; i++) {
            Tile t = new Tile(TileType.GAME, 3);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.BOOK, 1);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.BOOK, 2);
            this.bag.add(t);
        }
        for (int i = 0; i < 8; i++) {
            Tile t = new Tile(TileType.BOOK, 3);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.TROPHY, 1);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.TROPHY, 2);
            this.bag.add(t);
        }
        for (int i = 0; i < 8; i++) {
            Tile t = new Tile(TileType.TROPHY, 3);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.FRAME, 1);
            this.bag.add(t);
        }
        for (int i = 0; i < 7; i++) {
            Tile t = new Tile(TileType.FRAME, 2);
            this.bag.add(t);
        }
        for (int i = 0; i < 8; i++) {
            Tile t = new Tile(TileType.FRAME, 3);
            this.bag.add(t);
        }
        Collections.shuffle(this.bag);
    }

    public Stack<Tile> getBag() {
        return bag;
    }

    /**
     * checks if the common goal cards are completed
     * @param player to be checked the bookshelf
     * @author Chiara Nguyen Ba
     */
    public int checkCommonGoalCards(Player player) {
        int score = 0;
        if(!player.isDoneFirstCommon() && commongoalcards.get(0).check(player.getBookshelf())) {
            player.setDoneFirstCommon(true);
            score = commongoalcardscores.get(0).getStack().pop();
            player.setScore(player.getScore() + score);
        }
        if(!player.isDoneSecondCommon() && commongoalcards.get(1).check(player.getBookshelf())) {
            player.setDoneSecondCommon(true);
            score = commongoalcardscores.get(1).getStack().pop();
            player.setScore(player.getScore() + score);
        }
        return score;
    }

    /**
     * sets at the end of the game the ranking scores of the players
     * @author Chiara Nguyen Ba
     */
    public void setRankingScore() {
        Map<String, Integer> scores = new HashMap<>();
        for (Player p : players) {
            scores.put(p.getNickname(), p.getScore());
        }
        playerScore = scores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public Map<String, Integer> getPlayerScore() {
        return playerScore;
    }

    /**
     * clear all the game settings
     */
    public void clear() {
        removeAllObservers();
        players.clear();
        playerScore.clear();
        personalgoalcards.clear();
        commongoalcardscores.clear();
        commongoalcards.clear();
    }

    /**
     * replace the game for the implementation of persistence
     * @param players list of players
     * @param num number of players
     * @param board board
     * @param commonGoalCards list of common goal cards
     * @param commonGoalCardScores list of common goal card scores
     * @param bag bag of tiles
     * @param playerScore map of player scores
     */
    public void replaceGame(List<Player> players, int num, Board board, List<CommonGoalCard> commonGoalCards, List<CommonGoalCardScore> commonGoalCardScores, Stack<Tile> bag, Map<String, Integer> playerScore) {
        this.players = players;
        this.num = num;
        this.board = board;
        this.commongoalcards = commonGoalCards;
        this.commongoalcardscores = commonGoalCardScores;
        this.bag = bag;
        this.playerScore = playerScore;
        for (Player p : players) {
            p.setBookshelf(getPlayerByNickname(p.getNickname()).getBookshelf());
            p.setPersonalGoalCard(getPlayerByNickname(p.getNickname()).getPersonalGoalCard());
            p.setDoneFirstCommon(getPlayerByNickname(p.getNickname()).isDoneFirstCommon());
            p.setDoneSecondCommon(getPlayerByNickname(p.getNickname()).isDoneSecondCommon());
            p.setScore(getPlayerByNickname(p.getNickname()).getScore());
        }
    }
}
