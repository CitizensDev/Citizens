package com.fullwall.resources.redecouverte.NPClib;


public class NPC {

    private String uniqueId;
    private String name;

    public NPC(String uniqueId, String name)
    {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public String getName()
    {
        return this.name;
    }


    public String getUniqueId()
    {
        return this.uniqueId;
    }

}