package src.simulacion;

//import java.io.BufferedReader;
import java.io.*;
//import java.io.FileReader;
import src.list.List;
import src.list.SNode;
//import java.util.Scanner;


public class Main {

	private static List generateList(String ruta) {
		List lista = new List();
		File archivo;
		FileReader fr = null;
		BufferedReader br;

		try {
			archivo = new File(ruta);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null)
				addRide(linea, lista);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lista;
	}

	 private static void addRide(String linea, List lista) {
		int[] ride = new int[6];
		String sNum = "";
		for (int i = 0, k = 0; i < linea.length() && k < 6; i++) {
			if (linea.charAt(i) != ' ' && i != linea.length() - 1) {
				sNum += linea.charAt(i);
			} else if (linea.charAt(i) != ' ' && i == linea.length() - 1) {
				sNum += linea.charAt(i);
				ride[k] = Integer.parseInt(sNum);
				sNum = "";
				k++;
			} else {
				ride[k] = Integer.parseInt(sNum);
				sNum = "";
				k++;

			}
		}
		lista.addLast(ride);
	}

	private static void generate_output(String ruta, Coche[] coches) {
		FileWriter fichero = null;
		PrintWriter pw;
		try {
			fichero = new FileWriter(ruta);
			pw = new PrintWriter(fichero);
			for (Coche coche : coches) {//int j = 0; j < coches.length; j++
				String linea = "";
				linea += coche.rides.size();
				for (int i = 0; i < coche.rides.size(); i++) {
					linea += " " + coche.rides.get(i);
				}
				pw.println(linea);

			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Nuevamente aprovechamos el finally para
				// asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static int assignRide(Coche coche, int t, List lista, int puntos, int bonus) {

		if (coche.arriving_time == -1 || coche.arriving_time == t) {//si no tiene viaje asignado se le asigna

			int i = 0, menor = 999999999,indice_menor = -1;
			for (SNode nodeIt = lista.first; nodeIt != null; nodeIt = nodeIt.next, i++) {
				int llegada = Math.abs(nodeIt.ride[0] - coche.x) + Math.abs(nodeIt.ride[1] - coche.y);
				int espera;// = 0;
				if (nodeIt.ride[4] - (t + llegada) > 0) {
					espera = nodeIt.ride[4] - (t + llegada);
				} else {
					espera = 0;
				}
				int viaje = Math.abs(nodeIt.ride[2] - nodeIt.ride[0]) + Math.abs(nodeIt.ride[3] - nodeIt.ride[1] + 1);

				if (t + llegada + espera + viaje < menor) {
					menor = t + llegada + espera + viaje;
					indice_menor = i;
				}
			}
			if (lista.isEmpty())
				return puntos;


			if (menor < lista.getAt(indice_menor).ride[5]) {
				puntos += Math.abs(lista.getAt(indice_menor).ride[2] - lista.getAt(indice_menor).ride[0]) + Math.abs(lista.getAt(indice_menor).ride[3] - lista.getAt(indice_menor).ride[1] + 1);

				if (lista.getAt(indice_menor).ride[4] - (t + Math.abs(lista.getAt(indice_menor).ride[0] - coche.x) + Math.abs(lista.getAt(indice_menor).ride[1] - coche.y)) >= 0) {
					puntos += bonus;
				}
			}


			coche.rides.add(lista.getAt(indice_menor).ride_index);
			coche.x = lista.getAt(indice_menor).ride[2];
			coche.y = lista.getAt(indice_menor).ride[3];
			coche.arriving_time = menor;
			lista.removeAt(indice_menor);

			return puntos;

		}
		return puntos;
	}


	public static void main(String[] args) {
		int totalPuntos = 0;
		String ruta = "";
		for (int archivo = 0; archivo < 5; archivo++) {
			switch (archivo) {
				case 0:
					ruta = "in/a_example.in";
					break;
				case 1:
					ruta = "in/b_should_be_easy.in";
					break;
				case 2:
					ruta = "in/c_no_hurry.in";
					break;
				case 3:
					ruta = "in/d_metropolis.in";
					break;
				case 4:
					ruta = "in/e_high_bonus.in";
					break;
			}
			System.out.println();
			System.out.println(ruta);

			//Creacion lista de viajes
			List lista = generateList(ruta);

			//Generacion mapa
			int[] city = lista.first.ride;
			lista.removeFirst();

			//Creacion flota de coches
			Coche[] coches = new Coche[city[2]];
			for (int i = 0; i < city[2]; i++)//int i = 0; i < city[2]; i++
				coches[i] = new Coche();

			//Bucle principal que recorre el tiempo
			int t = 0;
			int puntos = 0;
			while (!lista.isEmpty()) {
				for (Coche coche : coches) {//Asignar viaje si un coche no tiene viaje asignado
					puntos = assignRide(coche, t, lista, puntos, city[4]);
				}
				if (lista.isEmpty()) {
					System.out.println("Puntos: " + puntos);
					totalPuntos += puntos;
					generate_output("out/" + ruta.charAt(3) + ".txt", coches);
				}
				t++;
			}
		}
		System.out.println("PUNTUACION TOTAL: "+totalPuntos);
	}

}
