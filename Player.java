/* Title:  Player.java
 * 
 * Author: Wayne Snyder (waysnyder@gmail.com)
 * Modified by: Kristel Tan (ktan@bu.edu) and Nisa Gurung (ngurung@bu.edu)
 * 
 * Date: 11/29/13
 * Purpose: This is a simple random player provided as part of the code distribution for HW 08 for CS 112, Fall 2014
 */

import java.util.*; 

public class Player {
  
  private static int[][] B = new int[8][8];
  
  private final static int Blue = 10;    
  private final static int Red = 1; 
  private final static int Blank = 0; 
  
  private final static int Inf = 1000000; 
  
  private static final int D = 6;
  
  private static  int column(int move) {
    return move % 8;
  }
  
  private static  int row(int move) {
    return move / 8;
  }
  
  /*
   * move(...) returns the best move on the board
   */ 
  public int move(int[][] B) {
    
    int max = -10000;     
    int bestMove = -1;  
    for(int c = 7; c >= 0; c--) {
      for(int r = 7; r >= 0; r--) { 
        if(B[r][c] == 0) {     // move is available
          System.out.println("r: " + r + "c: " + c); 
          
          B[r][c] = Blue;          // make the move
          
          int val = minMax(B, 1, -Inf, Inf); 
          
          if(val == Inf) { 
            B[r][c] = 0;
            return c; 
          } 
          System.out.println("val: " + val + " max: " + max + "\n"); 
          if(val >= max) {                             // if better move, remember it
            bestMove = c; 
            System.out.println("changed bestMove to: " + c + "\n"); 
            max = val; 
          }
          B[r][c] = 0; 
          break;
        }   
      }
    }// end if
                                                // end for
    System.out.println("bestMove: " + bestMove); 
    return bestMove; 
  }
  
  /*
   * eval(...) calls the helper method countPieces(...) to return 
   * a heuristic value that labels how strong a board is
   */ 
  private static int eval(int[][] B) {
    
    if(winForRed(B)) {
        return -Inf; 
    } else if(winForBlue(B)) {
      return Inf;
    } 
    
    int sum = 0;
    
    // Count rows
    for(int r = 0; r < 8; ++r) {
      for(int c = 0; c < 5; ++c) {
        sum += pointValue(countPieces(B[r][c],B[r][c+1],B[r][c+2],B[r][c+3]));
      } 
    }
    
    // Count columns
    for(int c = 0; c < 8; ++c) { 
      for(int r = 0; r <  5; ++r) { 
        sum += pointValue(countPieces(B[r][c],B[r+1][c],B[r+2][c],B[r+3][c])); 
      } 
    } 
    
    // Count diagonals
    for(int r = 7; r > 2; --r){
      for(int c = 7; c > 2; --c) { 
        sum += pointValue(countPieces(B[r][c],B[r - 1][c - 1], B[r - 2][c - 2], B[r - 3][c - 3])); 
      } 
    }
    
    for(int r = 7; r > 2; --r){
      for(int c = 0; c < 5; ++c) { 
        sum += pointValue(countPieces(B[r][c],B[r - 1][c + 1], B[r - 2][c + 2], B[r - 3][c + 3])); 
      } 
    }
    
    return sum;
    
  }
  
  /*
   * pointValue(...) converts the number of pieces on the board into 
   * an exteremely positive or negative heuristic value to signify
   * how good a board is
   */ 
  private static int pointValue(int numPieces) {
    if (numPieces == 1) {
      return 10;
    } else if (numPieces == -1) {
      return -10;
    } else if (numPieces == 2) {
      return 100;
    } else if (numPieces == -2) {
      return -100;
    } else if (numPieces == 3) {
      return 1000; 
    } else if (numPieces == -3) {
      return -1000;
    } else if(numPieces == 4) { 
      return 10000; 
    } else if (numPieces == -4) { 
       return -10000; 
    } else {
      return 0;
    }
  }
  
  /*
   * countPieces(...) returns a positive number, a negative number, or a 0 to signify
   * whether or not a row, column, or diagonal has all blue pieces, all red pieces, 
   * neither, or a combination of both
   */ 
  private static int countPieces(int a, int b, int c, int d) {
    int n = a + b + c + d;
    int numBlue = n / 10;
    int numRed = n % 10;
    if(numBlue > 0 && numRed > 0) {
      return 0;                                  // No move for either in this row, return 0
    } else if(numRed == 0) {                     // Only blues in this sequence
      return numBlue;
    } else if(numBlue == 0) {                    // Only reds in this sequence
      return -numRed; 
    }
    return 0;                                    // Needed for compilation
  }
  
  /*
   * isLeaf(...) checks whether or not there are any moves left 
   */ 
  private static boolean isLeaf(int[][] B) {
    if(winForBlue(B) == true || winForRed(B) == true) {
      return true; 
    }
    for(int col = 0; col < 8; ++col) {
      for(int row = 0; row < 8; ++row) {
        if(B[row][col] == Blank) {
          return false;
        }
      }
    }
    return true;
  }
  
