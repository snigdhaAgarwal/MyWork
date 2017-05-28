/*
 * Implemented for nxn 
 * Computer can play against Human in 3x3
 * Command line arguments:
 * 3 Human Computer
*/

import java.util.Scanner;

class TicTacToe {
  public static void main(final String[] args) throws InterruptedException {
    final Game g = new Game(Integer.parseInt(args[0]), args[1], args[2]);

    boolean playAgain = false;
    do {
      g.start();
      playAgain = askToPlayAgain();
    } while (playAgain);

  }

  private static boolean askToPlayAgain() {
    System.out.println("Do you want to play again? Type Y or N");
    final Scanner scanner = new Scanner(System.in);
    final String line = scanner.nextLine();
    if (line.equalsIgnoreCase("Y")) {
      return true;
    } else {
      return false;
    }
  }
}


class Game {
  int gridSize;
  Player player1;
  Player player2;
  GameState state;

  public Game(final int gridSize, final String player1, final String player2) {
    this.gridSize = gridSize;
    this.player1 = PlayerFactory.createPlayer(player1, Symbol.X);
    this.player2 = PlayerFactory.createPlayer(player2, Symbol.O);
    this.player1.setOpponent(this.player2);
    this.player2.setOpponent(this.player1);
    load();
    initialize();
  }

  public void initialize() {
    state = new GameState(gridSize, player1);
  }

  private void load() {
    // load resources here
  }

  public void start() throws InterruptedException {

    initialize();
    draw();

    while (!state.isEnded()) {
      update();
      draw();
      if (player1 instanceof AIPlayer && player2 instanceof AIPlayer) {
        Thread.sleep(1000);
      }
    }
  }

  // all game logic here
  // take user input
  // update variables and gamestate
  // physics etc.
  public void update() {
    final Player currentPlayer = state.getCurrentPlayer();
    GameMove gameMove = currentPlayer.decideMove(state);
    while (!gameMove.isValidMove(state.getGrid())) {
      gameMove = currentPlayer.decideMove(state);
    }
    state.updateGrid(gameMove);
  }

  // draw to screen according to gamestate
  public void draw() {
    System.out.println("The current state is as below:");
    state.getGrid().draw();
    // check if game ended
    if (state.isADraw()) {
      System.out.println("Game Over! No winner..It's a draw :)");
    } else if (state.isEnded()) {
      System.out.println("Winner of the game is : "
          + (state.getWinner() == player1 ? "player1" : "player2") + ". Good job :D");
    } else {
      System.out.println("Next turn to be played by "
          + (state.getCurrentPlayer() == player1 ? "player 1" : "player 2"));
    }
  }
}


class GameState {


  TicTacToeGrid grid;
  Player currentPlayer;
  GameMove latestGameMove;
  private boolean isEnded;
  Player winner;
  private boolean isADraw;


  GameState(final int gridSize, final Player player) {
    grid = new TicTacToeGrid(gridSize);
    currentPlayer = player;
    isEnded = false;
    isADraw = false;
  }

  /**
   * The constructor for <code>GameState</code> having following parameters
   * 
   * @param state
   */
  public GameState(final GameState state) {
    grid = new TicTacToeGrid(state.getGrid());/*
    if(state.getCurrentPlayer() instanceof AIPlayer){
      currentPlayer = new AIPlayer(state.getCurrentPlayer());
    } else if(state.getCurrentPlayer() instanceof HumanPlayer){
      currentPlayer = new HumanPlayer(state.getCurrentPlayer());
    }*/
    currentPlayer = state.getCurrentPlayer();
    isEnded = state.isEnded();
    isADraw = state.isADraw();
  }

  public Player getWinner() {
    return winner;
  }

  public boolean isADraw() {
    return isADraw;
  }

  public GameMove getLatestGameMove() {
    return latestGameMove;
  }

