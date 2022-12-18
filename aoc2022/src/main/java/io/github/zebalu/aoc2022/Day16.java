package io.github.zebalu.aoc2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {
    private static final String START="AA";
    public static void main(String[] args) {
        var valveMap = INPUT.lines().map(Valve::parse).collect(Collectors.toMap(Valve::id, v->v));
        System.out.println(part1(valveMap));
        System.out.println(part2(valveMap));
    }

    private static int part1(Map<String, Valve> valveMap) {
        var best = bestPath(30, valveMap, Set.of());
        return releaseToTheLimit(30, best);
    }
    
    private static int part2(Map<String, Valve> valveMap) {
        var me = bestPath(26, valveMap, new HashSet<>());
        var elephant = bestPath(26, valveMap, me.opened());
        return releaseToTheLimit(26, me)+releaseToTheLimit(26, elephant);
    }
    
    private static State bestPath(int limit, Map<String, Valve> valveMap, Set<String> forbidden) {
        var prices = calcPrices(valveMap);
        var valuables = valveMap.values().stream().filter(v->v.flowRate()>0).map(v->v.id).toList();
        var steps = new ArrayDeque<State>();
        steps.add(new State(START, 0, 0, 0, Set.of()));
        int maxReleased = Integer.MIN_VALUE;
        State bestPath = null;
        while (!steps.isEmpty()) {
            State current = steps.poll();
            valuables.stream().filter(v->!current.at().equals(v)&&!forbidden.contains(v)).forEach(v->{
                int price = prices.get(new IdPair(current.at(), v));
                if(!current.opened().contains(v)&&current.minute()+price<limit) {
                    steps.add(new State(v, current.minute()+price+1, current.releases()+valveMap.get(v).flowRate(), current.released()+(price+1)*current.releases(), current.open(v)));
                }
            });
            
            if(current.minute()<=limit) {
                int r = releaseToTheLimit(limit, current);
                if(maxReleased<r) {
                    maxReleased = r;
                    bestPath = current;
                }
            }
        }
        return bestPath;
    }
    
    private static int releaseToTheLimit(int limit, State state) {
        if(state.minute()<limit) {
            return state.released() + (limit-state.minute())*state.releases();
        }
        return state.released();
    }
    
    private static final record State(String at, int minute, int releases, int released, Set<String> opened) {
        Set<String> open(String id) {
            var c = new HashSet<>(opened());
            c.add(id);
            return c;
        }
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

    private static Map<IdPair, Integer> calcPrices(Map<String, Valve> valveMap) {
        var valuables = new ArrayList<>(valveMap.values().stream().filter(v -> v.flowRate() > 0).toList());
        valuables.add(valveMap.get(START));
        var result = new HashMap<IdPair, Integer>();
        valuables.forEach(from -> {
            valuables.forEach(to -> {
                if (from != to && !to.id().equals(START)) {
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
            var next = go.poll();
            if (next.get(0).equals(to)) {
                return next.size() - 1;
            }
            valveMap.get(next.get(0)).available().stream().filter(s -> !visited.contains(s)).forEach(n -> {
                var path = new LinkedList<>(next);
                path.addFirst(n);
                go.add(path);
                visited.add(n);
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
