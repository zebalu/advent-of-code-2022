package io.github.zebalu.aoc2022;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Day10 {
    public static void main(String[] args) {
        var duringCycle = calculateDuringCycleValues();
        System.out.println(part1(duringCycle));
        System.out.println(part2(duringCycle));
    }

    private static int part1(Map<Integer, Integer> duringCycle) {
        var sum = 0;
        for (int i = 20; i <= 220; i += 40) {
            sum += i * duringCycle.get(i);
        }
        return sum;
    }

    private static String part2(Map<Integer, Integer> cycleValues) {
        var cycle = 1;
        var rowJoiner = new StringJoiner("\n");
        for (var i = 0; i < 6; ++i) {
            var lineBuilder = new StringBuilder();
            for (var j = 0; j < 40; ++j, ++cycle) {
                lineBuilder.append(Math.abs(cycleValues.get(cycle) - j) <= 1 ? "#" : " ");
            }
            rowJoiner.add(lineBuilder.toString());
        }
        return rowJoiner.toString();
    }

    private static Map<Integer, Integer> calculateDuringCycleValues() {
        var duringCycle = new HashMap<Integer, Integer>();
        var x = 1;
        var cycle = 1;
        for (var line : INPUT.lines().toList()) {
            if (line.equals("noop")) {
                duringCycle.put(cycle++, x);
            } else {
                duringCycle.put(cycle++, x);
                duringCycle.put(cycle++, x);
                x += Integer.parseInt(line.split(" ")[1]);
            }
        }
        return duringCycle;
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
