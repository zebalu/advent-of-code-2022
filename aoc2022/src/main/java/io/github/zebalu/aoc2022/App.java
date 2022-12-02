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

import java.util.ArrayList;
import java.util.function.Consumer;

public class App {

    public static void main(String[] args) {
        var days = new ArrayList<DayData>();
        days.add(new DayData(1, "Calorie Counting", Day01::main));
        for(var day: days) {
            System.out.println(day.header());
            day.method.accept(args);
            System.out.println(day.footer());
        }
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
            String title = String.format(" Day %02d: %s ", id, this.title);
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
}
