package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextManipulator {

    private static final Logger log = LoggerFactory.getLogger(TextManipulator.class);

    private final char LEFT_COMMAND =  'h';
    private final char RIGHT_COMMAND =  'l';
    private final char REPLACE_COMMAND =  'r';
    private final char ZERO =  '0';
    private final char NINE =  '9';

    private final int MAX_LONG_LENGTH = 19;

    public Result executeCommand(String text, String command) {
        if (text == null || command == null) {
            throw new IllegalArgumentException("Text or command is null");
        }

        int cursor = 0;
        int maxCursor = text.length() - 1;

        for (int i = 0; i < command.length(); i++) {
            char current = command.charAt(i);

            if (current == LEFT_COMMAND && cursor > 0) {
                cursor--;
            } else if (current == RIGHT_COMMAND && cursor < maxCursor) {
                cursor++;
            } else if (current == REPLACE_COMMAND && cursor <= maxCursor) {
                current = command.charAt(++i);
                text = replace(text, current, 1, cursor);
            } else if (isDigit(current)) {
                // handling multiplications
                StringBuilder sb = new StringBuilder();
                while (isDigit(command.charAt(i)) && i < command.length() - 1) {
                    sb.append(command.charAt(i++));
                }
                int multiplier = text.length();
                if(sb.length() < MAX_LONG_LENGTH) {
                    long multiplierLong = Long.parseLong(sb.toString());
                    multiplier = (int) Math.min(multiplierLong, multiplier);
                }

                current = command.charAt(i);
                if (current == LEFT_COMMAND && cursor > 0) {
                    cursor = Math.max(0, cursor - multiplier);
                } else if (current == RIGHT_COMMAND && cursor < maxCursor) {
                    cursor = Math.min(maxCursor, cursor + multiplier);
                } else if (current == REPLACE_COMMAND && cursor < maxCursor) {
                    current = command.charAt(++i);
                    text = replace(text, current, multiplier, cursor);
                    cursor = Math.min(maxCursor, cursor + multiplier - 1);
                }
            }
        }

        return new Result(text, cursor);
    }

    private String replace(String text, Character newChar, int multiplier, int cursor) {
        char[] textArray = text.toCharArray();
        for (int j = 0; j < multiplier; j++) {
            textArray[cursor + j] = newChar;
        }
        return new String(textArray);
    }

    private boolean isDigit(char c) {
        return c >= ZERO && c <= NINE;
    }
}
