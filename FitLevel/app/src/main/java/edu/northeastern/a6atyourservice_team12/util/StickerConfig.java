package edu.northeastern.a6atyourservice_team12.util;

import edu.northeastern.a6atyourservice_team12.R;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class StickerConfig {

    private static final LinkedHashMap<String, Integer> STICKER_MAP = new LinkedHashMap<>();

    static {
        STICKER_MAP.put("sticker_happy", R.drawable.sticker_happy);
        STICKER_MAP.put("sticker_sad", R.drawable.sticker_sad);
        STICKER_MAP.put("sticker_love", R.drawable.sticker_love);
        STICKER_MAP.put("sticker_laugh", R.drawable.sticker_laugh);
        STICKER_MAP.put("sticker_angry", R.drawable.sticker_angry);
        STICKER_MAP.put("sticker_cool", R.drawable.sticker_cool);
        STICKER_MAP.put("sticker_surprised", R.drawable.sticker_surprised);
        STICKER_MAP.put("sticker_thumbsup", R.drawable.sticker_thumbsup);
    }

    public static int getDrawableForSticker(String stickerId) {
        if (stickerId != null && STICKER_MAP.containsKey(stickerId)) {
            return STICKER_MAP.get(stickerId);
        }
        return R.drawable.sticker_unknown;
    }

    public static Set<String> getAllStickerIds() {
        return STICKER_MAP.keySet();
    }

    public static Map<String, Integer> getStickerMap() {
        return new LinkedHashMap<>(STICKER_MAP);
    }

    public static boolean isKnownSticker(String stickerId) {
        return stickerId != null && STICKER_MAP.containsKey(stickerId);
    }
}