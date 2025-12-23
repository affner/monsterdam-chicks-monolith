package com.monsterdam.app.service.bff;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

/**
 * Service that abstracts the retrieval of media URLs.  At present it simply
 * returns a filesystem path constructed from the S3 key or media id.  In a
 * future iteration this service can be swapped out to return pre‑signed
 * Amazon S3 URLs.  By encapsulating the logic here we avoid leaking
 * storage concerns into the controller layer.
 */
@Service
public class MediaTokenService {

    private static final String FILESYSTEM_ROOT = "/files/";

    /**
     * Resolve a media id to a URL.  For now this simply prefixes the id
     * with a local path.  When integrating with S3 this method should
     * generate and return a pre‑signed URL instead.
     */
    public String getTokenForMedia(@NotNull Long mediaId) {
        return FILESYSTEM_ROOT + "media/" + mediaId;
    }

    /**
     * Resolve an S3 key to a URL.  This method supports profile photos,
     * cover photos and thumbnails.  The default implementation returns a
     * filesystem path for development environments.
     */
    public String getTokenForKey(@NotNull String key) {
        return FILESYSTEM_ROOT + key;
    }
}
