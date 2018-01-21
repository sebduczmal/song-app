package com.sebduczmal.songapp.data;

import android.provider.ContactsContract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.inject.Qualifier;

import io.reactivex.Single;

/**
 * @author Sebastian Duczmal
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface DataRepoQualifier {

    String value();
}
