package ab3.impl.Kadiric;


import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import ab3.BTreeMap;
import ab3.BTreeNode;
import ab3.BTreeNode.*;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
    public class Bstablo implements BTreeMap
    {
        public Integer T=0;
        public Integer sz;
        public BTreeNode korijen;

        public Bstablo()
        {
            // we create an empty tree, consisting only of roots
            korijen = new BTreeNode();
            sz=0;
            T=0;
        }

        private String trazi(BTreeNode x, int key)
        {
            List<KeyValuePair> kvpairs = x.getKeyValuePairs();
            if(kvpairs==null)
                return null;
            int i=0; // here is the smallest key within the node
            for(KeyValuePair el:kvpairs)
            {
                if(key<=el.getKey())  // we stop if our key is less than the key in the node
                    break;
                i++;
            }
            if(i<kvpairs.size() && kvpairs.get(i).getKey().equals(key))
                return kvpairs.get(i).getValue(); // I found it!
            else
            {
                // he searches among the children
                List<BTreeNode> djeca = x.getChildren();
                if(djeca==null || djeca.isEmpty() || i>djeca.size())
                    return null;
                BTreeNode dijete =  djeca.get(i);
                return trazi(dijete,key);
            }

        }

        private void Btree_split_child(BTreeNode x, int i)
        {
            // split the node y=x.child[i] into y and z
            List<BTreeNode> djeca = x.getChildren();
            BTreeNode y = djeca.get(i);
            BTreeNode z = new BTreeNode();
            List<KeyValuePair> y_lista = y.getKeyValuePairs();
            List<KeyValuePair> z_lista = new ArrayList<KeyValuePair>();
            // there are 2t-1 elements in the y list
            // and there should be t-1 elements
            // We add the last t-1 elements to the z list
            // we remove the last t elements from the y list
            z_lista.addAll(y_lista.subList(T, y_lista.size()));
            KeyValuePair sredina = y_lista.get(T-1);
            y_lista.removeAll(y_lista.subList(T-1, y_lista.size()));
            // the y list is now the first t elements
            // the last t elements are now in the z list
            List<BTreeNode> djeca_od_y = y.getChildren();
            if(djeca_od_y!=null && !djeca_od_y.isEmpty())
            {
                // we put the second half of the children of y into the children of z
                List<BTreeNode> djeca_od_z = new ArrayList<BTreeNode>();
                djeca_od_z.addAll(djeca_od_y.subList(T, djeca_od_y.size()));
                // remove the second half of y's children
                djeca_od_y.removeAll(djeca_od_y.subList(T, djeca_od_y.size()));
                z.setChildren(djeca_od_z);
                y.setChildren(djeca_od_y);
            }
            // put the middle of y in x
            List<KeyValuePair> x_lista = x.getKeyValuePairs();
            if(x_lista==null)
                x_lista=new ArrayList<KeyValuePair>();
            x_lista.add(i, sredina);
            // save everything we changed
            z.setKeyValuePairs(z_lista);
            y.setKeyValuePairs(y_lista);
            // put z among the children of x
            djeca.set(i, y);
            djeca.add(i+1, z);
            x.setKeyValuePairs(x_lista);
            x.setChildren(djeca);
        }

        private BTreeNode Btree_split_root()
        {
            BTreeNode s= new BTreeNode();
            List<BTreeNode> djeca = new ArrayList<>();
            djeca.add(korijen);
            s.setChildren(djeca);
            korijen = s;
            Btree_split_child(s,0); // he old root is the only child of the new root
            return s;
        }

        private Boolean Btree_insert_nonfull(BTreeNode x, int key, String value)
        {
            List<KeyValuePair> kvpairs = x.getKeyValuePairs();
            if(kvpairs==null || kvpairs.isEmpty())
            {
                kvpairs = new ArrayList<KeyValuePair>();
                kvpairs.add(new KeyValuePair(key,value));
                x.setKeyValuePairs(kvpairs);
                this.sz++;
                return true;
            }

            //we are looking for where to insert it
            int i=0; // here is the smallest key within the node
            for(KeyValuePair el:kvpairs)
            {
                if(key<=el.getKey())  //we stop if our key is less than the key in the node
                    break;
                i++;
            }

            if(i<kvpairs.size() && key==kvpairs.get(i).getKey())
                return false;  					// we do not insert duplicates


            List<BTreeNode> djeca = x.getChildren();

            // if it's a leaf, insert the key value pair into its proper place
            if(djeca==null || djeca.isEmpty())
            {
                // insert it in place i
                // means everything greater than it should be moved one place to the right
                KeyValuePair kvp = new KeyValuePair(key,value);
                kvpairs.add(i,kvp);
                x.setKeyValuePairs(kvpairs);
                this.sz++;
                return true;
            }

            BTreeNode dijete = djeca.get(i);
            if(dijete.getKeyValuePairs().size()==2*T-1)
            {
                // split a child
                Btree_split_child(x,i);
                kvpairs = x.getKeyValuePairs();
                if(key>kvpairs.get(i).getKey())
                    i++;
                dijete = djeca.get(i);
            }
            return Btree_insert_nonfull(dijete,key,value);

        }

        private Boolean insert(BTreeNode x, int key, String value)
        {
            List<KeyValuePair> kvpairs = x.getKeyValuePairs();
            if(kvpairs!=null && kvpairs.size()==2*T-1)
            {
                // split the node
                if(x==this.getRoot())
                {
                    x=Btree_split_root();
                }
            }
            return Btree_insert_nonfull(x,key,value);

        }

        @Override
        public void setMinDegree(int t) throws IllegalStateException, IllegalArgumentException
        {
            try
            {
                if(this.T>=2) throw new IllegalStateException();
                if(t<2) throw new IllegalArgumentException();
            }
            finally
            {

            }
            this.T=t;
        }

        @Override
        public boolean put(int key, String value) throws IllegalStateException, IllegalArgumentException
        {
            // TODO Auto-generated method stub
            try
            {
                if(T<2)
                    throw new IllegalStateException();
                return insert(getRoot(),key,value);
            }
            finally {}
        }

        private boolean leaf(BTreeNode x)
        {
            return(x.getChildren()==null || x.getChildren().isEmpty());
        }

        private int nadji1(BTreeNode x, int key)
        {
            List<KeyValuePair> popis = x.getKeyValuePairs();
            for (int i = 0; i < popis.size(); i++)
            {
                if (popis.get(i).getKey().equals(key))
                {
                    return i;
                }
            }
            return -1;
        }

        private boolean brisi(BTreeNode x, int key)
        {
            int pos = nadji1(x,key);
            List<KeyValuePair> kvp = x.getKeyValuePairs();

            if(leaf(x))
            {
                //case 1: x is a leaf
                if(pos>=0)
                {
                    // 1a: the key exists in list x, we delete it
                    kvp.remove(pos);
                    x.setKeyValuePairs(kvp);
                    this.sz--;
                    if(this.sz==0)
                    {
                        // root has been removed
                        korijen.setChildren(new ArrayList<BTreeNode>());
                    }
                    return true;
                }
                else
                {
                    // 1b: key does not exist in x
                    return false;
                }
            }
            else
            {
                if(pos>=0)
                {
                    // case 2: key exists in internal node k at pos
                    List<BTreeNode> djeca = x.getChildren();
                    BTreeNode dijete_pos = djeca.get(pos);
                    BTreeNode dijete_pos1 = djeca.get(pos+1);
                    List<KeyValuePair> kljucevi_pos = dijete_pos.getKeyValuePairs();
                    List<KeyValuePair> kljucevi_pos1 = dijete_pos1.getKeyValuePairs();
                    int br_kljuceva_pos = kljucevi_pos.size();
                    int br_kljuceva_pos1 = kljucevi_pos1.size();

                    if(br_kljuceva_pos>=T)
                    {
                        // 2a: child pos of k contains at least T keys
                        // we are looking for k', the predecessor of the key k in the child pos (it is the last key in the RIGHT-FIRST LIST)
                        BTreeNode tmp_dijete_pos=dijete_pos;
                        while(!leaf(tmp_dijete_pos))
                        {
                            List<BTreeNode> tmp2_djeca = tmp_dijete_pos.getChildren();
                            tmp_dijete_pos = tmp2_djeca.get(tmp2_djeca.size()-1);
                        }
                        List<KeyValuePair> tmp_kvp = tmp_dijete_pos.getKeyValuePairs();
                        KeyValuePair predhodnik = tmp_kvp.get(tmp_kvp.size()-1); // JE LI OVO DOBRO?
                        // RECURSIVELY delete from the left subtree
                        // keys pos.remove(k_crta);
                        boolean tmp11 = brisi(dijete_pos,predhodnik.getKey());
                        kvp.set(pos, predhodnik); //we insert into k in place of the key k
                        x.setKeyValuePairs(kvp);
                        return true;
                    }
                    else if(br_kljuceva_pos1>=T)
                    {
                        // 2b: child pos contains T-1 keys, and pos+1 of x contains at least T keys
                        // we are looking for k', the follower from the key k in the child pos+1 (it is the first key in the LEFTmost LIST)
                        BTreeNode tmp_dijete_pos=dijete_pos1;
                        while(!leaf(tmp_dijete_pos))
                        {
                            List<BTreeNode> tmp2_djeca = tmp_dijete_pos.getChildren();
                            tmp_dijete_pos = tmp2_djeca.get(0);
                        }
                        List<KeyValuePair> tmp_kvp = tmp_dijete_pos.getKeyValuePairs();
                        KeyValuePair sljedbenik = tmp_kvp.get(0); // IS THIS GOOD?
                        // RECURSIVELY delete from the right subtree
                        // keys pos.remove(k_crta);
                        boolean tmp12 = brisi(dijete_pos1,sljedbenik.getKey());
                        kvp.set(pos, sljedbenik); //we insert into k in place of the key k
                        x.setKeyValuePairs(kvp);
                        return true;
                    }
                    else
                    {
                        // 2c: child pos and child pos+1 both contain T-1 key
                        // merge key k into child pos
                        kljucevi_pos.add(kvp.get(pos));
                        // merge all keys from child pos+1 into child pos
                        kljucevi_pos.addAll(kljucevi_pos1);
                        // now a child and contains 2T-1 keys
                        //need to add children from child pos+1 to children from child_pos
                        List<BTreeNode> unuci_pos = dijete_pos.getChildren();
                        List<BTreeNode> unuci_pos1 = dijete_pos1.getChildren();
                        if(unuci_pos1!=null && !unuci_pos1.isEmpty())
                            unuci_pos.addAll(unuci_pos1);

                        //we delete the key k from x
                        kvp.remove(pos);    // HERE IT IS POSSIBLE TO EMPTY THE ROOT
                        // we delete child i+1
                        djeca.remove(pos+1);
                        // delete key k from child i (RECURSIVE)
                        // keys pos.remove(T-1);
                        dijete_pos.setKeyValuePairs(kljucevi_pos);
                        djeca.set(pos, dijete_pos); // do we need this?
                        x.setChildren(djeca);
                        x.setKeyValuePairs(kvp);
                        if(kvp.isEmpty())
                        {
                            // THE EMPTY ROOT CASE
                            korijen = dijete_pos;
                        }

                        KeyValuePair za_brisati = kljucevi_pos.get(T-1);
                        boolean tmp13 = brisi(dijete_pos, za_brisati.getKey());
                        return true;
                    }
                }
                else
                {
                    // case 3: key is not in internal node x
                    // we are looking for the child i that contains the key i
                    int i=0;
                    boolean iduci=false, prosli=false;
                    for(i=0;i!=kvp.size() && kvp.get(i).getKey()<key;i++);
                    List<BTreeNode> djeca = x.getChildren();
                    BTreeNode dijete_i = djeca.get(i);
                    List<KeyValuePair> kvp_dijete_i = dijete_i.getKeyValuePairs();
                    if(kvp_dijete_i.size()==T-1)
                    {
                        // case 3a or 3b
                        BTreeNode dijete_susjed;
                        List<KeyValuePair> dijete_susjed_kvp;
                        if(i<djeca.size()-1)
                        {
                            dijete_susjed = djeca.get(i+1);
                            dijete_susjed_kvp = dijete_susjed.getKeyValuePairs();
                            if(dijete_susjed_kvp.size()>=T)
                            {
                                iduci = true;
                                // 3a with follower
                                // the key from x from position i should be moved to the child and to the last position
                                kvp_dijete_i.add(kvp.get(i));
                                // the key from i+1 from position 0 should be transferred to k at position i
                                kvp.set(i,dijete_susjed_kvp.get(0));
                                dijete_susjed_kvp.remove(0);
                                // the first child from i+1 should be transferred to i
                                List<BTreeNode> susjedova_djeca = dijete_susjed.getChildren();
                                List<BTreeNode> djeca_od_i = dijete_i.getChildren();
                                if(djeca_od_i!=null && susjedova_djeca!=null)
                                {
                                    djeca_od_i.add(susjedova_djeca.get(0));
                                    susjedova_djeca.remove(0);
                                }
                            }
                        }
                        if(iduci==false && i>0)
                        {
                            dijete_susjed = djeca.get(i-1);
                            dijete_susjed_kvp = dijete_susjed.getKeyValuePairs();
                            if(dijete_susjed_kvp.size()>=T)
                            {
                                prosli = true;
                                // 3a with predecessor
                                // key from k from place i should be transferred to child and to FIRST place
                                kvp_dijete_i.add(0,kvp.get(i-1));
                                // the key from i-1 from place n-1 should be transferred to k at place i-1
                                kvp.set(i-1,dijete_susjed_kvp.get(dijete_susjed_kvp.size()-1));
                                dijete_susjed_kvp.remove(dijete_susjed_kvp.size()-1);
                                //the last child from i-1 should be transferred to i-1
                                List<BTreeNode> susjedova_djeca = dijete_susjed.getChildren();
                                List<BTreeNode> djeca_od_i = dijete_i.getChildren();
                                if(djeca_od_i!=null && susjedova_djeca!=null)
                                {
                                    djeca_od_i.add(0,susjedova_djeca.get(susjedova_djeca.size()-1)); // at the beginning!
                                    susjedova_djeca.remove(susjedova_djeca.size()-1);
                                }
                            }
                        }
                        if(prosli==false && iduci == false)
                        {
                            // case 3b
                            if(i!=djeca.size()-1)
                            {
                                //we connect node i and i+1
                                dijete_i = djeca.get(i);
                                dijete_susjed = djeca.get(i+1);
                                // we transfer the key from k (place i) to the child (as median)
                                kvp_dijete_i = dijete_i.getKeyValuePairs();
                                dijete_susjed_kvp = dijete_susjed.getKeyValuePairs();
                                List<BTreeNode> susjedova_djeca = dijete_susjed.getChildren();
                                List<BTreeNode> djeca_od_i = dijete_i.getChildren();
                                kvp_dijete_i.add(kvp.get(i));
                                kvp.remove(i); //HERE IT IS POSSIBLE TO EMPTY THE ROOT
                                // we transfer the keys from i+1 to i
                                kvp_dijete_i.addAll(dijete_susjed_kvp);
                                // we transfer children from i+1 to i
                                if(djeca_od_i!=null && susjedova_djeca!=null)
                                    djeca_od_i.addAll(susjedova_djeca);
                                // we remove child i+1
                                djeca.remove(i+1);
                                // FIX REFERENCES
                                // EMPTY ROOT CASE
                                if(kvp.isEmpty())
                                {
                                    korijen = dijete_i;
                                }
                            }
                            else
                            {
                                // join node i-1 and i
                                dijete_susjed = djeca.get(i-1);
                                // we transfer the key from k (place i) to the child (as median)
                                dijete_susjed_kvp = dijete_susjed.getKeyValuePairs();
                                List<BTreeNode> susjedova_djeca = dijete_susjed.getChildren();
                                List<BTreeNode> djeca_od_i = dijete_i.getChildren();
                                dijete_susjed_kvp.add(kvp.get(i-1));
                                kvp.remove(i-1); // HERE IT IS POSSIBLE FOR THE ROOT TO BE EMPTY
                                // we transfer the keys from i to i-1
                                dijete_susjed_kvp.addAll(kvp_dijete_i);
                                // we transfer children from i to i-1
                                if(djeca_od_i!=null && susjedova_djeca!=null)
                                    susjedova_djeca.addAll(djeca_od_i);
                                // we remove the child i
                                djeca.remove(i);
                                dijete_i = dijete_susjed;
                                // FIX REFERENCES
                                // CASE EMPTY ROOT
                                if(kvp.isEmpty())
                                {
                                    korijen = dijete_i;
                                }
                            }
                        }
                    }
                    return brisi(dijete_i,key);
                }
            }
        }


        @Override
        public boolean delete(int key) throws IllegalStateException, IllegalArgumentException {
            // TODO Auto-generated method stub
            try
            {
                if(T<2)
                    throw new IllegalStateException();
            }
            finally {}
            //System.out.println(key);
            return brisi(getRoot(),key);
        }

        @Override
        public String contains(int key) throws IllegalStateException
        {
            // TODO Auto-generated method stub
            try
            {
                if(T<2)
                    throw new IllegalStateException();
            }
            finally {}
            return trazi(getRoot(),key);
        }

        @Override
        public BTreeNode getRoot() throws IllegalStateException {
            // TODO Auto-generated method stub
            try
            {
                if(T<2)
                    throw new IllegalStateException();
            }
            finally {}
            return korijen;
        }

        private List<KeyValuePair> getKeysValues(BTreeNode x)
        {
            List<KeyValuePair> kvpairs = new ArrayList<KeyValuePair>();
            List<KeyValuePair> tmp = x.getKeyValuePairs();
            if(tmp==null)
                return null;
            kvpairs.addAll(tmp);
            // trazi medju djecom
            List<BTreeNode> djeca = x.getChildren();
            if(djeca==null || djeca.isEmpty())
                return kvpairs;
            for(BTreeNode dijete:djeca)
            {
                List<KeyValuePair> kvp_lista_djeteta = getKeysValues(dijete);
                if(kvp_lista_djeteta!=null && !kvp_lista_djeteta.isEmpty())
                {
                    kvpairs.addAll(kvp_lista_djeteta);
                }
            }
            return kvpairs;
        }

        @Override
        public List<Integer> getKeys() throws IllegalStateException
        {
            // TODO Auto-generated method stub
            try
            {
                if(T<2)
                    throw new IllegalStateException();
            }
            finally {}
            List<KeyValuePair> kvpairs = getKeysValues(this.getRoot());
            if(kvpairs==null)
                return null;
            kvpairs.sort(new Comparator<KeyValuePair>()
            {
                public int compare(KeyValuePair a, KeyValuePair b)
                { return a.getKey()-b.getKey(); }

            });
            List<Integer> keys=new ArrayList<Integer>();
            for(KeyValuePair kvp:kvpairs)
            {
                keys.add(kvp.getKey());
            }
            return keys;

        }

        @Override
        public List<String> getValues() throws IllegalStateException {
            // TODO Auto-generated method stub
            try
            {
                if(T<2)
                    throw new IllegalStateException();
            }
            finally {}
            List<KeyValuePair> kvpairs = getKeysValues(this.getRoot());
            if(kvpairs==null)
                return null;
            kvpairs.sort(new Comparator<KeyValuePair>()
            {
                public int compare(KeyValuePair a, KeyValuePair b)
                { return a.getKey()-b.getKey(); }

            });
            List<String> values=new ArrayList<String>();
            for(KeyValuePair kvp:kvpairs)
            {
                values.add(kvp.getValue());
            }
            return values;
        }

        @Override
        public void clear() {
            // TODO Auto-generated method stub
            sz=0;
            T=0;
            korijen=new BTreeNode();

        }

        @Override
        public int size()
        {
            // TODO Auto-generated method stub
            return this.sz;
        }

    }

