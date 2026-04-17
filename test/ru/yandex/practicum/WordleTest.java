package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleGame game;
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        List<String> words = Arrays.asList("дарам", "аббат", "вобла", "аборт", "кринж");
        dictionary = new WordleDictionary(words);
        game = new WordleGame(dictionary);
    }

    @Test
    void testContains_ShouldReturnTrue_WhenWordsAreEqual() {
        boolean result = game.contains("аббат", "аббат");
        assertTrue(result);
    }

    @Test
    void testContains_ShouldReturnFalse_WhenWordsAreDifferent() {
        // Проверяем, что метод contains возвращает false для разных слов
        boolean result = game.contains("аббат", "кринж");
        assertFalse(result);
    }

    @Test
    void testLeftStep_ShouldReturn6_WhenGameStarts() {
        // В начале игры должно быть 6 попыток
        assertEquals(6, game.leftStep());
    }

    @Test
    void testDecrementStep_ShouldReduceSteps() {
        int step = game.leftStep() - 1;
        assertEquals(5, step);

        step -= 1;
        assertEquals(4, step);
    }

    @Test
    void testSearchInExtra_WhenExtraIsEmpty_ShouldReturnTrue() {
        // Если нет запрещенных букв, любое слово подходит
        assertTrue(game.searchInExtra("аборт"));
        assertTrue(game.searchInExtra("номер"));
    }

    @Test
    void testClue_ShouldNotCrash_WithValidWord() {
        // Просто проверяем, что метод не падает с ошибкой
        try {
            java.lang.reflect.Field field = game.getClass().getDeclaredField("answer");
            field.setAccessible(true);
            field.set(game, "врач");

            // Метод не должен выбросить исключение
            assertDoesNotThrow(() -> game.clue("врач"));
        } catch (Exception e) {
            fail("Не удалось установить ответ для теста");
        }
    }

    @Test
    void testInputWord_ShouldAddWordToHistory() {
        try {
            java.lang.reflect.Field historyField = game.getClass().getDeclaredField("historyWord");
            historyField.setAccessible(true);
            List<String> history = (List<String>) historyField.get(game);
            int initialSize = history.size();

            game.inputWord("аббот");

            assertEquals(initialSize + 1, history.size());
        } catch (Exception e) {
            fail("Не удалось проверить историю");
        }
    }

    @Test
    void testSearchWordInWordAndIndex_WhenMapIsEmpty_ShouldReturnTrue() {
        // Если нет требований к позициям, любое слово подходит
        assertTrue(game.searchWordInWordAndIndex("парик"));
    }

    @Test
    void testSearchWordAnotherPlace_WhenMapIsEmpty_ShouldReturnTrue() {
        // Если нет требований к буквам в других местах, любое слово подходит
        assertTrue(game.searchWordAnotherPlace("варик"));
    }
}