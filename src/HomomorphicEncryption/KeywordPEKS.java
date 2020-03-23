package HomomorphicEncryption;

import java.math.BigInteger;

public class KeywordPEKS {
    private int id;
    private BigInteger ci1;
    private BigInteger ci2;
    public KeywordPEKS(int id, BigInteger ci1, BigInteger ci2){ //from contract table
        this.id = id;
        this.ci1 = ci1;
        this.ci2 = ci2;
    }

}
