package io.github.zebalu.aoc2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

public class Day16 {
    public static void main(String[] args) {
        Map<String, Valve> valveMap = new HashMap<>();
        INPUT.lines().map(Valve::parse).forEach(v -> {
            valveMap.put(v.id, v);
        });
        // part1(valveMap);
        part2_2(valveMap);
    }

    private static void part1(Map<String, Valve> valveMap) {
        Queue<Step> steps = new ArrayDeque<>();
        steps.add(new Step("AA", 0, 0, 0, new HashSet<String>(), Set.of("AA")));
        int maxReleased = Integer.MIN_VALUE;
        Step last = null;
        while (!steps.isEmpty()) {
            Step current = steps.poll();
            if (current.minute() < 30) {
                if (!current.open().contains(current.at()) && valveMap.get(current.at).flowRate() > 0) {
                    steps.add(new Step(current.at(), current.minute() + 1,
                            current.releases() + valveMap.get(current.at).flowRate(),
                            current.released() + current.releases(), current.open(current.at()), current.visited()));
                }
                var availables = valveMap.get(current.at()).available();
                availables.stream().filter(s -> !current.visited.contains(s)).forEach(id -> {
                    steps.add(new Step(id, current.minute() + 1, current.releases(),
                            current.released() + current.releases(), current.open(), current.visit(id)));
                });
            }
            if (maxReleased < current.released()) {
                maxReleased = current.released();
                last = current;
            }
            System.out.println("step.size: " + steps.size() + "\t" + current.minute());
        }
        System.out.println(maxReleased);
    }

//    private static void part2(Map<String, Valve> valveMap) {
//        Stack<ElephantStep> steps = new Stack<>();// new ArrayDeque<>();
//        steps.add(new ElephantStep("AA", "AA", 0, 0, 0, new HashSet<String>(), Set.of("AA"), Set.of("AA")));
//        int maxReleased = Integer.MIN_VALUE;
//        ElephantStep last = null;
//        long c = 0L;
//        while (!steps.isEmpty()) {
//            ++c;
//            var current = steps.pop();
//            if (current.minute() < 26) {
//                var iShouldOpen = !current.open().contains(current.at()) && valveMap.get(current.at).flowRate() > 0;
//                var eShouldOpen = !current.open().contains(current.eAt()) && valveMap.get(current.eAt).flowRate() > 0;
//                if (iShouldOpen && eShouldOpen && !current.at().equals(current.eAt())) {
//                    steps.add(new ElephantStep(current.at(), current.eAt(), current.minute() + 1,
//                            current.releases() + valveMap.get(current.at()).flowRate()
//                                    + valveMap.get(current.eAt()).flowRate(),
//                            current.released() + current.releases(), current.open(current.at(), current.eAt()),
//                            current.visited(), current.eVisited()));
//                } else if (iShouldOpen) {
//                    valveMap.get(current.eAt()).available().stream()
//                            /* .filter(s->!current.eVisited().contains(s)) */.forEach(id -> {
//                                steps.add(new ElephantStep(current.at(), id, current.minute() + 1,
//                                        current.releases() + valveMap.get(current.at()).flowRate(),
//                                        current.released() + current.releases(), current.open(current.at(), null),
//                                        current.visited(), current.eVisit(id)));
//                            });
//                } else if (eShouldOpen) {
//                    valveMap.get(current.at()).available().stream()
//                            /* .filter(s->!current.visited().contains(s)) */.forEach(id -> {
//                                steps.add(new ElephantStep(id, current.eAt(), current.minute() + 1,
//                                        current.releases() + valveMap.get(current.eAt()).flowRate(),
//                                        current.released() + current.releases(), current.open(null, current.eAt()),
//                                        current.visit(id), current.eVisited()));
//                            });
//                } else {
//                    valveMap.get(current.at()).available().stream()
//                            /* .filter(s->!current.visited().contains(s)) */.forEach(id -> {
//                                valveMap.get(current.eAt()).available().stream()
//                                        /* .filter(s->!current.eVisited().contains(s)) */.forEach(eId -> {
//                                            steps.add(new ElephantStep(id, eId, current.minute() + 1,
//                                                    current.releases(), current.released() + current.releases(),
//                                                    current.open(), current.visit(id), current.eVisit(eId)));
//                                        });
//                            });
//                }
//            }
//            if (maxReleased < current.released()) {
//                maxReleased = current.released();
//                last = current;
//            }
//            if (c % 1_000_000 == 0) {
//                System.out.println("step.size: " + steps.size() + "\t" + current.minute() + "\t" + c);
//            }
//        }
//        System.out.println(maxReleased);
//    }