  public boolean isEnded() {
    return isEnded;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public TicTacToeGrid getGrid() {
    return grid;
  }

  public void setCurrentPlayer(final Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public void updateGrid(final GameMove gameMove) {
    this.latestGameMove = gameMove;
    grid.update(gameMove, currentPlayer.getSymbol());
    if (grid.checkIfHasWinner(gameMove)) {
      winner = currentPlayer;
      isEnded = true;
    } else if (grid.checkIfFull()) {
      isADraw = true;
      isEnded = true;
    }
    currentPlayer = currentPlayer.getOpponent();
  }
}


class TicTacToeGrid {
  Symbol[][] grid;
  int numOfElements;

  TicTacToeGrid(final int size) {
    grid = new Symbol[size][size];
    numOfElements = 0;
  }

  public TicTacToeGrid(final TicTacToeGrid copyGrid) {
    grid = new Symbol[copyGrid.getSide()][copyGrid.getSide()];
    for(int i=0;i<copyGrid.getSide();i++){
      for(int j=0;j<copyGrid.getSide();j++) {
        grid[i][j] = copyGrid.getElement(i, j);
      }
    }
    numOfElements = copyGrid.getNumOfElements();
  }

  /**
   * @return the numOfElements
   */
  public int getNumOfElements() {
    return numOfElements;
  }

  /**
   * @param numOfElements the numOfElements to set
   */
  public void setNumOfElements(final int numOfElements) {
    this.numOfElements = numOfElements;
  }

  public int getSide() {
    return grid.length;
  }

  public boolean checkIfHasWinner(final GameMove latestGameMove) {
    // 1 player has not completed n moves
    if (numOfElements < 2 * grid.length - 1) {
      return false;
    }
    int count = 0;
    final int currentRow = latestGameMove.getGridRow();
    final int currentCol = latestGameMove.getGridCol();
    // check vertically:
    for (final Symbol[] element : grid) {
      if (element[currentCol] == grid[currentRow][currentCol]) {
        count++;
      }
    }
    if (count == grid.length) {
      return true;
    }

    count = 0;
    // check horizontally
    for (int i = 0; i < grid.length; i++) {
      if (grid[currentRow][i] == grid[currentRow][currentCol]) {
        count++;
      }
    }
    if (count == grid.length) {
      return true;
    }

    count = 0;
    // check diagonally
    if (currentCol == currentRow) {
      for (int i = 0; i < grid.length; i++) {
        if (grid[i][i] == grid[currentRow][currentCol]) {
          count++;
        }
      }
      if (count == grid.length) {
        return true;
      }
    }

    if (currentCol + currentRow + 1 == grid.length) {
      for (int i = 0; i < grid.length; i++) {
        if (grid[i][grid.length - i - 1] == grid[currentRow][currentCol]) {
          count++;
        }
      }
      if (count == grid.length) {
        return true;
      }
    }

    return false;
  }

  public boolean checkIfFull() {
    if (numOfElements == grid.length * grid.length) {
      return true;
    }
    return false;
  }

  public Symbol getElement(final int row, final int col) {
    return grid[row][col];
  }


  public void draw() {
    for (int i = 0; i < 2 * grid.length + 1; i++) {
      System.out.print("_");
    }
    System.out.println();
    for (final Symbol[] element : grid) {
      for (int j = 0; j < grid.length; j++) {
        if (element[j] == null) {
          System.out.print("| ");
        } else {
          System.out.print("|" + element[j].getValue());
        }
      }
      System.out.print("|");
      System.out.println();
      for (int i = 0; i < 2 * grid.length + 1; i++) {
        System.out.print("-");
      }
      System.out.println();
    }

  }

  public void update(final GameMove gameMove, final Symbol symbol) {
    grid[gameMove.getGridRow()][gameMove.getGridCol()] = symbol;
    numOfElements++;
  }

  public void setElement(final int i, final int j, final Symbol symbol) {
    grid[i][j] = symbol;
  }
}


class GameMove {

  int gridRow;
  int gridCol;

  public GameMove() {
    this.gridRow = -1;
    this.gridCol = -1;
  }


  /**
   * @return the gridRow
   */
  public int getGridRow() {
    return gridRow;
  }


  /**
   * @param gridRow the gridRow to set
   */
  public void setGridRow(final int gridRow) {
    this.gridRow = gridRow;
  }


  /**
   * @return the gridCol
   */
  public int getGridCol() {
    return gridCol;
  }


  /**
   * @param gridCol the gridCol to set
   */
  public void setGridCol(final int gridCol) {
    this.gridCol = gridCol;
  }


  public Boolean isValidMove(final TicTacToeGrid grid) {
    return (gridRow < grid.getSide() && gridRow >= 0 && gridCol < grid.getSide() && gridCol >= 0 && grid
        .getElement(gridRow, gridCol) == null);
  }
}


abstract class Player {
  Symbol symbol;
  Player opponent;

  Player(final Symbol symbol) {
    this.symbol = symbol;
  }

   /**
   * @return the opponent
   */
  public Player getOpponent() {
    return opponent;
  }

  /**
   * @param opponent the opponent to set
   */
  public void setOpponent(final Player opponent) {
    this.opponent = opponent;
  }

  /**
   * The method <code>setSymbol</code> {description}
   * @param symbol2
   * @see
   */
  public void setSymbol(final Symbol symbol2) {
    // TODO Auto-generated method stub
    
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public abstract GameMove decideMove(GameState state);
}


class PlayerFactory {
  public static Player createPlayer(final String specification, final Symbol symbol) {
    if (specification.equals("Human")) {
      return new HumanPlayer(symbol);
    } else if (specification.equals("Computer")) {
      return new AIPlayer(symbol);
    }
    return null;
  }
}


class HumanPlayer extends Player {

  HumanPlayer(final Symbol symbol) {
    super(symbol);
  }

  @Override
  public GameMove decideMove(final GameState state) {
    System.out.print("Please state your move in row <space> column format: ");
    final Scanner scanner = new Scanner(System.in);
    final String line = scanner.nextLine();
    final GameMove gameMove = new GameMove();
    gameMove.setGridRow(Integer.parseInt(line.split(" ")[0]));
    gameMove.setGridCol(Integer.parseInt(line.split(" ")[1]));
    return gameMove;
  }
}


class AIPlayer extends Player {

  public AIPlayer(final Symbol symbol) {
    super(symbol);
  }

  @Override
  public GameMove decideMove(final GameState state) {
    final GameMove gameMove = new GameMove();
    final GameState copyGameState = new GameState(state); // for fear of modification by minMax
                                                          // method
    minMax(copyGameState, gameMove, 0);
    return gameMove;
  }


  //NOTE: enhancements - don't pass GameMove Call for children of first state and not from first state itself
  // makeGetChildren method which returns List<GameState>
  private int minMax(final GameState state, final GameMove gameMove, final int level) {
    
    
    int utility;
    int optimalUtility;
    
    final TicTacToeGrid grid = state.getGrid();
    if (level % 2 == 0) {
      utility = Integer.MAX_VALUE;
      optimalUtility = Integer.MAX_VALUE;
    } else {
      utility = Integer.MIN_VALUE;
      optimalUtility = Integer.MIN_VALUE;
    }
    for (int i = 0; i < grid.getSide(); i++) {
      for (int j = 0; j < grid.getSide(); j++) {
        if (grid.getElement(i, j) == null) {
          gameMove.setGridRow(i);
          gameMove.setGridCol(j);
          final GameState copyGameState = new GameState(state);
          copyGameState.updateGrid(gameMove);
          
          if(copyGameState.isADraw()) {
            return 0;
          } else if(copyGameState.isEnded()){
            if(level%2==0) {
              return -1; // my win
            } else {
              return 1;
            }
          } else{
            utility = minMax(copyGameState, gameMove, level + 1);
            if (level % 2 == 0 && utility < optimalUtility || level % 2 != 0 && utility > optimalUtility) {
              optimalUtility = utility;
              gameMove.setGridRow(i);
              gameMove.setGridCol(j);
            }
          }
        }
      }
    }
    return optimalUtility;
  }

}


enum Symbol {
  X('X'), O('O');

  char value;

  Symbol(final char value) {
    this.value = value;
  }

  public char getValue() {
    return value;
  }


}
