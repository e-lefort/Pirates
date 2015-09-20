package fr.elefort.piratesremastered.model.bonus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import fr.elefort.piratesremastered.model.Animation;
import fr.elefort.piratesremastered.model.Player;
import fr.elefort.priratesremastered.R;

/**
 * Created by Eric on 20/09/2015.
 */
public enum BonusItem implements BonusAction{
    SPEED_PLUS_1 ('s') {
        private ArrayList<Bitmap> bitmaps;

        @Override
        public void perform(Player player) {
            player.increaseVelocity();
        }

        @Override
        public Animation getAnimation(Context context) {
            if (bitmaps == null) {
                bitmaps = new ArrayList<>();
                bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.bonus_speed_plus_1));
            }
            return new Animation(bitmaps);
        }

        @Override
        public String getName() {
            return "Life +1";
        }
    },
    SPEED_MINUS_1 ('d') {
        private ArrayList<Bitmap> bitmaps;

        @Override
        public void perform(Player player) {
            player.decreaseVelocity();
        }

        @Override
        public Animation getAnimation(Context context) {
            if (bitmaps == null) {
                bitmaps = new ArrayList<>();
                bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.bonus_speed_minus_1));
            }
            return new Animation(bitmaps);
        }

        @Override
        public String getName() {
            return "Life -1";
        }
    },
    JUMP ('j') {
        private ArrayList<Bitmap> bitmaps;

        @Override
        public void perform(Player player) {
            player.jump();
        }

        @Override
        public Animation getAnimation(Context context) {
            if (bitmaps == null) {
                bitmaps = new ArrayList<>();
                bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.bonus_jump));
            }
            return new Animation(bitmaps);
        }

        @Override
        public String getName() {
            return "Jump";
        }
    },
    LIFE_PLUS_1 ('l') {
        private ArrayList<Bitmap> bitmaps;

        @Override
        public void perform(Player player) {
            player.getStats().lifes += 1;
        }

        @Override
        public Animation getAnimation(Context context) {
            if (bitmaps == null) {
                bitmaps = new ArrayList<>();
                bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.bonus_life_plus_1));
            }
            return new Animation(bitmaps);
        }

        @Override
        public String getName() {
            return "Speed +1";
        }
    },
    LIFE_MINUS_1 ('h') {
        private ArrayList<Bitmap> bitmaps;

        @Override
        public void perform(Player player) {
            player.getStats().hurt();
        }

        @Override
        public Animation getAnimation(Context context) {
            if (bitmaps == null) {
                bitmaps = new ArrayList<>();
                bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.bonus_life_minus_1));
            }
            return new Animation(bitmaps);
        }

        @Override
        public String getName() {
            return "Speed -1";
        }
    };

    private final char value;
    BonusItem(char value) { this.value = value; }
    @Override
    public char getValue() { return value; }
}