    private static void part2_2(Map<String, Valve> valveMap) {
        var prices = calcPrices(valveMap);
        var valuables = valveMap.values().stream().filter(it -> it.flowRate() > 0).map(Valve::id).toList();
        Stack<ElephantStep> steps = new Stack<>();// new ArrayDeque<>();
        steps.add(new ElephantStep(List.of("AA"), List.of("AA"), new HashSet<String>()));
        int maxReleased = Integer.MIN_VALUE;
        ElephantStep last = null;
        long c = 0L;
        while (!steps.isEmpty()) {
            ++c;
            var current = steps.pop();
            if (current.minutes(prices) < 26 || current.eMinutes(prices) < 26) {
                String start = current.route().get(0);
                String eStart = current.eRoute().get(0);
                valuables.stream().filter(s -> !s.equals(start) && !s.equals(eStart) && !current.open().contains(s)).forEach(v -> {
                    valuables.stream().filter(eS -> !eS.equals(start) && !eS.equals(eStart) && !current.open().contains(eS) && !eS.equals(v))
                            .forEach(eV -> {
                                steps.add(new ElephantStep(current.visit(v), current.eVisit(eV), current.open(v, eV)));
                            });
                });
            }
            if(maxReleased < current.released(prices, valveMap)) {
                maxReleased = current.released(prices, valveMap);
                last = current;
                System.out.println(maxReleased);
                System.out.println(last);
            }
            if (c % 1_000_000 == 0) {
                System.out.println("step.size: " + steps.size() + "\t" + current.minutes(prices) + "\t" + current.eMinutes(prices)+"\t"+ c);
            }
        }
        System.out.println(maxReleased);
    }

    private static final record Valve(String id, int flowRate, List<String> available) {

