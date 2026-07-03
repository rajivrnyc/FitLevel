// References:
// Sending resource IDs instead of pixels over network
package edu.northeastern.a6atyourservice_team12.model;

/**
 * We send the sticker ID string to Firebase instead of the actual image
 */
public class StickerItem {

    private final String stickerId;
    private final int drawableResId;

    public StickerItem(String stickerId, int drawableResId) {
        this.stickerId = stickerId;
        this.drawableResId = drawableResId;
    }

    public String getStickerId() {
        return stickerId;
    }

    public int getDrawableResId() {
        return drawableResId;
    }
}
