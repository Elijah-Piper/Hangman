import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class HangmanTest {
    @Test
    void selectWordReturnsEmptyStringIfNoWordIsFound() {
        assert(Hangman.selectWord(1, 0, 0).equals(""));
    }

    @Test
    void selectWordReturnsEmptyStringIfMaxLengthIsLessThanMinLength() {
        assert(Hangman.selectWord(1, 5, 4).equals(""));
    }

    @Test
    void selectWordReturnsWord() {
        String word = Hangman.selectWord(2, 0, 15);
        assert("" != word);
    }

    @Test
    void generateGallowsAttempt6DifferentThan5() {
        Hangman game1 = new Hangman();
        Hangman game2 = new Hangman();
        game2.setAttempts(5);
        assert(game1.generateGallows() != game2.generateGallows());
    }

    @Test
    void generateGallowsAttempt5DifferentThan4() {
        Hangman game1 = new Hangman();
        Hangman game2 = new Hangman();
        game1.setAttempts(5);
        game2.setAttempts(4);
        assert(game1.generateGallows() != game2.generateGallows());
    }

    @Test
    void generateGallowsAttempt4DifferentThan3() {
        Hangman game1 = new Hangman();
        Hangman game2 = new Hangman();
        game1.setAttempts(4);
        game2.setAttempts(3);
        assert(game1.generateGallows() != game2.generateGallows());
    }

    @Test
    void generateGallowsAttempt3DifferentThan2() {
        Hangman game1 = new Hangman();
        Hangman game2 = new Hangman();
        game1.setAttempts(3);
        game2.setAttempts(2);
        assert(game1.generateGallows() != game2.generateGallows());
    }

    @Test
    void generateGallowsAttempt2DifferentThan1() {
        Hangman game1 = new Hangman();
        Hangman game2 = new Hangman();
        game1.setAttempts(2);
        game2.setAttempts(1);
        assert(game1.generateGallows() != game2.generateGallows());
    }

    @Test
    void generateGallowsAttempt1DifferentThan0() {
        Hangman game1 = new Hangman();
        Hangman game2 = new Hangman();
        game1.setAttempts(1);
        game2.setAttempts(0);
        assert(game1.generateGallows() != game2.generateGallows());
    }

    @Test
    void generateMissedReturnsOutputSorted() {
        Hangman game = new Hangman();
        game.addToGuessedSet('d');
        game.addToGuessedSet('c');
        game.addToGuessedSet('b');
        game.addToGuessedSet('a');
        assert(game.generateMissed().equals("Guessed letters: a, b, c, d"));
    }

    @Test
    void generateMissedIsCaseInsensitive() {
        Hangman game = new Hangman();
        game.addToGuessedSet('A');
        game.addToGuessedSet('a');
        assert(game.generateMissed().equals("Guessed letters: a"));
    }

    @Test
    void userInputForDigitsReturnsDigits() {
        System.setIn(new ByteArrayInputStream("3".getBytes()));
        String output = Hangman.userInput(
                "",
                Pattern.compile("[\\d]"),
                ""
        );

        assert(output.equals("3"));
    }

    @Test
    void userInputForCharsReturnsChars() {
        System.setIn(new ByteArrayInputStream("d".getBytes()));
        String output = Hangman.userInput(
                "",
                Pattern.compile("[A-Za-z]"),
                ""
        );

        assert(output.equals("d"));
    }

    @Test
    void generateHiddenWordAddsCorrectGuesses() {
        Hangman game = new Hangman();
        game.setWord("apple");
        game.addToGuessedSet('p');
        assert(game.generateHiddenWord().equals("_pp__"));
    }
}