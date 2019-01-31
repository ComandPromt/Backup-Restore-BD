package p;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Main {

	private static void Exportar(int tipo, String pass, String bd) {
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
			Process p = Runtime.getRuntime().exec(ruta + "mysqldump.exe -u root -p" + pass + " " + bd);

			if (p.waitFor() > 0) {
				InputStream is = p.getInputStream();
				FileOutputStream fos = new FileOutputStream("C:\\Users\\formacion\\Desktop\\backus.sql");
				byte[] buffer = new byte[1000];

				int leido = is.read(buffer);

				while (leido > 0) {
					fos.write(buffer, 0, leido);
					leido = is.read(buffer);
				}

				fos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
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
		Exportar(1, "rootroot", "pruebas");
		restaurar("rootroot", "pruebas", "C:\\Users\\Usuario\\Desktop\\backus.sql");
	}

}
