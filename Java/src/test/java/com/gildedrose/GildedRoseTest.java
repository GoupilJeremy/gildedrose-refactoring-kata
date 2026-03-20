package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    // --- Item normal ---

    @Test
    void normalItem_beforeExpiration_qualityDecreasesBy1() {
        Item[] items = new Item[]{new Item("Normal Item", 10, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(9, items[0].sellIn);
        assertEquals(19, items[0].quality);
    }

    @Test
    void normalItem_onExpirationDay_qualityDecreasesBy1() {
        Item[] items = new Item[]{new Item("Normal Item", 0, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(-1, items[0].sellIn);
        assertEquals(18, items[0].quality);
    }

    @Test
    void normalItem_afterExpiration_qualityDecreasesBy2() {
        Item[] items = new Item[]{new Item("Normal Item", -1, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(-2, items[0].sellIn);
        assertEquals(18, items[0].quality);
    }

    @Test
    void normalItem_qualityNeverGoesNegative() {
        Item[] items = new Item[]{new Item("Normal Item", 5, 0)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, items[0].quality);
    }

    // --- Aged Brie ---

    @Test
    void agedBrie_beforeExpiration_qualityIncreasesBy1() {
        Item[] items = new Item[]{new Item("Aged Brie", 10, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(9, items[0].sellIn);
        assertEquals(21, items[0].quality);
    }

    @Test
    void agedBrie_afterExpiration_qualityIncreasesBy2() {
        Item[] items = new Item[]{new Item("Aged Brie", -1, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(22, items[0].quality);
    }

    @Test
    void agedBrie_qualityNeverExceeds50() {
        Item[] items = new Item[]{new Item("Aged Brie", 10, 50)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(50, items[0].quality);
    }

    // --- Sulfuras ---

    @Test
    void sulfuras_neverChanges() {
        Item[] items = new Item[]{new Item("Sulfuras, Hand of Ragnaros", 10, 80)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(10, items[0].sellIn);
        assertEquals(80, items[0].quality);
    }

    // --- Backstage passes ---

    @Test
    void backstagePass_moreThan10DaysLeft_qualityIncreasesBy1() {
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(21, items[0].quality);
    }

    @Test
    void backstagePass_10DaysOrLess_qualityIncreasesBy2() {
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(22, items[0].quality);
    }

    @Test
    void backstagePass_5DaysOrLess_qualityIncreasesBy3() {
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(23, items[0].quality);
    }

    @Test
    void backstagePass_afterConcert_qualityDropsToZero() {
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, items[0].quality);
    }
}
