package io.github.zebalu.aoc2022;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Day10 {
    public static void main(String[] args) {
        Map<Integer, Integer> duringCycle = calculateDuringCycleValues();
        part1(duringCycle);
        part2(duringCycle);
    }

    private static void part1(Map<Integer, Integer> duringCycle) {
        int sum = 0;
        for (int i = 20; i <= 220; i += 40) {
            sum += i * duringCycle.get(i);
        }
        System.out.println(sum);
    }

    private static Map<Integer, Integer> calculateDuringCycleValues() {
        Map<Integer, Integer> duringCycle = new HashMap<>();
        int x = 1;
        var lines = INPUT.lines().toList();
        int cycle = 1;
        for (int i = 0; i < lines.size(); ++i) {
            String line = lines.get(i);
            if (line.equals("noop")) {
                duringCycle.put(cycle, x);
            } else {
                int value = Integer.parseInt(line.split(" ")[1]);
                duringCycle.put(cycle, x);
                ++cycle;
                duringCycle.put(cycle, x);
                x += value;
            }
            ++cycle;
        }
        return duringCycle;
    }

    private static void part2(Map<Integer, Integer> duringCycle) {
        int cycle = 1;
        StringJoiner rowsJoiner = new StringJoiner("\n");
        for (int i = 0; i < 6; ++i) {
            StringBuilder lineBuilder = new StringBuilder();
            for (int j = 0; j < 40; ++j) {
                if (Math.abs(duringCycle.get(cycle) - j) <= 1) {
                    lineBuilder.append("#");
                } else {
                    lineBuilder.append(" ");
                }
                ++cycle;
            }
            rowsJoiner.add(lineBuilder.toString());
        }
        System.out.println(rowsJoiner.toString());
    }

    private static final String INPUT = """
            addx 1
            addx 4
            addx 1
            noop
            addx 4
            addx 3
            addx -2
            addx 5
            addx -1
            noop
            addx 3
            noop
            addx 7
            addx -1
            addx 1
            noop
            addx 6
            addx -1
            addx 5
            noop
            noop
            noop
            addx -37
            addx 7
            noop
            noop
            noop
            addx 5
            noop
            noop
            noop
            addx 9
            addx -8
            addx 2
            addx 5
            addx 2
            addx 5
            noop
            noop
            addx -2
            noop
            addx 3
            addx 2
            noop
            addx 3
            addx 2
            noop
            addx 3
            addx -36
            noop
            addx 26
            addx -21
            noop
            noop
            noop
            addx 3
            addx 5
            addx 2
            addx -4
            noop
            addx 9
            addx 5
            noop
            noop
            noop
            addx -6
            addx 7
            addx 2
            noop
            addx 3
            addx 2
            addx 5
            addx -39
            addx 34
            addx 5
            addx -35
            noop
            addx 26
            addx -21
            addx 5
            addx 2
            addx 2
            noop
            addx 3
            addx 12
            addx -7
            noop
            noop
            noop
            noop
            noop
            addx 5
            addx 2
            addx 3
            noop
            noop
            noop
            noop
            addx -37
            addx 21
            addx -14
            addx 16
            addx -11
            noop
            addx -2
            addx 3
            addx 2
            addx 5
            addx 2
            addx -15
            addx 6
            addx 12
            addx -2
            addx 9
            addx -6
            addx 7
            addx 2
            noop
            noop
            noop
            addx -33
            addx 1
            noop
            addx 2
            addx 13
            addx 15
            addx -21
            addx 21
            addx -15
            noop
            noop
            addx 4
            addx 1
            noop
            addx 4
            addx 8
            addx 6
            addx -11
            addx 5
            addx 2
            addx -35
            addx -1
            noop
            noop""";
}
