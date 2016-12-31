package com.sdz.vue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.sdz.controler.Controler;
import com.sdz.modele.Partie;

public class PanelLancerDe extends JPanel implements Observer {

	private JLabel lblDe;
	private Partie partie;
	private Controler ctr;
	private String faceDe;

	public PanelLancerDe(Controler ctr) {
		this.ctr = ctr;
		this.setLayout(null);
		this.setSize(200, 132);
		Border lineBorder = BorderFactory.createLineBorder(Color.blue);
		this.setBorder(lineBorder);

		JButton btnLancer = new JButton("Lancer le d�!");
		btnLancer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctr.lancerDe();
			}
		});
		btnLancer.setBounds(0, 0, 200, 25);
		add(btnLancer);

		lblDe = new JLabel("");
		lblDe.setBounds(39, 30, 123, 95);
		this.setBgLabel("images/de.PNG");
	}

	public void setBgLabel(String link) {
		try {
			BufferedImage image = ImageIO.read(new File(link));
			ImageIcon icon = new ImageIcon(image.getScaledInstance(123, 95, image.SCALE_SMOOTH));
			lblDe.setIcon(icon);
			add(lblDe);
		} catch (IOException ex) {
			Logger.getLogger(PanelJP.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.repaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.faceDe = ctr.getPartie().getFaceDe();
		if (this.faceDe.equals("Jour")) {
			this.setBgLabel("images/Jour.png");
		} else if (this.faceDe.equals("Nuit")) {
			this.setBgLabel("images/Nuit.png");
		} else if (this.faceDe.equals("N�ant")) {
			this.setBgLabel("images/Neant.png");
		}
	}

//	public static void main(String[] args) {
//		JFrame j = new JFrame();
//		j.setSize(j.getMaximumSize());
//		j.getContentPane().add(new PanelLancerDe(new Controler(new Partie())));
//		j.setVisible(true);
//		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}
}