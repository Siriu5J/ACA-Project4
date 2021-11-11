/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tomasulogui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.jdesktop.application.FrameView;

/**
 *
 * @author GALLAGHD
 */
public class ROBRowGUI extends JPanel {
    FrameView parent;
    int slotNum;

    JLabel slot = new JLabel();
    JTextField pc = new JTextField();
    JTextField inst = new JTextField();
    JTextField dest = new JTextField();
    JTextField value = new JTextField();
    JTextField predict = new JTextField();
    JRadioButton mispredict = new JRadioButton();
    JRadioButton valid = new JRadioButton();

    public ROBRowGUI(FrameView parent, int slotNum) {
        this.parent = parent;
        this.slotNum = slotNum;

//        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        slot.setText(Integer.toString(slotNum));

        pc.setEditable(false);
        pc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        inst.setEditable(false);
        inst.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dest.setEditable(false);
        dest.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        value.setEditable(false);
        value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        predict.setEditable(false);
        predict.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout myLayout = new javax.swing.GroupLayout(this);
        this.setLayout(myLayout);
        myLayout.setHorizontalGroup(
            myLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(slot)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mispredict)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inst, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dest, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(value, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(valid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(predict, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        myLayout.setVerticalGroup(
            myLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, myLayout.createSequentialGroup()
//                .addContainerGap(1, Short.MAX_VALUE)
                .addGroup(myLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(predict, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(myLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(myLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(valid)
                        .addComponent(mispredict))
                    .addComponent(slot)))
 //               .addGap(5, 5, 5))
        );


    }

}
