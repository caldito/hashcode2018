package src.simulation;

import java.io.*;
import src.list.*;


public class Main {

	private static List generateList(String path) {
		List list = new List();
		File file;
		FileReader fr = null;
		BufferedReader br;

		try {
			file = new File(path);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null)
				addRide(line, list);
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
		return list;
	}

	 private static void addRide(String line, List list) {
		int[] ride = new int[6];
		String sNum = "";
		for (int i = 0, k = 0; i < line.length() && k < 6; i++) {
			if (line.charAt(i) != ' ' && i != line.length() - 1) {
				sNum += line.charAt(i);
			} else if (line.charAt(i) != ' ' && i == line.length() - 1) {
				sNum += line.charAt(i);
				ride[k] = Integer.parseInt(sNum);
				sNum = "";
				k++;
			} else {
				ride[k] = Integer.parseInt(sNum);
				sNum = "";
				k++;

			}
		}
		list.addLast(ride);
	}

	private static void generate_output(String path, Car[] cars) {
		FileWriter file = null;
		PrintWriter pw;
		try {
			file = new FileWriter(path);
			pw = new PrintWriter(file);
			for (Car car : cars) {
				String line = "";
				line += car.rides.size();
				for (int i = 0; i < car.rides.size(); i++) {
					line += " " + car.rides.get(i);
				}
				pw.println(line);

			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != file)
					file.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static int assignRide(Car car, int t, List list, int points, int bonus) {

		if (car.arriving_time == -1 || car.arriving_time == t) { // car gets assigned ride if it hadn't any

			int i = 0, quickest = 999999999,index_quickest = -1;
			for (SNode nodeIt = list.first; nodeIt != null; nodeIt = nodeIt.next, i++) {
				int tArrival = Math.abs(nodeIt.ride[0] - car.x) + Math.abs(nodeIt.ride[1] - car.y);
				int tWait;
				if (nodeIt.ride[4] - (t + tArrival) > 0) {
					tWait = nodeIt.ride[4] - (t + tArrival);
				} else {
					tWait = 0;
				}
				int tRide = Math.abs(nodeIt.ride[2] - nodeIt.ride[0]) + Math.abs(nodeIt.ride[3] - nodeIt.ride[1] + 1);

				if (t + tArrival + tWait + tRide < quickest) {
					quickest = t + tArrival + tWait + tRide;
					index_quickest = i;
				}
			}
			if (list.isEmpty())
				return points;


			if (quickest < list.getAt(index_quickest).ride[5]) {
				points += Math.abs(list.getAt(index_quickest).ride[2] - list.getAt(index_quickest).ride[0]) + Math.abs(list.getAt(index_quickest).ride[3] - list.getAt(index_quickest).ride[1] + 1);

				if (list.getAt(index_quickest).ride[4] - (t + Math.abs(list.getAt(index_quickest).ride[0] - car.x) + Math.abs(list.getAt(index_quickest).ride[1] - car.y)) >= 0) {
					points += bonus;
				}
			}


			car.rides.add(list.getAt(index_quickest).ride_index);
			car.x = list.getAt(index_quickest).ride[2];
			car.y = list.getAt(index_quickest).ride[3];
			car.arriving_time = quickest;
			list.removeAt(index_quickest);

			return points;

		}
		return points;
	}


	public static void main(String[] args) {
		int totalPoints = 0;
		String path = "";

		for (int file = 0; file < 5; file++) {
			switch (file) {
				case 0:
					path = "in/a_example.in";
					break;
				case 1:
					path = "in/b_should_be_easy.in";
					break;
				case 2:
					path = "in/c_no_hurry.in";
					break;
				case 3:
					path = "in/d_metropolis.in";
					break;
				case 4:
					path = "in/e_high_bonus.in";
					break;
			}
			System.out.println();
			System.out.println(path);

			// Ride list creation
			List list = generateList(path);

			// Map creation
			int[] city = list.first.ride;
			list.removeFirst();

			// Car pool creation
			Car[] cars = new Car[city[2]];
			for (int i = 0; i < city[2]; i++)
				cars[i] = new Car();

			// Base loop that iterates through
			int t = 0;
			int points = 0;
			while (!list.isEmpty()) {
				for (Car car : cars) { // Assign ride si a car doesn't have any ride assigned
					points = assignRide(car, t, list, points, city[4]);
				}
				if (list.isEmpty()) {
					System.out.println("Points: " + points);
					totalPoints += points;
					generate_output("out/" + path.charAt(3) + ".txt", cars);
				}
				t++;
			}
		}

		System.out.println("Total points: "+totalPoints);
	}

}
