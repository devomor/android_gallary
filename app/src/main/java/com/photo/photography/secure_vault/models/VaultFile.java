package com.photo.photography.secure_vault.models;

import java.io.Serializable;

public class VaultFile implements Serializable {
    //private variables
    int _id;
    String _oldpath;
    String _oldfilename;
    String _newpath;
    String _newfilename;
    String _type;
    String _thumbnail;
    boolean _selected;

    // Empty constructor
    public VaultFile() {
    }

    // constructor
    public VaultFile(String _oldpath, String _oldfilename, String _newpath, String _newfilename, String _type,
                     String _thumbnail, boolean _selected) {
        this._oldpath = _oldpath;
        this._oldfilename = _oldfilename;
        this._newpath = _newpath;
        this._newfilename = _newfilename;
        this._type = _type;
        this._thumbnail = _thumbnail;
        this._selected = _selected;
    }

    // constructor
    public VaultFile(int _id, String _oldpath, String _oldfilename, String _newpath, String _newfilename, String _type,
                     String _thumbnail) {
        this._id = _id;
        this._oldpath = _oldpath;
        this._oldfilename = _oldfilename;
        this._newpath = _newpath;
        this._newfilename = _newfilename;
        this._type = _type;
        this._thumbnail = _thumbnail;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getOldPath() {
        return this._oldpath;
    }

    public void setOldPath(String oldPath) {
        this._oldpath = oldPath;
    }

    public String getOldFileName() {
        return this._oldfilename;
    }

    public void setOldFileName(String oldFileName) {
        this._oldfilename = oldFileName;
    }

    public String getNewPath() {
        return this._newpath;
    }

    public void setNewPath(String newPath) {
        this._newpath = newPath;
    }

    public String getNewFileName() {
        return this._newfilename;
    }

    public void setNewFileName(String newFileName) {
        this._newfilename = newFileName;
    }

    public String getType() {
        return this._type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public String getThumbnail() {
        return this._thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this._thumbnail = thumbnail;
    }

    public boolean getIsSelected() {
        return this._selected;
    }

    public void setIsSelected(boolean selected) {
        this._selected = selected;
    }
}
