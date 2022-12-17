package io.github.zebalu.aoc2022;

import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

public class Day15 {
    public static void main(String[] args) {
        var pairs = INPUT.lines().map(Pair::parse).toList();
        System.out.println(part1(pairs));
        System.out.println(part2(pairs));
    }

    private static long part1(List<Pair> pairs) {
        var startPair = pairs.stream().filter(p -> p.beacon().y() == 2_000_000).findAny().orElseThrow();
        long distance = 1;
        boolean changed = true;
        var count = 0L;
        while (changed) {
            changed = false;
            Coord left = new Coord(startPair.beacon().x() - distance, 2_000_000);
            Coord right = new Coord(startPair.beacon().x() + distance, 2_000_000);
            if (isInRadiousOfAny(pairs, left)) {
                changed = true;
                ++count;
            }
            if (isInRadiousOfAny(pairs, right)) {
                changed = true;
                ++count;
            }
            ++distance;
        }
        return count;
    }

    private static long part2(List<Pair> pairs) {
        var c = find(pairs);
        return 4000000 * c.x() + c.y();
    }

    private static Coord find(List<Pair> pairs) {
        var found = pairs.stream()
                .map(p -> LongStream.rangeClosed(0, p.radious() + 1)
                        .mapToObj(d -> List.of(new Coord(p.sensore().x() - p.radious() - 1 + d, p.sensore().y() + d),
                                new Coord(p.sensore().x() - p.radious() - 1 + d, p.sensore().y() - d),
                                new Coord(p.sensore().x() + p.radious() + 1 - d, p.sensore().y() + d),
                                new Coord(p.sensore().x() + p.radious() + 1 - d, p.sensore().y() - d)))
                        .flatMap(l -> l.stream()))
                .flatMap(l -> l).filter(c -> 0 <= c.x() && 0 <= c.y() && c.x() <= 4_000_000 && c.y() <= 4_000_000
                        && !isInRadiousOfAny(pairs, c))
                .findAny();
        return found.orElseThrow();
    }

    private static boolean isInRadiousOfAny(List<Pair> pairs, Coord point) {
        return pairs.stream().filter(p -> p.sensore().distance(point) <= p.radious()).findAny().isPresent();
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

        static Pair parse(String desc) {
            var sbStr = desc.split(": ");
            return new Pair(Coord.parse(sbStr[0]), Coord.parse(sbStr[1]));
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