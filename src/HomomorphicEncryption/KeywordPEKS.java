package HomomorphicEncryption;

import java.math.BigInteger;

public class KeywordPEKS {
    public int id;
    public BigInteger c1;
    public BigInteger c2;
    public KeywordPEKS(int id, BigInteger c1, BigInteger c2){ //from contract table
        this.id = id;
        this.c1 = c1;
        this.c2 = c2;
    }

    public KeywordPEKS(BigInteger c1, BigInteger c2){ //from contract table
        this.c1 = c1;
        this.c2 = c2;
    }

}
