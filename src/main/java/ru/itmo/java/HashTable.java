package ru.itmo.java;

import java.util.Map;

public class HashTable {

    private int size = 0;
    private int actualSize = 0;
    private int capacity;
    private int threshold;
    private Entry[] entries;

    public HashTable(int capacity) {
        this.capacity = capacity;
        this.threshold = capacity / 2;
        entries = new Entry[capacity];
    }

    public HashTable(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.threshold = (int) (capacity * loadFactor);
        entries = new Entry[capacity];
    }

    private int keyToIndex(Object key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    private int findVacantIndex(Entry[] array, Object key) {
        int index = keyToIndex(key);
        while (array[index] != null && !array[index].deleted && !array[index].key.equals(key)) {
            index++;
            if (index == array.length) {
                index = 0;
            }
        }
        return index;
    }

    private int findKey(Entry[] array, Object key) {
        int index = keyToIndex(key);
        int count = 0;
        while (array[index] != null && !array[index].key.equals(key)) {
            index++;
            count++;
            if (index == array.length) {
                index = 0;
            }
            if (count > capacity) {
                break;
            }
        }
        return index;
    }

    private void doubleCapacity(){
        capacity *= 2;
        threshold *= 2;
        Entry[] doubled = new Entry[capacity];
        for (Entry entry : entries) {
            if (entry != null) {
                doubled[findVacantIndex(doubled, entry.key)] = entry;
            }
        }
        entries = doubled;
    }


    Object put(Object key, Object value) {
        if (actualSize + 1 > threshold) {
            doubleCapacity();
        }
        int index = findKey(entries, key);
        if (entries[index] != null && !entries[index].deleted) {
            Object res = entries[index].value;
            entries[index].value = value;
            return res;
        } else {
            Entry entry = new Entry(key, value);
            entries[index] = entry;
            size++;
            actualSize++;
            return null;
        }
    }

    Object get(Object key) {
        int index = findKey(entries, key);
        if (entries[index] != null && !entries[index].deleted) {
            return entries[index].value;
        } else {
            return null;
        }
    }

    Object remove(Object key) {
        int index = findKey(entries, key);
        if (entries[index] != null && !entries[index].deleted) {
            Object res = entries[index].value;
            entries[index].deleted = true;
            size--;
            return res;
        } else {
            return null;
        }
    }

    int size() {
        return size;
    }

    private class Entry {
        private Object key;
        private Object value;
        private boolean deleted;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.deleted = false;
        }
    }

}
