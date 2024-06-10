package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SimpleDB {
    private static final Logger log = LoggerFactory.getLogger(SimpleDB.class);

    private final Map<String, Integer> dataStore = new HashMap<>();
    private final Map<Integer, Integer> valueCount = new HashMap<>();
    private final Deque<Map<String, Integer>> transactionStack = new ArrayDeque<>();

    /**
     * Sets the specified name to the given value within the database.
     * If the name already exists, the value is updated and the old value count is decremented.
     * Time Complexity: O(1) worst-case,
     *
     * @param name  the key under which the value is stored
     * @param value the value to set
     */
    public void set(String name, int value) {
        Integer oldValue = dataStore.put(name, value);
        if (oldValue != null) {
            updateCount(oldValue, -1);
        }
        updateCount(value, 1);
        recordChange(name, oldValue);
    }


    /**
     * Retrieves the value associated with the specified name. If the name does not exist,
     * returns null.
     * Time Complexity: O(1) in worst-case
     *
     * @param name the key whose value is to be retrieved
     * @return the value as an integer, or null if the key does not exist
     */
    public Integer get(String name) {
        if(dataStore.containsKey(name)) {
            return dataStore.get(name);
        } else {
            log.info("NULL");
            return null;
        }
    }

    /**
     * Removes the entry associated with the specified name from the database. If the
     * name exists, its count is also decremented.
     * Time Complexity: O(1) in worst-case
     *
     * @param name the key to remove
     */
    public void delete(String name) {
        Integer removedValue = dataStore.remove(name);
        if (removedValue != null) {
            updateCount(removedValue, -1);
            recordChange(name, removedValue);
        }
    }

    /**
     * Counts the number of entries that have the specified value.
     * Time Complexity: O(1) in worst-case
     *
     * @param value the value to count within the database
     * @return the number of times the value occurs
     */
    public int count(Integer value) {
        return valueCount.getOrDefault(value, 0);
    }

    /**
     * Begins a new transaction. All subsequent changes until a commit or rollback
     * are reversible and isolated within this transaction.
     * Time Complexity: O(1) in worst-case
     */
    public void begin() {
        transactionStack.push(new HashMap<>());
    }

    /**
     * Rolls back the most recent transaction. All changes made during the most recent
     * transaction are undone.
     * Time Complexity: O(m) where m is the number of operations in the transaction being rolled back.
     *
     * @return a flag if operation was successful
     */
    public boolean rollback() {
        if (transactionStack.isEmpty()) {
            log.info("NO TRANSACTION");
            return false;
        } else {
            Map<String, Integer> lastChanges = transactionStack.pop();
            lastChanges.forEach((key, value) -> {
                if (value == null) {
                    delete(key);
                } else {
                    set(key, value);
                }
            });
            return true;
        }
    }

    /**
     * Commits all open transactions, making all changes permanent. Clears the transaction stack.
     * Time Complexity: O(1) in the worst-case
     *
     * @return a flag if operation was successful
     */
    public boolean commit() {
        if (transactionStack.isEmpty()) {
            log.info("NO TRANSACTION");
            return false;
        } else {
            transactionStack.clear();
            return true;
        }
    }

    private void updateCount(Integer value, int delta) {
        valueCount.put(value, valueCount.getOrDefault(value, 0) + delta);
        if (valueCount.get(value) == 0) {
            valueCount.remove(value);
        }
    }

    private void recordChange(String key, Integer originalValue) {
        if (!transactionStack.isEmpty() && !transactionStack.peek().containsKey(key)) {
            transactionStack.peek().put(key, originalValue);
        }
    }
}
