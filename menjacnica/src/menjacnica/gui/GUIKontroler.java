package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import menjacnica.Menjacnica;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	private static MenjacnicaGUI glavniProzor;
	private static Menjacnica sistem;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					glavniProzor = new MenjacnicaGUI();
					glavniProzor.setVisible(true);
					sistem = new Menjacnica();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(glavniProzor, "Da li ZAISTA zelite da izadjete iz apliacije",
				"Izlazak", JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(glavniProzor, "Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(glavniProzor);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(glavniProzor);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				sistem.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute();
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziSveValute() {
		MenjacnicaTableModel model = (MenjacnicaTableModel) (glavniProzor.getTable().getModel());
		model.staviSveValuteUModel(sistem.vratiKursnuListu());

	}

	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI(glavniProzor);
		prozor.setLocationRelativeTo(glavniProzor);
		prozor.setVisible(true);
	}

	public static void prikaziObrisiKursGUI() {

		if (glavniProzor.getTable().getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (glavniProzor.getTable().getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(glavniProzor,
					model.vratiValutu(glavniProzor.getTable().getSelectedRow()));
			prozor.setLocationRelativeTo(glavniProzor);
			prozor.setVisible(true);
		}
	}

	public static void prikaziIzvrsiZamenuGUI() {
		if (glavniProzor.getTable().getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (glavniProzor.getTable().getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(glavniProzor,
					model.vratiValutu(glavniProzor.getTable().getSelectedRow()));
			prozor.setLocationRelativeTo(glavniProzor);
			prozor.setVisible(true);
		}
	}

	public static void unesiKurs(String naziv, String skraceniNaziv, Integer sifra, String prodajniKurs,
			String kupovniKurs, String srednjiKurs) {
		{
			try {
				Valuta valuta = new Valuta();

				valuta.setNaziv(naziv);
				valuta.setSkraceniNaziv(skraceniNaziv);
				valuta.setSifra(sifra);
				valuta.setProdajni(Double.parseDouble(prodajniKurs));
				valuta.setKupovni(Double.parseDouble(kupovniKurs));
				valuta.setSrednji(Double.parseDouble(srednjiKurs));

				// Dodavanje valute u kursnu listu
				sistem.dodajValutu(valuta);

				// Osvezavanje glavnog prozora
				prikaziSveValute();

			} catch (Exception e1) {
				JOptionPane.showMessageDialog(glavniProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public static double izvrsiZamenu(Valuta v, boolean prodaja, String iznos) {
		try {
			double konacniIznos = glavniProzor.sistem.izvrsiTransakciju(v, prodaja, Double.parseDouble(iznos));

			return konacniIznos;
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
	}

	public static void obrisiValutu(Valuta v) {
		try {
			sistem.obrisiValutu(v);

			prikaziSveValute();

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

}
