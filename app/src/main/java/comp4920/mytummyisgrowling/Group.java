package comp4920.mytummyisgrowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl Oehme on 20/10/2015.
 */

class Group {
    private int id;
    private String name;
    private String idString;
    private String pass;
    private int leaderId;
    private String leaderName;
    private List<Integer> memberIds;
    private boolean selected;

    public Group() {
    }

    public Group(int id, String name, String pass, String idString, String leaderName, int leaderId) {
        this.id = id;
        this.name = name;
        this.idString = idString;
        this.pass = pass;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.memberIds = new ArrayList<>();
        memberIds.add(leaderId);
        selected = false;
    }

    public void addMemberId(int id) {
        this.memberIds.add(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdString() {
        return idString;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(ArrayList<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
