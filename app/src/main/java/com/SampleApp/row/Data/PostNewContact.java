package com.SampleApp.row.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 16-08-2016.
 */

public class PostNewContact {

    private List<Newmember> newmembers = new ArrayList<Newmember>();

    public List<Newmember> getNewmembers() {
        return newmembers;
    }

    public void setNewmembers(List<Newmember> newmembers) {
        this.newmembers = newmembers;
    }
}
