package fr.elefort.piratesremastered.model.gravity;

import fr.elefort.piratesremastered.maths.Geometry;
import fr.elefort.piratesremastered.model.BaseObject;
import fr.elefort.piratesremastered.model.Movable;
import fr.elefort.piratesremastered.model.Obstacle;

/**
 * Created by Eric on 15/09/2015.
 */
public enum Gravity implements GravityEngine {
    NONE {
        @Override
        public int getOrientation() {
            return 0;
        }

        @Override
        public void update(Movable sender, long timeDelta) {

        }

        @Override
        public boolean intersect(Movable sender, BaseObject obj) {
            return false;
        }

        @Override
        public void checkBottom(Movable sender) {

        }
    },

    UP {
        @Override
        public int getOrientation() {
            return 180;
        }

        @Override
        public void update(Movable sender, long timeDelta) {
            int x = sender.getPosition().x + sender.velocity * (-1);
            int y = sender.getPosition().y + sender.impulsion;

            sender.setPosition(x, y);
            checkBottom(sender);
        }

        @Override
        public boolean intersect(Movable sender, BaseObject obj) {
            return Geometry.intersect(sender.getHitBox().left, sender.getHitBox().top - 1, sender.getHitBox().right, sender.getHitBox().bottom,
                    obj.getHitBox().left, obj.getHitBox().top, obj.getHitBox().right, obj.getHitBox().bottom);
        }

        @Override
        public void checkBottom(Movable sender) {
            Obstacle obstacle = sender.getObstacle();

            if(sender.impulsion >= 0)
                sender.impulsion -= 1;

            if(obstacle != null) {
                if (intersect(sender, obstacle))
                {
                    sender.setPosition(sender.getPosition().x, (int)(obstacle.getHitBox().bottom + (sender.getHitBox().height() * 0.5f) ));
                    sender.impulsion = 0;
                    return;
                }
                else {
                    sender.setObstacle(null);
                }
            }
        }
    },

    DOWN {
        @Override
        public int getOrientation() {
            return 0;
        }

        @Override
        public void update(Movable sender, long timeDelta) {
            int x = sender.getPosition().x + sender.velocity;
            int y = sender.getPosition().y + sender.impulsion * (-1);

            sender.setPosition(x, y);
            checkBottom(sender);
        }

        @Override
        public boolean intersect(Movable sender, BaseObject obj) {
            return Geometry.intersect(sender.getHitBox().left, sender.getHitBox().top, sender.getHitBox().right, sender.getHitBox().bottom + 1,
                    obj.getHitBox().left, obj.getHitBox().top, obj.getHitBox().right, obj.getHitBox().bottom);
        }

        @Override
        public void checkBottom(Movable sender) {
            Obstacle obstacle = sender.getObstacle();

            if(sender.impulsion >= 0)
                sender.impulsion -= 1;

            if(obstacle != null) {
                if (intersect(sender, obstacle))
                {
                    sender.setPosition(sender.getPosition().x, (int)(obstacle.getHitBox().top - (sender.getHitBox().height() * 0.5f) ));
                    sender.impulsion = 0;
                    return;
                }
                else {
                    sender.setObstacle(null);
                }
            }
        }
    },

    LEFT {
        @Override
        public int getOrientation() {
            return 90;
        }

        @Override
        public void update(Movable sender, long timeDelta) {
            int y = sender.getPosition().y + sender.velocity;
            int x = sender.getPosition().x + sender.impulsion;

            sender.setPosition(x, y);
            checkBottom(sender);
        }

        @Override
        public boolean intersect(Movable sender, BaseObject obj) {
            return Geometry.intersect(sender.getHitBox().left - 1, sender.getHitBox().top, sender.getHitBox().right, sender.getHitBox().bottom,
                    obj.getHitBox().left, obj.getHitBox().top, obj.getHitBox().right, obj.getHitBox().bottom);
        }

        @Override
        public void checkBottom(Movable sender) {
            Obstacle obstacle = sender.getObstacle();

            if(sender.impulsion >= 0)
                sender.impulsion -= 1;

            if(obstacle != null) {
                if (intersect(sender, obstacle))
                {
                    sender.setPosition((int)(obstacle.getHitBox().right + (sender.getHitBox().width() * 0.5f)), sender.getPosition().y);
                    sender.impulsion = 0;
                    return;
                }
                else {
                    sender.setObstacle(null);
                }
            }
        }
    },

    RIGHT {
        @Override
        public int getOrientation() {
            return 270;
        }

        @Override
        public void update(Movable sender, long timeDelta) {
            int y = sender.getPosition().y + sender.velocity * (-1);
            int x = sender.getPosition().x + sender.impulsion * (-1);

            sender.setPosition(x, y);
            checkBottom(sender);
        }

        @Override
        public boolean intersect(Movable sender, BaseObject obj) {
            return Geometry.intersect(sender.getHitBox().left, sender.getHitBox().top, sender.getHitBox().right + 1, sender.getHitBox().bottom,
                    obj.getHitBox().left, obj.getHitBox().top, obj.getHitBox().right, obj.getHitBox().bottom);
        }

        @Override
        public void checkBottom(Movable sender) {
            Obstacle obstacle = sender.getObstacle();

            if(sender.impulsion >= 0)
                sender.impulsion -= 1;

            if(obstacle != null) {
                if (intersect(sender, obstacle))
                {
                    sender.setPosition((int)(obstacle.getHitBox().left - (sender.getHitBox().width() * 0.5f)), sender.getPosition().y);
                    sender.impulsion = 0;
                    return;
                }
                else {
                    sender.setObstacle(null);
                }
            }
        }
    }
}
