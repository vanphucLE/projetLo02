package com.sdz.controler;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.sdz.cartes.CarteAction;
import com.sdz.modele.EffectuerCapacite;
import com.sdz.modele.Joueur;
import com.sdz.modele.JoueurPhysique;
import com.sdz.modele.Partie;
import com.sdz.vue.PanelJeu;

/*
 * Ce classe est pour permet l'interaction entre le moteur de jeu et l'interface graphique
 * @author LE Van Phuc
 */
public class Controler {
	private Partie partie;
	private JoueurPhysique jP;
	private PanelJeu panelJeu;
	private EffectuerCapacite effectuerCapacite;

	/*
	 * Creer une controler
	 * 
	 * @param partie Une variable de partie
	 */
	public Controler(Partie partie) {
		this.partie = partie;
		this.effectuerCapacite = new EffectuerCapacite(partie);
		this.jP = (JoueurPhysique) this.partie.getListeJoueurs().get(0);
	}

	/*
	 * Defausser la carte
	 * 
	 * @param carte Une variable de CarteAction
	 */
	public void defausserCarte(CarteAction carte) {
		CarteAction carteA = jP.getLaMain().seDeffausserCarte(carte.getId());
		this.partie.getJeuDeCartes().recupererCarteAction(carte);
	}

	/*
	 * finir le tour
	 */
	public void finir() {
		this.partie.resume();
	}

	/*
	 * permet de jouer une carte
	 * 
	 * @param carte une variable de Carte Action
	 */
	public void jouerCarte(CarteAction carte) {
		Boolean valid = this.setPtAction(carte);
		if (valid) {
			if ((partie.getEstApocalypseAvant() != 0 && partie.getEstApocalypseAvant() != -1)
					|| !carte.getType().equals("Apocalypse")) {
				this.panelJeu.dessinerPanelCarteJouee(carte);
			}
			switch (carte.getType()) {
			case "Croyant":
				this.jP.setActionEnTrain("jouerCroyant");
				this.jP.jouerCroyant(carte, partie.getEspaceCommun());
				this.jP.setActionEnTrain("jouer");
				break;
			case "GuideSpirituel":
				this.jP.setActionEnTrain("jouerGuideSpirituel");
				this.jP.jouerGuideSpirituel(carte);
				break;
			case "DeusEx":
				this.jP.setActionEnTrain("jouerDeusEx");
				this.jP.jouerDeusEx(carte);
				this.jP.setActionEnTrain("jouer");
				break;
			case "Apocalypse":
				this.jP.setActionEnTrain("jouerApocalypse");
				this.jP.jouerApocalypse(carte);
				this.jP.setActionEnTrain("jouer");
				break;
			}
		}
	}

