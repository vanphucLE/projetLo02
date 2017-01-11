package com.sdz.modele;

import java.util.LinkedList;

import com.sdz.cartes.CarteAction;

public class Expert implements Stategie {

	public CarteAction choisirCarteJouer(JoueurVirtuel jV, Partie partie) {
		CarteAction carteChoisi = new CarteAction();
		for (CarteAction carteA : jV.laMain.getListeCarteA()) {
			carteChoisi = this.carteEtPoint(carteA, jV, partie);
			if (carteChoisi.equals(carteA)) {
				break;
			}
		}
		return carteChoisi;
	}

	private CarteAction carteEtPoint(CarteAction carteA, JoueurVirtuel jV, Partie partie) {
		CarteAction carteChoisi = new CarteAction();
		if (carteA.getOrigine().equals("Jour") && this.testEntree(carteA, partie)) {
			if (jV.getPtAction_Jour() > 0) {
				carteChoisi = carteA;
				jV.setPtAction_Jour(jV.getPtAction_Jour() - 1);
			}
		} else if (carteA.getOrigine().equals("Nuit") && this.testEntree(carteA, partie)) {
			if (jV.getPtAction_Nuit() > 0) {
				carteChoisi = carteA;
				jV.setPtAction_Nuit(jV.getPtAction_Nuit() - 1);
			}
		} else if (carteA.getOrigine().equals("N�ant") && this.testEntree(carteA, partie)) {
			if (jV.getPtAction_Neant() > 0) {
				carteChoisi = carteA;
				jV.setPtAction_Neant(jV.getPtAction_Neant() - 1);
			} else if (jV.getPtAction_Nuit() >= 2) {
				carteChoisi = carteA;
				jV.setPtAction_Nuit(jV.getPtAction_Nuit() - 2);
			} else if (jV.getPtAction_Jour() >= 2) {
				carteChoisi = carteA;
				jV.setPtAction_Jour(jV.getPtAction_Jour() - 2);
			}
		}
		return carteChoisi;
	}

	private Boolean testEntree(CarteAction carte, Partie partie) {
		Boolean test = true;
		if (carte.getType().equals("GuideSpirituel")) {
			test = false;
			if (carte.getType().equals("GuideSpitituel")) {
				for (CarteAction carteA : partie.getEspaceCommun().getListeCartesPret()) {
					for (String dogmeA : carteA.getDogme()) {
						for (String dogmeD : carte.getDogme()) {
							if (dogmeD.equals(dogmeA)) {
								test = true;
								break;
							}
						}
					}
				}
			}
		}
		if (carte.getType().equals("Apocalypse")) {
			if (partie.getEstApocalypseAvant() == 0 || partie.getEstApocalypseAvant() == -1) {
				test = false;
			}
		}
		return test;
	}

	public void jouerCapaciteSpecial() {

	};

	@Override
	public LinkedList<CarteAction> choisirCartesDefausser(Partie partie) {
		Joueur joueurEnCours = partie.getJoueurEncours();
		LinkedList<CarteAction> cartesRecupere = new LinkedList<CarteAction>();
		int nbDe = (int) Math.ceil(Math.random() * 6);
		for (int i = nbDe; i >= 1; i--) {
			// Choisir au hasard les carte d�fauss�e.
			int nbCa = (int) Math.ceil(Math.random() * (joueurEnCours.getLaMain().getListeCarteA().size() - 1));
			CarteAction carte = joueurEnCours.getLaMain().getListeCarteA().get(nbCa);
			joueurEnCours.getLaMain().seDeffausserCarte(carte);
			cartesRecupere.add(carte);
		}
		return cartesRecupere;
	}

	public int choisirIdDivinite(Partie partie) {
		int idChoisi = 0;
		do {
			idChoisi = (int) Math.ceil(Math.random() * partie.getListeJoueurs().size());
		} while (idChoisi == partie.getJoueurEncours().getId() || idChoisi == 0);
		return idChoisi;
	}
}
