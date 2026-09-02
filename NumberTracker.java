import java.util.ArrayList;

public class NumberTracker {

  int[] regularNumber = new int[48];
  int[] powerNumber = new int[28];

  public void addDraw(int[] numbers) {

    for (int i = 0; i < 5; i++) {
      regularNumber[numbers[i]]++;
    }

    powerNumber[numbers[5]]++;
  }

  public int getRegular(int number) {
    return regularNumber[number];
  }

  public int getPower(int number) {
    return powerNumber[number];
  }

  public void showStats() {

    ArrayList<int[]> regularStats = new ArrayList<>();

    for (int i = 1; i <= 47; i++) {
      regularStats.add(new int[] {i, regularNumber[i]});
    }

    regularStats.sort((a, b) -> b[1] - a[1]);

    System.out.println("\n***** Regular Numbers *****");

    for (int[] x : regularStats) {
      System.out.printf("Regular %2d: %d%n", x[0], x[1]);
    }

    /////////////////////////////////////////////////////

    ArrayList<int[]> powerStats = new ArrayList<>();
    
    for (int i = 1; i <= 27; i++) {
      powerStats.add(new int[] {i, powerNumber[i]});
    }

    powerStats.sort((a, b) -> b[1] - a[1]);

    System.out.println("\n***** Mega Numbers *****");
    for (int[] x : powerStats) {
      System.out.printf("Mega %2d: %d%n", x[0], x[1]);
    }
  }
  
}