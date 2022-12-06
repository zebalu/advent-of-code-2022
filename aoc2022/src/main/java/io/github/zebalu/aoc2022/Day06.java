package io.github.zebalu.aoc2022;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day06 {
    public static void main(String[] args) {
        System.out.println(firstWithoutRepeat(4, INPUT));
        System.out.println(firstWithoutRepeat(14, INPUT));
    }

    private static int firstWithoutRepeat(int length, String str) {
        return IntStream.range(length - 1, str.length()).filter(i -> isOK(i, length, str)).findAny().orElseThrow() + 1;
    }

    private static boolean isOK(int index, int length, String str) {
        return str.substring(index - length + 1, index + 1).chars().mapToObj(i -> (char) i).collect(Collectors.toSet())
                .size() == length;
    }

    private static final String INPUT = "bgdbdsbsbsttldddzzwnzzmpzmmzmqqcgglrglgbbbtmtddrssjtjqqtrtqtqppcvcddswdwbwlblfljfljlhhpchcfcgfcfwfllfccjlcjllbvbgglccznzrnzzvfzvffvzvccnwnnrtrqtttzmmndnqnvvlwvvgcclplccbggcscqscqcnndwdlwlvlffdssrzrtttbvvqttfdfrddhthbblnlmlmqmhhpvpcpwpccmdddbcbcgctggsstwwbcwwqllchlcccfwccvjcjhhvggnvvcssjwjhhdvdhdcdhdqhhwrwcrwcrrjzjccqhhvnnrppsqqplqqcvczzlpprlrqqvpvwwstwwzqqsnqqsrqrlqlggzztzhhvbbcncwnwhwbbqpbqpqdqsqjqrjrddpjpwwvlwlnwwmpwwnmmzgzqqdcdnnqllghhzwhwwggljjwswgwffsbffbggzfzcfzczpptrrnwrrcqqcwqqdttzqzjzqqltlggwlglgwgrgfgnffgqffnlntlnlccjwjfjnfjnffqvfqfcfsslwswfwvfwfvflfhhntngnhggqbbsggchghfggcmgmsggshsmmqffjpjnngwwftwffgqqvmqqslqslqqdzqzhzbhhdzhhllnzzlmzzltlwwsmswwtswtssvqsqhssfdsdtdjdqqqrffqjffrzrppjpgjjpgpmmzbbrcbbprbpbnpbpcpsspqqfggclcpczzngznzvzlvvcwvwcwdwcddhdbdwdhwhllpjllrmmhbmbgmgmpmhpmpqmpprggvsgvsvbsbbqbmmjdjfddsnnqpnnphppsbpsbpsprpjjqhhvrhvvdhdjhdhwdhhdjjrlrbbzhbbjljhllttrccbdbffznnfmnfnvfvrvbblmltlmmlbmmdqmqnmqqmmhchvcvpccnrccqhccshchshzshschcffpwwbdbqbbjhbhmbhmmzzzcscddbsbnnpzpfzzrfrlrmmzzhshbbtjtbtzbbddrwwhchvhtvvmhvmmwssqqzrzdrdqqntnjnrjrbbgqgzgbzgbzgbzgbbqtbbbqjjgvglvlzlqlbbjwjddjtjbjffcsfsnsnpspgpnnglnngrgqgbqgqtgtfggczzmbbvqqrssdqssgrssdzzvjvccbbgcgppgwwtmwwrnwnfwwnnzmzvvvmnnvdddrmrbrtbrbrvrqqcbbgjgfjfcfttgrrjmrmrttbnnsrnsnzsnzztmtgtjgjljdjtjrtrddtbtjbtjbtthhtmmllngnhgnnhthssgffljlnjllfvlvslvsvwvcvfccgqqtsqtqggbjbtjjtjvjtvtvddttqzqczzcvvdtvthvvfrfmrrclrlflbbhhcllfbbcwwgmwmnmpmgpptnpnjpnjjbqjqjddfdfjjpzzjnnvzzwtwpwfppzhphbbmsbmsbmbqqfpfsscttfrtrzzrznrrrbgbdbtdbdjjsmjmttlbtthbttrprpjppsbsjbjcbclchlhhlttntznnzfnfgnncsncnmmdndnlnclnndqnqssbsjbbrzrtzztszzvnznqqpnpbpnpvnpnhpphvpvfvhhrvhrrpttctjcjvcvzcvcscttqptpffscfsccqhchhdcdczcnzcncnhccfrrbprpbpqppdccjhjvjmjpjmpjjvfvrrwppgjgjjdgdmgdggcpccbrrgssbsjbsjsfscfcvvcrvcrcttbtffpqphpchclccwhcwcbwccbzzlvzvffbrbrbjbwjjqvgtcfnhtjvrcwbfjdbvgtqbvmbtscwzrwdfmwtjvswgrvncmftgmppvlcwjpnpffggrmvgtfqgqmhbhpslfwdfvfmbfndrmgfhdbbtdgvnslzpdfvdttqjpcnbzsjcvrprgrhpglwfwtdcbgdsjhnqjntjnsjcgwccjnvvngfpvqwvnclcsvhmwsrccvbjnnrjspwqdvjpfnfvbsslngzpdgjrcsnqfvdlsqdhdllcndshglztgrrjnptqfvllshmhbgdszvmvqdntpgzdvhstgrppwpdtdqvzsfgqfrgmmjqcsjhvrlmnjvfjghlvwbnqcggpqtrjztfzshnqpzznvlqcmnzvrwqlcbnbpwmljpvdzhbvbgtdjlzflsvzlcqdnsgzfjlccvjclqlzdhqzzrscttjmrvjvcqzvtzqlmsssnfcfmvcgmqjjwdnhtvlqrgdvlbbrffmrpnfsmwwwbnwclrgbfnzlbqvjfqjlfvfnfrhdqstddwnwrmsdnvzwfjfgpwcrfqqzbdwwtzprvqtgvtzbttlhcdczlhvlgrbptztswftvnjmgrnbwpfwnztvqmqbznvnllgjmqrwprvwtnptlbfwbblzsblptwpdwgcvwsbmbrtqfvjsbzfvsfvpwfwbnnfcsddhsnwnvvqthjdgvzgjprtqmvhdqjqhgqppqqcpzfcwzcmrslftgrvbvdsdgfzfmvvcqzcszfwdvghdnlwwpddzdsqsdqvvrwhphbqvcbjbtnqgnqqdsqcmrllhmdvpffnqmrgfddjbrjwflshzswvjtmqgqmzvcnlctvdpjhzzlgpzgprjncrscnlmdhvdqpfllsqgstmssvlzmrtjmgwppfqjsrfmlnszdnhngzhtbbnsnvmtzpfsdcsvsvvjnfnzhrzmvlhrbslrsbgwwcvrzpgcnmjqnvgmzmlvpccrmggtzzhsdtbbcdnpdlnbztgjhttmqdhjphcrbgjtctqmgbfmflgqztztcjqvgsscrmwfbvnrnbgbjgqmwdzhwwnddwgrprhvlgftcbnwjqmcgczpbhfqcqzdbdwhmzfmgvcjdsfzdbrzjjvfrvftdblnlhpbqvdprnsjdpznbbgqpgnnjmcnsbszfwblthtwlwrdphjltclmqnbpcgngdnfpltttsrvdmhrcvlqfplqmqvslwgcbrwrmchscczrfgspwjtdqtlbddbsclrlbmhdzqdrgjfsgldfjmgcglbgjhmghntndqcbgqwmdvczbwgcctzvcrsqqctwwddfhhfhwlsrsljpnrlqtbdzprjbfrjgztwbpnfqnlftzcgrpmpcnljhscfbsqzbsgwqcgbvctnhswhrsmwjgcccdsnbscwllmpstpnrccjspnjqmtgcsgbjzpfvzjrhlvnfblqmcmgcrvnpchwhlsqsbhzhsgdvwmdcwphwccvzmmqqjrvqwphbnmddzcmggmbsqrhbcqmdlgvccbhhmhwdjhhhcwnffthmgfhpltqbhnvdqfrzjdvlppqhzfdgbzbrtfllsbvjjcgbwsbcmfrbjtvzqsntzdzprnpmfpfpgmfprlbcdqbdzjsfjbtczdpdnhlwdhmwsjtvmztbhdbbdgvrtbqsqbsnwjjhlslzcblrwlfpzqlvdvmgqhrpjmbjbntmjsjvgsmdsnctlgtnlqgfvhwqbjbrczpfzmzwgvrphfmnnhrlzwzgthzqnzzmptppzdszlcpjjvbpjbtjfrqtbnpnwsdglbbjftvngcghjlnsqwspmmfdpslsmqtpngbtvvrvbqqdsphfhvsnmhprfclnjmfrtqnlqcbmfrggbstwdbwsvtpvflvfgqltmqjpnfclbwtlwhmqrmzcrbztstgpjrdsnwpqrcnvvnnnszlrtpqjtsnbjrdcthrzrtccgcvnnlzfjlcdnzzqclvtncjbznrlpnzhvcwmrfrzpcldfmfzfpchlmddgvcfdqdhzzdtwhsfcvsthtmqgvhzdpjcgwsmrvwsnqmhdnfqdrrnmjwcpjjftfdhvwrwwtvptzfrmgffdcrhvcmccfqctswzzmlsjvdjzgjgndhmmrwvwmmtrnpgsnmtcqdbdpqjmcddcrbcfmmccnvsfhwtvfhsjfmlfttspfghpfggrffnrwjggqwggrmpzscprvdzmzhwjjcsmpsltzwgchttwpngrlptprqnjzzpbpbcvrclggtqwlcwdpjpnjrhtgqwsvhsswwqtlnglnqnvffrgmlbzthvnhrzvsvclgdmmjzrpfv";
}