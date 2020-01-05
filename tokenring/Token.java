package it.unipr.sowide.actodes.examples.tokenring;

import it.unipr.sowide.actodes.registry.Reference;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Token {

    private static final long serialVersionUID = 1L;
    private int id;
    private Reference reference;
    List<Integer> tokenID;

    public Token()
    {
        tokenID = new ArrayList<Integer>();
    }

    public void emptyList(){
        this.tokenID.clear();
    }





    public int getID()
    {
        return this.id;
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
