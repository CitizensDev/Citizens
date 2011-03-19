package com.fullwall.resources.redecouverte.NPClib;

import com.fullwall.Citizens.Citizens;


public class NPC {

    private String name;
    private int UID;

    public NPC(int UID, String name)
    {
        this.name = name;
        this.UID = UID;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public String getName()
    {
    	if(Citizens.convertUnderscores == true){
    		String returnName = "";
    		String[] brokenName = this.name.split(" ");
    		for(int i = 0; i < brokenName.length; i++){
				if(i == 0) returnName = brokenName[i];
				else returnName += "_" + brokenName[i];
			}return returnName;
    	}
        return this.name;
    }
    
    public String getSpacedName()
    {
    	return this.name;
    }
    
    public int getUID()
    {
    	return this.UID;
    }

}