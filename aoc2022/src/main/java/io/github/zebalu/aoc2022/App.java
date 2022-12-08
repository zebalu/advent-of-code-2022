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
package io.github.zebalu.aoc2022;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class App {

    public static void main(String[] args) {
        var days = new ArrayList<DayData>();
        days.add(new DayData(1, "Calorie Counting", Day01::main));
        days.add(new DayData(2, "Rock Papper Scissors", Day02::main));
        days.add(new DayData(3, "Rucksack Reorganization", Day03::main));
        days.add(new DayData(4, "Camp Cleanup", Day04::main));
        days.add(new DayData(5, "Supply Stacks", Day05::main));
        days.add(new DayData(6, "Tuning Trouble", Day06::main));
        days.add(new DayData(7, "No Space Left On Device", Day07::main));
        days.add(new DayData(8, "Treetop Tree House", Day08::main));
        Instant beforAll = Instant.now();
        for(var day: days) {
            System.out.println(day.header());
            Instant before = Instant.now();
            var origOut = System.out;
            var measuring = new MeasurerPrintStream(true, StandardCharsets.UTF_8, origOut);
            System.setOut(measuring);
            day.method.accept(args);
            System.setOut(origOut);
            Instant after = Instant.now();
            var measurements = measuring.getDurations();
            for(int i=0; i<measurements.size(); ++i) {
                System.out.println("Solution "+(i+1)+": "+measurements.get(i).toMillis()+" ms");
            }
            System.out.println("Total time: "+Duration.between(before, after).toMillis()+" ms");
            System.out.println(day.footer());
        }
        Instant afterAll = Instant.now();
        System.out.println("Whole execution took: "+Duration.between(beforAll, afterAll).toMillis()+" ms");
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
            int preLength = (80 - title.length())/2;
            for(int i=0; i<preLength; ++i) {
                sb.append(chr);
            }
            sb.append(title);
            for(int i=sb.length(); i<80; ++i) {
                sb.append(chr);
            }
            return sb.toString();
        }
    }
    
    private static class MeasurerPrintStream extends PrintStream {
        private final List<Instant> measurements = new ArrayList<>();
        public MeasurerPrintStream(boolean autoFlush, Charset charSet, OutputStream outputStream) {
            super(outputStream,autoFlush, charSet);
            measurements.add(Instant.now());
        }
        public List<Duration> getDurations() {
            return IntStream.range(0, measurements.size()-1).mapToObj(i->Duration.between(measurements.get(i), measurements.get(i+1))).toList();
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
    
    
}
