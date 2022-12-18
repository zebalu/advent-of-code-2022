package io.github.zebalu.aoc2022;

import java.util.HashMap;
import java.util.Map;

public class Day17 {
    private static final long TRILLION = 1_000_000_000_000L;

    public static void main(String[] args) {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() {
        var corridor = new Data("", 0, 0);
        var ip = 0;
        for (int iter = 0; iter < 2022; ++iter) {
            var rock = new Data(ORDER[(iter % ORDER.length)], 2, corridor.minY() - 4);
            ip = step(corridor, ip, rock);
        }
        return Math.abs(corridor.minY());
    }

    private static long part2() {
        var rows = new HashMap<Pointers, Long>();
        var heights = new HashMap<Pointers, Integer>();
        var corridor = new Data("", 0, 0);
        var ip = 0;
        var calculatedHeight = Long.MIN_VALUE;
        var jumped = false;
        for (var iter = 0L; iter < TRILLION; ++iter) {
            var rock = new Data(ORDER[(int) (iter % ORDER.length)], 2, corridor.minY() - 4);
            ip = step(corridor, ip, rock);
            if (!jumped && corridor.topRow().equals("#######")) {
                var pointers = new Pointers((int) (iter % ORDER.length), ip % INPUT.length());
                if (!jumped && rows.containsKey(pointers)) {
                    var heightRepeat = -corridor.minY() - heights.get(pointers);
                    var repeatLength = iter - rows.get(pointers);
                    var skippSize = (TRILLION - iter) / repeatLength;
                    calculatedHeight = skippSize * heightRepeat;
                    iter += skippSize * repeatLength;
                    jumped = true;
                } else {
                    rows.put(pointers, iter);
                    heights.put(pointers, -corridor.minY());
                }
            }
        }
        return Math.abs(corridor.minY()) + calculatedHeight;
    }

    private static int step(Data corridor, int windPointer, Data rock) {
        var ip = windPointer;
        var valid = true;
        while (valid) {
            char push = INPUT.charAt(ip++ % INPUT.length());
            int xTrans = 0;
            if (push == '>') {
                xTrans = 1;
            } else {
                xTrans = -1;
            }
            rock.xTrans += xTrans;
            var pushed = rock.transformD();
            if (!corridor.isValid(pushed)) {
                rock.xTrans -= xTrans;
            }
            rock.yTrans += 1;
            var fallen = rock.transformD();
            if (!corridor.isValid(fallen)) {
                rock.yTrans -= 1;
                valid = false;
                corridor.merge(rock.transformD());
            }
        }
        return ip;
    }

    private static final record Pointers(int piece, int wind) {

    }

    private static final class Data {
        Map<Coord, Character> matrix = new HashMap<>();
        int xTrans = 0;
        int yTrans = 0;
        int minY = 0;
        int maxY = 0;

        public Data(String desc, int x, int y) {
            var lines = desc.lines().toList();
            for (var i = 0; i < lines.size(); ++i) {
                String line = lines.get(i);
                for (var j = 0; j < line.length(); ++j) {
                    if (line.charAt(j) == '#') {
                        matrix.put(new Coord(x + j, y + i - lines.size() + 1), '#');
                    }
                }
            }
            minY = findMinY();
            maxY = findMaxY();
        }

        Data(Map<Coord, Character> matrix) {
            this.matrix = matrix;
            this.minY = findMinY();
            this.maxY = findMaxY();
        }

        int minX() {
            return matrix.keySet().stream().mapToInt(Coord::x).min().orElseThrow();
        }

        int maxX() {
            return matrix.keySet().stream().mapToInt(Coord::x).max().orElseThrow();
        }

        int minY() {
            return minY;
        }

        int maxY() {
            return maxY;
        }

        Map<Coord, Character> transform() {
            var result = new HashMap<Coord, Character>();
            for (var e : matrix.entrySet()) {
                result.put(new Coord(e.getKey().x() + xTrans, e.getKey().y() + yTrans), e.getValue());
            }
            return result;
        }

        Data transformD() {
            return new Data(transform());
        }

        void merge(Data d) {
            matrix.putAll(d.matrix);
            minY = Math.min(minY, d.minY);
        }

        boolean isValid(Data unit) {
            if (unit.minX() < 0 || 6 < unit.maxX()) {
                return false;
            }
            if (unit.maxY() == 0) {
                return false;
            }
            for (var k : unit.matrix.keySet()) {
                if (matrix.containsKey(k)) {
                    return false;
                }
            }
            return true;
        }

        String topRow() {
            var sb = new StringBuilder();
            for (int x = 0; x < 7; ++x) {
                var c = matrix.get(new Coord(x, minY));
                sb.append(c == null ? ' ' : c);
            }
            return sb.toString();
        }

        private int findMinY() {
            return matrix.keySet().stream().mapToInt(Coord::y).min().orElse(0);
        }

        private int findMaxY() {
            return matrix.keySet().stream().mapToInt(Coord::y).max().orElse(0);
        }
    }

    private static final record Coord(int x, int y) {

    }

    private static final String LINE = "####";
    private static final String CROSS = """
            .#.
            ###
            .#.""";
    private static final String EL = """
            ..#
            ..#
            ###""";
    private static final String ROD = """
            #
            #
            #
            #""";
    private static final String BLOCK = """
            ##
            ##""";
    private static final String[] ORDER = new String[] { LINE, CROSS, EL, ROD, BLOCK };
    private static final String INPUT = ">>>><<>><<>>><>>>><<<<>>>><<<<>>><<<>><<<<>><<<<>><<<>><>>><<<>><<<<>>><<>><<<>>>><<<<>>><<<>><<<>><<>><<<<>>><<<<><<><<<>>><<<<><<>>>><<>>>><<<<><<<<><<<<>>><<<>><<>>><><<<>>>><<>><<>><<<<>><<>><<><<>>>><<>>><<>>>><<<<>>><>>>><<<>>><<<<>>><>>><<>>>><<<<>><<<><<<<>>>><>><<<<>><<<><>><><<<><>><<<<>>>><<>>>><<>><<<>>>><<<>><<>><<>>>><>>><<<>><<>>><>>>><<><>>>><<<>>><>>>><>>><<<<><<<<><>>>><><<>>>><<<<><<<<><<>><<<>>><<<><<>>><<<><<<>>>><>>>><<<<>>>><>><<>>>><<<>><<<>>><><<<>><<>>>><<<<>>>><<><<><<>>><><<>>><>>>><<><<<><<<>>><<<>><<<>>>><<>>><<<><>>>><<<>>>><<<>><<<<>><>>>><<<<><<><<<<>>><>>>><>><<<<>><<>>>><<<<>>><<<><<<<>>>><<<><<<<>>><><<>>>><<<<>>><<>><><<>>>><<<>>>><<<><>>><<<><<<<>><<<<><<<<>><<<<>>><<>>><<>>>><<<<>><<<<><>>>><<<<>><<<>><<<><<><<<<>>>><<<<>>><><<<>>><><<<>>>><<<>><<<>>>><<<>><><>><<<>><>>>><>>><<><<>><<>>><<<<>><<<>>>><<<>>><<<<>>><>>><><>>>><<<>>>><<<>>>><<<>>>><<<>>>><<><<>>>><<<>><>>>><<<>>>><<>>><<>>><>>>><<>><<<>><<<>>>><><<<<><>>><<><<<>>>><<>>>><><<><<>>><<<<><<<<>><>>>><><<<>>>><<<>>>><><<><<>><><<>>>><<<<>>><>>>><<<><<>><>><<>>><<<<>>><>>>><<<<>><<><<<<>><<>>><<<>><<><>>><<<<>>>><<>>>><<<<>>><<<>>><>><<<<><<>>><>>>><<<<><<<>>>><<<>>>><<>>><>><<<<>><<<<>>>><<<>><<<<><><<<<><<<><<<<>><>>><<<<>><<>>>><<>><<<><<<<>><<><<>>><<<<>>><<<>>>><<>>><>><<<>><<<<>>>><<>>><<>>>><<>><><<>><<<>>>><<<>>>><<>><<<><<<>>>><<<<>><<<>>>><<<>>>><>><<<>><<>>><<><<<<>>>><<<>>>><<<<>><>>>><<>>><<<<><>>>><<<<><>>>><<<>>><<<><>><<<<>>><<<>>><<><<<><<>><<<<>>>><<<<><<<><<<<>><<<<>><><<><<<<>><>><>>><<<<>>>><<><<<>>><<>>>><<>>>><<<<><<>>>><<>>><>>><>>>><<<>>><>>>><<<<><<<<>>>><<<<>>><<>>><<<<><<<><<>>><<><<>><<<>><<<>><<<><<<<>>><<>>>><<<>>>><<>>>><>>>><<<<>>>><<>>>><<<<>><<>>>><<>><><<>>>><>>>><<>>><<>>>><<>>><<><<<>><<>><<<><<<><><>>>><><>>><<<>>><<>><<>><>>><>><>><<>>>><<<<>><><>>><<<<>>><>><<>>><<<>><<>>>><<<<>>><<<><<>>><>>>><<<><<<>><><<<>>><<<>>><>><<<<>>><<<<>><>><><>>><<>><><<>>><><><>>>><>>>><<>><<<><<<>><<<<>><>>><<<<>><<<>><>>>><<<<>>><<<<><<><>><<<><<<>>>><<<<>>><>>>><>>>><<<<><<><>>><<<<>><<<>><>><<<>>>><<<<>>>><<<>>>><<>><<<>><>>><<><<<<>><<<<><>>>><<>><<<>>>><<<<>>>><<><><><><<<<>><<<>>>><<>>>><<>>>><>>>><>>><>>><<<<>><<<>><>>><><>><<<<>>>><<<><<<<>><><<><><<<>>><<<>><<<>>><><<<<>><<<<><>><><<<><<<>>><><<><<<<><<>><<<>>><<<>><<>>><<<<>>>><>>><<<>><<><>>><<<>>>><<<<>><<<>>><<<<>>>><<>>><<<>>><<><<<<>>>><>>>><<>><<<>><<<<><<<>>>><<>>>><<>><<>><<<>>><><<<<>>>><<<<>>>><><<>><<>>><<<<>>><<><<><<><>>><<>>><<<>>><<<>>>><>>><>><<>>>><>>><>><<><<<<>><<<><<<>>>><<>>><<>>><><<<>><<<>>><>><<><<>>><<<>><<>>><><><<><<<>>><<<<>>>><>>>><<<<>><>>>><<<>>><<>>>><<>>>><>>><<<<>><<<<>><<<<>>>><<<<>>>><<<<>><<<<>><<<>>>><><<<>>><<>>><<<>>><<<<>>><<<<>>><<<<><<>>>><<>>><><<<><<<><><>><>><<>>>><<<<>>><>><<<>><<<<>>><<<><<<>><<<<>>>><<<<>><<<<><<<>><<><>>><<<>>><><<>>>><<>><<<>>>><<<>>>><<>>>><<<>>>><<<<>><<><<<<>>><<<<>>>><<<>>>><>>><<<<>>>><<<<>>><<>>>><<<>>>><>>><<><<<<>>>><<><<<>>><<>><<<><<><<<<>>>><<<<><<<>><>><<<>>><>>>><<<<>>><<><<<>>><<>>>><>>><<>>>><>><>><<<<>>>><<<<>>><<<><<<><<<<>>>><<><<<<>>>><<<><<>><<<<>>>><<<<><<<<>><<>>>><<<>><<><<<>><<<>><<<><>><<>><<<>>>><<>>><>>>><<<<>>>><<>>><<<>>><<<<><<>>>><<<<>>>><>><<>><<><<<>>><<>>>><<<><<<<>><<<>>>><<<<>><><<>>><<><<><>>><<<><<>><<<>>><<>>><<<>>>><>>><<<><>>>><>>>><<<>><>>>><<<<><<<>><<>>>><<<>>>><>><<><<<<>>><<<>><<<>><><<>><<><<<<>><>><<<<>>>><<>>>><<<<>>><<<>><>>>><>>><<<<>>><<<>><<<<>><<>>><<<<><<<<><>>><>>>><>><<>><<<>>>><<<<>><<<<><<<<>>>><>>><<>><>><<<<>>><<<>>><<<><>>>><<<><<<><<>><<<<>>>><<<<><<<<>>><><<<<>><<<<>>><<<<><<><<<><>>><<<<>>>><<<>>><<<<>>>><<<<>><<<>><<<<>><<<><<<<>>>><<<<><<><<<<>>>><<>>><<>><<><>>>><<<>>><>><<<><<<<>>>><>>>><<<>>><<<<>>>><>>>><<<<>>>><<<<>><<<<><<><<><><<<<>>><<<<>>>><<<<>><<<>>><>>><<<>>>><<<<>><<<<>>>><<<<><<>>><><<<<>><<><<<>>>><><<<<>><<<<>>><<<>><<>>>><<<><<<<>>><<>>><<>>><>><<>>>><<>>><<<>>><<<<>>><<<<><<>>><<<><<<>>>><<>><<<>>><<<<>>>><<<>>><<<>>><<<><<<<><>>>><>>><<>>><>>>><<<<><<<><<<<>>>><<<<>>><>>>><>><<<<>>><<<<>><<<>><<>><<<<>>><<<<>>><<<<>><<><><<>><<<<>><<<><>>>><>><<>>><><<>><>>>><><<<>>><<<><<<<>>><<><<<>>><<><>>><<<<>><<<>>>><<<>>>><<<>>><<<<>><<<<>>><<<<><<<><<>><>>><<<><>>><>>>><><<>>><>>>><<<<>>><<<<>>><<<>><>><<<>>>><<<><<<>>>><<<<>>><<><<>>><<>>>><>>>><<<<>>><<>>>><<>><<<<>>><<<<><<<><><<<>>><<<<><<<<>>><<<>><<><>>><<<>><<><<<>>>><<<<><<<>>>><<<>>><<><>>>><>><<<>><<<<>>><<<<><<<<>>><<>>>><<><>>><<<>>>><><><<<<>>><<<>>><>>><<<><<<>>><<<><><<<<>>>><>><>>><<<<>><><<>>>><<<>><<<>>>><<<<>><<<><<>>>><>>>><<<>><<<<>><<<<>><>><<<>>>><><<><<><<<><>>><>><<><<>><<<><<<<><<<<>>>><>>>><<<<>>>><<<>><<<>><<>>>><<<>>><<>><<<>>><<>>><<>>>><<<>><<<>>><<<<><<>><>><>><<>>><<><>><<<>>>><<><<>>><<<<><<><<<>>><<<>>><<<>><<<>>>><<>>>><<<<><<>><<<>>><>>><<>><<<><<<><<>>>><>>><<<>><<<<><>><<<<>>>><<<>><<>><<><<>>><><<><>><<<<>>><<>>>><>>><<<>><>><<<><<><<<<>><<<<>><<>>>><<<>>><<<<>>><<<>>><<<><<<<><<<<><<<<>>>><<>>>><<<<>><>>><<<>>><<<<>>><>><<<<>>><<<<>>><><<<<>>>><<<><<<>><<>><>><<><>>>><<<>>>><<<<>><<<>>>><<>>>><<<<><<><<>><<<<>>>><<<<>>>><<<<>>><<<<><<<><>>>><<<<>>>><><<<<>>>><><<<><<>>>><>>><<<<>><>>>><>>>><<<<>>>><<<>><<<>>><<>>>><<<>><>>><<<>><<<<>>>><<<><<>>><<<>>><<>>>><>>><>>>><<<<>>><>><><>>>><><>>>><<>>><><<<<>>>><>><<>>><<<>><<>>>><>><<>><<<>><<<<>>><<<<>>><<<<>>><><<<<>><<><<>><>><<<<>><<><>>><<<>>>><>><<<><<<>><<<<><>>><<<>>><<<<>><<><<<<>>><>>><>>><<>>><<<<>>><<<><<<>>><<<><<<>>>><>>>><<>>>><><<>>>><<>>>><<<<>>>><>>><<<<>><<>>><<><<<<><<<>>><>>>><<<>><<><>><<<>>>><>>>><<<><<>>>><<<<>><<>>>><>>>><<<>>><<<<><<<><<<<><<<<><<<<>><<<>>><<<>><<>>><>>><>>>><<<<>>><<<<>><<<<>><<>><>><<<<><><<><<<>>>><<<><>><<<<>><<<<>>>><<<<>>>><<>>>><<<<>>>><<<<><<<<>><<<>>><<>>>><>>><<<>>><<<>>><<>>>><<>>>><<>><<<>><<>><<<>><<>>><<>>>><>><>><<<<>>><<>><>><<>>>><>>>><<>><<<<>><<<><<>><<<<>>><<<<><<<>>>><<<<><<<>>>><<<>>><<<<>><<<><<<<><<<<>>>><>>>><<>>>><<<>>>><<<<><<><>>>><>><>>><<<>><>>>><>><><<>>><><<>><><<<>><<<>>>><<<>><<<>>>><<<>>>><>><>><<<>>>><><<<<>><<><>><<>>>><<<>>><<<<><<>>><<<<>>>><<>>>><>>>><<>>>><<<>>>><>>>><>><<<<>>><<<<>>><<<<>><<<<>><<<>>><<<>>>><>><>>><<<>><>>><<<><<>>><<>><><<<<>>><><<>>><<><>>><>>><<<<>><<><>>>><<>>><<>>><<<>><<<<>><<>>><<<>>>><<<<>>>><<>>><<>><<<<><<<>>>><<>>><>>><<<>>>><<<>><<<><>>><>>><<<><<>>>><<<<>>><<<><<<>>><<<<><<<>>><<<<>>><<<<>><<<<>><<>>><<<>>>><<<<>>>><<>><<<<>><>><<>><<<>>>><<><><<>><>><<><<>>>><<<><<<<>>><<<><>><<>>><<<<>><<<>><<<<>>><>><><<<>>><><<<<>>>><<<><<<<>>><<<<>><<<><><<<<>>><<>><>>>><<>>><<<<><<>><>><<>>><<><<<<>>>><<><<<>>><<>>>><<<>>>><>>><<<>>><<>><<<<>><>>><<><<<><>>>><>>>><<<<>>>><<<>><<<<>>><<<>>>><<<<><<>>><<<<>><<<><><>>>><<><<>>>><>>>><<><<<>>><<<<><<<<>>><>>><<<>><<>>><>>><<<>>><<<<>>>><<<><>><<<>>><<>>>><>>>><>>>><<<<>>><>><<>>>><>>><<<>>>><><<>>>><<<><<<<>><><<<<>>><><<>><>>>><<>><<<<>>><>><>><>>><<>>><<>>>><><>><<>>>><<<><<<>>><>>><<<<>><<>>>><<<<>>>><<<<>><<<<>>><<>><<>>>><>>>><>>>><><<<><<<<>>><><<><>>>><<<<>><<>><<<>><<<<>>>><<<><<<<>>>><<<>>><<<<>>><>>>><<<<>><<>>><>>><<>>>><<<<>><<<><<<>>>><><<<><<>><<<>>>><<>><<<<>>>><>><<<<>>><<>>><><>>><>><<<>><>><>>>><<<<>><<<>>><<<><<<<>>><<<>>>><<>>>><<><<<<>>><<<<><<<<>><>><<>>><<<<>>><<<>>><<<>>>><<<<>>>><<<>>><<<><<<<><<<>>>><<<<>>>><<<<>>><>>><<<>>><<<<>><<>><<<>><<<>>><<<<>>><<><<<>><<<>>>><<<<>>>><<<<>><<>>>><<<<>>><<>>>><<<<>>><<<<>>>><>>><<<<>>>><<<>>>><<><<<>><<>>><><>>><<><>>>><<<<><<><<<<>>>><<>>>><>>>><<<<><<>>>><<<<><<<<>>><<<>>>><<>>>><>>><><<<<><<<<>>>><>>>><<><<<><>>>><><><<>>>><>>>><>>>><><<<>><<<>>>><<>><<>>>><<><<><<<>>><<<>>>><<>><<<>>>><>>><<<><<<<>>><>>><<<<>>><<><<>>>><<<>><<><<<>>>><<<<>><<<<>>>><<><<><<<>>>><>><<<>><<>>>><<<<>>><<<>>><<<>>><>>><><<<>>><<<>>><<<>><<<<>>>><><<<>>>><<>>><<>>>><>>>><>><>>><>>>><<<><<<<>>><<>><<><<<>>>><<<>>><<><<<>>><<>>>><>><><<>>>><><>><>>>><<>><<<<>><<>>>><>>>><><>>>><<<<>>>><<<<>>>><<>>><<<>><<<<>>><>>><<<<>><<>>><<<<><<<>><<<<>>><<<>>><>><>>><<<<>>>><<<<>><<<>>>><>>><<>>><>>>><<<>><<<>>>><><<<>>>><<<>>>><<<<>>>><<><<><<<>><>>><<<<>>>><<<<>>>><<>>>><<<><<>><>>>><<<<>>><>>>><<<<>><<<>><<>>><<>>>><<>>><><<<<>>><<>>>><<<><<><<>><>><<<>>>><>><<<>>><><>>>><>>><>>>><<<<><<<<>>>><<<<><<<<><<<>>><<<<><<<<>>><<<<>>><<<>>>><<<<>>>><<>>><<>>><<<><<>>><<<<>>><>><<<>>>><<<>><<<<>><<>>><>>><<<>><<<>>>><><<<><<><<>><>>>><<<>><<<><<>>><<<>>>><><<<>><<<>>><<>>><>>><<<<><<<><>>><<<>>>><>><<<<>>>><>>>><><<>>><>><>><<<<><<<>>><<<><<<>>><<<>>><<<<><<>>><<<><<<><<<<><>>><<>>>><<>>>><<>>><<<>>>><<><>><<<><<<<><<>>>><<<<>>>><>>>><<<>><>>>><<<><<>><><<<<><><<<<><>>><<>>>><<<>><>>>><<><><<<<>><<<>>>><<>>>><<<<>><<<<>><<<>><<<><<<<><<><<<>>><>>><<>><<<>><<<>>><>>>><><><<<<>><<><>><<<<>>>><<<<><<<<>>><>>><<<>>><<<><<<><<>><>><<<>><<><<<<>>>><<<>>>><<<>>>><<>>><<><>>><><>>><<<<><<<><<<>>>><<<<>>>><<<<>>>><>><<<><><><<<>>><<<><<<>><<>>>><<<><<<<>>><><<<>>>><>><<<<>>><<<<>>><>>>><<<>>><<<<>><<>><<<<>><<<><>>><<<<>><>><<<><<>><<<>>><<<<>>>><<>><<<>>>><<>><<<><<<<>>><<>>>><><>>>><<>>>><<<<>><>><<>>><<>>><<><<>>>><<<><<<>><><<<>>><<<>>>><<>><>>>><<>><<<<>>><<<><<<><<<><<<>>><<<>><<<>><><><>><<>>>><<<<>>>><<>><<>><<<><<<<>>>><<<<><>><>><>><<<><<<>>>><>><>>>><><<<<><><<<<><<>><<>>>><<<<>>><<<>>><<<<>><<<<><>>>><<>>><<<>>>><>>><<>>><<<>>><<<>><<>>>><<<><<<<>><>><><<<<>>><<>>><<>>><<><<<><<<<>><>><<>><><<<><<><<<<>>><<<<>><<<><<<><<>>>><<><>>><<>>><>>><<>><<>><<<<>>><<<<>>>><>>><>><<<<>>>><<<>><<<<>><<<><<><<<<><<><<<<><>>><<<>>>><<<<><<<>>><>>>><<<<>>>><<<<><<<><><>>>><><<<<>>>><>><>>><>>>><>>><<>><<>>>><<<>>>><<>><><<<><<<><<<><>>>><<<>>>><<<<>>>><>>>><>>>><<>>><<<>>>><<<><<<<>><<<<>>>><<<>>>><<<>><<<><<<><<>>>><<<<><><<<>><<<><>>><<<>><>><<<<>>>><>><<>><<><>><<<>>>><>>><<<>>><<<<>>>><>>>><<>><>>>><>>>><<<>><>>><>>><<>>><<<<><<>>><<<<><<<<><<><<<<>>>><>>>><<<<>>>><<<>>>><<<<>>>><>>>><<<>>>><<<>>><>>>><<<<><<<<>><<<><<>>><<<<><<>>><<>>>><<<>>><<<>>><<>>><>>><<>><<>>>><<<<><<<<>>>><<<>><<<<>>>><>>><<<>>><>>><<>>>><<<>>><<<>><<>><<<>>>><>><<><<>>><>><>>><<<<>>>><<<<>><>><<>><>>><<<<>>><<<<>>>><><<<>>>><<<<>>><<<<>><>>><<>><>><>><<>>><<>>><<<>>>><><<<<>>>><<<>><<<<>>><<<<><><<<<><<>><<<<>><>><<<<>><<>><>>><<>><<>>>><<<>><<<>>>><<<>>><<<>>><<>>><<<<><<>>><<>><<<<>>>><<<<>>>><<<<>>>><<";
}
