// // Copyright 2015 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//	
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package bot;
import java.util.Random;

/**
 * BotStarter class
 * 
 * Magic happens here. You should edit this file, or more specifically
 * the makeTurn() method to make your bot do more than random moves.
 * 
 * @author Jim van Eeden <jim@starapple.nl>, Joost de Meij <joost@starapple.nl>
 */

public class BotStarter {	
   Field field;
   BotParser parser;
   private int mBotId;

    /**
     * Makes a turn. Edit this method to make your bot smarter.
     *
     * @return The column where the turn was made.
     */
  public int makeTurn() {
    // int move = new Random().nextInt(7);    
    int move = 0;               // what my next move should be
    int maxValue = -100;    // what that move is worth to me

    field = parser.getField();
    int nc = field.getNrColumns();
    int nr = field.getNrRows();

    /**
     * Iterate through all possible moves
     * If I have a move that wins me the game, then make that move
     * Otherwise, make the move that gives opponent the least
     * amount of value
     */
    int [] preferred_cols = { 3, 2, 4, 1, 5, 0, 6 };
    for (int i=0; i<nc; i++) {
      int c = preferred_cols[i];
      // if column is full, then this is not a valid move
      if (field.isColumnFull(c))
        continue;

      // create a copy of the current field
      Field newField = new Field(0, 0);
      newField.parseFromString(field.toString());

      newField.addDisc(c, mBotId);

      // Make this move if I've just won the game
      if (winGame(newField, mBotId)) {
        move = c;
        break;
      }

      /**
       * Iterate through the opponent's possible moves
       * If the opponent is now in a position to win this game
       * Then we assign this move a value of -10
       */
      int value = minMax(newField, 3 - mBotId, 4);

      if (value > maxValue) {
        maxValue = value;
        move = c;
      }
    }

    return move;
  }
  
  private int minMax(Field field, int botId, int steps) {
      if (steps == 0) {
        return 0;   //TODO: replace with heuristic function
      }

      if (winGame(field, 3 - botId)) {
        return (botId == mBotId ? 1000 : -1000);
      }

      int bestValue = botId == mBotId ? -1000 : 1000;
      int nc = field.getNrColumns();
      for (int d=0; d<nc; d++) {
        if (field.isColumnFull(d))
          continue;

        Field newField = new Field(0, 0);
        newField.parseFromString(field.toString());
        
        newField.addDisc(d, botId);
         
        int value = minMax(newField, 3-botId, steps-1);
        if (botId == mBotId && value > bestValue) {
          bestValue = value;
        } else if (botId != mBotId && value < bestValue) {
          bestValue = value;
        }
      }

      return bestValue;
  }

  /**
   * Define the heuristic function as A - B
   * A = # of possible ways (available lines of 4) for botId to win
   * B = # of possible ways for other bot to win
   
  private int heuristic_fcn(Field field, int botId) {
    int nc = field.getNrColumns();
    int nr = field.getNrRows();

    int countBot = 0, countOther = 0;

    // check columns
    for (int i=0; i<nc; i++) {
      
      for (int j=0; j<nr-3; j++) {
        for (int k=j; k<4; k++) {

        }
        if (field.getDisc(i, j) == botId) {
          count = count + 1;
        } else {
          count = 0;
        }

        if (count >= 4) {
          return true;
        }
      }
    }
    */

  private boolean winGame(Field field, int botId) {
    int nc = field.getNrColumns();
    int nr = field.getNrRows();
    int count = 0;

    // check columns
    for (int i=0; i<nc; i++) {
      count = 0;
      for (int j=0; j<nr; j++) {
        if (field.getDisc(i, j) == botId) {
          count = count + 1;
        } else {
          count = 0;
        }

        if (count >= 4) {
          return true;
        }
      }
    }

    // check rows
    for (int j=0; j<nr; j++) {
      count = 0;
      for (int i=0; i<nc; i++) {
        if (field.getDisc(i, j) == botId) {
          count = count + 1;
        } else {
          count = 0;
        }

        if (count >= 4) {
          return true;
        }
      }
    }

    // check diagnols
    // For each diagnol, either c + r = constant or c - r = constant
    for (int k=1; k<=6; k++) {
      count = 0;
      for (int i=0; i<nc; i++) {
        int j = k - i + 4;
        if (j >= 0 && j < nr && field.getDisc(i, j) == botId) {
          count = count + 1;
        } else {
          count = 0;
        }
        if (count >= 4) {
          return true;
        }
      }

      count = 0;
      for (int i=0; i<nc; i++) {
        int j = k + i - 4;
        if (j >= 0 && j < nr && field.getDisc(i, j) == botId) {
          count = count + 1;
        } else {
          count = 0;
        }
        if (count >= 4) {
          return true;
        }
      }
    }
    return false;
  }

  public void setBotId(int mBotId) {
    this.mBotId = mBotId;
  }

  public void setParser(BotParser parser) {
    this.parser = parser;
  }

 	public static void main(String[] args) {
 		BotParser parser = new BotParser(new BotStarter());
 		parser.run();
 	}
 	
 }
