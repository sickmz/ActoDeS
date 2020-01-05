package it.unipr.sowide.actodes.examples.tokenring;

import it.unipr.sowide.actodes.registry.Reference;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Store {

    private static final long serialVersionUID = 1L;
    private int id;
    private Reference reference;
    List<Integer> tokenID;

    public Store()
    {
        tokenID = new ArrayList<Integer>();
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public int getID()
    {
        return this.id;
    }

    public void setReference(Reference r)
    {
        this.reference = r;
    }

    public Reference getReference()
    {
        return this.reference;
    }

    public void setTokenID(int token) {
        tokenID.add(token);
    }

    public List<Integer> getTokenID() {
        return tokenID;
    }

}
