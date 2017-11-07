package com.csw.aidltest.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by caisw on 2017/11/7.
 */

public class Book implements Parcelable {
    private String name;
    private String author;

    public Book(Parcel source) {
        name = source.readString();
        author = source.readString();
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(author);
    }


    public static final Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
