package io.github.zebalu.aoc2022;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day05 {
public static void main(String[] args) {
    var parts = INPUT.split("\n\n");
    var commands = parts[1].lines().map(Command::parse).toList();
    part1(parts[0], commands);
    part2(parts[0], commands);
}

private static void part1(String desc, List<Command> commands) {
    List<List<Character>> stacks = readStacks(desc);
    commands.forEach(c->c.execute9000(stacks));
    printStackTops(stacks);
}

private static void part2(String desc, List<Command> commands) {
    List<List<Character>> stacks = readStacks(desc);
    commands.forEach(c->c.execute9001(stacks));
    printStackTops(stacks);
}

private static void printStackTops(List<List<Character>> stacks) {
    System.out.println(stacks.stream().map(l->l.isEmpty()?"":""+l.get(0)).collect(Collectors.joining()));
}

private static List<List<Character>> readStacks(String desc) {
    List<List<Character>> stacks = new ArrayList<>();
    load(desc, stacks);
    return stacks;
}
private static void load(String desc, List<List<Character>> stacks) {
    for(int i=0; i<9; ++i) {
        stacks.add(new LinkedList<>());
    }
    var lines = desc.lines().toList();
    lines = lines.subList(0, lines.size()-1);
    for(var line :  lines) {
        int j = 0;
        for(int i=1; i<38 && i<line.length(); i+=4, ++j) {
            char c = line.charAt(i);
            if(c!=' ') {
                stacks.get(j).add(c);
            }
        }
    }
}
private static final record Command(int count, int from, int to) {
    private static final Pattern PATTERN = Pattern.compile( "move (\\d+) from (\\d) to (\\d)");
    
    void execute9000(List<List<Character>> stacks) {
        for(int i=0; i<count; ++i) {
            char c = stacks.get(from).remove(0);
            stacks.get(to).add(0, c);
        }
    }
    
    void execute9001(List<List<Character>> stacks) {
        List<Character> copy = new LinkedList<>();
        for(int i=0; i<count; ++i) {
            char c = stacks.get(from).remove(0);
            copy.add(c);
        }
        stacks.get(to).addAll(0, copy);
    }
    
    static Command parse(String line) {
        var matcher = PATTERN.matcher(line);
        if(matcher.matches()) {
            return new Command(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))-1, Integer.parseInt(matcher.group(3))-1);
        }
        throw new IllegalStateException("line: '"+line+"' does not match");
    }
}
private static final String INPUT="""
        [H]     [W] [B]            
    [D] [B]     [L] [G] [N]        
[P] [J] [T]     [M] [R] [D]        
[V] [F] [V]     [F] [Z] [B]     [C]
[Z] [V] [S]     [G] [H] [C] [Q] [R]
[W] [W] [L] [J] [B] [V] [P] [B] [Z]
[D] [S] [M] [S] [Z] [W] [J] [T] [G]
[T] [L] [Z] [R] [C] [Q] [V] [P] [H]
 1   2   3   4   5   6   7   8   9 

move 3 from 2 to 9
move 1 from 1 to 6
move 6 from 6 to 7
move 13 from 7 to 6
move 2 from 4 to 5
move 1 from 4 to 3
move 5 from 9 to 8
move 1 from 8 to 5
move 3 from 1 to 6
move 2 from 1 to 8
move 1 from 2 to 1
move 1 from 9 to 3
move 2 from 9 to 8
move 2 from 5 to 9
move 4 from 5 to 4
move 10 from 8 to 4
move 5 from 6 to 2
move 5 from 5 to 9
move 7 from 3 to 7
move 1 from 9 to 8
move 1 from 1 to 9
move 1 from 7 to 3
move 3 from 8 to 9
move 8 from 6 to 7
move 3 from 9 to 4
move 3 from 2 to 6
move 6 from 6 to 3
move 10 from 7 to 9
move 1 from 7 to 5
move 1 from 5 to 7
move 2 from 3 to 6
move 8 from 4 to 2
move 7 from 4 to 3
move 5 from 2 to 3
move 2 from 4 to 6
move 6 from 2 to 8
move 14 from 9 to 1
move 6 from 3 to 2
move 7 from 3 to 7
move 4 from 2 to 3
move 1 from 6 to 5
move 3 from 6 to 5
move 2 from 2 to 4
move 3 from 1 to 2
move 2 from 4 to 1
move 3 from 5 to 6
move 1 from 9 to 6
move 1 from 2 to 6
move 7 from 7 to 4
move 5 from 8 to 1
move 11 from 3 to 5
move 2 from 2 to 5
move 8 from 5 to 1
move 4 from 7 to 2
move 2 from 6 to 8
move 3 from 2 to 4
move 1 from 8 to 3
move 1 from 3 to 2
move 11 from 1 to 8
move 4 from 6 to 5
move 1 from 4 to 1
move 2 from 6 to 4
move 14 from 1 to 9
move 1 from 1 to 6
move 1 from 1 to 9
move 10 from 4 to 3
move 3 from 3 to 2
move 8 from 8 to 9
move 1 from 4 to 5
move 8 from 5 to 8
move 10 from 9 to 5
move 5 from 3 to 2
move 1 from 3 to 7
move 1 from 2 to 5
move 6 from 2 to 3
move 7 from 3 to 5
move 1 from 6 to 9
move 2 from 5 to 9
move 4 from 2 to 9
move 1 from 2 to 1
move 1 from 1 to 5
move 1 from 7 to 4
move 17 from 9 to 1
move 4 from 1 to 5
move 9 from 5 to 8
move 21 from 8 to 6
move 1 from 4 to 6
move 3 from 5 to 1
move 10 from 1 to 5
move 12 from 5 to 3
move 3 from 3 to 6
move 5 from 5 to 7
move 5 from 5 to 9
move 5 from 7 to 5
move 2 from 5 to 7
move 1 from 8 to 5
move 1 from 7 to 3
move 3 from 1 to 7
move 11 from 6 to 5
move 1 from 7 to 3
move 5 from 9 to 7
move 8 from 3 to 6
move 4 from 9 to 6
move 3 from 1 to 6
move 1 from 9 to 5
move 6 from 5 to 1
move 1 from 1 to 6
move 3 from 1 to 3
move 2 from 1 to 2
move 19 from 6 to 1
move 2 from 5 to 9
move 5 from 3 to 1
move 1 from 5 to 6
move 5 from 6 to 7
move 3 from 7 to 9
move 6 from 5 to 9
move 1 from 5 to 6
move 4 from 6 to 9
move 2 from 2 to 1
move 1 from 3 to 2
move 1 from 2 to 7
move 7 from 7 to 6
move 21 from 1 to 3
move 2 from 7 to 8
move 7 from 3 to 2
move 2 from 7 to 9
move 8 from 3 to 8
move 4 from 3 to 1
move 6 from 1 to 9
move 7 from 2 to 9
move 1 from 3 to 6
move 1 from 8 to 7
move 1 from 1 to 6
move 12 from 6 to 9
move 1 from 3 to 6
move 1 from 7 to 5
move 1 from 1 to 9
move 1 from 5 to 9
move 39 from 9 to 4
move 3 from 9 to 6
move 1 from 9 to 6
move 7 from 8 to 4
move 1 from 9 to 8
move 44 from 4 to 1
move 1 from 6 to 3
move 28 from 1 to 8
move 15 from 8 to 1
move 1 from 3 to 2
move 11 from 1 to 5
move 1 from 4 to 7
move 1 from 4 to 5
move 16 from 1 to 6
move 1 from 2 to 6
move 12 from 8 to 2
move 1 from 7 to 4
move 3 from 2 to 4
move 7 from 2 to 4
move 4 from 1 to 6
move 10 from 5 to 6
move 1 from 1 to 5
move 3 from 5 to 9
move 3 from 8 to 7
move 1 from 2 to 3
move 1 from 2 to 4
move 3 from 7 to 4
move 30 from 6 to 8
move 1 from 3 to 7
move 20 from 8 to 4
move 1 from 7 to 3
move 1 from 9 to 8
move 25 from 4 to 6
move 1 from 3 to 5
move 8 from 8 to 5
move 3 from 8 to 4
move 2 from 9 to 5
move 2 from 5 to 2
move 21 from 6 to 4
move 2 from 2 to 6
move 28 from 4 to 5
move 1 from 8 to 6
move 5 from 4 to 8
move 3 from 6 to 7
move 15 from 5 to 2
move 3 from 7 to 6
move 1 from 4 to 3
move 17 from 5 to 1
move 1 from 3 to 4
move 1 from 4 to 8
move 4 from 2 to 4
move 4 from 4 to 1
move 5 from 6 to 8
move 11 from 8 to 3
move 4 from 6 to 7
move 5 from 3 to 2
move 4 from 3 to 1
move 25 from 1 to 7
move 3 from 6 to 7
move 8 from 2 to 3
move 11 from 7 to 2
move 2 from 2 to 7
move 16 from 2 to 6
move 1 from 2 to 8
move 1 from 7 to 6
move 1 from 5 to 2
move 16 from 6 to 2
move 3 from 5 to 7
move 6 from 2 to 8
move 1 from 5 to 4
move 1 from 4 to 3
move 4 from 8 to 9
move 4 from 3 to 9
move 2 from 6 to 2
move 6 from 2 to 4
move 1 from 9 to 7
move 1 from 2 to 8
move 7 from 3 to 6
move 4 from 2 to 6
move 2 from 9 to 5
move 1 from 2 to 4
move 6 from 6 to 9
move 2 from 5 to 1
move 1 from 1 to 4
move 1 from 9 to 4
move 2 from 7 to 6
move 1 from 2 to 5
move 1 from 5 to 9
move 4 from 8 to 1
move 7 from 9 to 8
move 3 from 1 to 7
move 1 from 8 to 3
move 4 from 9 to 6
move 6 from 8 to 1
move 6 from 1 to 2
move 1 from 1 to 9
move 1 from 1 to 7
move 21 from 7 to 5
move 11 from 5 to 3
move 1 from 9 to 5
move 1 from 2 to 8
move 5 from 7 to 5
move 10 from 3 to 9
move 1 from 8 to 5
move 8 from 4 to 2
move 1 from 3 to 4
move 2 from 7 to 3
move 5 from 5 to 3
move 5 from 9 to 8
move 10 from 6 to 2
move 1 from 6 to 4
move 1 from 9 to 4
move 4 from 9 to 3
move 19 from 2 to 5
move 2 from 4 to 5
move 11 from 5 to 1
move 15 from 5 to 2
move 4 from 8 to 1
move 12 from 1 to 5
move 1 from 8 to 1
move 1 from 4 to 8
move 3 from 1 to 3
move 8 from 5 to 4
move 7 from 3 to 9
move 4 from 3 to 5
move 4 from 4 to 1
move 3 from 9 to 3
move 2 from 4 to 1
move 4 from 3 to 8
move 4 from 2 to 3
move 1 from 9 to 5
move 4 from 8 to 6
move 2 from 4 to 3
move 1 from 4 to 5
move 5 from 3 to 4
move 3 from 3 to 6
move 5 from 1 to 6
move 2 from 4 to 6
move 1 from 9 to 2
move 7 from 6 to 3
move 1 from 8 to 9
move 2 from 1 to 4
move 2 from 4 to 7
move 4 from 6 to 4
move 5 from 3 to 7
move 1 from 7 to 2
move 3 from 6 to 7
move 1 from 4 to 5
move 4 from 2 to 6
move 3 from 6 to 9
move 1 from 6 to 5
move 1 from 9 to 2
move 5 from 9 to 3
move 11 from 5 to 1
move 3 from 7 to 8
move 2 from 8 to 9
move 4 from 5 to 1
move 10 from 2 to 7
move 5 from 3 to 7
move 1 from 9 to 3
move 6 from 1 to 7
move 22 from 7 to 9
move 3 from 2 to 4
move 4 from 5 to 3
move 1 from 8 to 4
move 5 from 4 to 7
move 19 from 9 to 8
move 2 from 1 to 5
move 2 from 4 to 5
move 2 from 4 to 9
move 4 from 9 to 2
move 4 from 7 to 3
move 5 from 7 to 5
move 7 from 3 to 7
move 2 from 8 to 4
move 3 from 4 to 7
move 12 from 8 to 1
move 4 from 3 to 7
move 1 from 3 to 6
move 1 from 6 to 1
move 1 from 5 to 9
move 3 from 9 to 3
move 2 from 2 to 4
move 3 from 8 to 3
move 2 from 4 to 7
move 7 from 5 to 1
move 2 from 8 to 3
move 8 from 7 to 9
move 2 from 9 to 7
move 3 from 9 to 5
move 11 from 1 to 2
move 5 from 3 to 8
move 16 from 1 to 5
move 1 from 9 to 8
move 3 from 3 to 2
move 6 from 2 to 6
move 6 from 7 to 4
move 2 from 5 to 2
move 6 from 4 to 9
move 11 from 5 to 7
move 2 from 6 to 5
move 9 from 5 to 1
move 2 from 8 to 5
move 13 from 7 to 4
move 6 from 1 to 5
move 10 from 2 to 9
move 1 from 4 to 5
move 4 from 6 to 9
move 3 from 2 to 4
move 2 from 8 to 2
move 15 from 4 to 5
move 1 from 2 to 8
move 1 from 2 to 3
move 2 from 8 to 7
move 3 from 7 to 1
move 1 from 7 to 8
move 3 from 5 to 9
move 1 from 7 to 1
move 21 from 5 to 2
move 3 from 9 to 1
move 5 from 1 to 4
move 1 from 3 to 4
move 1 from 8 to 5
move 1 from 8 to 9
move 1 from 5 to 3
move 5 from 2 to 5
move 5 from 5 to 3
move 7 from 9 to 2
move 3 from 3 to 6
move 2 from 1 to 4
move 1 from 3 to 4
move 2 from 3 to 2
move 25 from 2 to 1
move 11 from 9 to 2
move 9 from 2 to 8
move 4 from 9 to 5
move 6 from 4 to 3
move 3 from 3 to 5
move 9 from 8 to 2
move 3 from 4 to 3
move 1 from 9 to 4
move 4 from 3 to 8
move 2 from 8 to 1
move 3 from 5 to 9
move 2 from 8 to 1
move 4 from 2 to 9
move 6 from 9 to 4
move 1 from 9 to 2
move 1 from 6 to 4
move 3 from 4 to 3
move 2 from 3 to 9
move 3 from 1 to 9
move 2 from 2 to 7
move 2 from 7 to 2
move 2 from 3 to 2
move 5 from 9 to 7
move 2 from 7 to 2
move 28 from 1 to 7
move 1 from 1 to 9
move 10 from 2 to 5
move 1 from 9 to 5
move 14 from 7 to 1
move 6 from 1 to 6
move 12 from 7 to 9
move 6 from 1 to 5
move 1 from 3 to 8
move 4 from 7 to 1
move 4 from 4 to 8
move 4 from 6 to 1
move 1 from 2 to 8
move 1 from 2 to 1
move 1 from 6 to 1
move 5 from 9 to 8
move 16 from 5 to 7
move 2 from 7 to 1
move 6 from 8 to 1
move 2 from 9 to 4
move 2 from 1 to 3
move 1 from 6 to 8
move 2 from 5 to 3
move 3 from 5 to 7
move 4 from 8 to 7
move 4 from 9 to 8
move 6 from 8 to 6
move 10 from 7 to 8
move 1 from 9 to 1
move 11 from 7 to 6
move 2 from 3 to 9
move 1 from 3 to 4
move 4 from 1 to 2
move 3 from 2 to 3
move 1 from 9 to 1
move 3 from 4 to 2
move 9 from 6 to 4
move 2 from 3 to 5
move 8 from 4 to 9
move 4 from 1 to 8
move 3 from 8 to 2
move 2 from 2 to 6
move 1 from 7 to 2
move 11 from 6 to 5
move 7 from 8 to 6
move 7 from 5 to 8
move 5 from 8 to 5
move 1 from 2 to 5
move 3 from 5 to 7
move 8 from 5 to 6
move 2 from 4 to 5
move 1 from 7 to 9
move 2 from 3 to 8
move 3 from 8 to 5
move 13 from 6 to 2
move 2 from 8 to 5
move 5 from 1 to 9
move 3 from 6 to 4
move 5 from 5 to 8
move 1 from 5 to 4
move 4 from 1 to 4
move 1 from 7 to 2
move 12 from 9 to 7
move 2 from 9 to 1
move 3 from 8 to 3
move 1 from 5 to 4
move 3 from 8 to 9
move 2 from 4 to 7
move 4 from 9 to 5
move 5 from 4 to 9
move 3 from 9 to 2
move 1 from 9 to 4
move 1 from 9 to 3
move 12 from 7 to 4
move 1 from 4 to 8
move 1 from 8 to 1
move 1 from 5 to 4
move 2 from 3 to 5
move 11 from 2 to 3
move 4 from 5 to 7
move 7 from 7 to 2
move 1 from 1 to 9
move 1 from 8 to 3
move 1 from 9 to 1
move 2 from 1 to 5
move 2 from 5 to 4
move 1 from 8 to 1
move 2 from 5 to 8
move 5 from 1 to 9
move 11 from 3 to 9
move 1 from 3 to 6
move 1 from 6 to 3
move 3 from 3 to 6
move 3 from 2 to 6
move 13 from 9 to 7
move 2 from 6 to 1
move 8 from 4 to 9
move 7 from 4 to 2
move 2 from 8 to 6
move 1 from 1 to 9
move 5 from 2 to 1
move 2 from 1 to 3
move 10 from 2 to 8
move 3 from 9 to 3
move 1 from 7 to 4
move 6 from 7 to 5""";
}
