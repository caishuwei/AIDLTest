package com.csw.aidltest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.csw.aidltest.IService;
import com.csw.aidltest.entities.Book;

/**
 * Created by caisw on 2017/11/7.
 */

public class AIDLService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IService.Stub() {

            @Override
            public String hello(String name) throws RemoteException {
                return "hello "+name;
            }

            @Override
            public Book newBook(String name, String author) throws RemoteException {
                Book book = new Book(name,author);
                Log.e("AIDLService",book.toString());
                return book;
            }
        };
    }
}
