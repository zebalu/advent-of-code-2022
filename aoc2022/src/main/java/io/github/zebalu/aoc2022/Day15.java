package io.github.zebalu.aoc2022;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public class Day15 {
    public static void main(String[] args) {
        var pairs = INPUT.lines().map(Pair::parse).toList();
        System.out.println(part1(pairs));
        System.out.println(part2(pairs));
    }

    private static long part1(List<Pair> pairs) {
        var coveredRange = pairs.stream().map(p -> List.of(p.rangeIn(2_000_000))).reduce(List.of(), Range::plus)
                .stream().distinct().mapToLong(Range::size).sum();
        var beaconCount = pairs.stream().map(p->p.beacon()).distinct().mapToLong(b -> b.y() == 2_000_000L ? 1L : 0L).sum();
        return coveredRange - beaconCount;
    }

    // original idea: https://github.com/p-kovacs/advent-of-code-2022/blob/master/src/main/java/com/github/pkovacs/aoc/y2022/Day15.java#L41
    private static long part2(List<Pair> pairss) {
        var max = 4000000L;

        var sums = new HashSet<Long>();
        var difs = new HashSet<Long>();
        for (var s : pairss) {
            sums.add(s.sensore().x() + s.sensore().y() - s.radious() - 1);
            sums.add(s.sensore().x() + s.sensore().y() + s.radious() + 1);
            difs.add(s.sensore().x() - s.sensore().y() - s.radious() - 1);
            difs.add(s.sensore().x() - s.sensore().y() + s.radious() + 1);
        }
        sums.add(max);
        difs.add(0L);

        for (long a : new HashSet<>(sums)) {
            for (long b : new HashSet<>(difs)) {
                var x = (a + b) / 2;
                var y = a - x;
                var p = new Coord(x, y);
                if ((a + b) % 2 == 0 && x >= 0 && x <= max && y >= 0 && y <= max
                        && pairss.stream().noneMatch(s -> s.excludes(p))) {
                    return (long) x * max + y;
                }
            }
        }
        throw new NoSuchElementException();
    }
 
    private static final record Coord(long x, long y) {
        long distance(Coord other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }

        static Coord parse(String desc) {
            var xyStr = desc.split(" at ")[1];
            var xy = Arrays.stream(xyStr.split(", ")).mapToLong(s -> Long.parseLong(s.substring(2))).toArray();
            return new Coord(xy[0], xy[1]);
        }
    }

    private static final record Pair(Coord sensore, Coord beacon, long radious) {
        Pair(Coord sensore, Coord beacon) {
            this(sensore, beacon, sensore.distance(beacon));
        }

        Range rangeIn(int y) {
            Coord atCenter = new Coord(sensore().x(), y);
            long distance = atCenter.distance(sensore());
            if (distance >= radious()) {
                return Range.EMPTY;
            } else {
                long dif = radious() - distance;
                return new Range(sensore().x() - dif, sensore().x() + dif);
            }
        }
        
        boolean excludes(Coord c) {
            return sensore().distance(c) <= radious();
        }

        static Pair parse(String desc) {
            var sbStr = desc.split(": ");
            return new Pair(Coord.parse(sbStr[0]), Coord.parse(sbStr[1]));
        }
    }

    private static final record Range(long from, long to) implements Comparable<Range> {
        private static final Comparator<Range> COMPARATOR = Comparator.comparingLong(Range::from)
                .thenComparingLong(Range::to);
        static final Range EMPTY = new Range(0, -1);

        long size() {
            if (to < from) {
                return 0;
            }
            return Math.abs(to - from) + 1;
        }

        @Override
        public int compareTo(Range o) {
            return COMPARATOR.compare(this, o);
        }

        boolean hasIntersection(Range other) {
            return (from() <= other.from() && other.from() <= to()) || (other.from() <= from() && from() <= other.to());
        }

        List<Range> plus(Range other) {
            if (this == EMPTY && other == EMPTY) {
                return List.of();
            } else if (this == EMPTY) {
                return List.of(other);
            } else if (other == EMPTY) {
                return List.of(this);
            }
            if (from() == other.from() && to() == other.to()) {
                return List.of(this);
            }
            if (from() < other.from()) {
                if (other.from() < to()) {
                    if (other.to() < to()) {
                        return List.of(this);
                    } else if (to() == other.to()) {
                        return List.of(this);
                    } else {
                        return List.of(new Range(from(), other.from() - 1), other);
                    }
                } else if (other.from() == to()) {
                    return List.of(new Range(from(), to() - 1), other);
                } else {
                    return List.of(this, other);
                }
            } else if (from() == other.from()) {
                if (other.to() < to()) {
                    return List.of(this);
                } else {
                    return List.of(other);
                }
            } else {
                return other.plus(this);
            }
        }

        static List<Range> plus(List<Range> ranges, Range range) {
            if (ranges.isEmpty()) {
                return List.of(range);
            }
            return distincRanges(ranges.stream().flatMap(r -> r.plus(range).stream()).toList());
        }

        static List<Range> distincRanges(List<Range> ranges) {
            if (ranges.isEmpty()) {
                return ranges;
            }
            var filtered = ranges.stream().filter(r -> r.size() > 0).sorted().toList();
            if (filtered.size() < 2) {
                return filtered;
            }
            boolean shouldCheck = false;
            do {
                shouldCheck = false;
                var collector = new HashSet<Range>();
                for (int i = 0; i < filtered.size() && !shouldCheck; ++i) {
                    var set = new HashSet<Range>();
                    for (int j = i + 1; j < filtered.size() && !shouldCheck; ++j) {
                        var a = filtered.get(i);
                        var b = filtered.get(j);
                        if (a.hasIntersection(b)) {
                            shouldCheck = true;
                            set.addAll(a.plus(b));
                            set.addAll(filtered.subList(j + 1, filtered.size()));
                        }
                    }
                    if (!shouldCheck) {
                        collector.add(filtered.get(i));
                    } else {
                        collector.addAll(set);
                    }
                }
                filtered = collector.stream().sorted().toList();
            } while (shouldCheck);
            return filtered;
        }

        static List<Range> plus(List<Range> left, List<Range> right) {
            if (left.size() > 0) {
                return distincRanges(left.stream().flatMap(r -> plus(right, r).stream()).toList());
            } else if (right.size() > 0) {
                return plus(right, left);
            } else {
                return List.of();
            }
        }
    }

    private static final String INPUT = """
            Sensor at x=1384790, y=3850432: closest beacon is at x=2674241, y=4192888
            Sensor at x=2825953, y=288046: closest beacon is at x=2154954, y=-342775
            Sensor at x=3553843, y=2822363: closest beacon is at x=3444765, y=2347460
            Sensor at x=2495377, y=3130491: closest beacon is at x=2761496, y=2831113
            Sensor at x=1329263, y=1778185: closest beacon is at x=2729595, y=2000000
            Sensor at x=2882039, y=2206085: closest beacon is at x=2729595, y=2000000
            Sensor at x=3903141, y=2510440: closest beacon is at x=4006219, y=3011198
            Sensor at x=3403454, y=3996578: closest beacon is at x=3754119, y=4475047
            Sensor at x=3630476, y=1048796: closest beacon is at x=3444765, y=2347460
            Sensor at x=16252, y=2089672: closest beacon is at x=-276514, y=2995794
            Sensor at x=428672, y=1150723: closest beacon is at x=-281319, y=668868
            Sensor at x=2939101, y=3624676: closest beacon is at x=2674241, y=4192888
            Sensor at x=3166958, y=2890076: closest beacon is at x=2761496, y=2831113
            Sensor at x=3758241, y=3546895: closest beacon is at x=4006219, y=3011198
            Sensor at x=218942, y=3011070: closest beacon is at x=-276514, y=2995794
            Sensor at x=52656, y=3484635: closest beacon is at x=-276514, y=2995794
            Sensor at x=2057106, y=405314: closest beacon is at x=2154954, y=-342775
            Sensor at x=1966905, y=2495701: closest beacon is at x=2761496, y=2831113
            Sensor at x=511976, y=2696731: closest beacon is at x=-276514, y=2995794
            Sensor at x=3094465, y=2478570: closest beacon is at x=3444765, y=2347460
            Sensor at x=806671, y=228252: closest beacon is at x=-281319, y=668868
            Sensor at x=3011731, y=1976307: closest beacon is at x=2729595, y=2000000""";
}
