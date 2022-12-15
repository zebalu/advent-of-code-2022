package io.github.zebalu.aoc2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

public class Day15 {
    public static void main(String[] args) {
        var coords = INPUT.lines().map(Pair::parse).toList();
        coords.forEach(System.out::println);
        long xMin = Integer.MAX_VALUE;
        long xMax = Integer.MIN_VALUE;
        for(var p: coords) {
            if(p.beacon().x()<xMin) {
                xMin=p.beacon().x();
            }
            if(xMax<p.beacon().x()) {
                xMax=p.beacon().x();
            }
            if(p.sensore().x()<xMin) {
                xMin=p.sensore().x();
            }
            if(xMax<p.sensore().x()) {
                xMax=p.sensore().x();
            }
        }
        System.out.println(xMin);
        System.out.println(xMax);
        Set<Coord> beacons = new HashSet<>(coords.stream().map(Pair::beacon).toList());
        Pair startPair = coords.stream().filter(p->p.beacon().y()==2_000_000).findAny().orElseThrow();
        long distance = 1;
        boolean changed = true;
        var count = 0;
        while(changed) {
            changed = false;
            Coord left =new Coord(startPair.beacon().x()-distance, 2_000_000);
            Coord right = new Coord(startPair.beacon().x()+distance, 2_000_000);
            if(isInRadiousOfAny(coords, left)) {
                //System.out.println("left");
                changed = true;
                ++count;
            }
            if(isInRadiousOfAny(coords, right)) {
                //System.out.println("right");
                changed = true;
                ++count;
            }
            ++distance;
            if(distance%1_000_000 == 0) {
            System.out.println("distance: "+distance+"\t"+count);
            }
        }
        System.out.println(count);
        var c = find2(coords);
        System.out.println(4000000*c.x()+c.y());
    }
    
    private static Coord find(List<Pair> pairs) {
        for(int x=0; x<=4_000_000; ++x) {
            for(int y=0; y<=4_000_000; ++y) {
                if(!isInRadiousOfAny(pairs, new Coord(x,y))) {
                    return new Coord(x,y);
                }
            }
        }
        throw new NoSuchElementException();
    }
    
    private static Coord find2(List<Pair> pairs) {
        for(var p:pairs) {
            long r = p.radious();
            long req = r+1;
            for(int d=0; d<=req;++d) {
                var c = new Coord(p.sensore().x()-req+d, p.sensore().y()+d);
                if( 0<=c.x() && 0<=c.y() && c.x()<=4_000_000 && c.y()<=4_000_000 && !isInRadiousOfAny(pairs,c)) {
                    return c;
                }
                c = new Coord(p.sensore().x()-req+d, p.sensore().y()-d);
                if( 0<=c.x() && 0<=c.y() && c.x()<=4_000_000 && c.y()<=4_000_000 && !isInRadiousOfAny(pairs,c)) {
                    return c;
                }
                c = new Coord(p.sensore().x()+req-d, p.sensore().y()+d);
                if( 0<=c.x() && 0<=c.y() && c.x()<=4_000_000 && c.y()<=4_000_000 && !isInRadiousOfAny(pairs,c)) {
                    return c;
                }
                c = new Coord(p.sensore().x()+req-d, p.sensore().y()-d);
                if( 0<=c.x() && 0<=c.y() && c.x()<=4_000_000 && c.y()<=4_000_000 && !isInRadiousOfAny(pairs,c)) {
                    return c;
                }
            }
        }
        throw new NoSuchElementException();
    }
    
    private static boolean isInRadiousOfAny(List<Pair> pairs, Coord point) {
        /*
        if(startPair.sensore().distance(point)<startPair.radious()) {
            return true;
        } else {
            return false;
        }
        */
       return pairs.stream().filter(p->p.sensore().distance(point)<=p.radious()).findAny().isPresent();
    }

    private static Set<Coord> collectCircle(Coord center, long radious) {
        Set<Coord> result = new HashSet<>();
        Queue<Coord> queue = new ArrayDeque<>();
        queue.add(center);
        result.add(center);
        for(int i=0; i<radious; ++i) {
            List<Coord> newValues = new ArrayList<>();
            queue.forEach(c->{
                c.next().forEach(nc->{
                   if(result.add(nc)) {
                       newValues.add(nc);
                   }
                });
            });
            queue = new ArrayDeque<>(newValues);
        }
        return result;
    }

    private static final record Coord(long x, long y) {
        List<Coord> next() {
            return List.of(new Coord(x, y + 1), new Coord(x + 1, y), new Coord(x, y - 1), new Coord(x - 1, y));
        }
        
        long distance(Coord other) {
            return Math.abs(x-other.x) + Math.abs(y-other.y);
        }

        static Coord parse(String desc) {
            var xyStr = desc.split(" at ")[1];
            var xy = Arrays.stream(xyStr.split(", ")).mapToLong(s -> Long.parseLong(s.substring(2))).toArray();
            return new Coord(xy[0], xy[1]);
        }
    }

    private static final record Pair(Coord sensore, Coord beacon) {
        long radious() {
            return sensore.distance(beacon);
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
