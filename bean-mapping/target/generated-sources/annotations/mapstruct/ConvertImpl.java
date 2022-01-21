package mapstruct;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-12-29T23:41:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_152 (Oracle Corporation)"
)
public class ConvertImpl implements Convert {

    @Override
    public B aToB(A a) {
        if ( a == null ) {
            return null;
        }

        B b = new B();

        b.setVal( a.getVal() );

        return b;
    }
}
