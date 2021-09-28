package com.NEWROW.row.Data;

/**
 * Created by USER1 on 15-11-2016.
 */
public class ReplicaInfoData {
    String moduleId, replicaOf;

    public ReplicaInfoData(String moduelid, String replicaOf) {
        this.moduleId = moduelid;
        this.replicaOf = replicaOf;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getReplicaOf() {
        return replicaOf;
    }

    @Override
    public String toString() {
        return "ReplicaInfoData{" +
                "moduleId='" + moduleId + '\'' +
                ", replicaOf='" + replicaOf + '\'' +
                '}';
    }

    public void setReplicaOf(String replicaOf) {
        this.replicaOf = replicaOf;
    }
}
