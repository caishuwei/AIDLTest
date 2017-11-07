// IService.aidl
package com.csw.aidltest;
import com.csw.aidltest.entities.Book;
// Declare any non-default types here with import statements

interface IService {

    String hello(String name);

    Book newBook(String name,String author);
}