  /*
   * winForBlue(...) checks whether or not the Blue player has won  
   */ 
  private static boolean winForBlue(int[][] B) {
    
    boolean flag = false;
    
    for(int r = 0; r < 8; ++r) {
      for(int c = 0; c < 5; ++c) {
        if ((B[r][c] + B[r][c+1] + B[r][c+2] + B[r][c+3]) == 40) {
          flag = true; 
          return flag;
        }
      } 
    }
    
    for(int c = 0; c < 8; ++c) { 
      for(int r = 0; r <  5; ++r) { 
        if ((B[r][c] + B[r+1][c] + B[r+2][c] + B[r+3][c]) == 40) {
          flag = true;
          return flag;
        }
      } 
    }
    
    for(int r = 7; r > 2; --r){
      for(int c = 7; c > 2; --c) { 
        if ((B[r][c] + B[r - 1][c - 1] + B[r - 2][c - 2] + B[r - 3][c - 3]) == 40) {
          flag = true; 
          return flag;
        } 
      }
    }
    return flag;
  }
  
  /*
   * winForRed(...) checks whether or not the Red player has won  
   */ 
  private static boolean winForRed(int[][] B) {
    
    boolean flag = false;
    
    for(int r = 0; r < 8; ++r) {
      for(int c = 0; c < 5; ++c) {
        if ((B[r][c] + B[r][c+1] + B[r][c+2] + B[r][c+3]) == 4) {
          flag = true; 
          return flag;
        }
      } 
    }
    
    for(int c = 0; c < 8; ++c) { 
      for(int r = 0; r <  5; ++r) { 
        if ((B[r][c] + B[r+1][c] + B[r+2][c] + B[r+3][c]) == 4) {
          flag = true;
          return flag;
        }
      } 
    }
    
    for(int r = 7; r > 2; --r){
      for(int c = 7; c > 2; --c) { 
        if ((B[r][c] + B[r - 1][c - 1] + B[r - 2][c - 2] + B[r - 3][c - 3]) == 4) {
          flag = true; 
          return flag;
        } 
      }
    }
    return flag;
  }
  
  /*
   * minMax(...) uses alpha-beta pruning to return the evaluation function
   * of the best possible move that maximizes the computer's move value 
   * and minimizes the opponent's move value
   */ 
  private static int minMax(int[][] B, int depth, int alpha, int beta) {
    if(isLeaf(B) || depth == D) { 
      return eval(B);                                 // Stop searching and return eval
    } else if(depth % 2 == 0) {
      int val = -Inf;
      for(int c = 7; c >= 0; c--) {
        for(int r = 7; r >= 0; r--) { 
          if(B[r][c] == Blank) { 
            B[r][c] = Blue; 
            alpha = Math.max(alpha, val);             // Update alpha with max so far
            if (beta < alpha) { 
              B[r][c] = Blank;
              break;                                  // terminate loop
            }
            val = Math.max(val, minMax(B, depth + 1, alpha, beta)); 
            
            B[r][c] = Blank; 
            break; 
          } 
        } 
      }  
      return val;
    } else {                                          // Is a min node
      int val = Inf;
      for(int c = 7; c >= 0; c--) {
        for(int r = 7; r >= 0; r--) { 
          if(B[r][c] == Blank) { 
            B[r][c] = Red;
            beta = Math.min(beta, val);               // Update beta with min so far
            if (beta < alpha) { 
              B[r][c] = Blank; 
              break;                                  // Terminate loop
            }
            val = Math.min(val, minMax(B, depth + 1, alpha, beta));
            B[r][c] = Blank; 
            break; 
          } 
        } 
      } 
      return val;
    } 
  }

  public static void main(String [] args) {
    
    Player machine = new Player(); 
    
    int[][] T = new int[8][8]; 
    T[7][0] = Blue; 
//  T[7][1] = Blue; 
//  T[7][2] = Red; 
//  T[7][3] = Red;
//  T[7][4] = Blue; 
//  T[6][0] = Red; 
//  T[6][2] = Blue; 
//  T[6][3] = Red; 
//  T[6][4] = Red; 
//  T[5][3] = Blue; 
//  T[5][4] = Blue; 
    
    int m = machine.move(T); 
    
    int e1 = eval(T);
    System.out.println("eval: " + e1); 
    
    for(int r = 0; r < 8; r++) {
      for(int c = 0; c < 8; c++) { 
        if(T[r][c] == 10) {
        System.out.print("[X]"); 
        } else if(T[r][c] == 1) {
          System.out.print("[0]");
        } else { 
          System.out.print("[ ]");    
        } 
      } 
      System.out.println(); 
    }  
  }
  
}



