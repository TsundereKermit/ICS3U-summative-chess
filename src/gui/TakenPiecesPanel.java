package gui;

import board.Move;
import com.google.common.primitives.Ints;
import gui.Table.MoveLog;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import pieces.Piece;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Description: JPanel for the pieces that have been taken (WIP)
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class TakenPiecesPanel extends JPanel{

    
    private static final EtchedBorder BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private final JPanel northPanel;
    private final JPanel southPanel;
    
    //Sets up the taken pieces JPanel
    public TakenPiecesPanel() {
        super (new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(Color.WHITE);
        this.southPanel.setBackground(Color.WHITE);
        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(40, 80));
        
    }
    
    //Updates the panel when called
    public void redo(MoveLog moveLog) {
        
        southPanel.removeAll();
        northPanel.removeAll();
        
        List<Piece> whiteTakenPieces = new ArrayList<>();
        List<Piece> blackTakenPieces = new ArrayList<>();
        
        for(Move move : moveLog.getMoves()) {
            if(move.isAttacked()) {
                Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else if (takenPiece.getPieceAlliance().isBlack()) {
                    blackTakenPieces.add(takenPiece);
                }
            }
        }
        
        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        
        for (Piece takenPiece : whiteTakenPieces) {
            try {
                BufferedImage image = ImageIO.read(new File("images/" + 
                                      takenPiece.getPieceAlliance().toString().substring(0, 1) +
                                      takenPiece.toString() + ".png"));
                ImageIcon icon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            } catch (IOException e) {
                System.out.println("Stop messing with the pictures!");
            }
        }
        
        for (Piece takenPiece : blackTakenPieces) {
            try {
                BufferedImage image = ImageIO.read(new File("images/" + 
                                      takenPiece.getPieceAlliance().toString().substring(0, 1) +
                                      takenPiece.toString() + ".png"));
                ImageIcon icon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(icon);
                this.northPanel.add(imageLabel);
            } catch (IOException e) {
                System.out.println("Stop messing with the pictures!");
            }
        }
        
        validate();
    } 
}