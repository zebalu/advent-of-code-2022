package io.github.zebalu.aoc2022;

import java.util.Map;

public class Day25 {
    public static void main(String[] args) {
        System.out.println(part1());
        System.out.println("Merry Christmas!");
    }
    
    private static final String part1() {
        var sum = INPUT.lines().mapToLong(Day25::toDecimal).sum();
        return toSnafu(sum);
    }

    private static final long toDecimal(String snafu) {
        long num = 0L;
        long power = 1L;
        for (int i = snafu.length() - 1; i >= 0; --i) {
            char c = snafu.charAt(i);
            num += SNAFU_TO_DIGIT.get(c) * power;
            power *= 5;
        }
        return num;
    }

    private static final String toSnafu(long value) {
        StringBuilder sb = new StringBuilder();
        long num = value;
        while (num > 0) {
            long r = num % 5L;
            sb.append(DIGIT_TO_SNAFU.get(r));
            if (r > 2) {
                num += 5L;
            }
            num /= 5;
        }
        return sb.reverse().toString();
    }

    private static final Map<Character, Integer> SNAFU_TO_DIGIT = Map.of('0', 0, '1', 1, '2', 2, '-', -1, '=', -2);
    private static final Map<Long, Character> DIGIT_TO_SNAFU = Map.of(0L, '0', 1L, '1', 2L, '2', 4L, '-', 3L, '=');
    private static final String INPUT = """
            21211-122
            1=--02-10=-=00=-0
            1=-2
            1-1-1-===2=0--1-
            1211=
            1=22=-=
            1-0-=0210
            2-0-02
            1=1--1=-0210---=-1
            200-0=02--2
            20112=02
            1201--=-022
            1-100==
            1-
            1=-1=22===200101-2
            1==0010221-=22--0-02
            1002=11022
            1=02
            222=---112-=21=02=
            21==10--=01-1-=1
            1===--11=102
            2==2=0022=1=102
            101221=-2-=-00-12
            10=12220==---
            1-2
            2--01112
            11=01-=1002-
            1==-00-=10
            10=2==-=
            10-1=-=20-2-=
            1=1
            2120-
            2-2-=-0==-
            1-2-22=001-=-000
            21222-2222=102-2--
            101010
            1=0110
            1-21
            10
            1=022==2-
            102-21==010
            20=-2-
            1==-==
            1=-0212===101
            111202220=12-1-=-2
            1-11110-==0=0-0=2
            2=2-0=0=02-2=-0-0=0
            10=22=11-1-1-21-021
            1212
            20=1=00202-==2--
            1==2
            100--
            122=-
            2=220010
            202-222=212100-110
            20021=222==1--==-=
            1==2212=-
            1=-11--221===1==
            21=21021-=1
            2-
            11=02-=-----1=0=
            10=2-00200
            111-
            12=1=12121==-=-=-
            1==0-
            2--2-0=0-=2=21
            1==02-0=022-1=2-
            1-112
            1--01--2=2=
            221=1=20-=0-==
            1=2-0-21--
            1=11
            1120-=00-==2=
            1-=1-02101-02
            221-1=
            1---=-=1211
            22=
            221-=21=2010
            2-2102=
            2--121=11-011
            1-1-01-1=2=001
            2-=-=22-=01--
            1-0=20=22
            2002122211=02
            1-2-21-21211012=
            2=0200201=
            2=-102-
            1==202=-2==1=
            1=0=010120=
            2==1100-01==-0
            1220112102111=0
            1=10=1-=-2=
            1=-200020=-1001
            11=11-=2
            1==01=11=0=2-2==
            2=0-0-1=11-222=
            1221=0-1=
            1-=1-022
            10-1=22---021=1
            1011-1-=22-12
            1=0-122=-1==
            2111-0=20=
            1==01100-=
            200111102
            12=0-2=2112--121=1
            2=01
            1==221=2211--2011""";
}
