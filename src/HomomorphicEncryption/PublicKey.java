package HomomorphicEncryption;

import java.math.BigInteger;

public class PublicKey implements Comparable<PublicKey>{
    public BigInteger q;
    public BigInteger r;
    public BigInteger pk;
    public PublicKey(BigInteger q, BigInteger r){
        this.q = q;
        this.r = r;
        setPk();
    }
    public void setQ(BigInteger q){
        this.q = this.q.add(q);
        setPk();
    }
    public void setR(BigInteger r){
        this.r = this.r.add(r);
        setPk();
    }
    public void setPk(){
        this.pk = KGC.p.multiply(q).add(r);
    }
    @Override
    public int compareTo(PublicKey publicKey) {
        //return this.getPublicKey().compareTo(publicKey.getPublicKey()); //오름차순
        return publicKey.pk.compareTo(this.pk); //내림차순
    }
}
