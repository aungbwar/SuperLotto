import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SuperLotto {

  public static void main(String[] args) {

    Random random = new Random();

    String fileName = "data.txt";
    String randomFileName = "random.txt";
    String resultFileName = "result.txt";

    int noDateNumber = 1;
    int numEligible = 0;

    NumberTracker tracker = new NumberTracker();

    ArrayList<PreviousWinning> mainList = new ArrayList<>();
    ArrayList<PreviousWinning> duplicate_new = new ArrayList<>();
    ArrayList<PreviousWinning> duplicate_past = new ArrayList<>();
    ArrayList<PreviousWinning> random_generated = new ArrayList<>();

    HashMap<String, PreviousWinning> existingNumbers = new HashMap<>();

    try (PrintWriter result =
        new PrintWriter(new BufferedWriter(new FileWriter(resultFileName)))) {

      result.println("\n\n*********** SuperLotto ***********");

      try (BufferedReader reader =
          new BufferedReader(new FileReader(fileName))) {

        String line;

        while ((line = reader.readLine()) != null) {

          if (line.trim().isEmpty()) {
            continue;
          }

          String[] parts = line.trim().split("\\s+");

          if (parts.length < 10) {
            continue;
          }

          int[] newNum = new int[6];

          for (int i = 0; i < 6; i++) {
            newNum[i] = Integer.parseInt(parts[i]);
          }

          String date =
              String.join(" ", parts[7], parts[8], parts[9]);

          String key = Arrays.toString(newNum);

          if (existingNumbers.containsKey(key)) {

            PreviousWinning original =
                existingNumbers.get(key);

            duplicate_past.add(
                new PreviousWinning(
                    original.getNumber(),
                    original.getDate()
                )
            );

            duplicate_new.add(
                new PreviousWinning(newNum, date)
            );

            result.printf(
                "Not Eligible: %-25s - %s%n",
                Arrays.toString(newNum),
                date
            );

            continue;
          }

          numEligible++;

          result.printf(
              "%d Eligible:     %-25s - %s%n",
              numEligible,
              Arrays.toString(newNum),
              date
          );

          PreviousWinning winning =
              new PreviousWinning(newNum, date);

          mainList.add(winning);

          existingNumbers.put(key, winning);

          tracker.addDraw(newNum);
        }

      } catch (FileNotFoundException e) {

        result.println("Could not locate data.txt.");
        return;
      }

      try (BufferedReader randomReader =
          new BufferedReader(new FileReader(randomFileName))) {

        String randomLine;

        while ((randomLine = randomReader.readLine()) != null) {

          if (randomLine.trim().isEmpty()) {
            continue;
          }

          String[] parts =
              randomLine.trim().split("\\s+");

          if (parts.length < 10) {
            continue;
          }

          noDateNumber =
              Integer.parseInt(parts[9]) + 1;
        }

      } catch (FileNotFoundException e) {

        noDateNumber = 1;
      }

      try (
          BufferedWriter dataWriter =
              new BufferedWriter(
                  new FileWriter(fileName, true)
              );

          BufferedWriter randomWriter =
              new BufferedWriter(
                  new FileWriter(randomFileName, true)
              )
      ) {

        // Generate x games

        for (int i = 1; i <= 1 ; i++) {

          boolean generated = false;

          while (!generated) {

            int[] tempNumbers = new int[6];

            ArrayList<Integer> usedNumbers =
                new ArrayList<>();

            for (int j = 0; j < 5; j++) {

              int number =
                  random.nextInt(1, 48);

              while (usedNumbers.contains(number)) {
                number =
                    random.nextInt(1, 48);
              }

              tempNumbers[j] = number;
              usedNumbers.add(number);
            }

            Arrays.sort(tempNumbers, 0, 5);

            tempNumbers[5] =
                random.nextInt(1, 28);

            String key =
                Arrays.toString(tempNumbers);

            if (existingNumbers.containsKey(key)) {
              continue;
            }

            String noDate =
                String.format(
                    "00 zzz %02d",
                    noDateNumber
                );

            PreviousWinning generatedWinning =
                new PreviousWinning(
                    tempNumbers,
                    noDate
                );

            random_generated.add(generatedWinning);

            mainList.add(generatedWinning);

            existingNumbers.put(
                key,
                generatedWinning
            );

            String outputLine =
                tempNumbers[0] + " " +
                tempNumbers[1] + " " +
                tempNumbers[2] + " " +
                tempNumbers[3] + " " +
                tempNumbers[4] + " " +
                tempNumbers[5] + " - " +
                noDate;

            dataWriter.write(outputLine);
            dataWriter.newLine();

            randomWriter.write(outputLine);
            randomWriter.newLine();

            generated = true;
            noDateNumber++;
          }
        }



      }

      result.println("\n\n------------------------------------");
      result.println("          Total draws: " + mainList.size());
      result.println("------------------------------------");

      result.println("\n\n***** Duplicate New Numbers *****");

      if (duplicate_new.isEmpty()) {
        result.println("There is no duplicate new draws.\n\n");
      } else {
        for (PreviousWinning x : duplicate_new) {
          result.println(x);
        }
      }

      result.println("\n\n***** Duplicate Past Numbers *****");

      if (duplicate_past.isEmpty()) {
        result.println("There is no duplicate past draws.\n\n");
      } else {
        for (PreviousWinning x : duplicate_past) {
          result.println(x);
        }
      }

      tracker.showStats(result);

      result.println("\n\n***** Random Generated Numbers *****");

      for (PreviousWinning x : random_generated) {
        result.println(x);
      }

      result.println();

    } catch (IOException e) {

      System.out.println("Something went wrong.");
      e.printStackTrace();
    }
  }
}