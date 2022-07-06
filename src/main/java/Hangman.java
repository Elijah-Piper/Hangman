import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Hangman {

    private String word;
    private int attempts;
    private HashSet<Character> guessedSet;

    // CONSTRUCTORS
    public Hangman() {
        word = selectWord(1, 1, 20);
        attempts = 6;
        guessedSet = new HashSet<>();
    }

    public Hangman(int repo) {
        word = selectWord(repo, 1, 20);
        attempts = 6;
        guessedSet = new HashSet<>();
    }

    public Hangman(int repo, int minLength, int maxLength) {
        word = selectWord(repo, minLength, maxLength);
        attempts = 6;
        guessedSet = new HashSet<>();
    }

    // GETTERS AND SETTERS
    public void setWord(String w) {
        this.word = w;
    }
    public String getWord() {
        return word;
    }
    public void setAttempts(int a) {
        this.attempts = a;
    }
    public int getAttempts() {
        return attempts;
    }
    public void addToGuessedSet(char c) {
        // Case-insensitive; converts capital to lower-case
        this.guessedSet.add(("" + c).toLowerCase().charAt(0));
    }
    public HashSet<Character> getGuessedSet() {
        return guessedSet;
    }

    public static String selectWord(int repo, int minLength, int maxLength) {
        if (maxLength < 1) return "";
        if (maxLength == 2 && repo == 2) return "";
        if (maxLength < minLength) return "";

        String word;
        int linesInFile = repo == 1 ? 1000 : 25322;

        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(String.format("src/main/resources/wordrepo%d.txt", repo)));
            List<String> lines = reader.lines().toList();

            boolean wordNotFound = true;
            while (wordNotFound) {
                int randomLine = ThreadLocalRandom.current().nextInt(0, linesInFile);
                word = lines.get(randomLine);

                Pattern wordPat = Pattern.compile("[0-9- ]");
                if (word.length() > minLength && word.length() < maxLength) { // Length check
                    if (!wordPat.matcher(word).find()) { // Check for numbers, spaces, or hyphens
                        return word;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateGallows() {
        String gallows = switch (attempts) {
            case 0 -> """
                     +---+
                     0   |
                    /|\\  |
                    / \\  |
                         |
                    _____|_____
                    """;
            case 1 -> """
                     +---+
                     O   |
                    /|\\  |
                      \\  |
                         |
                    _____|_____
                    """;
            case 2 -> """
                     +---+
                     O   |
                    /|\\  |
                         |
                         |
                    _____|_____
                    """;
            case 3 -> """
                     +---+
                     O   |
                     |\\  |
                         |
                         |
                    _____|_____
                    """;
            case 4 -> """
                     +---+
                     O   |
                     |   |
                         |
                         |
                    _____|_____
                    """;
            case 5 -> """
                     +---+
                     O   |
                         |
                         |
                         |
                    _____|_____
                    """;
            default -> """
                     +---+
                         |
                         |
                         |
                         |
                    _____|_____
                    """;
        };
        return gallows;
    }

    public String generateMissed() {
        String output = "Guessed letters: ";
        for (char c: guessedSet.stream().sorted().toList()) {
            output = output.length() == 17 ? output.concat("" + c) : output.concat(", " + c);
        }
        return output;
    }

    public String generateHiddenWord() {
        String hiddenWord = "";
        for (char c: word.toCharArray()) {
            if (guessedSet.contains(c)) {
                hiddenWord = hiddenWord.concat("" + c);
            } else {
                hiddenWord = hiddenWord.concat("_");
            }
        }
        return hiddenWord;
    }

    public static boolean userInputYesOrNo(String question) {
        // Asks the user a yes or no question, returning boolean depending on answer
        Scanner sc = new Scanner(System.in);

        String suffix = "(y or n)\n";
        System.out.println(question + suffix);
        while (true) {
            try {
                String response = sc.nextLine().toLowerCase();
                if (response.equals("y")) return true;
                else if (response.equals("n")) return false;
                // Commences another loop after invalid input
                System.out.println("Invalid input. Please enter " + suffix);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String userInput(String question, Pattern validInputs, String invalidInputSuffix) {
        // Asks user a question,
        Scanner sc = new Scanner(System.in);

        System.out.println(question);
        while (true) {
            try {
                String response = sc.nextLine();
                if (validInputs.matcher(response).matches()) return response;
                // Commences another loop after invalid input
                System.out.println("Invalid input. Please enter " + invalidInputSuffix);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean playing = true;

        while (playing) {
            Hangman game;

            // GAME INITIALIZATION
            boolean custom = Hangman.userInputYesOrNo("Would you like a custom game?");
            if (custom) {
                boolean expanded = Hangman.userInputYesOrNo(
                        "Would you like to use the expanded (potentially more difficult) word list?");
                int minLength = Integer.parseInt(Hangman.userInput(
                        "Minimum word length? (number)\n",
                        Pattern.compile("[0-9]{1,3}"),
                        "a single integer.\n"
                ));
                int maxLength = Integer.parseInt(Hangman.userInput(
                        "Maximum word length? (number greater than minimum length)\n",
                        Pattern.compile("[0-9]{1,3}"),
                        "a single integer.\n"
                ));
                game = new Hangman(expanded ? 2 : 1, minLength, maxLength);
            } else {
                game = new Hangman();
            }

            // GAME START
            int found = 0;
            System.out.println("H A N G M A N\n");
            do {
                System.out.println(game.generateGallows());
                System.out.println(game.generateMissed());
                System.out.println(game.generateHiddenWord());
                while (true) { // GUESS LOOP
                    String guess = Hangman.userInput(
                            "Guess a letter\n",
                            Pattern.compile("[A-Za-z]"),
                            "a single letter"
                    );
                    if (!game.getGuessedSet().contains(guess.charAt(0))) {
                        if (!game.getWord().contains(guess)) {
                            game.setAttempts(game.getAttempts() - 1);
                        }
                        game.addToGuessedSet(guess.charAt(0));
                        break;
                    } else if (game.getGuessedSet().contains(guess.charAt(0))) {
                        System.out.println("You already made that guess");
                    }
                    if (!game.getWord().contains(guess)) {
                        game.setAttempts(game.getAttempts() - 1);
                    }
                    for (char c: game.getWord().toCharArray()) {
                        if (game.getGuessedSet().contains(c)) {
                            found++;
                        }
                    }
                }
            } while (game.getAttempts() > 0);
            if (found == game.getWord().length() - 1) {
                System.out.println(game.generateGallows());
                System.out.println("Congratulations! You won!\n");
            } else {
                System.out.println(game.generateGallows());
                System.out.println("Game over...\nThe word was %s".formatted(game.getWord()));
            }

            playing = Hangman.userInputYesOrNo("Would you like to play again?");
        }
    }
}
