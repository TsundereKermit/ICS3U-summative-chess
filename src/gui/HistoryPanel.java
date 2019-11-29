package gui;

import javax.swing.*;
import java.awt.*;
import gui.Table.*;
import board.Board;
import board.Move;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Description: The JPanel for recording all the moves played
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class HistoryPanel extends JPanel {

        private final DataModel model;
        //The scrollpane is in case the JTable is not big enough
        private final JScrollPane scrollPane;
        
        //Sets up the JPanel for the move history
        HistoryPanel() {
            
            this.setLayout(new BorderLayout());
            this.model = new DataModel(); 
            JTable table = new JTable(model);
            table.setRowHeight(15);
            this.scrollPane = new JScrollPane(table);
            scrollPane.setColumnHeaderView(table.getTableHeader());
            //this size is pretty arbitrary as the JFrame's layout will force it to fit
            scrollPane.setPreferredSize(new Dimension(150, 400)); 
            this.add(scrollPane, BorderLayout.CENTER);
            this.setVisible(true);
            
        }
        
    //This method will be called after each move to update the move log
    public void redo(Board board, MoveLog moveLog) {
        
            int currentRow = 0;
            this.model.clear(); //Clears move log
            
            //Adds all the moves to the move log
            for (Move move : moveLog.getMoves()) {
                String moveText = move.toString();
                //The move will be added to column 1 if the move was made by white
                if(move.getMovedPiece().getPieceAlliance().isWhite()) {
                    this.model.setValueAt(moveText, currentRow, 0);
                } 
                //The move will be added to column 2 if the move was made by black
                else if (move.getMovedPiece().getPieceAlliance().isBlack()) {
                    this.model.setValueAt(moveText, currentRow, 1);
                    //Moves on to next row since both sides made their move
                    currentRow++;
                }
            }
            
            //Makes sure there are no ArrayOutOfBoundsExceptions
            if (moveLog.getMoves().size() > 0) {
                
                Move lastMove = moveLog.getMoves().get(moveLog.size() - 1);
                String moveText = lastMove.toString();
                
                //Updates the move text in case of check/checkmate
                if (lastMove.getMovedPiece().getPieceAlliance().isWhite()) {
                    this.model.setValueAt(moveText + calculateCheckAndCheckmateHash(board), currentRow, 0);
                } else if (lastMove.getMovedPiece().getPieceAlliance().isBlack()) {
                    this.model.setValueAt(moveText + calculateCheckAndCheckmateHash(board), currentRow - 1, 1);
                }
            }
            
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
            
        }
        
        //Adds symbol for check/checkmate
        private String calculateCheckAndCheckmateHash(Board board) {
            if(board.currentPlayer().isInCheckmate()) {
                return "#";
            } else if (board.currentPlayer().isInCheck()) {
                return "+";
            } else {
                return "";
            }
        }
        
        private static class DataModel extends DefaultTableModel {
            private final List<Row> values;
            private final static String[] NAMES = {"White", "Black"};
            
            DataModel() {
                this.values = new ArrayList<>();
            }
            
            public void clear() {
                this.values.clear();
                setRowCount(0);
            }
            
            @Override 
            public int getRowCount() {
                if(this.values == null) {
                    return 0;
                }
                return this.values.size();
            }
            
            @Override
            public int getColumnCount() {
                return 2;
            }
            
            //Returns the move at chosen row & column
            @Override
            public Object getValueAt(int row, int column) {
                Row currentRow = this.values.get(row);
                switch (column) {
                    case 0:
                        return currentRow.getWhiteMove();
                    case 1:
                        return currentRow.getBlackMove();
                    default:
                        return null;
                }
            }
            
            //Puts the String at the place indicated by row & column
            @Override
            public void setValueAt(Object o, int row, int column) {
                Row currentRow;
                if(this.values.size() <= row) {
                    currentRow = new Row();
                    this.values.add(currentRow);
                } else {
                    currentRow = this.values.get(row);
                }
                
                if (column == 0) {
                    currentRow.setWhiteMove((String) o);
                    fireTableRowsInserted(row, row);
                } else if (column == 1) {
                    currentRow.setBlackMove((String) o);
                    fireTableCellUpdated(row, column);
                }
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                return Move.class;
            }
            
            @Override
            public String getColumnName(int column) {
                return NAMES[column];
            }
            
        }
        
        //Defines the "row" in the move history table
        private static class Row {
            
            private String whiteMove;
            private String blackMove;
            
            Row() {
                //System.out.println("new row");
            }
            
            public String getWhiteMove() {
                return this.whiteMove;
            }
            
            public String getBlackMove() {
                return this.blackMove;
            }
            
            public void setWhiteMove(String move) {
                this.whiteMove = move;
            }
            
            public void setBlackMove(String move) {
                this.blackMove = move;
            }
        }
}