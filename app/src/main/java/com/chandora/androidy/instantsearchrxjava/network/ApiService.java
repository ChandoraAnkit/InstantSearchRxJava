package com.chandora.androidy.instantsearchrxjava.network;

import com.chandora.androidy.instantsearchrxjava.network.model.Contact;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("contacts.php")
    Single<List<Contact>> getContacts(@Query("source") String source,@Query("search") String query);
}
