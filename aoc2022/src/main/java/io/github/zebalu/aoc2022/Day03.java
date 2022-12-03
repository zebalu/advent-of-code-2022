package io.github.zebalu.aoc2022;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class Day03 {
    public static void main(String[] args) {
        System.out.println(solution1(INPUT));
        System.out.println(solution2(INPUT));
    }

    private static int solution1(String input) {
        return input.lines().map(Day03::commonCharsInLine).mapToInt(Day03::sumChars).sum();
    }

    private static int solution2(String input) {
        var lines = input.lines().toList();
        return IntStream.iterate(0, i -> i + 3).takeWhile(i -> i < lines.size())
                .mapToObj(i -> commonCharInLines(lines.get(i), lines.get(i + 1), lines.get(i + 2)))
                .mapToInt(Day03::sumChars).sum();
    }

    private static Set<Character> commonCharInLines(String... lines) {
        return Arrays.stream(lines).map(Day03::toSet).reduce((a, b) -> {
            a.retainAll(b);
            return a;
        }).orElseThrow();
    }

    private static Set<Character> toSet(String line) {
        Set<Character> result = new HashSet<>();
        for (var ch : line.toCharArray()) {
            result.add(ch);
        }
        return result;
    }

    private static int charToInt(char ch) {
        if (Character.isLowerCase(ch)) {
            return (int) ch - (int) 'a' + 1;
        } else {
            return (int) ch - (int) 'A' + 27;
        }
    }

    private static Set<Character> commonCharsInLine(String line) {
        Set<Character> r1 = new HashSet<>();
        Set<Character> r2 = new HashSet<>();
        for (int i = 0; i < line.length() / 2; ++i) {
            r1.add(line.charAt(i));
        }
        for (int i = line.length() / 2; i < line.length(); ++i) {
            r2.add(line.charAt(i));
        }
        r1.retainAll(r2);
        return r1;
    }

    private static int sumChars(Collection<Character> chars) {
        return chars.stream().mapToInt(Day03::charToInt).sum();
    }

    private static final String INPUT = """
            NJvhJcQWTJWTNTFFMTqqGqfTmB
            VwVzPldRZVLVRmfsvfjvqfmm
            ZDPDHZHVcvDhbvnv
            FHHwHBzzVCWWmmCzCPrVmgBwbLTtRFFbbbttRGRLjTcLpbbT
            vhZZvdsNSdSMdNvjncppCLcLnGnj
            CDZZsNZMZqdNSdlNZCqrzPHDzgrgzwVVWwmwwm
            ndlndntsFJntFvccLjjLrjBShcBBfc
            GpCGHzVwmmzqQWSSSfWHBhQL
            mpCMGGCZVzVwGGVwmJsZnFtZnTSTJtdsvl
            nCnPDGmDNmVCsVQDmGSWqvzchWSjjcWGqS
            gTnBRLpfTRnrTdZgdLfRdrThvqcvWWhFFWvcFSSgjqqzjv
            pfZfTMwrbLTTfsbmQtlVtHHnbs
            wNdSdsbTvTZMTvTv
            rrdRWdWQhFVdHWBGWQmmmnnMvCfmnhvmCmtZ
            rJrVDRWpGddpbSlNSlspPP
            chTNrthMMwWMTjfsmRzZszJpwm
            BLnFFCngbcBnbbldDlpRjGpmsCzGsGsRGmmG
            dqvnvlgbqtcPPMhH
            QcLNqZbCzJDQBJJRpwzRpdnRldgnpf
            GmmmvVGsHrWffrlwdCWd
            CMsFVVFjCmFStGQbbLZNBbJBcTjc
            LQVggbQvcLbQLHgvVLhWGGsChssrMWfzGccc
            qDnRTTRqJttPfWMChJhGslWlzh
            qRTRwPBTBtRZdnjnqqqnQVbjbNLFbbfLgVmgHLQm
            cZbzwCwZPlJcMLrNSNfHWNBBNZ
            vsQsDCqtsDhmtjVrBNWNjBHrhr
            TtDTGnvTlgbbRCGg
            BgBlplHlsgNNsJlVpBtPwJhMPRRQSSttRtSP
            bvhTnmdFTzddStwStQRddt
            ZnZDLvnvqZzbbhFzzmTbnFsVjVlNgsCCNVsVLpNWVgsB
            TdptqrrcVGhhzFtw
            DRnSfwJlDmmDDVGv
            RCSQNSCQZndwbcMqQrBB
            wvRlrlwVwwqzgbZRdCJBWfmdzCWfBdhf
            cFcsQpNtLLsGTtNGpMdPmDdPBmmBvJPWvDtC
            TpjssTFFvLLLcFFQpwbwwHngjHRrZRqZVH
            mqqddrPPcPmqPDlrQnjTrbvMvbHzzsjjpTvz
            gtBWgGgVhLGWHzMDztzstDHj
            hfWRhBBNBGgLNQDPwdNPcPdw
            LhQzdhhbTzpMhddhhhTzhnZcBFllHZFtrrHZHMHFjlHr
            mwwssqDvjptrvplr
            NCSgVDPDwmDgVJVpLfTznQJdhfLhnhQQ
            GzjzDhjhhZzcrRgQCBjBPBBjQCgT
            vHHHmntsbSgLwbsSmNHbwNbvpqPCBVppCpFTpTPTBtqWBCqV
            NJbwNSwdndvmvwhGhgzcfMcDJfgJ
            GncgDvvcMGnttjDvrgRRFSZZLZFWdJFJwGQwZBWZ
            bPqpChPfsshfZZBdZdLTFZ
            lNqqsClmbsNlPbHqPsmblmsrHdvdMngcVrjggvrvggRDcn
            bDvtgVVVpMQvjQWmQL
            rwTflmlfZJBBdQWQWjQqdM
            HsJJmZZwscHrwTrcRbzpcbPgtCSbgz
            CsCsRvshMjpbqCqf
            ncblgDBgtDmmmTlBgwlgbHHqMFHLqPDMHPHHpqWfFM
            TcBctSmTZTtSTzsZvsvJZRsGVb
            znznvngttwltzlLwhtThHbqHPvNbNHSSHmmNWHjP
            FBcLrRMFQpPqpPSpqHHW
            fRQMJZJfrcMcMVrQJJftnwCzVCltgTnstTVnVL
            MfLlRfCMrLzRlQgwNqQFcsGd
            jtTjjBTvbdqcGjqFcj
            vvShDSBDppzhCmzq
            plWMptTvfrnncvcRfwqzqLGhzhzThNzNNJqD
            jSdSHFPQQbdPCQCssjSbBmhJGNZZNGNqqJNBlJqqLh
            VCCCVCQgjdddjCgljCjbbwgRRttgrpftfWrgvpwpnf
            MWlbBcPjjvvjPWWMPqgRQZfJZDGGbRZJffQQwh
            HrHrnncHpzrJQJfVDQVR
            zzsSTtSTLzsspSdtTmHHmpmtFgqcgPlgFqWBqqqBMdWWvFlg
            nSqBbJbqlnBBClVZcMgZVgcP
            FQwrwHrRwWWFBRPNgNgcCGZZZC
            rWFWFTwpwwWzHrnDbfJDLDbBBbbz
            BMmNtLMMtFCNFNMvvLmcndpgcdgppPrgrGPPrgJD
            WVWWhbTtVnGpjrrPhr
            HWssSTHWfRHRsQQFLvfvFFCLCNMNlt
            sTmDsQffVrrLCjTFltTFWL
            BnwwQBJbJndMMRzMwCLlWlLWWCWLLtRlWF
            cqqBMcMqwnznMGzcvDmQhrvssHmPDVssrP
            pQGQGJDDrDVJbbfVzvvgPcCZwhZhncscZWWc
            SqMMlBBljMmRlchhPTqThCZnPs
            FMjMBmjRNFHQJJpHVhVDhG
            tHNNdBdNtBBBMgsMpsZm
            wVPzVvbwqzhrVqvjqzzsZpDsZDsZmsCPCgZgCM
            bVbvLThvvbrWqHmmnJLdHdJQLn
            PzTspPZpdLLDZTplPLpPDpvbfhnqNvqzfvNMzQQfNwnQ
            GWRHmjmFWMMSnhbhHw
            JWWcmtBrBtWBFWGJpsgTgldhLVLpJl
            DwLMDzLMhvMcwvgdVqWWlCVgvlqF
            TTSBBRpbStHZVgjWFldjRVlV
            SnbTBdJBmnpQzMPDMcMznr
            nNlMNBPPNtJQnbZhZsgSbh
            czzCjcwTdvSbgQNcgNQq
            VTdNdGDTzDTdlFFPtBrtLtDr
            FMbbfMlzvFsmgVZmmg
            SrNTHGmdSQDqLhtQhhgggs
            dRDTSDPPcHRdHGDHlwJBbmwljmMcfjbW
            sQgWLtqLtWhdqlpNZRpG
            blTHTjlvTCJnJvRZdGGhHHGZhFGV
            CCDlJclnCmbrmBMgcwcLWtcBsB
            vqPWWvqwwCFvFZfZPRFRrcGQrQwsDrNcrwnbDNcQ
            LVgJLSBBVtzTLzBMmTMJmLnnDNQcrsGbsQbNbrbDjs
            zggVSmmhVdfqFhvHWG
            WwdndGGmmmLwwwmRwWSncLRnZqZqhqZthBtqtBqZBgtdtvMH
            FfHHzlQQDsFzzrNsVTfttZvTvttTqqtbqb
            lQjFDNQFPjCsVCCDjGCwwSGGnccwcHppGp
            mrjggcFsFMjdjZRpSZpn
            NCqfLCFNbQPzPPlPzNfSRTRZdSdWWwndpqRSSd
            vDvzzbPQFNCFtllLLNMBhMcDHGBGMggMmcBc
            jhjlBvvnjbtDNPjtSjBDBbDNgHggrQrhghRQrqRrZcRwwqVg
            pLdTMsWdLLmpMdqZZdPdVqZgHPwH
            WLTCGmMLfPSlbGjlnnJD
            gtbwhgHbHgqqbgQthgQLtZZCRjMcjjnRnrRNJmMRJrNhRc
            bGWVTTvDvfpVFFBpvvVTdRDMJcrccCrJnMRnNnNCcc
            FVWTBsdvdTzTBFWssVQtLgSQtHqqPzPbqHbw
            dlzrPTSSjSrllzWhsvVmVtTRTWtf
            bJMpLGcqGhNbJQttVQmmvRWWsp
            qLbMwqqbGHFGzrlZrjhPHCrj
            rNrrffVlqqrfLlPpltcBBTTGRzzZRPRsBTcJ
            msbsmWSsMmQwjdMbWMhMhQmcRZRzGjTBGTBcBJBjCHJGcC
            FwWbvdhbmrsFrfrgsN
            rHjrQHdhdQrvSddcHWLssBSVVpBSWWWWWf
            JNfTGtqDwVWBMBMpwM
            qlltZgfJFvcRgcRjvc
            CqfcwfDqwwmRnnqmRdNRBTRTRrdGdNpTvF
            WVbzsZszBbrsvpdMpdQM
            tJhbVZHWLLHDgnSwnSSgHB
            TZCqqlTsqpZVVsZQJSBSLpLmppnJzmFz
            brSgNtGjjRjRRjDddDtrRJcJJbJmmwcmBmnPcJFwFB
            jgdRtMjNNjfqlMvShvSZSZ
            dJTdqCwMNCgqTQllGBdlGBmmmZ
            fcVfVcnbVfrwDLWVfncZBQPlBHRGljLZQjHGQl
            brwnnfSFDvfzCTqFzgMJTh
            njnsPBjjsrrnGLnbTTjGvcldQPCMllNzMvRQPCdd
            ggZgfZtmZVpqZqZWDgFmgqfCcQRcRcWhQcccQddMcvRQdQ
            tfqgggVgHpDwDtfwbGLJRjbLjsrLTj
            JmrfrmTlDWTfgQCdHCdpqBvQdD
            jsZtVzNsSNVQQHnBlVQR
            PljljFjPljSsLPtFLTTgTcFrrfMJmrrmrr
            hmGcmmndhmGnfmtGnDzFLwrFJQsQFzNFrNJG
            ZSqPlSWcWlbgqWVTVWRVZPrjQqjzjFNJzLsNJsLJNqNL
            RHcWTZbSMMMPgZcWgSWPPbVMDnBffmtdpDBddfnnvmCdfC
            vSJvsbFfJfvqCsTHJswssJnLTZjjhzrrzLrzLMrzhdjM
            pBNQDPcpmWDcBNgMMnZPVjdddnndhH
            QWlDgmpmgDBlGRgDDgffSqwSwGCwHfvqwSFJ
            jvlgvMJclPdGdtdcjMVmMHbFHFVHWHbZHZ
            CwhLzLhzQpnqfpfqDVHCHbsbDFZDmHmj
            LnBzfQjSzQrPvJvdSSrr
            wpcvcsqclDCnVCVvWfnZ
            BLRMRtbnbbBLNCjNCjVVZhbC
            rFgMPSRnrRpmqpJwqFDs
            LZQNQbMrZppLNLQplvlGLNvVmmmfjbwVCfjbwJwCmBCwfj
            ShTPRFtTHZPCsnwswsFwCF
            WtHRPdThSqZTRtDqtdRWTdpGDLLzrNczvzMGLlQLGDDM
            hdcffBvldjhCMljqPwWwWNwWdwqHZr
            LtQmbQRVsZQZMZPQSN
            tmMRsJMpDhjJzJhv
            wNQCMFCDQDBmrHmmRWrrHN
            SShLnfqpcqpSZSfrzJvRVrvfrrJH
            cRpqdGclpScltTQQtsFQMQsTCT
            NCjggZmgfBgnBmgWbcwcTFctcWWfvb
            HsDGthRGrtppSQpbFFJTVcJdFbTRvd
            rPDGhDDrSzZLtzBLZMCB
            RsBBMBsCBlFFCgRsBJzlMjMPNSdPhSrSrzLbmSDrDNmDSd
            pZHZZJpGHHHpTTHvTncZqVLdqLbhLrDLdhrSLLbLDDdD
            tGtwnJccvCtCffMBgt
            wbddvVjfwPhbjjbDbbvbjvTNCNmfHZfpCZRJNzCmJmnJNC
            BslcLtclZWsZJWNrRRNRpRmR
            BSLBlScGtFMcssMBBFGLlQZTDZQjPddVwwbTdvvdhTZb
            NSZHzmLZBnzHmLLzLSntDttDDtddhDtttDWW
            QgfjsrrvNNJwtMddcvcvtq
            jrfgfQpQrTTVLSNBClFV
            GQWcWWPPQRcrJQNDdRcDmmLCFSnqNSmqhCNvFnql
            zHfwjzpMjwZmCLqvvnlljC
            ZgtVZBtHHZtgQGgPrbPRJdPv
            TWdWpJTJTdgLWfWLlLFLrfrgBGsNqhGslBGHqSNqqBNshnws
            ZpQmjzbZZCjZCCCPZtttRCCwsBnHNssBHbShsshHqsGBqN
            RDRRPpPCzmZCtRpVVJFrfTfWFLLJggJrDv
            pDDFlglsvFMgntlTMMqNffmTdfddRM
            jhGJLVCHQpHGQCCzLjWdTTdZZdNdcRWNccWfNN
            jQjSGjrjCQLhzVSLSCSHGDpngbrnDFtFBwBglBnBvg
            wsLzstsgszcpcGLHGpcgcghlDBvQvjQvbFbQCbJBtCCJJv
            mnSqRSSqSRThWRnmWWRSJDFTFCFCblbBCFQFCjFj
            rZRRWqSSdZZfMVnZLspPsMgHpzMhHGPg
            mwHrCLSWWwrsHCHDDsVrsmhfFZFnSSBlFlgZbbgBglbggj
            GJdpcRtGJvNRdcPtdpJJdbQZfjfQBlnQBjnBtbfFnB
            qcPpqqzFzJqvPVCCmWrVwhrWrz
            jjMbvbhDvnRjNRGMmjbMZftSSwwwthJSffStctcwqd
            lTQrVlpCVvCcfdcSJqLVcw
            srHFWCHrFlrHlrsBsprljjRmDZZnmbDngNBgbNZv
            MgTlQJlTQJZWpgLrRssrVqqqpRts
            bBNbbzSSjMBPjzhMjsPtRVVRVPRqLttGGs
            SjHBbfjNCDfjZgTlZdMJnDJW
            lpThgTwtplhghgwhThqnnrdZctSZSjSZcRSRfbdrrc
            RBVBGvmBmfdrcvrbbr
            PmVGNGmmGRLLQwwLqTnglQ
            nHwnBwBTnFHQwRsMhwghmzcm
            GtprdCpdtqWdbqbrfdnPPszsWmRzRnShPszS
            dGptbCfCrlnVDBJNLDLLVDLQ
            CZtCjhTndCzqbCNq
            dwpGvpsmwGslDszrNNrzqDMzWMgJ
            vmcGccvpBVPTVTjTdTTTdZ
            jWZhvZLjZfCZDwrDrSSzJGhVdJccscGsgV
            blMBlRqqqgSJLBLcsJ
            blmHLmFMMMnRqLmMMFqHmfPDfjQDnCDDQrZvfCjvDr
            rnvnHrDLFZmMFLvrHQBMGQggBztzglplRl
            sbWWhdNzsshsfhcsjJJPPbWdtQGVGllRTRjRRgBgQlpRlppB
            PPCCwNWhPhNfWCzbqmFnDFFnCDLSrvZS
            GChNjwWlWJWTJZBggvdgnQgdhdnd
            HPsHfHHrpHDpFFrcSfsfpCMmQdntLBMgtmtBgDdLLC
            SqpPscpPzpSWzjlCjjCGjl
            nvgLvcLgvgvngbLprpJNTDCCRNVJrNPlDDTV
            WZsMtsffGQtMzWFqFmWmWsVNJNlDwwCDVRTwJlCCDVLz
            BQfGZGmmsMWFstWFmfMsfBccdncbpbSbvbbvHnLbpc
            tsmDsvswNZmcZTccfh
            zCTpGCbWBRWFWHGRFZJbMbJfnrhnhfMnnZ
            TzFGFBRLdpHHNNQddDQDvwQN
            fhBBpJgdHddjZQfmVmNzNNLmFN
            qvMRrvlbwqlbTTMBMvLssFNmVzzwFDmLLzVD
            TRSRWqRRMcBHhGHcdGgPGp
            lSjHmtmnpHStblnpSlHSrtmMzLWzqzqCZDDTzTTWqMFqCqVV
            sLRLLfPPRQfCTqqVVqFT
            dNJgRPNQNsJJhBRvdJvQvNNsjSrrSmrcctpbpHtBrBjLjmSH
            nwFwpppjfwSlpLTsqsTgNshhjM
            ccBRGvtsmgGNPqNNGP
            BCcJHvssdcWBCVmVHSSrZrwVzblpwbzZnf
            rcfQRrBPPczjcRBctZDNlnVNHbgZGjVDjN
            TvMsFJGSFMhJnNZlwVVnDNTZ
            qhSqqmqLCLhFdJLqSvLhmQRQRWcRPczPtzrCrWGRBp
            JVhdPhsFPFqLDBHVdHLPvhHDCMwcgJJwbwRgnnCMbwGwcmGC
            fzjzpTZTQQQLwCbgGgbMmQcR
            jzNpTzfSZtfNSWZlVVtdFFFDHHqLHVqv
            TwSNnSnSGVTpNppGlPTlTcVqQrRhVBqdqBRqZqQZqQ
            DcDCMfDbCMHJdrRBqbdjRBRZ
            gvftMCJHcHfCDmDLgfMmMmmWlwWnWsTTwlGTlWTwppNlGL
            pbGMbllDQPhhWWQDpPgVGlMCvRRrQLcCCcfBBQzLBcvQBv
            wqnJjSmjrstdqwwFBLcRsBRRszzLFC
            qwdddTJTdHtjndqJqHZHmwVWGpDbGTlbWWpWWrPGhhhM
            WGllqLjjLCpSffmBmvfpHs
            dnrQwZzRTdZwnCThdzzFTVmcBHBJBmsHfBPHcfvcSVHs
            QgQrzCdrTRCZzrZLbjGLqNMWGgNNLt
            sgPnhPPTTPTTwlJfwNHlqcfs
            LMCpFbLLbRpMGbMcCFLVlNlNqrHqVfbHHwNDwr
            GjBcCCtWMtMRZTSvgWQTngvg
            BCMtJJMpRDlMMvBJBBnfjtcjPhPmZgnhgdcf
            NrsrsqFNvrVLVGVrsHsqFgfmcPGdcmhfjdPgfjcnZd
            zFTzsNqHqFssLVLQqNTFbsBDwCCwvWlDwRMRCTRBDMDS
            zQtLgvggSRtgvVRtLvvnzdnjnGwGdmmrlpnlGz
            JssBFpqsDqPNnlWWjrrjqrnj
            DHDFBNDfPbJBsFHNMPvpvStQvMRVTtgVTVtv
            FvzttFvBTJJzLbvwhCnnVnWwjCnBNC
            mQdZgZPDPdPPSsMSQPdZgCwVGmnwnWpGnGhqNWjWCG
            ggdDgfQSdcjtFHjlLJfF
            ghcgScNNSsCvGSzmpVFlZbrzcFcV
            MWWRLRqqqdQwTtLjjmqMlFpFlzVnbFVDwplFzlDr
            LHMHqdHWjdQMdMtLHHLtWjJRsGCGSNghmSvPBJBNhsGfvfGP
            CbVqqqDbcbMHnnDqcCbrRFCfBvvwGjzrBwQGzrwwBjGwBQ
            sTPmpNWdWPTJssSSLPfNljjBvflGtjwwBzMG
            mmWgmgSZLTLMZWpnhqZbhFFCnhqnnn
            QQmjmZqnmQrfTZlbbcVbBcfbHfzf
            vpdSNShNppFdSRtdGBqvJBDlDzqbPPHVBH
            tRNSNRFhNpSRhFRMFtGhRGswLZZsZqWnmrmZwqwsTZmmmQ
            gGWCllFCGWtGGWdlGlWNZdwpnnSbwpMvpphZpndn
            RsshDDLcQVMSJQwJwnvw
            HVPzrPcDNhPFGhPC
            jtHQGHjGGtdTLjnqTQlmvRPRPBBwRBnFPPWP
            hZbzNzVrczZzcbNssVspZZVvBwbmPmJPWmvbBRvPlmvRJF
            fzNVDsZMhzpVhpVhlZcMNfcDDdQTLTjGDTCqGCjtSQHdHL
            GrbFggGrTrzSrgfwJjdTmwmNJZJd
            VMPQplPDptchwdsjmlml
            MqMWtBDPPWDWHQtvqQtWPjbzCGLgSBgGbzgrzFgnnz
            fcJccCcwcDfcpbRnCfWJnQJqtqtqPQdsGdgPsgTQqg
            LSjVMhzSFFrljdNbltNGtgdqQq
            MMhSHFFMLzBWDcHHcfcHwb
            rwmWtJWMwSNRJMtwNmMrrSsmtTjjlgqnTqZZZPlHnTngTTgn
            BGqGqqFBFggjjdGHlj
            QDhhLbDQCDFMNcmhRhqJNW
            BnRnRvMnLGLSCHvvSnlRfWbbTNQJsJsbNbJTBfQT
            tzMmmMwjhcpFjDmMcptrcjzFQggfQPTsWsfgNbbgfhJbPhQT
            FdzcrtDwDMtcwtFGRZdRLvdnHRSZZv
            HVpsSpvjpNjsBmbGFBnMNnDM
            WRRWhZtfrVtLJrBZMnDmDbnZBTGF
            thhPLzWzhzwPtLRLWrQlpPvvClcVcCppSvpl
            lZPbhnZLRPnnPZZPdlGMBWcBMgMQHBBcvvvzBL
            jpFjmwwwCDDbsjvjjgcvQgcNBQ
            rbFmppbwhqhGRGZr
            ggrLwFgWCBwbMWBbFwLMgNBZdmZHclJPllnJlNRPmSNZRR
            ppszzDfhDfhsqpnvDVTfGpSPlPmclHcdRcZmmmdPPGSP
            pvtDDVDVpqDfzDfngBLCwQrgCtCwFwrg
            pbGjFFGGDjpbsGsmNhNFNRBBBtRhhhHv
            JnczJVCvwWJvhPgghgNtNtNJ
            nwVSSzdzzqSpvQSZQG
            mssLLttQrsMrMzLCRmMmrrSQpvWpDNlBTBDlvNTccDQl
            HdHJwJqVPwHnqJwbjJbGjnSgSTWPpNgWWpgBBgcvDWWN
            ZHVwVZGwwdndqJVJqfHbGwnwrRLtLMftMvMMRrhmLMthhLmz
            RgHGLbTqlZlPRZPHfvvfZttJnvfvjnzr
            sVcChDVDccwNhhvjTvVzWJjnzFff
            mpNcCMTCGmLqBLGH
            wVJwHJHVMtMpBmDDWPQVPWDGDD
            zCrlZzCblBvnCDWNGLmvGDLPNG
            dqZglgbzrzbbgZqzTFSBHHFJSSSfjjSMfwhj
            NMWJSjLMCnHHNMNNHWCHMbVVGBPZTrPVPBVDrBSDGTTr
            zvttlFpgdtldwwvftPDPTWQdBZrsrWrGBZ
            hFlFmhRFvfCbmWJWHcnj""";
}
