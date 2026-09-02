public class PreviousWinning {
  int[] numbers;
  String date;

    PreviousWinning(int[] numbers) {
    this.numbers = numbers;
    this.date = "N/A";
  }

  PreviousWinning(int[] numbers, String date) {
    this.numbers = numbers;
    this.date = date;
  }

  public int[] getNumber() {
    return numbers;
  }

  public String getDate() {
    return date;
  }

@Override
public String toString() {
  return String.format(
      "[%2d, %2d, %2d, %2d, %2d, %2d] - %s",
      numbers[0],
      numbers[1],
      numbers[2],
      numbers[3],
      numbers[4],
      numbers[5],
      date
  );
}
}
