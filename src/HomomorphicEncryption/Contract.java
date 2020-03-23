package HomomorphicEncryption;

import java.math.BigInteger;

public class Contract {
    public int id;
    public BigInteger ci2;
    public BigInteger ci3;

    public Contract(int id, BigInteger ci2, BigInteger ci3){
        this.id = id;
        this.ci2 = ci2;
        this.ci3 = ci3;
    }

}
