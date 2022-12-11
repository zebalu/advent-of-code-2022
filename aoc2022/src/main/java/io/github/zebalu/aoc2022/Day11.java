package io.github.zebalu.aoc2022;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Day11 {
    public static void main(String[] args) {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() {
        return monkeyBusiness(20, true);
    }

    private static long part2() {
        return monkeyBusiness(10_000, false);
    }

    private static long monkeyBusiness(int rounds, boolean calm) {
        var msi = INPUT.split("\n\n");
        var monkeys = new LinkedHashMap<Integer, Monkey>();

        Arrays.stream(msi).forEach(sss -> {
            var lines = sss.lines().toList();
            var id = Integer.parseInt(lines.get(0).substring(0, lines.get(0).length() - 1).split(" ")[1]);
            var items = Arrays.stream(lines.get(1).substring("  Starting items: ".length()).split(", "))
                    .map(s -> Long.parseLong(s)).map(l -> BigInteger.valueOf(l)).toList();
            var operation = toOperation(lines.get(2).substring("  Operation: new = old ".length()).split(" "));
            var test = toTest(lines.get(3).split(" "));
            var to = toSelection(lines.get(4).split(" "), lines.get(5).split(" "));
            monkeys.put(id, new Monkey(items, operation, test, to, calm));
        });
        for (var i = 0; i < rounds; ++i) {
            for (var m : monkeys.values()) {
                m.round(monkeys);
            }
        }

        return monkeys.values().stream().sorted((a, b) -> (int) (b.count - a.count)).mapToLong(m -> m.count).limit(2)
                .reduce(1L, (a, b) -> a * b);
    }

    private static final Function<BigInteger, BigInteger> toOperation(String[] parts) {
        boolean isOld = "old".equals(parts[parts.length - 1]);
        long v = isOld ? -1 : Integer.parseInt(parts[parts.length - 1]);
        if (parts[0].equals("+")) {
            return isOld ? (a) -> a.add(a) : (a) -> a.add(BigInteger.valueOf(v));
        } else if (parts[0].equals("*")) {
            return isOld ? a -> a.multiply(a) : (a) -> a.multiply(BigInteger.valueOf(v));
        } else {
            throw new IllegalStateException("what is this? '" + parts[0] + "'");
        }
    }

    private static final BigInteger toTest(String[] parts) {
        return BigInteger.valueOf(Long.parseLong(parts[parts.length - 1]));
    }

    private static final Function<Boolean, Integer> toSelection(String[] trueLineParts, String[] falseLineParts) {
        int v1 = Integer.parseInt(trueLineParts[trueLineParts.length - 1]);
        int v2 = Integer.parseInt(falseLineParts[falseLineParts.length - 1]);
        return (b) -> b ? v1 : v2;
    }

    private static class Monkey {
        private final List<BigInteger> items = new LinkedList<>();
        private final Function<BigInteger, BigInteger> opertion;
        private final BigInteger test;
        private final Function<Boolean, Integer> target;
        private long count = 0L;
        private boolean shouldDivide = true;
        private BigInteger mulitples = null;

        public Monkey(List<BigInteger> startItems, Function<BigInteger, BigInteger> opertion, BigInteger test,
                Function<Boolean, Integer> target, boolean shouldDivide) {
            items.addAll(startItems);
            this.opertion = opertion;
            this.test = test;
            this.target = target;
            this.shouldDivide = shouldDivide;
        }

        void round(Map<Integer, Monkey> monkeys) {
            if (!shouldDivide && mulitples == null) {
                mulitples = monkeys.values().stream().map(m -> m.test).reduce(BigInteger.ONE, (a, b) -> a.multiply(b));
            }
            while (!(items.isEmpty())) {
                BigInteger item = items.remove(0);
                BigInteger newValue = opertion.apply(item);
                if (shouldDivide) {
                    newValue = newValue.divide(BigInteger.valueOf(3L));
                } else {
                    newValue = newValue.divideAndRemainder(mulitples)[1];
                }
                int to = target.apply(newValue.divideAndRemainder(test)[1].equals(BigInteger.ZERO));
                monkeys.get(to).items.add(newValue);
                ++count;
            }
        }
    }

    private static final String INPUT = """
            Monkey 0:
              Starting items: 66, 71, 94
              Operation: new = old * 5
              Test: divisible by 3
                If true: throw to monkey 7
                If false: throw to monkey 4

            Monkey 1:
              Starting items: 70
              Operation: new = old + 6
              Test: divisible by 17
                If true: throw to monkey 3
                If false: throw to monkey 0

            Monkey 2:
              Starting items: 62, 68, 56, 65, 94, 78
              Operation: new = old + 5
              Test: divisible by 2
                If true: throw to monkey 3
                If false: throw to monkey 1

            Monkey 3:
              Starting items: 89, 94, 94, 67
              Operation: new = old + 2
              Test: divisible by 19
                If true: throw to monkey 7
                If false: throw to monkey 0

            Monkey 4:
              Starting items: 71, 61, 73, 65, 98, 98, 63
              Operation: new = old * 7
              Test: divisible by 11
                If true: throw to monkey 5
                If false: throw to monkey 6

            Monkey 5:
              Starting items: 55, 62, 68, 61, 60
              Operation: new = old + 7
              Test: divisible by 5
                If true: throw to monkey 2
                If false: throw to monkey 1

            Monkey 6:
              Starting items: 93, 91, 69, 64, 72, 89, 50, 71
              Operation: new = old + 1
              Test: divisible by 13
                If true: throw to monkey 5
                If false: throw to monkey 2

            Monkey 7:
              Starting items: 76, 50
              Operation: new = old * old
              Test: divisible by 7
                If true: throw to monkey 4
                If false: throw to monkey 6""";
}
