/*
     Copyright 2022 Balázs Zaicsek

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package io.github.zebalu.aoc2022.main;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import io.github.zebalu.aoc2022.Day01;
import io.github.zebalu.aoc2022.Day02;
import io.github.zebalu.aoc2022.Day03;
import io.github.zebalu.aoc2022.Day04;
import io.github.zebalu.aoc2022.Day05;
import io.github.zebalu.aoc2022.Day06;
import io.github.zebalu.aoc2022.Day07;
import io.github.zebalu.aoc2022.Day08;
import io.github.zebalu.aoc2022.Day09;
import io.github.zebalu.aoc2022.Day10;
import io.github.zebalu.aoc2022.Day11;
import io.github.zebalu.aoc2022.Day12;
import io.github.zebalu.aoc2022.Day13;
import io.github.zebalu.aoc2022.Day14;
import io.github.zebalu.aoc2022.Day15;
import io.github.zebalu.aoc2022.Day16;
import io.github.zebalu.aoc2022.Day17;
import io.github.zebalu.aoc2022.Day18;
import io.github.zebalu.aoc2022.Day19;
import io.github.zebalu.aoc2022.Day20;
import io.github.zebalu.aoc2022.Day21;
import io.github.zebalu.aoc2022.Day22;
import io.github.zebalu.aoc2022.Day23;
import io.github.zebalu.aoc2022.Day24;
import io.github.zebalu.aoc2022.Day25;

public class AdventOfCode2022 {

    public static void main(String[] args) {
        var days = createDayList();
        Queue<RuntimeData> statistics = new PriorityQueue<>();
        Instant beforAll = Instant.now();
        for (var day : days) {
            System.out.println(day.header());
            Instant before = Instant.now();
            var origOut = System.out;
            var measuring = new MeasurerPrintStream(true, StandardCharsets.UTF_8, origOut);
            System.setOut(measuring);
            day.method.accept(args);
            System.setOut(origOut);
            Instant after = Instant.now();
            var measurements = measuring.getDurations();
            for (int i = 0; i < measurements.size(); ++i) {
                System.out.println("Solution " + (i + 1) + ": " + measurements.get(i).toMillis() + " ms");
                statistics.add(new RuntimeData(measurements.get(i).toMillis(), i+1, day.id, day.title));
            }
            System.out.println("Total time: " + Duration.between(before, after).toMillis() + " ms");
            System.out.println(day.footer());
        }
        Instant afterAll = Instant.now();
        System.out.println("Whole execution took: " + Duration.between(beforAll, afterAll).toMillis() + " ms");
        printStatistics(statistics);
    }

    private static void printStatistics(Queue<RuntimeData> statistics) {
        System.out.println(RuntimeData.headers());
        int pos = 0;
        while(!statistics.isEmpty()) {
            System.out.println(statistics.poll().format(++pos));
        }
    }

    private static ArrayList<DayData> createDayList() {
        var days = new ArrayList<DayData>();
        days.add(new DayData(1, "Calorie Counting", Day01::main));
        days.add(new DayData(2, "Rock Papper Scissors", Day02::main));
        days.add(new DayData(3, "Rucksack Reorganization", Day03::main));
        days.add(new DayData(4, "Camp Cleanup", Day04::main));
        days.add(new DayData(5, "Supply Stacks", Day05::main));
        days.add(new DayData(6, "Tuning Trouble", Day06::main));
        days.add(new DayData(7, "No Space Left On Device", Day07::main));
        days.add(new DayData(8, "Treetop Tree House", Day08::main));
        days.add(new DayData(9, "Rope Bridge", Day09::main));
        days.add(new DayData(10, "Cathode-Ray Tube", Day10::main));
        days.add(new DayData(11, "Monkey in the Middle", Day11::main));
        days.add(new DayData(12, "Hill Climbing Algorithm", Day12::main));
        days.add(new DayData(13, "Distress Signal", Day13::main));
        days.add(new DayData(14, "Regolith Reservoir", Day14::main));
        days.add(new DayData(15, "Beacon Exclusion Zone", Day15::main));
        days.add(new DayData(16, "Proboscidea Volcanium", Day16::main));
        days.add(new DayData(17, "Pyroclastic Flow", Day17::main));
        days.add(new DayData(18, "Boiling Boulders", Day18::main));
        days.add(new DayData(19, "Not Enough Minerals", Day19::main));
        days.add(new DayData(20, "Grove Positioning System", Day20::main));
        days.add(new DayData(21, "Monkey Math", Day21::main));
        days.add(new DayData(22, "Monkey Map", Day22::main));
        days.add(new DayData(23, "Unstable Diffusion", Day23::main));
        days.add(new DayData(24, "Blizzard Basin", Day24::main));
        days.add(new DayData(25, "Full of Hot Air", Day25::main));
        return days;
    }

    private static record DayData(int id, String title, Consumer<String[]> method) {
        public String header() {
            return appendString('#');
        }

        public String footer() {
            return appendString('*');
        }

        private String appendString(char chr) {
            StringBuilder sb = new StringBuilder();
            String title = String.format(" --- Day %02d: %s --- ", id, this.title);
            int preLength = (80 - title.length()) / 2;
            for (int i = 0; i < preLength; ++i) {
                sb.append(chr);
            }
            sb.append(title);
            for (int i = sb.length(); i < 80; ++i) {
                sb.append(chr);
            }
            return sb.toString();
        }
    }

    private static class MeasurerPrintStream extends PrintStream {
        private final List<Instant> measurements = new ArrayList<>();

        public MeasurerPrintStream(boolean autoFlush, Charset charSet, OutputStream outputStream) {
            super(outputStream, autoFlush, charSet);
            measurements.add(Instant.now());
        }

        public List<Duration> getDurations() {
            return IntStream.range(0, measurements.size() - 1)
                    .mapToObj(i -> Duration.between(measurements.get(i), measurements.get(i + 1))).toList();
        }

        @Override
        public void println(String s) {
            measurements.add(Instant.now());
            super.println(s);
        }

        @Override
        public void println(int i) {
            measurements.add(Instant.now());
            super.println(i);
        }

        @Override
        public void println(long L) {
            measurements.add(Instant.now());
            super.println(L);
        }

        @Override
        public void println(Object o) {
            measurements.add(Instant.now());
            super.println(o);
        }
    }

    private static final record RuntimeData(long runtime, int part, int day, String name) implements Comparable<RuntimeData> {
        private static final Comparator<RuntimeData> COMPARATOR = Comparator.comparingLong(RuntimeData::runtime).reversed().thenComparingInt(RuntimeData::part).thenComparingInt(RuntimeData::day).thenComparing(RuntimeData::name);
        @Override
        public int compareTo(RuntimeData o) {
            return COMPARATOR.compare(this, o);
        }
        public static String headers() {
            return String.format("%2s %8s %8s %8s         %-42s", "#", "time", "day", "part", "title");
        }
        public String format(int num) {
            return String.format("%2d %8d %8d %8d         %-42s", num, runtime, day, part, name);
        }
    }
}
