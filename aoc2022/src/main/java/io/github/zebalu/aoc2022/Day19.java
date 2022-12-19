package io.github.zebalu.aoc2022;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day19 {
    public static void main(String[] args) {
        // 434 is too low
        // 444 is too low
        // 499 is too low
        var blueprints = INPUT.lines().map(Blueprint::parse).toList();
        System.out.println(part1(blueprints));
        System.out.println(part2(blueprints));
    }

    private static int part1(List<Blueprint> blueprints) {
        int sum = 0;
        for (int i = 0; i < blueprints.size(); ++i) {
            var bp = blueprints.get(i);
            int max = maximumGeode(bp, 24);
            sum += bp.id * max;
        }
        return sum;
    }

    private static long part2(List<Blueprint> blueprints) {
        long sum = 1;
        for (int i = 0; i < 3; ++i) {
            var bp = blueprints.get(i);
            int max = maximumGeode(bp, 32);
            sum *= max;
        }
        return sum;
    }

    private static int maximumGeode(Blueprint blueprint, int maxStep) {
        Queue<State> states = new ArrayDeque<>();
        states.add(new State(0, 0, 1, 0, 0, 0, 0, 0, 0));
        int maxOre = IntStream
                .of(blueprint.oreCost, blueprint.clayCost, blueprint.geodOreCost, blueprint.obisidanOreCost).max()
                .orElseThrow();
        int minOre = IntStream
                .of(blueprint.oreCost, blueprint.clayCost, blueprint.geodOreCost, blueprint.obisidanOreCost).min()
                .orElseThrow();
        Set<State> visited = new HashSet<>();
        int maxGeod = Integer.MIN_VALUE;
        while (!states.isEmpty()) {
            var top = states.poll();
            if (top.minute <= maxStep) {
                if (top.geodStore > maxGeod) {
                    maxGeod = top.geodStore;
                }
                if (canBuildGeodRobot(blueprint, top)) {
                    var s = new State(top.minute + 1, top.oreStore + top.oreRobot - blueprint.geodOreCost, top.oreRobot,
                            top.clayStore + top.clayRobot, top.clayRobot,
                            top.obisidianStore + top.obsidianRobot - blueprint.geodeObisidanCost, top.obsidianRobot,
                            top.geodStore + top.geodRobot, top.geodRobot + 1);
                    if (!visited.contains(s)) {
                        states.add(s);
                        visited.add(s);
                    }
                } else {
                    if (canBuildObsidianRobot(blueprint, top) && top.obsidianRobot < blueprint.geodeObisidanCost) { // &&
                                                                                                                    // wothBuildingObsidinaRobot(blueprint,
                                                                                                                    // top))
                                                                                                                    // {
                        var s = new State(top.minute + 1, top.oreStore + top.oreRobot - blueprint.obisidanOreCost,
                                top.oreRobot, top.clayStore + top.clayRobot - blueprint.obisidanClayCost, top.clayRobot,
                                top.obisidianStore + top.obsidianRobot, top.obsidianRobot + 1,
                                top.geodStore + top.geodRobot, top.geodRobot);
                        if (!visited.contains(s)) {
                            states.add(s);
                            visited.add(s);
                        }
                    } // else
                    if (canBuildClayRobot(blueprint, top) && top.clayRobot < blueprint.obisidanClayCost) {// &&
                                                                                                          // wothBuildingClayRobot(blueprint,
                                                                                                          // top)) {
                        var s = new State(top.minute + 1, top.oreStore + top.oreRobot - blueprint.clayCost,
                                top.oreRobot, top.clayStore + top.clayRobot, top.clayRobot + 1,
                                top.obisidianStore + top.obsidianRobot, top.obsidianRobot,
                                top.geodStore + top.geodRobot, top.geodRobot);
                        if (!visited.contains(s)) {
                            states.add(s);
                            visited.add(s);
                        }
                    }
                    if (canBuildOreRobot(blueprint, top) && top.oreRobot < maxOre) {
                        var s = new State(top.minute + 1, top.oreStore + top.oreRobot - blueprint.oreCost,
                                top.oreRobot + 1, top.clayStore + top.clayRobot, top.clayRobot,
                                top.obisidianStore + top.obsidianRobot, top.obsidianRobot,
                                top.geodStore + top.geodRobot, top.geodRobot);
                        if (!visited.contains(s)) {
                            states.add(s);
                            visited.add(s);
                        }
                    }
                    var s = new State(top.minute + 1, Math.min(top.oreStore + top.oreRobot, maxOre + minOre),
                            top.oreRobot, Math.min(top.clayStore + top.clayRobot, blueprint.obisidanClayCost),
                            top.clayRobot,
                            Math.min(top.obisidianStore + top.obsidianRobot, blueprint.geodeObisidanCost),
                            top.obsidianRobot, top.geodStore + top.geodRobot, top.geodRobot);

                    if (!visited.contains(s)) {
                        states.add(s);
                        visited.add(s);
                    }
                }

            }
        }
        return maxGeod;
    }

    private static final boolean canBuildOreRobot(Blueprint bp, State state) {
        return state.oreStore >= bp.oreCost;
    }

    private static final boolean canBuildClayRobot(Blueprint bp, State state) {
        return state.oreStore >= bp.clayCost;
    }

    private static final boolean canBuildObsidianRobot(Blueprint bp, State state) {
        return state.oreStore >= bp.obisidanOreCost && state.clayStore >= bp.obisidanClayCost;
    }

    private static final boolean canBuildGeodRobot(Blueprint bp, State state) {
        return state.oreStore >= bp.geodOreCost && state.obisidianStore >= bp.geodeObisidanCost;
    }

    private static final boolean wothBuildingObsidinaRobot(Blueprint bp, State state) {
        int left = 24 - state.minute;
        State future = new State(state.minute + left, state.oreStore + left * state.oreRobot - bp.obisidanOreCost,
                state.oreRobot, state.clayStore + left * state.clayRobot - bp.obisidanClayCost, state.clayRobot,
                state.obisidianStore + left * (state.obsidianRobot + 1), state.obsidianRobot + 1, state.geodStore,
                state.geodRobot);
        return canBuildGeodRobot(bp, future);
    }

    private static final boolean wothBuildingClayRobot(Blueprint bp, State state) {
        int left = 24 - state.minute;
        State future = state;
        for (int i = 0; i < left; ++i) {
            future = new State(future.minute + 1, future.oreStore + future.oreRobot, future.oreRobot,
                    future.clayStore + future.clayRobot, future.clayRobot + 1,
                    future.obisidianStore + future.obsidianRobot, future.obsidianRobot,
                    future.geodStore + future.geodRobot, future.geodRobot);
            if (canBuildObsidianRobot(bp, future) && wothBuildingObsidinaRobot(bp, future)) {
                return true;
            }
        }
        return false;
    }

    private static final record State(byte minute, byte oreStore, byte oreRobot, byte clayStore, byte clayRobot,
            byte obisidianStore, byte obsidianRobot, byte geodStore, byte geodRobot) {
        State(int minute, int oreStore, int oreRobot, int clayStore, int clayRobot, int obisidianStore,
                int obsidianRobot, int geodStore, int geodRobot) {
            this((byte) minute, (byte) oreStore, (byte) oreRobot, (byte) clayStore, (byte) clayRobot,
                    (byte) obisidianStore, (byte) obsidianRobot, (byte) geodStore, (byte) geodRobot);
        }
    }

    private static final record Blueprint(int id, int oreCost, int clayCost, int obisidanOreCost, int obisidanClayCost,
            int geodOreCost, int geodeObisidanCost) {

        private static final Pattern ORE_ROBOT_PATTERN = Pattern.compile("Each ore robot costs (\\d+) ore");
        private static final Pattern CLAY_ROBOT_PATTERN = Pattern.compile("Each clay robot costs (\\d+) ore");
        private static final Pattern OBSIDIAN_ROBOT_PATTERN = Pattern
                .compile("Each obsidian robot costs (\\d+) ore and (\\d+) clay");
        private static final Pattern GEOD_ROBOT_PATTERN = Pattern
                .compile("Each geode robot costs (\\d+) ore and (\\d+) obsidian\\.");
        static Blueprint parse(String line) {
            var p1 = line.split(": ");
            var id = Integer.parseInt(p1[0].split(" ")[1]);
            var costs = p1[1].split("\\. ");
            var oreCost = Integer.parseInt(getMatchedMatcher(costs[0], ORE_ROBOT_PATTERN).group(1));
            var clayCost = Integer.parseInt(getMatchedMatcher(costs[1], CLAY_ROBOT_PATTERN).group(1));
            var obsidinMatcher = getMatchedMatcher(costs[2], OBSIDIAN_ROBOT_PATTERN);
            var obsOreCost = Integer.parseInt(obsidinMatcher.group(1));
            var obsClayCost = Integer.parseInt(obsidinMatcher.group(2));
            var geoMatcher = getMatchedMatcher(costs[3], GEOD_ROBOT_PATTERN);
            var geoOreCost = Integer.parseInt(geoMatcher.group(1));
            var geoObsCost = Integer.parseInt(geoMatcher.group(2));
            return new Blueprint(id, oreCost, clayCost, obsOreCost, obsClayCost, geoOreCost, geoObsCost);
        }

        private static Matcher getMatchedMatcher(String def, Pattern pattern) {
            var matcher = pattern.matcher(def);
            if (matcher.matches()) {
                return matcher;
            }
            throw new IllegalStateException("Pattern: '" + pattern + "' does not match '" + def + "'");
        }
    }

    private final static String INPUT = """
            Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 12 clay. Each geode robot costs 4 ore and 19 obsidian.
            Blueprint 2: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 11 clay. Each geode robot costs 2 ore and 7 obsidian.
            Blueprint 3: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 13 clay. Each geode robot costs 3 ore and 12 obsidian.
            Blueprint 4: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 18 clay. Each geode robot costs 2 ore and 19 obsidian.
            Blueprint 5: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 19 clay. Each geode robot costs 4 ore and 13 obsidian.
            Blueprint 6: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 7 clay. Each geode robot costs 4 ore and 11 obsidian.
            Blueprint 7: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 15 clay. Each geode robot costs 4 ore and 17 obsidian.
            Blueprint 8: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 13 clay. Each geode robot costs 3 ore and 7 obsidian.
            Blueprint 9: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 12 clay. Each geode robot costs 3 ore and 15 obsidian.
            Blueprint 10: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 4 ore and 11 obsidian.
            Blueprint 11: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 8 clay. Each geode robot costs 2 ore and 15 obsidian.
            Blueprint 12: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 8 clay. Each geode robot costs 3 ore and 7 obsidian.
            Blueprint 13: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 10 clay. Each geode robot costs 2 ore and 10 obsidian.
            Blueprint 14: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 13 clay. Each geode robot costs 2 ore and 20 obsidian.
            Blueprint 15: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 19 clay. Each geode robot costs 3 ore and 8 obsidian.
            Blueprint 16: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 16 clay. Each geode robot costs 2 ore and 18 obsidian.
            Blueprint 17: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 9 clay. Each geode robot costs 3 ore and 19 obsidian.
            Blueprint 18: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 11 clay. Each geode robot costs 4 ore and 8 obsidian.
            Blueprint 19: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 12 clay. Each geode robot costs 3 ore and 17 obsidian.
            Blueprint 20: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 3 ore and 17 obsidian.
            Blueprint 21: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 3 ore and 16 obsidian.
            Blueprint 22: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 16 clay. Each geode robot costs 4 ore and 16 obsidian.
            Blueprint 23: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 19 clay. Each geode robot costs 4 ore and 11 obsidian.
            Blueprint 24: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 4 ore and 9 obsidian.
            Blueprint 25: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 17 clay. Each geode robot costs 3 ore and 16 obsidian.
            Blueprint 26: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 20 clay. Each geode robot costs 4 ore and 7 obsidian.
            Blueprint 27: Each ore robot costs 2 ore. Each clay robot costs 2 ore. Each obsidian robot costs 2 ore and 8 clay. Each geode robot costs 2 ore and 14 obsidian.
            Blueprint 28: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 3 ore and 14 obsidian.
            Blueprint 29: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 4 ore and 8 obsidian.
            Blueprint 30: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 3 ore and 13 obsidian.""";
}
