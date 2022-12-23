package io.github.zebalu.aoc2022;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Day23 {
    private static final Character ELF = Character.valueOf('#');

    public static void main(String[] args) {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static final int part1() {
        var map = readElfMap(INPUT);
        expand(map, 10);
        return countEmptyArea(map);
    }

    private static final int part2() {
        return expand(readElfMap(INPUT), Integer.MAX_VALUE);
    }

    private static Set<Coord> readElfMap(String desc) {
        var map = new HashSet<Coord>();
        var lines = desc.lines().toList();
        for (int y = 0; y < lines.size(); ++y) {
            var line = lines.get(y);
            for (int x = 0; x < line.length(); ++x) {
                var c = line.charAt(x);
                if (c == ELF.charValue()) {
                    map.add(new Coord(x, y));
                }
            }
        }
        return map;
    }

    private static final int expand(Set<Coord> map, int maxRounds) {
        boolean changed = false;
        int round = 0;
        do {
            Map<Coord, List<Coord>> proposals = new ConcurrentHashMap<>();
            final int fRound = round;
            map.parallelStream().forEach(elf -> {
                var hasOccupiedNeighbours = elf.allNeighbors().stream().anyMatch(n -> map.contains(n));
                if (hasOccupiedNeighbours) {
                    boolean proposed = false;
                    var groups = elf.neighbourGroups();
                    for (int i = 0; i < groups.size() && !proposed; ++i) {
                        var neighbourGroup = groups.get((i + fRound) % groups.size());
                        var noneOccupiedInGroup = neighbourGroup.stream().noneMatch(n -> map.contains(n));
                        if (noneOccupiedInGroup) {
                            proposals.computeIfAbsent(neighbourGroup.get(0), k -> Collections.synchronizedList(new ArrayList<>())).add(elf);
                            proposed = true;
                        }
                    }
                }
            });
            proposals.entrySet().stream().filter(p->p.getValue().size()==1).forEach(p->{
                map.remove(p.getValue().get(0));
                map.add(p.getKey());
            });
            ++round;
            changed = proposals.size() > 0 && round < maxRounds;
        } while (changed);
        return round;
    }

    private static final int countEmptyArea(Set<Coord> map) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (var elf : map) {
            if (elf.x < minX) {
                minX = elf.x;
            }
            if (elf.x > maxX) {
                maxX = elf.x;
            }
            if (elf.y < minY) {
                minY = elf.y;
            }
            if (elf.y > maxY) {
                maxY = elf.y;
            }
        }
        int area = (maxX - minX + 1) * (maxY - minY + 1);
        return area - map.size();
    }

    private static final record Coord(int x, int y) implements Comparable<Coord> {
        private static final Comparator<Coord> COMPARATOR = Comparator.comparingInt(Coord::x).thenComparingInt(Coord::y);
        List<List<Coord>> neighbourGroups() {
            return List.of(
                    List.of(new Coord(x, y - 1), new Coord(x + 1, y - 1), new Coord(x - 1, y - 1)),
                    List.of(new Coord(x, y + 1), new Coord(x + 1, y + 1), new Coord(x - 1, y + 1)),
                    List.of(new Coord(x - 1, y), new Coord(x - 1, y - 1), new Coord(x - 1, y + 1)),
                    List.of(new Coord(x + 1, y), new Coord(x + 1, y - 1), new Coord(x + 1, y + 1)));
        }

        Set<Coord> allNeighbors() {
            return Set.of(
                    new Coord(x-1, y-1), new Coord(x, y-1), new Coord(x+1, y-1), 
                    new Coord(x-1, y), new Coord(x+1, y), 
                    new Coord(x-1, y+1), new Coord(x, y+1), new Coord(x+1, y+1));
        }
        @Override
        public int hashCode() {
            return (x << 8) | y;
        }
        @Override
        public int compareTo(Coord o) {
            return COMPARATOR.compare(this, o);
        }
    }

    private static final String INPUT = """
            ###.#.#.#...###.#.#...#######.#.#...###.#.###.#.#......#.###.###.###..
            .#..#..#..#####...#.##..##...#..########.#........#.#.#..#..####..###.
            .####.....#.#.#.#..#..........#..##...#.##.....##..####..##.#..##....#
            ##.#.#.#....#.#..#....#....##.##.#.##..##...#..#.##..##..#...##.##..##
            .#....#...##.#....#.#.#.###.#.##....#.##.#####.#.####..#.#.#.....#.###
            #####..##.#.#..#...#..##.####..#..##...#.####..#..###.#....#.#####....
            ...#...##.#..#.#####..#.#..##..#####.#.#.#.###...###.#.###.#..#.#.#...
            .###.###.##..##....######.#.#..###..#.#..#.#.#.#.#.##..##.###..#.##..#
            .###.###..#...#.#.....#....####..#.........###...#..##.#..#........#.#
            #.#.###.####.##..#..#.#####....#####..#........#....#...##..#...#.....
            .##.##.#..#.####......####.##.#.##...#..#.....##...#....#..##.##.###.#
            .#..#.#.....###..##.####.##.####.##..#.##....##..#.#.....#..#..#.##.#.
            ##.#.#.##..###.#...###...#...##...###.####..#####.#.##.##.#..#...####.
            ..#.#..##..##.##..#.#..##.#...#....#.#..##.###.#..##..#.##.##..#..#.#.
            ###.##...#.##..##.#.....##.###.#####..##.###.#..#.#.####.##..###.#....
            .###..###.#..#.....###.###..##.##.##..#.##.#.##....##....##...#..#.#.#
            .#########.#.#.##.#..##.##.#..#..#.#......###.#..#...##....#.#.#.#..#.
            ..####..#...###.#####.##.#.##.#.##...#######.#.#...#..###.#...#.###...
            ##.#.#...###..##.##.#.####.#.##...###..#....##.######......#.#......#.
            ..##.#.###.###.####....#.#..####.##.#.##.##..#.####....###.##.......#.
            .###.##...##.#.##..##.#.##....###.#.##.##.#..#.....##..##.##..#...##.#
            ..#.##..#...............###.#..####...#.#.##.##.#.##.....#####.##.##.#
            ##.#######.#.#...####.#.#.##..###..#.#.#.....##.##..###.#..#....#...##
            #...##.######..#####..####.########.....#.#.#.#.##..##.#...##.#...##.#
            ..##...##.###.#.#.#..####.#.#...#...##..##....#######...##.#...#.###..
            .##..#..###.##.#.#.###.###..#.##....#.#..##.....#..#.#######...####.#.
            #.....#........#..####.##.#.#.##....#.######.####....####...##..##.##.
            #####...#..###.#.#.##..##.#.#..#####.#######.##....#.#.##...####.#.###
            .....#.##.#.#.#..##..##..#.##.#....#.....#.#.......#..#.##..#....#...#
            #......#####...###.#.##..#.##.#######..####..#..#...#..#..##..##...#..
            ##...#.##...#..#...#.##...###.#..##.#.###.#.##.#...##..#.....###.#####
            #.#.#.#..##..#.#.#.....#.#.#####....#..#....##.##..#.#.#...#..###.#...
            ..#######..#...#.##...#.#..#..#...####.#.###.##......####.#..##.#.###.
            ..#..####.##...###.#.#......##....#.##..#.#..#...##..##.....##.#.##.#.
            ..#..###.#.#...##..###...#.###.#..###.#.##.#.###...#.##..###.#.#.#####
            ..#..#.###.#....##.#...##.######..#########.#...#.#.#.#....#..#.#..###
            #...#.....####.###..###.#...#...##.#...##.###..###.##.#####..#####....
            .#.#.#..#..#..##.#.......#..##...###....#..##..#...#.##.#.............
            .#....###...####..#....#.##.##..#####..#..###.#.#...#..##.###..##.###.
            #..##....###.#.....##...###.#....####.#..##...##.#.##....#...#..##.#..
            #.##...#.#.......####...#######.....#####.#....###.##..#.###....#....#
            #....####.#......##...##...####.##...#.#...#...##.........##..#..###.#
            ...####..####..#.####.#.##.##..##..###....#...##.#.##.##.#...##.###..#
            ..#.#...#.#.#..#..##.#####.#..#..##..#..###.##.#.##....##....##.#.#.##
            ...#.###.#......##.....###.#..##.####.#..#.....####...###.##.##...#.#.
            ####...#.##.#.#..##...#.#.##.##.#.###..#...#..#...#..#####.#.#.##.....
            ##...#.#.###.###.####.##..........#.##.##..#.##.#....#...####....#.###
            .#####.#.##.#.#....##......###..#.....##....#.#.###.#.####...##..##...
            #..#..#.#...#...####..#...####....#####....###.##......###..#.#####..#
            .##.#.#####.......#...##...####..#####..#.##.#...##..#.#####.#..###...
            ...#.##.##......##....##....#.###.###....####..#...##...##.#.###....#.
            #.###.##..####.#.#.#.##....#####.#.....#.#.##..##...#.#####.#...#..##.
            .###..###..#.####.#.###...#.......#.#..#.#.####..##.....##..###.####..
            ##.###.#.##..#.###...#.#.##..#.##.##.#.#.#...#.#..#..##......#.#..##.#
            .##.#.#..##..#....#.#..#.###.....#..####......#.#..#.#.#....##.##.#.#.
            #..##.###...##.#.....#...#......###.#.#..#.#..#.#...##.#.#.###.##.##..
            ##.#.##..###..#.#.#...##........###..######...###..##.##.#.##....#.#..
            ##..#.#.##.#.###..##..#.....#######..#......#.#..#..#####..#.##...#..#
            .##.#.##.##...#..#...#.##.##...#..#..#.#.##.###..#..#...#.....##..##.#
            .####.##.######.#######......#.##..#..#..#.#.####.###.##.#.###.#####.#
            #.#...#.##.##.#.#..###...##..#....#.#.#.....#.#....#.###..#..##.#.#..#
            #.##..##......#..##..#.###.##.#..#...#.#....#....##..###..###.##...##.
            ###.#....#...##.####.#.######..##.#....#.#.#####.####..#.###......#.#.
            ###.#.....#.#...###.#..#........#.##..#...........##..#.#...#.#.#.....
            ##......#.#...##.#.#..#.##.####..##..###...#####.##.#.#.##.#.###..##..
            .#.###.###..###..#..##.###.#....####..#.###.#.##.####.##.##.##.#####..
            .....##...#..#.#.......#.#..#.#.##.#.#..###..##..###.#####.##..#....##
            ..#.####.##....###...##....##.#..######.#.####....##.#...###....#..#.#
            ####...#######.#####..###.###.#...##.#....#..#..#.########.###...#.#.#
            ..###.#..#####..##..#.###....##.#...#..##.#.######.#####.##..####...##""";
}
