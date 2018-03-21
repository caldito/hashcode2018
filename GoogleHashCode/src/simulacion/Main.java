package simulacion;

//import java.io.BufferedReader;
import java.io.*;
//import java.io.FileReader;
import list.List;
import list.SNode;
import java.util.Scanner;


public class Main {

	public static List generateList(String ruta) {
		List lista = new List();
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
	
		try {
			archivo = new File (ruta);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea;
			while((linea=br.readLine())!=null)
			   addRide(linea,lista);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
			}
		}
		return lista;
	}
	
	public static void addRide(String linea,List lista) {	
		int [] ride = new int [6];
		String sNum = "";
		for (int i = 0,k = 0;i<linea.length() && k<6;i++) {
			if (linea.charAt(i) != ' ' && i != linea.length()-1) {
				sNum += linea.charAt(i);
			}else if(linea.charAt(i) != ' ' && i == linea.length()-1) {
				sNum += linea.charAt(i);
				ride[k] = Integer.parseInt(sNum);
				sNum = "";
				k++;
			}else {
				ride[k] = Integer.parseInt(sNum);
				sNum = "";
				k++;
				
			}
		}
		lista.addLast(ride);
	}
	
	public static void generate_output(String ruta, Coche[]coches) {
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);

            for (int j = 0;j<coches.length;j++) {
				String linea = "";
            	linea += coches[j].rides.size();
				for (int k = 0;k<coches[j].rides.size();k++) {
					linea += " "+coches[j].rides.get(k);
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
	public static int assignRide(Coche coche,int t, List lista, int puntos, int bonus) {
		
		if (coche.arriving_time == -1 || coche.arriving_time  == t) {
			
			int i = 0;
			int menor = 999999999;
			int indice_menor = -1;
			for (SNode nodeIt = lista.first; nodeIt != null ; nodeIt = nodeIt.next,i++) {
				int llegada = Math.abs(nodeIt.ride[0]-coche.x) + Math.abs(nodeIt.ride[1]-coche.y);
				int espera = 0;
				if (nodeIt.ride[4] - (t + llegada) > 0) {
					espera = nodeIt.ride[4] - (t + llegada);
				}else {
					espera = 0;
				}
				int viaje = Math.abs(nodeIt.ride[2]-nodeIt.ride[0]) + Math.abs(nodeIt.ride[3]-nodeIt.ride[1] + 1);
				
				if (t + llegada + espera + viaje < menor) {
					menor = t + llegada + espera + viaje;
					indice_menor = i;
				}
			}
			if(lista.isEmpty())
				return puntos;
			
			
			if(menor < lista.getAt(indice_menor).ride[5]) {
				puntos += Math.abs(lista.getAt(indice_menor).ride[2]-lista.getAt(indice_menor).ride[0]) + Math.abs(lista.getAt(indice_menor).ride[3]-lista.getAt(indice_menor).ride[1] + 1);
				
				if(lista.getAt(indice_menor).ride[4] - (t + Math.abs(lista.getAt(indice_menor).ride[0]-coche.x) + Math.abs(lista.getAt(indice_menor).ride[1]-coche.y)) >= 0) {
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
		
		//Selector de archivo
		Scanner sc = new Scanner(System.in);
		System.out.println("¿Que simulación?\na. example\nb. should be easy\nc. no hurry\nd. metropolis\ne. high bonus");
		String ruta = "";
		String archivo = sc.next();
		
		switch (archivo) {
		case "a":
			ruta ="entradas/a_example.in";
			break;
		case "b":
			ruta ="entradas/b_should_be_easy.in";
			break;
		case "c":
			ruta ="entradas/c_no_hurry.in";
			break;
		case "d":
			ruta ="entradas/d_metropolis.in";
			break;
		case "e":
			ruta ="entradas/e_high_bonus.in";
			break;
		}
		sc.close();
		
		
		//Creación lista de viajes
		List lista = generateList(ruta);
		
		//Generacion mapa
		int[]city = lista.first.ride;
		lista.removeFirst();
		
		//Creacion flota de coches
		Coche [] coches = new Coche [city[2]];
		for (int i = 0; i < city[2]; i++) {
			coches[i] = new Coche(i + 1);
		}
		
		//Bucle principal que recorre el tiempo
		int t=0;
		int puntos = 0;
		while(!lista.isEmpty()) {
			//System.out.println("Step: "+ t);
			//System.out.println("Tamaño lista: "+lista.getSize());
			
			for (int i = 0; i < coches.length; i++) {//Asignar viaje si un coche no tiene viaje asignado
				puntos = assignRide(coches[i],t, lista, puntos,city[4]);
			}
			
			if (lista.isEmpty()) {
				System.out.println("Puntos: "+puntos);
				generate_output("salidas/"+archivo+".txt", coches);
				for (int j = 0;j<coches.length;j++) {
					System.out.print(coches[j].rides.size());
					for (int k = 0;k<coches[j].rides.size();k++) {
						System.out.print(" "+coches[j].rides.get(k));
					}
					System.out.println();	
				}
				break;	
			}
			
			t++;
		
		}
		
	}
	
}
