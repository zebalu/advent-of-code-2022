package io.github.zebalu.aoc2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day24 {
    public static void main(String[] args) {
        List<BlizardMap> blizardSteps = readBlizardMap(INPUT);
        System.out.println(part1(blizardSteps));
        System.out.println(part2(blizardSteps));
    }

    private static int part1(List<BlizardMap> blizardSteps) {
        return findState(blizardSteps, new State(0, blizardSteps.get(0).start), blizardSteps.get(0).exit).minute();
    }

    private static int part2(List<BlizardMap> blizardSteps) {
        var start = new State(0, blizardSteps.get(0).start);
        var out = findState(blizardSteps, start, blizardSteps.get(0).exit);
        var back = findState(blizardSteps, out, blizardSteps.get(0).start);
        var end = findState(blizardSteps, back, blizardSteps.get(0).exit);
        return end.minute();
    }

    private static List<BlizardMap> readBlizardMap(String input) {
        var lines = input.lines().toList();
        BlizardMap bm = new BlizardMap();
        bm.minX = 1;
        bm.minY = 1;
        bm.maxX = lines.get(0).length() - 2;
        bm.maxY = lines.size() - 2;
        bm.start = new Coord(1, 0);
        bm.exit = new Coord(bm.maxX, bm.maxY + 1);
        for (int y = 1; y <= bm.maxY; ++y) {
            String line = lines.get(y);
            for (int x = 1; x <= bm.maxX; ++x) {
                char c = line.charAt(x);
                if (c != '.') {
                    Blizard b = new Blizard(new Coord(x, y), c);
                    bm.blizards.add(b);
                    bm.map.add(b.coord);
                }
            }
        }
        List<BlizardMap> blizardSteps = new ArrayList<>();
        blizardSteps.add(bm);
        return blizardSteps;
    }

    private static State findState(List<BlizardMap> blizardSteps, State initial, Coord end) {
        Queue<State> states = new ArrayDeque<>();
        Set<State> visited = new HashSet<>();
        states.add(initial);
        visited.add(initial);
        State found = null;

        while (found == null && !states.isEmpty()) {
            State next = states.poll();
            BlizardMap blizards = getBlizardAt(next.minute, blizardSteps);
            if (blizards.isValid(next.coord)) {
                if (next.coord.equals(end)) {
                    found = next;
                } else {
                    next.coord.nextCoords().stream().map(n -> new State(next.minute + 1, n)).forEach(n -> {
                        if (!visited.contains(n)) {
                            visited.add(n);
                            states.add(n);
                        }
                    });
                    State stay = new State(next.minute + 1, next.coord);
                    if (!visited.contains(stay)) {
                        visited.add(stay);
                        states.add(stay);
                    }
                }
            }
        }
        return found;
    }

    private static BlizardMap getBlizardAt(int at, List<BlizardMap> blizardSteps) {
        if (at < blizardSteps.size()) {
            return blizardSteps.get(at);
        }
        BlizardMap last = blizardSteps.get(blizardSteps.size() - 1);
        while (blizardSteps.size() <= at) {
            blizardSteps.add(last.next());
        }
        return blizardSteps.get(at);
    }

    private static final record Coord(int x, int y) {
        List<Coord> nextCoords() {
            return List.of(new Coord(x - 1, y), new Coord(x + 1, y), new Coord(x, y + 1), new Coord(x, y - 1));
        }
    }

    private static final record Blizard(Coord coord, char direction) {
        Blizard step(BlizardMap map) {
            Coord next;
            if (direction == 'v') {
                next = new Coord(coord.x, coord.y + 1);
            } else if (direction == '^') {
                next = new Coord(coord.x, coord.y - 1);
            } else if (direction == '>') {
                next = new Coord(coord.x + 1, coord.y);
            } else {
                next = new Coord(coord.x - 1, coord.y);
            }
            if (map.minX <= next.x && map.maxX >= next.x && map.minY <= next.y && map.maxY >= next.y) {
                return new Blizard(next, direction);
            } else {
                if (direction == 'v') {
                    next = new Coord(coord.x, map.minY);
                } else if (direction == '^') {
                    next = new Coord(coord.x, map.maxY);
                } else if (direction == '>') {
                    next = new Coord(map.minX, coord.y);
                } else {
                    next = new Coord(map.maxX, coord.y);
                }
                return new Blizard(next, direction);
            }
        }
    }

    private static final class BlizardMap {
        Set<Coord> map = new HashSet<>();
        List<Blizard> blizards = new ArrayList<>();
        int minX;
        int maxX;
        int minY;
        int maxY;
        Coord start;
        Coord exit;

        BlizardMap next() {
            var nextBs = blizards.stream().map(b -> b.step(this)).toList();
            var coords = nextBs.stream().map(Blizard::coord).distinct().collect(Collectors.toSet());
            BlizardMap bm = new BlizardMap();
            bm.minX = minX;
            bm.minY = minY;
            bm.maxX = maxX;
            bm.maxY = maxY;
            bm.exit = exit;
            bm.start = start;
            bm.blizards = nextBs;
            bm.map = coords;
            return bm;
        }

        boolean isValid(Coord coord) {
            return coord.equals(start) || coord.equals(exit)
                    || !map.contains(coord) && coord.x <= maxX && coord.y <= maxY && coord.x >= minX && coord.y >= minY;
        }
    }

    private static final record State(int minute, Coord coord) {

    }

    private static final String INPUT = """
            #.####################################################################################################
            #<>^<v<<vv^^v>^^<v>>v.<<^.v<v>>^v>v>>vv<.>>>^^v>^.><^>>v<>>>>v^^^.v^v<>^^v><^vv<vv<>v<^v>><<^.<<^>^<>#
            #>^v<^^>.<v<<^.<<<<^v<^<<><<<v^>^>>>^^>v>^^>v>^><^>^>vv^>vv>.^^<>.>>vv<^>.<<vv<vv<v^^><v<vv^^^<<>v<^>#
            #.^<.>v^>.>>>.<>^><vv>^^v^<<^<>v<v^.vv.>>>vv^<.<v<<<<.<.v<^<^v^>.vv>><<..v>>.v><^>v>^>^>^^><.<^<>>vv>#
            #>v^<<^v>vv<<.>>^<<<><>v>vv.<>v^vv<>v^^.<>vvv>^<<<>v^><>v<^.^^^.<v<^><>^v>>.v<<<^v<>.^v<>>vv<^v^<v>><#
            #.<><<>vv>><>.><<.>><>v>v^v^vv>.^^v>>>^v.><>>>>v^v^<v<><^.v>^v>><<^<>^v>..<<>^<v<^v.>^<>><.<^v^v.^><>#
            #><..v^v><<<v<^v^<<<<><<<>.<><v..<>v<<^^v>^^vvv^<v^v.v^<>v^<<><<v<<^vv>.v><<vv.v<.vvv>..>vv^v<<^<^^^>#
            #<>^>^>.v<>>v^^v>^>v^><v>.>><<>vv^^v>>v<v>><^v><<>>>^.^<v<..<<>.^^<.^>.><<<^v^><v^v>v..^^>^v<^.<v<<<>#
            #>^^.v^v<>.>><>vv>.<>><>>><<>v><.v<^^^<>v>^v><^^<^.v.>>>v>v.vv>v>>v^.>.^>v^^<><vvv>>v>^v^<.v>^.>v><^>#
            #<^<<>..<<v<.v^<v<vv^^.<^^.<>>.vvv>>^.vv<><v.vv^>><>^<<>v.<.^v>v>^><>^^<v<><><>^^.vv<v^<>.>^v>^^v<><>#
            #><.><v>^v><>..v>vv<<v<<.v<<<v^^<vv.<>^<><v.^^v.v<^<<v.v^vv>>^.^v<<v.v<v<.<v^vv>^vv<^>>^vvvv>^^>>^^><#
            #<^<>vvv>^.>v>>.><>><v>^.>v<>v^v^<^<^^><>>v<v<vv>^><^>.^v^v<^^v><>>v^^<<<vvv.^^>>>^><<<^v><>^^^.v<<v>#
            #.^^.<v>>v.>^.v<^.^.^vv^<>>.v>^<>v<v^v>>^v><<vv^^.v>^v>vvv^^^<>^<>^>vv>vvv.<>vv>^<.v>.<>.vv<>>.>vvv<>#
            #<^^<.^>>v><><^v<.<.><<v^v>v>>v>v^.<.v<.v.v<.>><^>^v<<^^<^^^.^.>>.v>^><v<^><<^>^.>^v^^>v^v<>^>^<.>.><#
            #<.^v>>>.v<<<^.v.>v^^<^>v<<v<^>^v^v^.^<>><v^v<<v.<vv<>^v>^v.^><^.v>><>vvv^^<<^>.<v<<<v<>^vv.^v.vv<..<#
            #.v^v>>v^vv^v>v.v<v>v>^<<v^v><..v<<>>vv^<<v><^^<<<<^>v>vv^<^v^vv><.^^<v^<^vv^<<>>v^><v.^^^vvv>^>^>^v>#
            #<>.>^vv<>v^<vv<<^<.^^^.v>^><>><^^>v<<^^>^>.^^<.v><>v<v>^^v>>^>>.>>.<>vv><^^<^<<>><^vv>>v>^^.^><.<vv<#
            #<^>^v^<^<><<^vv>><v^.v<>>.v<v<><>^v^^<<><>v>^v^v>^^^vvv>>v^<v^^v.v.v<>>>v>><>><>>^..v<>><^v^>><.>^<<#
            #<v^^>>^^<^>.<^>>>v<<v.<.^>vv^^><<vvv<>vvvv^<v^<^<<<v<>>^>vv<^<.v<<<>><v<<v>^v>>.<v<vv^.>.><v^vv<>^.>#
            #>>v>^<.>.v<v.<<<<<v.>^>>>.>v<v>.vv^>^>v>^<>^.^^<v>^^>><><v^^.^>..^.>v>^<v<^v^.>v.v.^<><<><<v<<<^>>><#
            #<>v>>.v>.^>^<<><^v<.>^vv.>^.<v.>.>v<<^^>^><vvv>>vv<>^v.<^v<^>><<<<^^v^v<<^^v.<>^<v^>vvv>^<v>><<>^>^<#
            #<><<v<<<.v>^><^.v<>^vv>v><vv.v><v<v^v>^>^^<.>^^^^vv<>v<^.vvv.^v^>>^.v>><v^>.>v^v<.><>.>>v>v<>>>v^<^<#
            #<^<<<v..<>^<v^<>v>v<vv<v^<<.v^<<<^><v^^>><<v>^v.>>^.vv>>^v<.vv^v<^v^.^^.<.vv>>v>>^<^<<.>>><<<<vv^vv<#
            #<><>vv>^.vv><^^>^>^v^><v<>v>.<>>^v^<>^^^^v^v>>v<<<^v^>>^v^v<vv>vv>v>.><<^^>^<><><<<^<v>vv<<<^<>>^^v>#
            #<><<<<v>>v><^>vv<^><><>^<<^^>^.^^><.vvv<^<>^v^v^v>><>^<vv><^>>><.v<^^>>^vvv^^^v<.>vv^v>v^<<>v.^<.>v<#
            #.>>vv<.<vvv^>v^^v>v<v<^^v^^^.v^>>..^<<^^>><^<>v>.>>.<^><v>><><>v^<^^^v^>v^v>^<><<^>><^<..v><>>^<<v<>#
            #>v.>v>v^^v.v<v^<<<<>><>.<^v>^^<>^^>v^^>>..v^.v<>v>^v>..<v>><^<<><v<^<<.>^>^^<^v>.<v^^<>v..>v^v^^..><#
            #<v.><^v<><^^<.>><<>^^<v<v.v><><.<.>^.vv><v.<>^vv><v>>.<><>^^>><vv^^vvv>>v<^^v.><<>><>^^<^><<><>>>>^.#
            #><<v<v^v<<>^<>v<^><><<<><<v<>^<v^.v>>^<^>^^v<><<<v.v><<>>><>.^.>v<v<^^.^.>v>v>^<v>^<<>^>.<^<vv^<.v>>#
            #.v<<<.v<^>>^<>>v<>v.><>^^v>v<vvvv<<>v<<^.v<^^v>.v>^v>^..^<<.<..^>^.<v<.<<<.<vv^<v<>^.v^^><>>>^..^vv>#
            #<>^>>^^v^>>>.>v^v<^^<<v^^><^<><v.^^v^v<>v>>.>><vv><<<><^v.v<>v><><v<.v<>^v>>>^^v<<v<v>^>vv^v>^v>^.<<#
            #><v^<><v<>vvvv.>vvvvv^.<<<><v.><^^v<v><<>^^.<^>v<^v>.^><>>v.<^.<<.^^.>vv<<<<^.>^^v<v^....v>>>><v.><<#
            #<^>.<<^^^<<>.<>v^^^v<<^>>vv<^.<>>v.^v<^^.<v>v<>^^v>>v.^>vv<<<<>vvv<<.^>v<v<.>>v<^v^vv^<^.^>v.>^.>^^<#
            #<><^<v.^><><v^<<><.>>^^>>v><^<^^v>>^>>^>^<>^>v<^><.<>v^<^v^<<<v^.>..<<vvv<>..v^>>>v>v<<>..<>>>vvv><<#
            #<>>vv>v.^v>.^<><>.v..<<^v><v^>v^v>^^><.>>><<^>v<v<>^^^^vv.<>v.vv>>^<^.>^<>.<^>^<<^v^^<v<>v>^>vv>^<>.#
            #<v<>><v^^v.^^^<^v>^>>v>v<>^<v.^^><.<.>><^^<.vvv<<<.vv^>^>^<v^v.>^vv<^>^>vvv^^^.v<<<^^<>v<<><vv^<<<>>#
            ####################################################################################################.#""";
}
