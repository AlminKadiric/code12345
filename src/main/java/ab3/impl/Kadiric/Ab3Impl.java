package ab3.impl.Kadiric;

import ab3.Ab3;
import ab3.BTreeMap;



public class Ab3Impl implements Ab3 {

    @Override
    public BTreeMap newBTreeInstance()
    {

        // YOUR CODE HERE
        return new Bstablo();
    }

}
