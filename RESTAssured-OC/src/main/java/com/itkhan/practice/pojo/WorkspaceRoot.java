package com.itkhan.practice.pojo;

public class WorkspaceRoot {
    public WorkspaceRoot(){

    }

    public WorkspaceRoot(Workspace workspace){
        this.workspace = workspace;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    Workspace workspace;
}
