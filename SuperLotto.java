import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SuperLotto {
  public static void main(String[] args) {

    Random random = new Random();
    String fileName = "data.txt";
    String randomFileName = "random.txt";
    int noDateNumber = 1;
    int numEligible = 0;


    NumberTracker tracker = new NumberTracker();
    ArrayList<PreviousWinning> mainList = new ArrayList<>();
    ArrayList<PreviousWinning> duplicate_new = new ArrayList<>();
    ArrayList<PreviousWinning> duplicate_past = new ArrayList<>();
    ArrayList<PreviousWinning> random_generated= new ArrayList<>();
    // ArrayList<Integer> mega_generated = new ArrayList<>();

    System.out.println("\n\n*********** SuperLotto ***********");

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

      String line;

      while ((line = reader.readLine()) != null) {

        if (line.trim().isEmpty()) {continue; }
        String [] parts = line.trim().split("\\s+");

        if (parts.length < 10) {continue; }
        int[] newNum = new int[6];

        for(int i = 0; i < 6; i++) {
          newNum[i] = Integer.parseInt(parts[i]);
        }

        String date = String.join(" ", parts[7], parts[8], parts[9]);

        boolean matched = false;

        for (PreviousWinning draw : mainList) {
          if(Arrays.equals(draw.getNumber(), newNum)) {
            duplicate_past.add(new PreviousWinning(draw.getNumber(), draw.getDate()));
            matched = true;
            break;
          }
        }

        if (matched) {
          System.out.printf("Not Eligible: %-25s - %s%n",
          Arrays.toString(newNum),
          date);

          duplicate_new.add(new PreviousWinning(newNum, date));
          continue;

        } else {
          numEligible++;
          System.out.printf("%d Eligible:     %-25s - %s%n", numEligible,
          Arrays.toString(newNum),
          date);
          mainList.add(new PreviousWinning(newNum, date));
          tracker.addDraw(newNum);
        }
      }


      // Read random.txt and continue the number from the previous run
      try (BufferedReader randomReader = new BufferedReader(new FileReader(randomFileName))) {

        String randomLine;

        while ((randomLine = randomReader.readLine()) != null) {

          if (randomLine.trim().isEmpty()) {continue; }

          String [] parts = randomLine.trim().split("\\s+");

          if (parts.length < 10) {continue; }

          noDateNumber = Integer.parseInt(parts[9]) + 1;
        }

      } catch (FileNotFoundException e) {
        // If random.txt does not exist yet, start at 1
        noDateNumber = 1;
      }




      // for (int i = 1; i <= 50000; i++) {

      //   boolean generated = false;

      //   while (!generated) {

      //     int[] tempNumbers = new int[6];
      //     ArrayList<Integer> usedNumbers = new ArrayList<>();

      //     for (int j = 0; j < 5; j++) {

      //       int number = random.nextInt(1, 48);

      //       while (usedNumbers.contains(number)) {
      //         number = random.nextInt(1, 48);
      //       }

      //       tempNumbers[j] = number;
      //       usedNumbers.add(number);
      //     }

      //     Arrays.sort(tempNumbers, 0, 5);

      //     tempNumbers[5] = random.nextInt(1, 28);

      //     boolean duplicate = false;
          
      //     for (PreviousWinning number : mainList) {
      //       if (Arrays.equals(number.getNumber(), tempNumbers)) {
      //         duplicate = true;
      //         break;
      //       }
      //     }
          
      //     if (!duplicate) {

      //       // mega_generated.add(mega);

      //       String noDate = String.format("77 Aug %02d", noDateNumber);

      //       // Add generated numbers to random_generated
      //       random_generated.add(new PreviousWinning(tempNumbers, noDate));

      //       // Add generated numbers to mainList so the next generated number
      //       // will also check against numbers generated during this run
      //       mainList.add(new PreviousWinning(tempNumbers, noDate));


      //       // Create the line that will be added to both files
      //       String outputLine = tempNumbers[0] + " " + tempNumbers[1] + " " +
      //                           tempNumbers[2] + " " + tempNumbers[3] + " " +
      //                           tempNumbers[4] + " " + tempNumbers[5] + " - " +
      //                           noDate + System.lineSeparator();


      //       // Add generated numbers to data.txt
      //       try (FileWriter writer = new FileWriter(fileName, true)) {
      //         writer.write(outputLine);
      //       }


      //       // Add generated numbers to random.txt
      //       try (FileWriter writer = new FileWriter(randomFileName, true)) {
      //         writer.write(outputLine);
      //       }

      //       generated = true;
      //       noDateNumber++;
      //     }
      //   }
      // }



      
      /////////////////////////////////////////////////////////////////
      System.out.println("\n\n------------------------------------");
      System.out.println("          Total draws: " + mainList.size());
      System.out.println("------------------------------------");
      
      
      
      System.out.println("\n\n***** Duplicate New Numbers *****");
      if (duplicate_new.isEmpty()) {
        System.out.println("There is no duplicate new draws.\n\n");
      } else {
        for (PreviousWinning x : duplicate_new) {
          System.out.println(x);
        }
      }
      
      System.out.println("\n\n***** Duplicate Past Numbers *****");
      if (duplicate_past.isEmpty()) {
        System.out.println("There is no duplicate past draws.\n\n");
      } else {
        for (PreviousWinning x : duplicate_past) {
          System.out.println(x);
        }
      }

      tracker.showStats();

      System.out.println("\n\n***** Random Generated Numbers *****");
      for (PreviousWinning x : random_generated) {
        System.out.println(x);
      }

      System.out.println("\n");
    

    } catch (FileNotFoundException e) {
      System.out.println("Could not locate file.");
    } catch (IOException e) {
      System.out.println("Something went wrong.");
    }
  }
}