package org.example.mapping;

import java.util.List;

public final class NameSplitter {

    private NameSplitter() {}

    /**
     * Splits a full name into first and last name.
     * Rules:
     *  - If one token only → firstName = token, lastName = "N/A"
     *  - If two tokens → firstName = first, lastName = second
     *  - If 3+ → firstName = first, lastName = join(rest with space)
     */
    public static String[] split(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[] {"N/A", "N/A"};
        }
        List<String> parts = List.of(fullName.trim().split("\\s+"));
        if (parts.size() == 1) {
            return new String[] {parts.get(0), "N/A"};
        } else if (parts.size() == 2) {
            return new String[] {parts.get(0), parts.get(1)};
        } else {
            String first = parts.get(0);
            String last = String.join(" ", parts.subList(1, parts.size()));
            return new String[] {first, last};
        }
    }
}
