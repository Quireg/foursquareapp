package ua.in.quireg.foursquareapp.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SerializationService {

    public static Completable serializeToFile(File file, Serializable obj) {

        Timber.v("serializeToFile() - %s", file.toString());
        return Completable.create(
                e -> {

                    ObjectOutputStream os = null;
                    try {
                        os = new ObjectOutputStream(new FileOutputStream(file));
                        os.writeObject(obj);
                        e.onComplete();
                    } catch (IOException ex) {
                        Timber.e("serialization failed for file %s, reason: %s", file.toString(), ex.getMessage());
                    } finally {
                        if (os != null) {
                            os.close();
                        }
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Serializable> deserializeFromFile(File file) {
        Timber.v("deserializeFromFile() - %s", file.toString());

        if (!file.exists()) {
            return Observable.empty();
        }

        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream(file));

            return Observable.just(is.readObject())
                    .cast(Serializable.class)
                    .subscribeOn(Schedulers.io());
        } catch (Exception e) {
            Timber.e("deserialization failed for file %s, reason: %s", file.toString(), e.getMessage());

            return Observable.empty();

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
