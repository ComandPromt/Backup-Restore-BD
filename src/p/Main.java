package p;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Main {
	public static String[] leerFicheroArray(String fichero, int longitud) {
		String[] salida = new String[longitud];
		String texto = "";
		int i = 0;
		try {
			FileReader flE = new FileReader(fichero);
			BufferedReader fE = new BufferedReader(flE);
			while (texto != null) {
				texto = fE.readLine();
				if (texto != null) {
					salida[i] = texto;
					i++;
				}
			}
			fE.close();
		} catch (IOException e) {

		}
		return salida;
	}

	public static void eliminarFichero(String archivo) {
		File fichero = new File(archivo);
		if (!fichero.exists()) {
			mensaje("El archivo " + archivo + " no existe", "ERROR");
		} else {
			fichero.delete();
		}

	}

	public static void mensaje(String mensaje, String titulo) {
		JLabel alerta = new JLabel(mensaje);
		alerta.setFont(new Font("Arial", Font.BOLD, 18));
		int tipo = 0;

		switch (titulo) {
		case "WARNING":
			tipo = JOptionPane.WARNING_MESSAGE;
			break;

		case "INFO":
			tipo = JOptionPane.INFORMATION_MESSAGE;
			break;

		case "ERROR":
			tipo = JOptionPane.ERROR_MESSAGE;
			break;
		}

		JOptionPane.showMessageDialog(null, alerta, titulo, tipo);

	}

	public static void exportarBd(int tipo) {
		String[] lectura = leerFicheroArray("Bd.txt", 3);
		String ruta = "";
		switch (tipo) {
		case 1:
			ruta = "C:\\AppServ\\mysql\\bin\\";
			break;
		case 2:
			ruta = "C:\\wamp\\bin\\mysql\\";
			break;
		case 3:
			ruta = "C:\\xampp\\mysql\\bin\\";
			break;

		}
		try {
			FileWriter flS = new FileWriter("backupbd.bat");
			BufferedWriter fS = new BufferedWriter(flS);
			fS.write("@echo off");
			fS.newLine();
			fS.write(ruta + "mysqldump.exe --no-defaults -h 192.168.1.50 -u User -p" + lectura[2] + " " + lectura[0]
					+ " >C:\\Users\\User\\Desktop\\backus.sql");
			fS.newLine();
			fS.write("exit");
			fS.close();
			Runtime aplicacion = Runtime.getRuntime();
			try {
				aplicacion.exec("cmd.exe /K backupbd.bat");
			} catch (Exception e) {
				System.out.println(e);
			}
			Process p = Runtime.getRuntime().exec("backupbd.bat");
			p.destroy();
			eliminarFichero("backupbd.bat");
			mensaje("Backup realizado correctamente", "INFO");
		} catch (Exception e) {
			mensaje("Error", "ERROR");
		}
	}

	private static void restaurar(String pass, String bd, String ruta) {
		try {
			Process p = Runtime.getRuntime().exec("C:\\xampp\\mysql\\bin\\mysql -urosot -p" + pass + " " + bd);

			OutputStream os = p.getOutputStream();

			FileInputStream fis = new FileInputStream(ruta);
			byte[] buffer = new byte[1000];

			int leido = fis.read(buffer);
			while (leido > 0) {
				os.write(buffer, 0, leido);
				leido = fis.read(buffer);
			}

			os.flush();
			os.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		exportarBd(1);
		restaurar("rootroot", "pruebas", "C:\\Users\\Usuario\\Desktop\\backus.sql");
	}

}
