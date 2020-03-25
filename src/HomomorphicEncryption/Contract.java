package HomomorphicEncryption;

import java.math.BigInteger;

public class Contract {
    public int id;
    //public ?? file
    public BigInteger c2;
    public BigInteger c3;

    public Contract(int id, BigInteger c2, BigInteger c3){
        this.id = id;
        this.c2 = c2;
        this.c3 = c3;
    }

}
