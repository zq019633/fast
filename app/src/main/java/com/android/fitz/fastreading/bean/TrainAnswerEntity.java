package com.android.fitz.fastreading.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fitz on 2017/07/25.
 */

public class TrainAnswerEntity implements Parcelable {
    private int iAnswer;
    private String pict;
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private List<Integer> iError;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getiAnswer() {
        return iAnswer;
    }

    public void setiAnswer(int iAnswer) {
        this.iAnswer = iAnswer;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public List<Integer> getiError() {
        return iError;
    }

    public void setiError(List<Integer> iError) {
        this.iError = iError;
    }

    public TrainAnswerEntity() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.iAnswer);
        dest.writeString(this.pict);
        dest.writeInt(this.index);
        dest.writeString(this.id);
        dest.writeList(this.iError);
    }

    protected TrainAnswerEntity(Parcel in) {
        this.iAnswer = in.readInt();
        this.pict = in.readString();
        this.index = in.readInt();
        this.id = in.readString();
        this.iError = new ArrayList<Integer>();
        in.readList(this.iError, Integer.class.getClassLoader());
    }

    public static final Creator<TrainAnswerEntity> CREATOR = new Creator<TrainAnswerEntity>() {
        @Override
        public TrainAnswerEntity createFromParcel(Parcel source) {
            return new TrainAnswerEntity(source);
        }

        @Override
        public TrainAnswerEntity[] newArray(int size) {
            return new TrainAnswerEntity[size];
        }
    };
}
