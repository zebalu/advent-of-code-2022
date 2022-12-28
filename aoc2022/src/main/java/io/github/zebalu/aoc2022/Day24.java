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
        BlizardMap blizardSteps = readBlizardMap(INPUT);
        System.out.println(part1(blizardSteps));
        System.out.println(part2(blizardSteps));

    }

    private static int part1(BlizardMap blizardMap) {
        return findState(blizardMap, new State(0, blizardMap.start), blizardMap.exit).minute();
    }

    private static int part2(BlizardMap blizardMap) {
        var start = new State(0, blizardMap.start);
        var out = findState(blizardMap, start, blizardMap.exit);
        var back = findState(blizardMap, out, blizardMap.start);
        var end = findState(blizardMap, back, blizardMap.exit);
        return end.minute();
    }

    private static BlizardMap readBlizardMap(String input) {
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
                    bm.addBlizzard(b);
                }
            }
        }
        bm.expand();
        return bm;
    }

    private static State findState(BlizardMap blizards, State initial, Coord end) {
        Queue<State> states = new ArrayDeque<>();
        Set<State> visited = new HashSet<>();
        states.add(initial);
        visited.add(initial);
        State found = null;
        int lastMinute = Integer.MIN_VALUE;

        while (found == null && !states.isEmpty()) {
            State next = states.poll();
            if(lastMinute<next.minute) {
                lastMinute = next.minute;
                visited.clear();
            }
            if (blizards.isValid(next.minute, next.coord)) {
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

    private static final record Coord(int x, int y) {
        List<Coord> nextCoords() {
            return List.of(new Coord(x - 1, y), new Coord(x + 1, y), new Coord(x, y + 1), new Coord(x, y - 1));
        }
        @Override
        public int hashCode() {
            return (x << 16) | y;
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
        List<Set<Coord>> horizontalMaps = new ArrayList<>();
        List<Set<Coord>> verticalMaps = new ArrayList<>();
        List<Blizard> horizontalBlizards = new ArrayList<>();
        List<Blizard> verticalBlizards = new ArrayList<>();
        int minX;
        int maxX;
        int minY;
        int maxY;
        int width;
        int height;
        Coord start;
        Coord exit;
        
        public BlizardMap() {
            horizontalMaps.add(new HashSet<>());
            verticalMaps.add(new HashSet<>());
        }

        boolean isValid(int minute, Coord coord) {
            var vMap = verticalMaps.get(minute%height);
            var hMap = horizontalMaps.get(minute%width);
            return coord.equals(start) || coord.equals(exit)
                    || !vMap.contains(coord) && !hMap.contains(coord) && coord.x <= maxX && coord.y <= maxY && coord.x >= minX && coord.y >= minY;
        }
        
        void addBlizzard(Blizard b) {
            if(b.direction == '<' || b.direction == '>') {
                horizontalBlizards.add(b);
                horizontalMaps.get(0).add(b.coord());
            } else {
                verticalBlizards.add(b);
                verticalMaps.get(0).add(b.coord());
            }
        }
        
        void expand() {
            width = maxX-minX+1;
            height = maxY-minY+1;
            var blizards = horizontalBlizards;
            for(int i=1; i<=width; ++i) {
                var nextHb = blizards.stream().map(b->b.step(this)).toList();
                horizontalMaps.add(nextHb.stream().map(Blizard::coord).collect(Collectors.toSet()));
                blizards = nextHb;
            }
            blizards = verticalBlizards;
            for(int i=1; i<=height; ++i) {
                var nextVb = blizards.stream().map(b->b.step(this)).toList();
                verticalMaps.add(nextVb.stream().map(Blizard::coord).collect(Collectors.toSet()));
                blizards = nextVb;
            }
        }
    }

    private static final record State(int minute, Coord coord) {
        @Override
        public int hashCode() {
            return coord.hashCode();
        }
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
