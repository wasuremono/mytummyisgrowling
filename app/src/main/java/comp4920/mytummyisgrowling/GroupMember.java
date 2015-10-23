package comp4920.mytummyisgrowling;

/**
 * Created by Carl Oehme on 21/10/2015.
 */


class GroupMember {
    private int id;
    private String name;
    private boolean checked;
    private boolean leader;

    public GroupMember(int id, String name) {
        this.id = id;
        this.name = name;
        this.checked = false;
        this.leader = false;
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

    public void toggleChecked() {
        this.checked = !this.checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }
}