        private static final Pattern PATTERN = Pattern
                .compile("Valve (\\w+) has flow rate=(\\d+); \\w+ \\w+ to \\w+ (.+)");
        static Valve parse(String line) {
            var matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                String id = matcher.group(1);
                int rate = Integer.parseInt(matcher.group(2));
                List<String> next = Arrays.asList(matcher.group(3).trim().split(", "));
                return new Valve(id, rate, next);
            }
            throw new IllegalArgumentException("does not match: " + line);
        }
    }

    private static final record Step(String at, int minute, int releases, int released, Set<String> open,
            Set<String> visited) {
        Set<String> open(String v) {
            var copy = new HashSet<>(open);
            copy.add(v);
            return copy;
        }

        Set<String> visit(String v) {
            var copy = new HashSet<>(visited);
            copy.add(v);
            return copy;
        }

        int calcMaxRelease() {
            return released + (30 - minute) * releases;
        }
    }

    private static final record ElephantStep(List<String> route, List<String> eRoute, Set<String> open) {
        Set<String> open(String v, String eV) {
            var copy = new HashSet<>(open);
            if (v != null) {
                copy.add(v);
            }
            if (eV != null) {
                copy.add(eV);
            }
            return copy;
        }

        List<String> visit(String v) {
            var copy = new LinkedList<>(route);
            copy.addFirst(v);
            return copy;
        }

        List<String> eVisit(String v) {
            var copy = new LinkedList<>(eRoute);
            copy.addFirst(v);
            return copy;
        }

        int minutes(Map<IdPair, Integer> prices) {
            return minutes(route, prices);
        }

        int eMinutes(Map<IdPair, Integer> prices) {
            return minutes(eRoute, prices);
        }

        private int minutes(List<String> r, Map<IdPair, Integer> prices) {
            if (r.size() < 2) {
                return 0;
            }
            int sum = 0;
            String prev = r.get(r.size()-1);
            for (int i = r.size()-2; i >= 0; --i) {
                ++sum;
                String next = r.get(i);
                sum += prices.get(new IdPair(prev, next));
                prev = next;
            }
            return sum;
        }
        
        int released(Map<IdPair, Integer> prices, Map<String, Valve> valves) {
            return released(route, prices, valves) + released(eRoute, prices, valves);
        }
        
        private int released(List<String> r, Map<IdPair, Integer> prices, Map<String, Valve> valves) {
            int res = 0;
            int rate = 0;
            int left = 26;
            //while(left>0) {
                String prev = r.get(r.size()-1);
                for (int i = r.size()-2; i >= 0 && left > 0; --i) {
                    String next = r.get(i);
                    var price = prices.get(new IdPair(prev, next));
                    if(left-price>1) {
                        left -= price;
                        res += (price+1) * rate;
                        --left;
                        rate += valves.get(next).flowRate();
                    } else {
                        res += left * rate;
                        left = 0;
                    }
                    prev = next;
                }
            //}
            return res;
        }
    }

    private static Map<IdPair, Integer> calcPrices(Map<String, Valve> valveMap) {
        var valuables = new ArrayList<>(valveMap.values().stream().filter(v -> v.flowRate() > 0).toList());
        valuables.add(valveMap.get("AA"));
        var result = new HashMap<IdPair, Integer>();
        valuables.forEach(from -> {
            valuables.forEach(to -> {
                if (from != to && !to.id.equals("AA")) {
                    int price = calcPrice(from.id(), to.id(), valveMap);
                    result.put(new IdPair(from.id(), to.id()), price);
                }
            });
        });
        return result;
    }

    private static int calcPrice(String from, String to, Map<String, Valve> valveMap) {
        Queue<List<String>> go = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        visited.add(from);
        go.add(List.of(from));
        while (!go.isEmpty()) {
            var n = go.poll();
            if (n.get(0).equals(to)) {
                return n.size() - 1;
            }
            valveMap.get(n.get(0)).available().stream().filter(s -> !visited.contains(s)).forEach(nn -> {
                var nnn = new LinkedList<>(n);
                nnn.addFirst(nn);
                go.add(nnn);
                visited.add(nn);
            });
        }
        throw new IllegalArgumentException();
    }

    private static final record IdPair(String from, String to) {

    }

    private static final String INPUT = """
            Valve SY has flow rate=0; tunnels lead to valves GW, LW
            Valve TS has flow rate=0; tunnels lead to valves CC, OP
            Valve LU has flow rate=0; tunnels lead to valves PS, XJ
            Valve ND has flow rate=0; tunnels lead to valves EN, TL
            Valve PD has flow rate=0; tunnels lead to valves TL, LI
            Valve VF has flow rate=0; tunnels lead to valves LW, RX
            Valve LD has flow rate=0; tunnels lead to valves AD, LP
            Valve DG has flow rate=0; tunnels lead to valves DR, SS
            Valve IG has flow rate=8; tunnels lead to valves AN, YA, GA
            Valve LK has flow rate=0; tunnels lead to valves HQ, LW
            Valve TD has flow rate=14; tunnels lead to valves BG, CQ
            Valve CQ has flow rate=0; tunnels lead to valves TD, HD
            Valve AZ has flow rate=0; tunnels lead to valves AD, XW
            Valve ZU has flow rate=0; tunnels lead to valves TL, AN
            Valve HD has flow rate=0; tunnels lead to valves BP, CQ
            Valve FX has flow rate=0; tunnels lead to valves LW, XM
            Valve CU has flow rate=18; tunnels lead to valves BX, VA, RX, DF
            Valve SS has flow rate=17; tunnels lead to valves DG, ZD, ZG
            Valve BP has flow rate=19; tunnels lead to valves HD, ZD
            Valve DZ has flow rate=0; tunnels lead to valves XS, CC
            Valve PS has flow rate=0; tunnels lead to valves GH, LU
            Valve TA has flow rate=0; tunnels lead to valves LI, AA
            Valve BG has flow rate=0; tunnels lead to valves TD, ZG
            Valve WP has flow rate=0; tunnels lead to valves OB, AA
            Valve XS has flow rate=9; tunnels lead to valves EN, DZ
            Valve AA has flow rate=0; tunnels lead to valves WG, GA, VO, WP, TA
            Valve LW has flow rate=25; tunnels lead to valves LK, FX, SY, VF
            Valve AD has flow rate=23; tunnels lead to valves DF, GW, AZ, LD, FM
            Valve EN has flow rate=0; tunnels lead to valves ND, XS
            Valve ZG has flow rate=0; tunnels lead to valves SS, BG
            Valve LI has flow rate=11; tunnels lead to valves YA, XM, TA, PD
            Valve VO has flow rate=0; tunnels lead to valves AA, OD
            Valve AN has flow rate=0; tunnels lead to valves IG, ZU
            Valve GH has flow rate=15; tunnels lead to valves VA, PS
            Valve OP has flow rate=4; tunnels lead to valves AJ, TS, FM, BX, NM
            Valve BX has flow rate=0; tunnels lead to valves OP, CU
            Valve RX has flow rate=0; tunnels lead to valves CU, VF
            Valve FM has flow rate=0; tunnels lead to valves OP, AD
            Valve OB has flow rate=0; tunnels lead to valves WP, XW
            Valve CC has flow rate=3; tunnels lead to valves QS, LP, DZ, OD, TS
            Valve LP has flow rate=0; tunnels lead to valves LD, CC
            Valve NM has flow rate=0; tunnels lead to valves WH, OP
            Valve HQ has flow rate=0; tunnels lead to valves XW, LK
            Valve GW has flow rate=0; tunnels lead to valves SY, AD
            Valve QS has flow rate=0; tunnels lead to valves CC, XW
            Valve DF has flow rate=0; tunnels lead to valves AD, CU
            Valve XM has flow rate=0; tunnels lead to valves LI, FX
            Valve VA has flow rate=0; tunnels lead to valves CU, GH
            Valve GA has flow rate=0; tunnels lead to valves IG, AA
            Valve YA has flow rate=0; tunnels lead to valves LI, IG
            Valve XW has flow rate=20; tunnels lead to valves OB, HQ, QS, WH, AZ
            Valve XJ has flow rate=24; tunnel leads to valve LU
            Valve AJ has flow rate=0; tunnels lead to valves WG, OP
            Valve WH has flow rate=0; tunnels lead to valves XW, NM
            Valve TL has flow rate=13; tunnels lead to valves PD, DR, ZU, ND
            Valve OD has flow rate=0; tunnels lead to valves CC, VO
            Valve ZD has flow rate=0; tunnels lead to valves SS, BP
            Valve DR has flow rate=0; tunnels lead to valves DG, TL
            Valve WG has flow rate=0; tunnels lead to valves AJ, AA""";
}