	// on utilise cette m�thode pour mettre � jour le point d'action de joueur
	// apr�s qu'il a choisi une carte pour jouer.
	/*
	 * Mettre a jour le point d'action pour les joueur quand il choisit une
	 * carte pour jouer
	 * 
	 * @param carte Une variable de carte Action
	 * 
	 * @return a Boolean data
	 */
	private Boolean setPtAction(CarteAction carte) {
		if (carte.getOrigine() != "") {
			if (carte.getOrigine().equals("Jour")) {
				if (this.jP.getPtAction_Jour() == 0) {
					System.out.println("Eurreur en choissant!!");
					JOptionPane.showMessageDialog(null,
							"Vous n'avez pas assez point d'action Jour pour jouer cette carte!");
					return false;
				} else {
					this.jP.setPtAction_Jour(this.jP.getPtAction_Jour() - 1);
				}
			} else if (carte.getOrigine().equals("Nuit")) {
				if (this.jP.getPtAction_Nuit() == 0) {
					System.out.println("Eurreur en choissant!!");
					JOptionPane.showMessageDialog(null,
							"Vous n'avez pas assez point d'action Nuit pour jouer cette carte!");
					return false;
				} else {
					this.jP.setPtAction_Nuit(this.jP.getPtAction_Nuit() - 1);
				}
			} else if (carte.getOrigine().equals("N�ant")) {
				if (this.jP.getPtAction_Neant() > 0) {
					this.jP.setPtAction_Neant(this.jP.getPtAction_Neant() - 1);
				} else if (this.jP.getPtAction_Jour() >= 2) {
					this.jP.setPtAction_Jour(this.jP.getPtAction_Jour() - 2);
				} else if (this.jP.getPtAction_Nuit() >= 2) {
					this.jP.setPtAction_Nuit(this.jP.getPtAction_Nuit() - 2);
				} else {
					System.out.println("Eurreur en choissant!!");
					JOptionPane.showMessageDialog(null, "Vous n'avez pas assez point d'action pour jouer cette carte!");
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * Guider une croyant
	 * 
	 * @param carte une variable de Carte Action
	 */
	public void guiderCroyant(CarteAction carte) {
		if (this.jP.getNbGuider() > 0) {
			LinkedList<CarteAction> listeCroyants = this.jP.croyantsPeutEtreGuidee();
			if (listeCroyants.indexOf(carte) == -1) {
				JOptionPane.showMessageDialog(null,
						"Vous ne pouvez pas faire guider cette Carte Croyant!\nChoissiez l'autre carte!");
			} else {
				this.jP.ajouterCroyantGuidee(carte);
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Vous ne pouvez faire guider la carte Croyant plus!\nCliquez button - Finir mes choices!");
		}
	}

	// quand on fini de choisir carte croyant guid�es, on va ajouter les cartes
	// � la main
	public void ajouterGuidee() {
		this.jP.ajouterGuidee();
		this.jP.setActionEnTrain("jouer");
		JOptionPane.showMessageDialog(null,
				"Vous avez fait guider les cartes. Ces Cartes est transmis � l'Espace Guid�e!");
	}

	/*
	 * Sacrifier une carte croyant ou carte guide
	 * 
	 * @param carte une variable de Carte Action
	 */
	public void sacrifier(CarteAction carte) {
		this.panelJeu.dessinerPanelCarteJouee(carte);
		this.jP.sacrifierCarte(carte);
	}

	// Capa id ~ 6
	/*
	 * Empecher la sacrifie une Carte
	 * 
	 * @param carte une variable de Carte Action
	 */
	public void empecherSacrifier(CarteAction carte) {
		carte.setEstSacrifie(false);
		JOptionPane.showMessageDialog(null, "Vous avez emp�cher la sacrifice de cette carte!");
		this.jP.setActionEnTrain("sacrifier");
	}

	// Capa id~9 10
	/*
	 * Choisir une carte d'un autre joueur pour la sacrifier
	 * 
	 * @param joueur une Variable de class Joueur
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void sacrifierCarte_special(Joueur joueur, CarteAction carte) {
		Joueur joueurPrin = this.partie.getJoueurEncours();
		this.partie.setJoueurEncours(joueur);
		this.sacrifier(carte);
		this.jP.setActionEnTrain("");
		this.partie.setJoueurEncours(joueurPrin);
	}

	// capa carte id 12
	/*
	 * reprendre une GuideSpirituel d'un autre joueur
	 * 
	 * @param joueur une Variable de class Joueur
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void recupererGuideSpirituel(Joueur joueur, CarteAction carte) {
		this.effectuerCapacite.recupererGuideSpirituel(joueur, carte);
		this.jP.setActionEnTrain("sacrifier");
	}

	// capa id=24
	/*
	 * permet de deffauser un Guide Spirituel d'un autre joueur
	 * 
	 * @param joueur une Variable de class Joueur
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void deffauserGuideSpirituel(Joueur joueur, CarteAction carte) {
		this.effectuerCapacite.deffauserGuideSpirituel(joueur, carte);
		this.jP.setActionEnTrain("sacrifier");
	}

	// capa id=26
	/*
	 * permet de beneficier la capacite d;une carte d'un autre joueur
	 * 
	 * @param joueur une Variable de class Joueur
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void beneficierCapacite(Joueur joueur, CarteAction carte) {
		joueur.setSacrifice(true);
		this.panelJeu.dessinerPanelCarteJouee(carte);
		joueur.sacrifierCroyant(carte.getId(), this.partie);
		this.jP.setActionEnTrain("sacrifier");
	}

	// capa id=50
	/*
	 * permet de sacrifier un Guide Spirituel Chaos d'un autre joueur
	 * 
	 * @param joueur une Variable de class Joueur
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void sacrifierGuideSpirituelCHAOS(Joueur joueur, CarteAction carte) {
		Joueur joueurPrin = this.partie.getJoueurEncours();
		this.partie.setJoueurEncours(joueur);
		joueur.setSacrifice(true);
		this.panelJeu.dessinerPanelCarteJouee(carte);
		this.effectuerCapacite.sacrifierGuideSpirituelCHAOS(joueur, carte);
		this.jP.setActionEnTrain("sacrifier");
		this.partie.setJoueurEncours(joueurPrin);
	};

	// capa carte id 54
	/*
	 * permet de reprendre un Guide Spirituel d'un autre joueur
	 * 
	 * @param joueur une Variable de class Joueur
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void recupererGuideSpirituel2(Joueur joueur, CarteAction carte) {
		this.effectuerCapacite.recupererGuideSpirituel2(joueur, carte);
		this.jP.setActionEnTrain("");
	}

	// Capa carte id 55
	private CarteAction carteG_1;

	/*
	 * permet de echanger un Guide Spirituel d'un autre joueur
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void choisirGuideSpirituelEchanger_1(CarteAction carte) {
		this.carteG_1 = carte;
		JOptionPane.showMessageDialog(null, "Choissiez une carte Guide Spirituel de l'autre joueur pour �changer!");
		this.jP.setActionEnTrain("choisirGuideSpirituelEchanger_2");
	}

	/*
	 * permet de echanger un Guide Spirituel d'un autre joueur
	 * 
	 * @param joueur une Variable de class Joueur
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void choisirGuideSpirituelEchanger_2(CarteAction carte, Joueur joueur) {
		this.effectuerCapacite.echangerGuideSpirituel((Joueur) this.jP, this.carteG_1, joueur, carte);
		this.jP.setActionEnTrain("sacrifier");
	}

	// Capa id 66
	/*
	 * permet de beneficier capacite d'un Guide Spirituel sans la sacrifier
	 * 
	 * @param carte une variable de CarteAction
	 */
	public void beneficierSansSacrifier(CarteAction carte) {
		carte.effectuerCapaciteSpecial(this.partie);
		this.jP.setActionEnTrain("jouer");
	}

	/*
	 * permet de lancer le de
	 */
	public void lancerDe() {
		this.jP.setActionEnTrain("");
		this.partie.resume();
	}

	/*
	 * permet de prendre la variable partie
	 */
	public Partie getPartie() {
		return partie;
	}

	/*
	 * permet de installer la valeur de PanelJeu
	 * 
	 * @param panelJeu une Variable de PanelJeu
	 */
	public void setPanelJeu(PanelJeu panelJeu) {
		this.panelJeu = panelJeu;
	}

}
