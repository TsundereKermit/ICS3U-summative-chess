package gui;

import board.Board;
import board.Move;
import board.Tile;
import board.BoardUtils;
import com.google.common.collect.Lists;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.util.List;
import javax.imageio.ImageIO;
import pieces.Piece;
import player.MoveTransition;

/**
 * Description: Sets up the actual gui for the chess game
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class Table {
    
        private final JFrame gameFrame;
        private final HistoryPanel historyPanel;
        //private TakenPiecesPanel takenPiecesPanel;
        //Idea scrapped due to time constraints (it still kinda works but the images are not resized)
        //There should be 4 comments with the takenPiecesPanel
        private final BoardPanel boardPanel;
        private final MoveLog log;
        private Board chessBoard;
        private Color lightTileColor;
        private Color darkTileColor;
        private boolean highlightLegalMoves;
        private Tile sourceTile;
        private Piece humanMovedPiece;
        private BoardDirection boardDirection;
        
        public Table() {
            gameFrame = new JFrame("Chess");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setLayout(new BorderLayout());
            gameFrame.setJMenuBar(populateMenuBar()); 
            highlightLegalMoves = false;
            chessBoard = Board.createStandardBoard();
            historyPanel = new HistoryPanel();
            //takenPiecesPanel = new TakenPiecesPanel();
            log = new MoveLog();
            gameFrame.setSize(740, 600);
            boardPanel = new BoardPanel();
            boardDirection = BoardDirection.NORMAL;
            //gameFrame.add(takenPiecesPanel, BorderLayout.WEST);
            gameFrame.add(historyPanel, BorderLayout.EAST);
            gameFrame.add(boardPanel, BorderLayout.CENTER);
            gameFrame.setVisible(true);
        }
        
        //Creates the JMenuBar at the top of the JFrame
        private JMenuBar populateMenuBar() {
            
            JMenuBar tableMenuBar = new JMenuBar();
            tableMenuBar.add(createFileMenu());
            tableMenuBar.add(createPreferencesMenu());
            
            return tableMenuBar;
        }
        
        //Sets up the file dropdown menu
        private JMenu createFileMenu() {
            
            //Unfortunately, I did not get enough time to make this work properly
            /*
            JMenuItem openPGN = new JMenuItem("Load PGN (broken)");
            openPGN.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("");
                }
            });
            fileMenu.add(openPGN);
            */
            
            JMenu fileMenu = new JMenu("File");
            
            JMenuItem newGame = new JMenuItem("New Game");
            newGame.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    newGame(); //"Destroys" the current frame 
                }
            });
            
            JMenuItem exit = new JMenuItem("Exit");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            
            fileMenu.add(newGame);
            fileMenu.addSeparator();
            fileMenu.add(exit);
            
            return fileMenu;
        }
        
        //Sets up the preferences dropdown menu
        private JMenu createPreferencesMenu() {
            
            JMenu preferencesMenu  = new JMenu ("Preferences");
            
            JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
            flipBoardMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boardDirection = boardDirection.opposite(); //Completely breaks castle in the flipped state
                    //Draws the board with the flipped pieces (board stays unflipped)
                    boardPanel.drawBoard(chessBoard);
                }
            });
            
            //Unfinished
            //Only shows possible moves instead of legal moves right now
            JCheckBoxMenuItem highlightBox = new JCheckBoxMenuItem("Highlight legal moves (Ugly)", false);
            highlightBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    highlightLegalMoves = highlightBox.isSelected();
                }
            });
            
            preferencesMenu.add(flipBoardMenuItem);
            preferencesMenu.addSeparator();
            preferencesMenu.add(highlightBox);
            
            return preferencesMenu;
            
        }
        
        public enum BoardDirection {
            
            //Standard position with white side at the bottom
            NORMAL {
                @Override
                List<TilePanel> traverse(List<TilePanel> tiles) {
                    return tiles;
                }

                @Override
                BoardDirection opposite() {
                    return FLIPPED;
                }
                
            },
            //Flipped position with black side at the bottom
            FLIPPED {
                @Override
                List<TilePanel> traverse(List<TilePanel> tiles) {
                    return Lists.reverse(tiles);
                }

                @Override
                BoardDirection opposite() {
                    return NORMAL;
                }
                
            };
            abstract List<TilePanel> traverse (List<TilePanel> tiles);
            abstract BoardDirection opposite();
            
        }
        
        private class BoardPanel extends JPanel {
            
            List<TilePanel> boardTiles;
            
            BoardPanel() {
                
                super(new GridLayout(8, 8));
                this.boardTiles = new ArrayList<>();
                //Adds all 64 of the tiles to the board
                for (int i = 0; i < 64; i++) {
                    TilePanel tilePanel = new TilePanel(this, i);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
                setPreferredSize(new Dimension(400, 400));
                validate(); //Comfirms actual size in the JFrame
                
            }
            
            //Puts all the tiles in their place
            public void drawBoard (Board board) {
                removeAll();
                for (TilePanel boardTile : boardDirection.traverse(boardTiles)) {
                    boardTile.drawTile(board);
                    add(boardTile);
                }
                validate();
                repaint();
            }
        }
        
        //This is a record of all the moves played used by the history panel
        public static class MoveLog {
            List<Move> moves;
            
            MoveLog() {
                this.moves = new ArrayList<>();
            }
            
            public List<Move> getMoves() {
                return this.moves;
            }
            
            public void addMove(Move move) {
                this.moves.add(move);
            }
            
            public int size() {
                return this.moves.size();
            }
            
            public void clear() {
                this.moves.clear();
            }
            
            //These two were built for the undo option which I never started on
            public Move removeMove(int index) {
                return this.moves.remove(index);
            }
            
            public boolean removeMove(Move move) {
                return this.moves.remove(move);
            }
            
        }
        
        //Sets up the individual tiles for the board
        private class TilePanel extends JPanel {
            
            private final int tileID;
            
            TilePanel(BoardPanel boardPanel, int tileID) {
                
                super(new GridBagLayout());
                this.tileID = tileID;
                setSize(60,60);
                tileColor();
                tileIcon(chessBoard);
                
                addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        
                        //Right mouse button resets everything
                        if(SwingUtilities.isRightMouseButton(e)) {
                            sourceTile = null;
                            humanMovedPiece = null;
                        } 
                        //Left mouse button
                        else if (SwingUtilities.isLeftMouseButton(e)) {
                            //First click (choose piece)
                            if (sourceTile == null) {
                                sourceTile = chessBoard.getTile(tileID);
                                humanMovedPiece = sourceTile.getPiece();
                                //Checks if the clicked tile was empty
                                if (humanMovedPiece == null) {
                                    sourceTile = null;
                                }
                            } 
                            //Second click (choose destination)
                            else {
                                //destTile = chessBoard.getTile(tileID); and destTile.getTileCoordinate()
                                Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), tileID);
                                MoveTransition trans = chessBoard.currentPlayer().makeMove(move);
                                //Check if the move is legal or not
                                if (trans.getMoveStatus().isDone()) {
                                    chessBoard = trans.getTransitionBoard();
                                    log.addMove(move);
                                }
                                sourceTile = null;
                                humanMovedPiece = null;
                            }
                        }
                        
                        //Ends the move
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                historyPanel.redo(chessBoard, log);
                                //takenPiecesPanel.redo(log);
                                boardPanel.drawBoard(chessBoard);
                                //Checks for checkmate
                                if (chessBoard.currentPlayer().isInCheckmate()) {
                                    JOptionPane.showMessageDialog(null, "Checkmate!");
                                    
                                    //Checks for player choice of new game
                                    int decision = JOptionPane.showConfirmDialog(null, 
                                                   "Would you like to play a new game?", 
                                                   "Good game!", 
                                                   JOptionPane.YES_NO_OPTION);
                                    if (decision == 1) {
                                        close();
                                    } else if (decision == 0) {
                                        newGame();
                                    }
                                } else if (chessBoard.currentPlayer().isInStalemate()) {
                                    JOptionPane.showMessageDialog(null, "Stalemate!");
                                    
                                    //Checks for player choice of new game
                                    int decision = JOptionPane.showConfirmDialog(null, 
                                                   "Would you like to play a new game?", 
                                                   "Good game!", 
                                                   JOptionPane.YES_NO_OPTION);
                                    if (decision == 1) {
                                        close();
                                    } else if (decision == 0) {
                                        newGame();
                                    }
                                }
                            }
                            
                        });
                    }
                    
                    //Unfinished
                    //Update these for drag'n'drop
                    @Override
                    public void mousePressed(MouseEvent e) {
                    
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    
                    }
                    
                });
                validate();
            }
            
            public void drawTile (Board board) {
                tileColor();
                tileIcon(board);
                highlightLegals(board);
                validate();
                repaint();
            }
            
            //Sets the icon of the tile with the piece (if possible)
            private void tileIcon(Board board) {
                this.removeAll();
                String iconPath = "images/png/";
                
                if (board.getTile(this.tileID).isTileOccupied()) {
                    try {
                        BufferedImage image = 
                                ImageIO.read(new File(iconPath + 
                                board.getTile(tileID).getPiece().getPieceAlliance().toString().substring(0,1) + 
                                board.getTile(tileID).getPiece().toString() + ".png"));
                        add(new JLabel(new ImageIcon(image)));
                    } catch (IOException e) {
                        System.out.println("Stop messing with the pictures!");
                    }
                }
            }
            
            //Highlights legal moves possible for a piece
            private void highlightLegals(Board board) {
                if (highlightLegalMoves == true) {
                    for (Move move : pieceLegalMoves(board)) {
                        if (move.getDestinationCoordinate() == this.tileID) {
                            try {
                                add(new JLabel(new ImageIcon(ImageIO.read(new File("images/grey_dot.png")))));
                            } catch (IOException e) {
                                System.out.println("Stop messing with the pictures!");
                            }
                        }
                    }
                }
            }
            
            //Returns all possible legal moves by a piece
            private Collection<Move> pieceLegalMoves(Board board) {
                //Checks for the alliance of the player & piece (white player cannot move a black piece
                if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                    return humanMovedPiece.calculateLegalMoves(board);
                } else {
                    return Collections.emptyList();
                }
            }
            
            //Sets up the checker pattern of the chess board
            private void tileColor() {
                darkTileColor = new Color(118,150,86);
                lightTileColor = new Color(238,238,210);
                if (BoardUtils.EIGHTH_RANK[this.tileID] ||
                    BoardUtils.SIXTH_RANK[this.tileID] ||
                    BoardUtils.FOURTH_RANK[this.tileID] ||
                    BoardUtils.SECOND_RANK[this.tileID]) {
                    setBackground(this.tileID % 2 == 0 ? lightTileColor : darkTileColor);
                } else if (BoardUtils.SEVENTH_RANK[this.tileID] ||
                           BoardUtils.FIFTH_RANK[this.tileID] ||
                           BoardUtils.THIRD_RANK[this.tileID] ||
                           BoardUtils.FIRST_RANK[this.tileID]) {
                    setBackground(this.tileID % 2 != 0 ? lightTileColor : darkTileColor);
                }
            }
            
        }
        
        private void newGame() {
            gameFrame.dispose();
            Table newTable = new Table();
        }
        
        private void close() {
            gameFrame.dispose();
        }
}