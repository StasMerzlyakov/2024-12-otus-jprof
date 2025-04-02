package ru.otus.cachehw;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MyCacheTest {

    @Test
    @DisplayName("Проверяем, что класс MyCache сохраняет данные и очищается")
    void doTest1() {
        MyCache<Long, String> cache = new MyCache<>();
        cache.put(100L, "test");
        assertThat(cache.get(100L)).isEqualTo("test");

        System.gc(); // WeakReference живет до ближайшей сборки мусора
        assertThat(cache.get(100L)).isNull();
    }
}
